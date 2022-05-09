const express = require('express');
const userSessionRouter = express.Router();
const usersRepository = require("../repositories/usersRepository");

userSessionRouter.use(function(req, res, next) {
    if ( req.session.user ) {
        usersRepository.findUser({ email: req.session.user}, {}).then(result =>
        {
            if(result.rol == "ADMIN") {
                next();
            } else {
                res.redirect("/users/list")
            }
        }).catch(error => res.redirect("users/login"));
    } else {
        res.redirect("/users/login");
    }
});
module.exports = userSessionRouter;