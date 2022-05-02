const {ObjectId} = require("mongodb");

module.exports = function (app, usersRepository) {

    app.get("/users", function (req, res) {
        let filter = u=>{return u.rol !== "ADMIN" && u._id !== ObjectId(req.session.user)};
        let options = {};

        let page = parseInt(req.query.page);
        if (typeof req.query.page === "undefined" || req.query.page === null || req.query.page === "0") {
            page = 1;
        }

        usersRepository.getUsersPg(filter, options)
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
                let response = {
                    users: result.posts,
                    pages: pages,
                    currentPage: page
                }
                res.render("users/list.twig", response);
            })
            .catch( error =>
                res.send("Error: " + error)
            );
    });

    app.get('/users/login', function (req, res) {
        res.render("login.twig");
    });

    app.post('/users/login',function (req,res) {
        let securePassword = app.get("crypto").createHmac('sha256',app.get('clave'))
            .update(req.body.password).digest('hex');
        let filter = {
            email: req.body.email,
            password: securePassword
        }
        let options = {}
        usersRepository.findUser(filter,options).then(user => {
            if(user == null){
                req.session.user = null;
                res.redirect("/users/login" +
                    "?message=Email o password incorrecto"+
                    "&messageType=alert-danger ");
            }else{
                req.session.user = user.email;
                res.redirect("/users/list");
            }
        }).catch( () => {
            req.session.user = null;
            res.redirect("/users/login" +
                "?message=Se ha producido un error al buscar el usuario"+
                "&messageType=alert-danger ");
        })
    });

}
