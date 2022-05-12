const {ObjectId} = require("mongodb");

module.exports = function (app, usersRepository, infoLogger) {

    /**
     * Funcionalidad listado de usuarios con busqueda y paginacion
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
                infoLogger.info(req.session.user + " -> Se accede a la lista de usuarios");
                res.render("users/list.twig", response);
            })
            .catch( () => {
                infoLogger.error("Ha habido un error al acceder a la lista de usuarios de administrador");
                res.redirect("/" +
                    "?message=Ha ocurrido un error al listar los usuarios." +
                    "&messageType=alert-danger ");
            });
    });

    /**
     * Listado de usuarios para administrador
     */
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
                infoLogger.warn(req.session.user + " -> Se accede a la lista de usuarios de administrador");
                res.render("users/admin/list.twig", response);
            })
            .catch( () => {
                infoLogger.error("Ha habido un error al acceder a la lista de usuarios de administrador");
                res.redirect("/" +
                    "?message=Ha ocurrido un error al listar los usuarios." +
                    "&messageType=alert-danger ");}
            );
    });

    /**
     * Funcionalidad borrado de usuarios
     */
    app.get('/users/delete', function (req, res) {
        infoLogger.warn("Se intenta borrar usuarios");
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

        infoLogger.info(req.session.user + " -> Borra usuarios");
        res.redirect("/users/admin/list");
    });

    /**
     * Funcion que borra un usuario de la base de datos
     * @param userId: _id del usuario que queremos borrar
     * @param res
     */
    function deleteUser(userId, res) {
        usersRepository.deleteUser({_id: ObjectId(userId)}, {}).then(result => {
            if (result == null || result.deletedCount == 0) {
                infoLogger.error("Ha habido un error al eliminar el usuario '" + userId + "'");
                res.write("No se ha podido eliminar el registro");
            }
            res.end();
        }).catch( () => {
            infoLogger.error("Ha habido un error al eliminar el usuario '" + userId + "'");
            res.redirect("/" +
                "?message=Ha ocurrido un error al eliminar usuarios." +
                "&messageType=alert-danger ")
        });
    }

    /**
     * Funcionalidad GET de login
     */
    app.get('/users/login', function (req, res) {
        infoLogger.info("Se accede al inicio de sesión");
        res.render("login.twig");
    });

    /**
     * POST de login
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
                infoLogger.warn("Se introducen incorrectamente los datos al intentar iniciar sesión");
                req.session.user = null;
                res.redirect("/users/login" +
                    "?message=Email o password incorrecto" +
                    "&messageType=alert-danger ");
            } else {
                infoLogger.info("El usuario '" + user.email + "' inicia sesión en el sistema");
                req.session.user = user.email;
                res.redirect("/users/list");
            }
        }).catch( () => {
            infoLogger.warn("Se produce un error al intentar iniciar sesión");
            req.session.user = null;
            res.redirect("/users/login" +
                "?message=Se ha producido un error al buscar el usuario" +
                "&messageType=alert-danger ");
        })
    });


    /**
     * Registro de usuarios GET
     */
    app.get('/users/signup', function (req, res) {
        infoLogger.info("Se accede al formulario de registro");
        res.render("signup.twig");
    })

    /**
     * Registro de usuarios POST
     */
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
                    infoLogger.warn("Ha habido un error al rellenar el formulario de registro (" + result[error] + ")");
                    url += "&message=" + result[error] + "&messageType=alert-danger "
                }
                res.redirect("/users/signup?" + url);
                return
            }
            usersRepository.insertUser(user).then( () => {
                infoLogger.info("El usuario '" + user.email + "' se ha registrado en el sistema");
                res.redirect("/users/login?message=Nuevo usuario registrado&messageType=alert-info");
            }).catch( () => {
                infoLogger.warn("Ha habido un error al registrar un usuario");
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
        infoLogger.info("El usuario " + req.session.user + " cierra sesión en el sistema");
        req.session.user = null;
        res.redirect("/users/login?message=Usuario desconectado correctamente&messageType=alert-info");
    });



}
