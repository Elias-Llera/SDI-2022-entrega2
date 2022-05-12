module.exports = function (app, usersRepository, friendshipsRepository, postsRepository) {


    /**
     *
     */
    app.get("/initbd", async function(req, res){

        let users = new Array();
        let name = "";
        for(i=1; i <16; i++) {

            if(i < 10)
                name = "user0"+i
            else
                name = "user" + i

            users.push({
                email: name+"@email.com",
                rol: "STANDARD",
                name:name,
                surname:name,
                password:app.get("crypto").createHmac('sha256', app.get('clave'))
                    .update(name).digest('hex')
            });
        }

        users.push({
            email: "admin@email.com",
            rol: "ADMIN",
            name: "admin",
            surname: "admin",
            password: app.get("crypto").createHmac('sha256', app.get('clave'))
                .update("admin").digest('hex')
        })

        await usersRepository.resetUsers(users);

        let friendships = new Array();

        friendships.push({
            sender:"user01@email.com",
            receiver:"user02@email.com",
            status:"ACCEPTED"
        })

        friendships.push({
            sender:"user01@email.com",
            receiver:"user06@email.com",
            status:"ACCEPTED"
        })

        friendships.push({
            sender:"user01@email.com",
            receiver:"user03@email.com",
            status:"PENDING"
        })

        friendships.push({
            sender:"user01@email.com",
            receiver:"user04@email.com",
            status:"PENDING"
        })

        friendships.push({
            sender:"user02@email.com",
            receiver:"user03@email.com",
            status:"PENDING"
        })

        friendships.push({
            sender:"user04@email.com",
            receiver:"user03@email.com",
            status:"PENDING"
        })

        await friendshipsRepository.resetFriendships(friendships);

        let posts = new Array();

        posts.push({
            title:"Post 1",
            content:"Content for post 1",
            author:"user01@email.com",
            date:"7/8/2022"
        })

        posts.push({
            title:"Post 2",
            content:"Content for post 2",
            author:"user01@email.com",
            date:"7/8/2022"
        })

        posts.push({
            title:"Post 3",
            content:"Content for post 3",
            author:"user01@email.com",
            date:"7/8/2022"
        })

        await postsRepository.resetPosts(posts);

        res.render("login.twig");

    });



}