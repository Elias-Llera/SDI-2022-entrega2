const {ObjectId} = require("mongodb");
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
        let registeredUser = req.session.user;
        let friendshipsFilter = f=>{return f.state==="PENDING" && (f.sender===registeredUser || f.receiver===registeredUser)};

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

    });

    async function canSendInvite(user, registeredUser){
        if(user === registeredUser)
            return false;
        let filter = f=>{return f.sender===registeredUser || f.receiver===registeredUser};
        friendshipsRepository.getFriendships(filter, {})
            .then(friendships => {
                return friendships.length > 0;
            })
            .catch( () => {
                return false
            });
    }

    app.patch("/friendships/accept/:friendshipId", function(req, res){

    });

}
