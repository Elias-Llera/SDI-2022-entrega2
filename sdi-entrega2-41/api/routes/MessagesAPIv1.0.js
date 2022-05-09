<<<<<<< HEAD
const {decode} = require("jsonwebtoken");
module.exports = function (app,friendshipsRepository,messagesRepository) {
    const {decode} = require("jsonwebtoken");

=======
module.exports = function (app,friendshipsRepository,messagesRepository) {
    const {decode} = require("jsonwebtoken");

    app.post('/api/v1.0/messages', function (req, res) {
        var message = req.body;
        message.leido = false;

        //Obtenemos el usuario usando su token
        let user = decode(req.cookies.token).user;

        if(!user){
            res.status(401).send({error: "Unauthorized"});
        }
        let filter = {$or: [{sender: user.id, receiver: message.receiver},{sender: message.receiver, receiver: user.id}]};
        friendshipsRepository.findFriendship(filter).then(function (friendship) {
                if(friendship && friendship.state === "ACCEPTED"){
                    messagesRepository.insertMessage(message).then(function (message) {
                            res.status(201).send(message);
                        }
                        ,function (error) {
                            res.status(500).send({error: error.message});
                        });
                }
                else{
                    res.status(403).send({error: "Tienes que ser amigo del usuario para enviarle un mensaje"});
                }
            }
            ,function (error) {
                res.status(500).send({error: error.message});
            });
    });

    app.get('/api/v1.0/messages', function (req, res) {

    });
}