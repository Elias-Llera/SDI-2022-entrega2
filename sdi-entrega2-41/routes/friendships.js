module.exports = function (app, friendshipsRepository, usersRepository, infoLogger) {

    /**
     *
     */
    app.get("/friendships/friends", function (req, res){
        let friendshipsFilter = {status: "ACCEPTED", $or: [{sender: req.session.user}, {receiver: req.session.user}] };

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
                    .then( result => {
                        let response = {
                            friends: result.users,
                            session:req.session,
                            pages: pages,
                            currentPage: page
                        }

                        infoLogger.info(req.session.user + " -> Accede a la lista de amigos");
                        res.render("friendships/friends.twig", response);
                    })
                    .catch( () => {
                        infoLogger.error("Ha habido un error al acceder a la lista de amigos");
                        res.redirect("/" +
                            "?message=Se ha producido un error al buscar los usuarios" +
                            "&messageType=alert-danger ");
                        }
                    );
            })
            .catch( () => {
                infoLogger.error("Ha habido un error al acceder a la lista de amigos");
                res.redirect("/" +
                    "?message=Se ha producido un error al buscar los amigos" +
                    "&messageType=alert-danger ");
                }
            );
    });

    /**
     *
     */
    app.get("/friendships/invitations", function (req, res){
        let friendshipsFilter = { status:"PENDING", receiver:req.session.user };

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
                    .then( result => {
                        let response = {
                            invitations: result.users,
                            session:req.session,
                            pages: pages,
                            currentPage: page
                        }
                        infoLogger.info(req.session.user + " -> Accede a la lista de invitaciones de amistad");
                        res.render("friendships/invitations.twig", response);
                    })
                    .catch( () => {
                        infoLogger.error("Ha habido un error al acceder a la lista de peticiónes de amistad");
                        res.redirect("/" +
                            "?message=Se ha producido un error al buscar los usuarios" +
                            "&messageType=alert-danger ");
                    });
            })
            .catch( () => {
                infoLogger.error("Ha habido un error al acceder a la lista de peticiónes de amistad");
                res.redirect("/" +
                    "?message=Se ha producido un error al buscar las invitaciones" +
                    "&messageType=alert-danger ");
                }
            );
    });

    /**
     * @param userID es un parametro de URL.
     * @param paametroEnBody es un parametro del cuerpo. tipo string y contiene blablabla
     */
    app.post("/friendships/send/:userEmail", async function(req, res){
        let canSendInvite = await canSendInviteTo(req.params.userEmail, req.session.user);
        if(canSendInvite){
            let friendship = {
                sender: req.session.user,
                receiver: req.params.userEmail,
                status: "PENDING"
            }
            friendshipsRepository.insertFriendship(friendship)
                .then( () => {
                    infoLogger.info(req.session.user + " -> Envía una petición de amistad a " + req.params.userEmail);
                    res.redirect("/users/list"  +
                        "?message=Invitación enviada" +
                        "&messageType=alert-info ")
                })
                .catch( () => {
                    infoLogger.error("Ha habido un error al enviar una petición de amistad");
                    res.redirect("/" +
                        "?message=Se ha producido un error al crear la invitación de amistad" +
                        "&messageType=alert-danger ");
                    }
                );
        } else {
            infoLogger.error("Ha habido un error al enviar una petición de amistad");
            res.redirect("/" +
                "?message=No puedes enviar una invitación a este usuario" +
                "&messageType=alert-danger ");
        }
    });

    /**
     *
     * @param userEmail
     * @param registeredUser
     * @returns {Promise<boolean>}
     */
    async function canSendInviteTo(userEmail, registeredUser){
        if(userEmail === registeredUser) {
            console.log("Same user")
            return false;
        }
        let filter = {$or: [{sender:registeredUser, receiver: userEmail}, {sender:userEmail, receiver: registeredUser}]};
        let friendships = await friendshipsRepository.getFriendships(filter, {});
        console.log(friendships);
        return friendships.length === 0;
    }

    /**
     *
     */
    app.post("/friendships/accept/:userEmail", function(req, res) {
        let filter = { sender:req.params.userEmail, status: "PENDING" };
        friendshipsRepository.findFriendship(filter, {})
            .then( friendship => {
                friendship.status = "ACCEPTED";
                friendshipsRepository.updateFriendship(friendship, filter, {})
                    .then( () => {

                        infoLogger.info(req.session.user + " -> Acepta la invitación de amistad de " + req.params.userEmail);
                        res.redirect("/friendships/friends" +
                            "?message=Invitación aceptada" +
                            "&messageType=alert-info ")})
                    .catch( () => {

                        infoLogger.error("Ha habido un error al aceptar una petición de amistad");
                        res.redirect("/" +
                            "?message=Se ha producido un error al aceptar la petición" +
                            "&messageType=alert-danger ")
                        }
                    );
            })
            .catch( () => {
                    infoLogger.error("Ha habido un error al aceptar una petición de amistad");
                    res.redirect("/" +
                        "?message=Se ha producido un error buscar la petición" +
                        "&messageType=alert-danger ");
                }
            );
    });

}
