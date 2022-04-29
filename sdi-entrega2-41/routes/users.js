module.exports = function (app, usersRepository) {

    app.get('/users/login', function (req, res) {
        res.render("login.twig");
    })

    app.post('/users/login',function (req,res){
        let securePassword = app.get("crypto").createHmac('sha256',app.get('clave'))
            .update(req.body.password).digest('hex');
        let filter = {
            email: req.body.email,
            password: securePassword
        }
        let options = {}
        usersRepository.findUser(filter,options).then(user=>{
            if(user == null){
                req.session.user = null;
                res.redirect("/users/login" +
                    "?message=Email o password incorrecto"+
                    "&messageType=alert-danger ");
            }else{
                req.session.user = user.email;
                res.redirect("/users/list");
            }
        }).catch(error=>{
            req.session.user = null;
            res.redirect("/users/login" +
                "?message=Se ha producido un error al buscar el usuario"+
                "&messageType=alert-danger ");
        })

    })


}
