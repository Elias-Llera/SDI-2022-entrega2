module.exports = function (app, usersRepository) {

    /**
     *
     */
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
                    session: req.session,
                    search: req.query.search
                }
                res.render("users/list.twig", response);
            })
            .catch( () =>
                res.redirect("/" +
                    "?message=Ha ocurrido un error al listar los usuarios." +
                    "&messageType=alert-danger ")
            );
    });


    app.get("/users/admin/list", function (req, res) {
        let filter = {};
        let options = {sort: {email: 1}};

        usersRepository.getUsers(filter, options)
            .then(result => {

                let response = {
                    users: result.users,
                    session: req.session,
                    search: req.query.search
                }
                res.render("users/admin/list.twig", response);
            })
            .catch( () =>
                res.redirect("/" +
                    "?message=Ha ocurrido un error al listar los usuarios." +
                    "&messageType=alert-danger ")
            );
    });

    app.get('/users/delete', function (req, res) {
        var list = [];

        if (req.query.deleteList != null && req.query.deleteList != undefined) {
            if (!Array.isArray(req.query.deleteList)) {
                list[0] = req.query.deleteList;
            } else {
                list = req.query.deleteList;
            }

            for (const listElement of list) {
                deleteUser(listElement, res);
            }
        }

        usersRepository.getUsers({}, { sort: {email: 1}})
            .then(result => {

                let response = {
                    users: result.users,
                    session: req.session,
                    search: req.query.search
                }
                res.redirect("/users/admin/list");
            })
            .catch( () =>
                res.redirect("/" +
                    "?message=Ha ocurrido un error al obtener los usuarios." +
                    "&messageType=alert-danger ")
            );
    });

    function deleteUser(userId, res) {
        usersRepository.deleteUser({_id: ObjectId(userId)}, {}).then(result => {
            if (result == null || result.deletedCount == 0) {
                res.write("No se ha podido eliminar el registro");
            }
            res.end();
        }).catch( () => {
            res.redirect("/" +
                "?message=Ha ocurrido un error al eliminar usuarios." +
                "&messageType=alert-danger ")
        });
    }

    /**
     *
     */
    app.get('/users/login', function (req, res) {
        res.render("login.twig");
    });

    /**
     *
     */
    app.post('/users/login',function (req,res) {
        let securePassword = app.get("crypto").createHmac('sha256',app.get('clave'))
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
        }).catch( () => {
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
            name: req.body.name,
            surname: req.body.surname,
            rol: "STANDARD"
        }
        await validateUser(user,req.body.password,req.body.passwordConfirm).then(result => {


            if (result.length > 0) {
                let url = ""
                for (error in result) {
                    url += "&message=" + result[error] + "&messageType=alert-danger "
                }
                res.redirect("/users/signup?" + url);
                return
            }
            usersRepository.insertUser(user).then( () => {
                res.redirect("/users/login?message=Nuevo usuario registrado&messageType=alert-info");
            }).catch( () => {
                res.redirect("/users/signup?message=Se ha producido un error al registrar usuario&messageType=alert-danger");
            });
        });
    })

    async function validateUser(user,originalPassword,confirmPassword) {
        let errors = [];
        if (user.email == null || user.email == "") {
            errors.push("El email es obligatorio");
        }
        if (user.password == null || user.password == "") {
            errors.push("El password es obligatorio");
        }
        if (user.name == null || user.name == "") {
            errors.push("El nombre es obligatorio");
        }
        if (user.surname == null || user.surname == "") {
            errors.push("El apellido es obligatorio");
        }
        if(originalPassword != confirmPassword){
            errors.push("Las contraseñas no coinciden");
        }
        //check that the email format is correct
        let emailRegex = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        if (!emailRegex.test(user.email)) {
            errors.push("El email no tiene un formato correcto");
        }
        let userFound = await usersRepository.findUser({email: user.email}, {});

        if (userFound != null) {
            errors.push("El email ya existe");
        }
        return errors;
    }

    app.get('/users/logout', function (req, res) {
        req.session.user = null;
        res.redirect("/users/login?message=Usuario desconectado correctamente&messageType=alert-info");
    });



}
