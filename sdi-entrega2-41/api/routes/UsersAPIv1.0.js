module.exports = function (app, usersRepository, friendshipsRepository) {
    app.post('/api/v1.0/users/login', function (req, res) {
        if (req.body.email === undefined || req.body.email === null) {
            res.status(400);
            res.json({error: "El nombre de usuario no puede ser nulo."});
            return;
        }
        if (req.body.password === undefined || req.body.password === null) {
            res.status(400);
            res.json({error: "La contraseña no puede ser nula."});
            return;
        }
        try {
            let securePassword = app.get("crypto").createHmac('sha256', app.get('clave'))
                .update(req.body.password).digest('hex');
            let filter = {
                email: req.body.email,
                password: securePassword
            }
            let options = {};
            usersRepository.findUser(filter, options).then(user => {
                if (user === null) {
                    res.status(401); // Unauthorized
                    res.json({
                        message: "Usuario no autorizado",
                        authenticated: false
                    })
                } else {
                    let token = app.get('jwt').sign(
                        {user: user.email, time: Date.now() / 1000}, "secreto");
                    res.status(200); //Correct
                    res.json({
                        message: "Usuario autorizado",
                        authenticated: true,
                        token: token
                    })
                }
            }).catch(() => {
                res.status(401); //error
                res.json({
                    message: "Se ha producido un error al verificar las credenciales",
                    authenticated: false
                })
            })
        } catch (e) {
            res.status(500);
            res.json({
                message: "Se ha producido un error al verificar las credenciales",
                authenticated: false
            })
        }
    });

    app.get("/api/v1.0/friends", function (req, res) {
        let user = res.user;
        let friendshipsFilter = {status: "ACCEPTED", $or:[{sender: user}, {receiver: user}] };

        friendshipsRepository.getFriendships(friendshipsFilter, {}).then(friendships => {
            let senders = friendships.map( f=> f.sender).filter( s => s !== user);
            let receivers = friendships.map( f=> f.receiver).filter( r => r !== user);
            let friends = senders.concat(receivers);

            let usersFilter = { email: { $in: friends }};
            usersRepository.getUsers(usersFilter, {})
                .then( users => {
                    res.status(200);
                    res.send({friends: users});
                })
                .catch( error => {
                    res.status(500);
                    res.json({error: "Se ha producido un error al recuperar los amigos."})
                });
        }).catch(error => {
            res.status(500);
            res.json({ error: "Se ha producido un error al recuperar los amigos." })
        });
    });
};