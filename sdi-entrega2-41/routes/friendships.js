module.exports = function (app, friendshipsRepository, usersRepository) {

    /**
     *
     */
    app.get("/friendships/friends", function (req, res){
        let registeredUser = req.session.user;
        let friendshipsFilter = {state: "ACCEPTED", $or:[{sender: registeredUser}, {receiver: registeredUser}] };

        let page = parseInt(req.query.page);
        if (typeof req.query.page === "undefined" || req.query.page === null || req.query.page === "0") {
            page = 1;
        }

        friendshipsRepository.getFriendshipsPg(friendshipsFilter, {}, page)
            .then( result => {
                // Cálculos de paginación
                let lastPage = result.total / 5;
                if (result.total % 5 > 0) { // Decimales
                    lastPage = lastPage + 1;
                }
                let pages = []; // Indices de páginas a mostrar
                for (let i = page - 2; i <= page + 2; i++) {
                    if (i > 0 && i <= lastPage) {
                        pages.push(i);
                    }
                }
                let senders = result.friendships.map( f=> f.sender).filter( s => s !== req.session.user);
                let receivers = result.friendships.map( f=> f.receiver).filter( r => r !== req.session.user);
                let friends = senders.concat(receivers);
                let usersFilter = { email: { $in: friends }};
                usersRepository.getUsers(usersFilter, {})
                    .then( users => {
                        let response = {
                            friends: users,
                            pages: pages,
                            currentPage: page
                        }
                        res.render("friendships/friends.twig", response);
                    })
                    .catch( error =>
                        res.send("Error: " + error)
                    );
            })
            .catch( error =>
                res.send("Error: " + error)
            );
    });

    /**
     *
     */
    app.get("/friendships/invitations", function (req, res){
        let friendshipsFilter = { state:"PENDING", receiver:req.session.user };

        let page = parseInt(req.query.page);
        if (typeof req.query.page === "undefined" || req.query.page === null || req.query.page === "0") {
            page = 1;
        }

        friendshipsRepository.getFriendshipsPg(friendshipsFilter, {}, page)
            .then( result => {
                // Cálculos de paginación
                let lastPage = result.total / 5;
                if (result.total % 5 > 0) { // Decimales
                    lastPage = lastPage + 1;
                }
                let pages = []; // Indices de páginas a mostrar
                for (let i = page - 2; i <= page + 2; i++) {
                    if (i > 0 && i <= lastPage) {
                        pages.push(i);
                    }
                }
                let senders = result.friendships.map( f=> f.sender);
                let usersFilter = { email: { $in: senders }};
                usersRepository.getUsers(usersFilter, {})
                    .then( users => {
                        let response = {
                            invitations: users,
                            pages: pages,
                            currentPage: page
                        }
                        res.render("friendships/invitations.twig", response);
                    })
                    .catch( error =>
                        res.send("Error: " + error)
                    );
            })
            .catch( error =>
                res.send("Error: " + error)
            );
    });

    /**
     * @param userID es un parametro de URL.
     * @param paametroEnBody es un parametro del cuerpo. tipo string y contiene blablabla
     */
    app.post("/friendships/send/:userEmail", function(req, res){
        canSendInviteTo(req.params.userEmail, req.session.user)
            .then(canSend=>{
                if(canSend){
                    let friendship = {
                        sender: req.session.user,
                        receiver: req.params.userEmail,
                        state: "PENDING"
                    }
                    friendshipsRepository.insertFriendship(friendship)
                        .then( () => {
                            res.redirect("/users/list")
                        })
                        .catch( () =>
                            res.send("You cannot send an invite to this user.")
                        );
                } else {
                    res.send("You cannot send an invite to this user.")
                }
            })
            .catch(error =>
                res.send("Error: " + error)
            );
    });

    /**
     *
     * @param userId
     * @param registeredUserId
     * @returns {Promise<boolean>}
     */
    async function canSendInviteTo(userEmail, registeredUser){
        if(userEmail === registeredUser)
            return false;
        let filter = {$or: [{sender:registeredUser, receiver: userEmail}, {sender:userEmail, receiver: registeredUser}]};
        let friendships = await friendshipsRepository.getFriendships(filter, {});
        return friendships.length === 0;
    }

    /**
     *
     */
    app.post("/friendships/accept/:userEmail", function(req, res) {
        let filter = { sender:req.params.userEmail, state: "PENDING" };
        friendshipsRepository.findFriendship(filter, {})
            .then( friendship => {
                friendship.state = "ACCEPTED";
                friendshipsRepository.updateFriendship(friendship, filter, {})
                    .then( () => {
                        res.redirect("/friendships/friends")})
                    .catch(error =>
                        res.send("Error: " + error)
                    );
            })
            .catch(error =>
                res.send("Error: " + error)
            );
    });

}
