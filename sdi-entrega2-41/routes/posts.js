const {ObjectId} = require("mongodb");

module.exports = function (app, postsRepository) {

    app.get("/posts/:userId", function(req, res){

        let filter = { author: ObjectId(req.params.id)};
        let options = {};

        let page = parseInt(req.query.page);
        if (typeof req.query.page === "undefined" || req.query.page === null || req.query.page === "0") {
            page = 1;
        }

        checkCanSeePostsFrom(ObjectId(req.params.id), req.session.user)
            .then( canSee => {
                if(canSee){
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
            }).catch( error => {
                res.send("Error: " + error);
        });
    });

    async function checkCanSeePostsFrom(user, registeredUser){
        if(user === registeredUser){
            return true;
        }

        //Check if friends here

    }

}
