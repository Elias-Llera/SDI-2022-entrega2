module.exports = function (app, friendshipsRepository, usersRepository) {

    app.get("/friendships/friends", function (req, res){
        let registeredUser = req.session.user;
        let friendshipsFilter = f=>{return f.state==="ACCEPTED" && (f.sender===registeredUser || f.receiver===registeredUser)};

        friendshipsRepository.getFriendshipsPg(friendshipsFilter, {})
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
                let usersFilter = u=>{ return result.contains(u._id)};
                usersRepository.getUsersPg(usersFilter, {})
                    .then( result => {
                        let response = {
                            users: result.users,
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

    app.get("/friendships/invitations", function (req, res){
        let friendshipsFilter = { state:"PENDING", receiver:req.session.user };
        friendshipsRepository.getFriendshipsPg(friendshipsFilter, {})
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
                let usersFilter = u => { return result.reduce(f=>f.sender).contains(u._id.toString())};
                usersRepository.getUsersPg(usersFilter, {})
                    .then( result => {
                        let response = {
                            users: result.users,
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

    app.post("/friendships/send/:userId", function(req, res){
        canSendInviteTo(req.params.userId, req.session.user)
            .then(canSend=>{
                if(canSend){
                    let friendship = {
                        sender: req.session.user,
                        receiver: req.params.userId,
                        state: "PENDING"
                    }
                    friendshipsRepository.insertFriendship(friendship)
                        .then( () => {
                            res.redirect("/users")
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

    async function canSendInviteTo(userId, registeredUserId){
        if(userId === registeredUserId)
            return false;
        let filter = f=>{return f.sender===registeredUserId || f.receiver===registeredUserId};
        friendshipsRepository.getFriendships(filter, {})
            .then(friendships => {
                return friendships.length > 0;
            })
            .catch( () => {
                return false
            });
    }

    app.patch("/friendships/accept/:senderId", function(req, res) {
        let filter = { sender:req.params.senderId, state: "PENDING" };
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
