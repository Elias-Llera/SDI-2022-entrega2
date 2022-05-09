const {ObjectId} = require("mongodb");

module.exports = function (app, usersRepository) {


    app.get("/users/list", function (req, res) {
        let filter = {rol: {$not: {$eq: "ADMIN"}}, email: {$not: {$eq: req.session.user}}};
        let options = {sort: {email: 1}};

        //For filtering
        if (req.query.search != null && typeof (req.query.search) != "undefined" && req.query.search != "") {
            filter = {
                rol: {$not: {$eq: "ADMIN"}}, email: {$not: {$eq: req.session.user}},
                email: {$regex: ".*" + req.query.search + ".*"}
            };
        }

        //For pagination
        let page = parseInt(req.query.page); // Es String !!!
        if (typeof req.query.page === "undefined" || req.query.page === null || req.query.page === "0") { //
            //Puede no venir el param
            page = 1;
        }

        usersRepository.getUsersPg(filter, options, page)
            .then(result => {
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

                let response = {
                    users: result.users,
                    pages: pages,
                    currentPage: page,
                    session: req.session
                }
                res.render("users/list.twig", response);
            })
            .catch(error =>
                res.send("Error: " + error)
            );
    });

    app.get('/users/login', function (req, res) {
        res.render("login.twig");
    });

    app.post('/users/login', function (req, res) {
        let securePassword = app.get("crypto").createHmac('sha256', app.get('clave'))
            .update(req.body.password).digest('hex');
        let filter = {
            email: req.body.email,
            password: securePassword
        }
        let options = {}
        usersRepository.findUser(filter, options).then(user => {
            if (user == null) {
                req.session.user = null;
                res.redirect("/users/login" +
                    "?message=Email o password incorrecto" +
                    "&messageType=alert-danger ");
            } else {
                req.session.user = user.email;
                res.redirect("/users/list");
            }
        }).catch(error => {
            req.session.user = null;
            res.redirect("/users/login" +
                "?message=Se ha producido un error al buscar el usuario" +
                "&messageType=alert-danger ");
        })
    });


    app.get('/users/signup', function (req, res) {
        res.render("signup.twig");
    })

    app.post('/users/signup', async function (req, res) {
        let securePassword = app.get("crypto").createHmac('sha256', app.get('clave'))
            .update(req.body.password).digest('hex');
        let user = {
            email: req.body.email,
            password: securePassword,
            rol: "STANDARD"
        }
        await validateUser(user).then(result => {


            if (result.length > 0) {
                let url = ""
                for (error in result) {
                    url += "&message=" + result[error] + "&messageType=alert-danger "
                }
                res.redirect("/users/signup?" + url);
                return
            }
            usersRepository.insertUser(user).then(userId => {
                res.redirect("/users/login?message=Nuevo usuario registrado&messageType=alert-info");
            }).catch(error => {
                res.redirect("/users/signup?message=Se ha producido un error al registrar usuario&messageType=alert-danger");
            });
        });
    })

    async function validateUser(user) {
        let errors = [];
        if (user.email == null || user.email == "") {
            errors.push("El email es obligatorio");
        }
        if (user.password == null || user.password == "") {
            errors.push("El password es obligatorio");
        }
        //check that the email format is correct
        let emailRegex = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        if (!emailRegex.test(user.email)) {
            errors.push("El email no tiene un formato correcto");
        }
        let userfound = await usersRepository.findUser({email: user.email});

        if (userfound != null) {
            errors.push("El email ya existe");
        }
        return errors;
    }

    app.get('/users/logout', function (req, res) {
        req.session.user = null;
        res.redirect("/users/login?message=Usuario desconectado correctamente&messageType=alert-info");
    });


}
