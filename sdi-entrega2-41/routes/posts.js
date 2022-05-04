const {ObjectId} = require("mongodb");

module.exports = function (app, postsRepository, friendshipsRepository) {

    /**
     *
     */
    app.get("/posts/:userId", function(req, res) {
        let filter = { author: ObjectId(req.params.userId)};
        let options = {};

        let page = parseInt(req.query.page);
        if (typeof req.query.page === "undefined" || req.query.page === null || req.query.page === "0") {
            page = 1;
        }

        let canSee = checkCanSeePostsFrom(ObjectId(req.params.userId), req.session.user)
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
                        userID: req.params.userId,
                        pages: pages,
                        currentPage: page
                    }
                    res.render("posts/list.twig", response);
                })
                .catch( error => {
                    res.send("Error: " + error);
                })
        } else {
            res.send("Acceso no autorizado");
        }
    });

    /**
     *
     * @param user
     * @param registeredUser
     * @returns {Promise<boolean>}
     */
    async function checkCanSeePostsFrom(user, registeredUser){
        if(user === registeredUser)
            return true;
        let filter = f=>{return f.state==="ACCEPTED" && (f.sender===registeredUser || f.receiver===registeredUser)};
        let friendships = await friendshipsRepository.getFriendships(filter, {})
        return friendships.length > 0;
    }

    /**
     *
     */
    app.get("/posts/add", function(req, res){
        res.render("posts/add.twig");
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
                .catch(error =>
                    res.send("Error: " + error)
                );
        } else {
            res.send("Errores en el formulario: " + errors)
        }
    });

    /**
     *
     * @param post
     * @returns {*[]}
     */
    function validatePost(post){
        let errors = [];
        if(post.title === 'undefined' || post.title.toString().trim().length === 0)
            errors.push("Error al insertar la publicación: título vacío.")
        if(post.title.toString().trim().length >= 20)
            errors.push("Error al insertar la publicación: el título no debe superar los 20 caracteres.")
        if(post.content === 'undefined' || post.content.text.toString().trim().length === 0)
            errors.push("Error al insertar la publicación: contenido vacío.")
        if(post.content.toString().trim().length < 10 || post.content.toString().trim().length > 300)
            errors.push("Error al insertar la publicación: el contenido debe tener entre 10 y 300 caracteres.")
        return errors;
    }

}
