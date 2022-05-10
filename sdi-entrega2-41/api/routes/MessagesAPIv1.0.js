const {decode} = require("jsonwebtoken");
const {ObjectId} = require("mongodb");
module.exports = function (app, friendshipsRepository, messagesRepository) {
    const {decode} = require("jsonwebtoken");

    app.post('/api/v1.0/messages', function (req, res) {
        var message = req.body;
        message.leido = false;

        //Obtenemos el usuario usando su token
        let user = decode(req.cookies.token).user;

        if (!user) {
            res.status(401).send({error: "Unauthorized"});
            return;
        }
        let filter = {
            $or: [{sender: user.id, receiver: message.receiver}, {
                sender: message.receiver,
                receiver: user.id
            }]
        };
        friendshipsRepository.findFriendship(filter).then(function (friendship) {
                if (friendship && friendship.state === "ACCEPTED") {
                    messagesRepository.insertMessage(message).then(function (message) {
                            res.status(201).send(message);
                        }
                        , function (error) {
                            res.status(500).send({error: error.message});
                        });
                } else {
                    res.status(403).send({error: "Tienes que ser amigo del usuario para enviarle un mensaje"});
                }
            }
            , function (error) {
                res.status(500).send({error: error.message});
            });
    });

    app.put('/api/v1.0/messages/read/:id', function (req, res) {

        console.log(req.params.id)
        let msgId = ObjectId(req.params.id)

        //Obtenemos el usuario usando su token
        let token = req.headers['token'] || req.body.token || req.query.token;

        let user = decode(token).user;

        if (!user) {
            res.status(401);
            res.json({error: "Lectura no autorizada"});
        } else {

            messagesRepository.findMessage({_id: msgId}, {}).then(msg => {
                if (msg.receiver !== user) {
                    res.status(401);
                    res.json({error: "Lectura no autorizada"});
                } else {
                    msg.read = true;

                    messagesRepository.readMessage(msg, {}, {}).then(newMsg => {
                        res.status(204);
                        res.json(newMsg);
                    });
                }
            });
        }

    });
}
