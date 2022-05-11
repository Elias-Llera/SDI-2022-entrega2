module.exports = function (app, postsRepository, friendshipsRepository) {

    /**
     *
     */
    app.get("/posts/add", function(req, res){
        res.render("posts/add.twig", {session:req.session});
    });

    /**
     *
     */
    app.post("/posts/add", function(req, res){
        let post = {
            title: req.body.title,
            content: req.body.content,
            author: req.session.user,
            date: new Date(Date.now()).toLocaleDateString()
        };
        let errors = validatePost(post);
        if(errors.length === 0){
            postsRepository.insertPost(post)
                .then(
                    res.redirect("/posts/" + req.session.user)
                )
                .catch( () =>
                    res.redirect("/posts/" + req.session.user +
                        "?message=Se ha producido un error al insertar la publicación" +
                        "&messageType=alert-danger ")
                );
        } else {
            let url = ""
            for (error in errors) {
                url += "&message=" + errors[error] + "&messageType=alert-danger "
            }
            res.redirect("/posts/" + req.session.user + "?" + url);
        }
    });

    /**
     *
     */
    app.get("/posts/:userEmail", async function(req, res) {
        console.log(req.params.userEmail);
        let filter = { author: req.params.userEmail};
        let options = {};

        let page = parseInt(req.query.page);
        if (typeof req.query.page === "undefined" || req.query.page === null || req.query.page === "0") {
            page = 1;
        }

        let canSee = await checkCanSeePostsFrom(req.params.userEmail, req.session.user)
        if(canSee) {
            postsRepository.getPostsPg(filter, options, page)
                .then( result =>{
                    // Cálculos de paginación
                    let lastPage = result.total / 4;
                    if (result.total % 4 > 0) { // Decimales
                        lastPage = lastPage + 1;
                    }
                    let pages = []; // Indices de páginas a mostrar
                    for (let i = page - 2; i <= page + 2; i++) {
                        if (i > 0 && i <= lastPage) {
                            pages.push(i);
                        }
                    }
                    let response = {
                        posts: result.posts,
                        userEmail: req.params.userEmail,
                        session: req.session,
                        pages: pages,
                        currentPage: page
                    }
                    res.render("posts/list.twig", response);
                })
                .catch( () => {
                    res.redirect("/" +
                        "?message=Se ha producido un error al buscar las publicaciones" +
                        "&messageType=alert-danger ");
                })
        } else {
            let response = {
                message: "Acceso no autorizado",
                error : {status: 403,
                stack: "No tienes permisos para acceder a esta información."}
            }
            res.render("error.twig", response);
        }
    });

    /**
     *
     * @param user
     * @param registeredUser
     * @returns {Promise<boolean>}
     */
    async function checkCanSeePostsFrom(user, registeredUser){
        console.log(registeredUser)
        if(typeof registeredUser == 'undefined' || registeredUser == null || registeredUser.toString().trim().length == 0)
            return false
        if(user === registeredUser)
            return true;
        let filter = {status:"ACCEPTED", $or: [{sender: registeredUser, receiver: user}, {sender:user, receiver: registeredUser}] };
        let friendships = await friendshipsRepository.getFriendships(filter, {})
        console.log(friendships);
        return friendships.length > 0;
    }

    /**
     *
     * @param post
     * @returns {*[]}
     */
    function validatePost(post){
        let errors = [];
        if(typeof (post.title) === "undefined" || post.title.toString().trim().length === 0)
            errors.push("Error al insertar la publicación: título vacío.")
        if(post.title.toString().trim().length >= 20)
            errors.push("Error al insertar la publicación: el título no debe superar los 20 caracteres.")
        if(typeof (post.content) === "undefined" || post.content.toString().trim().length === 0)
            errors.push("Error al insertar la publicación: contenido vacío.")
        if(post.content.toString().trim().length < 10 || post.content.toString().length > 300)
            errors.push("Error al insertar la publicación: el contenido debe tener entre 10 y 300 caracteres.")
        return errors;
    }

}
