module.exports = function (app, friendshipsRepository, messagesRepository) {
    const {decode} = require("jsonwebtoken");
    const {ObjectId} = require("mongodb");

    app.post('/api/v1.0/messages/insert', function (req, res) {
        var message = req.body;
        message.read = false; // el mensaje por defecto es no leido
        message.date = Date.now(); // la fecha sería la actual
        message.sender = res.user; // obtenemos el usuario actual a partir del token

        // En caso de que no se encuentre usuario, estamos hablando de que la operación no se puede realizar
        if (!message.sender)
            res.status(401).send({error: "Unauthorized"});

        // En otro caso insertamos el mensaje. Para ello tenemos que comprobar que son amigos
        let friendshipsFilter = {
            status: "ACCEPTED",
            $or:[{sender: message.sender, receiver: message.receiver}, {sender: message.receiver, receiver: message.sender}]
        };
        friendshipsRepository.findFriendship(friendshipsFilter, {}).then(function (friendship) {
                if (friendship) // procedemos a insertar el mensaje
                    messagesRepository.insertMessage(message).then((message) => {
                            res.status(201).send(message);
                        }
                        , (error) => res.status(500).send({error: error.message}));
                else // Notificamos de que no hemos encontrado tal relación de amistad
                    res.status(403).send(
                        { error: "Tienes que ser amigo del usuario para enviarle un mensaje" }
                    );
            } // En caso de error...
            , (error) => res.status(500).send({error: error.message}));
    });

    app.post('/api/v1.0/messages', function (req, res) {
        let user1 = res.user;
        let user2 = req.body.user;
      
        // En caso de que no se encuentre usuario, estamos hablando de que la operación no se puede realizar
        if (!user1)
            res.status(401).send({error: "Unauthorized"});

        // En cualquier otro caso realizamos el proceso para obtener los mensajes
        let chatFilter = { $or: [{sender: user1, receiver: user2}, {sender: user2, receiver: user1}] };
        messagesRepository.getMessages(chatFilter, {}).then((messages) => {
            res.status(201).send({messages: messages});
        }, (error) => {
            res.status(500).send({error: error.message});
        });
    });

    app.put('/api/v1.0/messages/read/:id', function (req, res) {
        let msgId = ObjectId(req.params.id)
        let user = res.user;

        if (!user) {
            res.status(401);
            res.json({error: "Lectura no autorizada"});
        } else {

            messagesRepository.findMessage({_id: msgId}, {}).then(msg => {
                if (msg.receiver !== user) {
                    res.status(401);
                    res.json({error: "Lectura no autorizada"});
                } else {
                    messagesRepository.readMessage({_id: msgId}, {}).then(newMsg => {
                        res.status(204);
                        res.json(newMsg);
                    });
                }
            });
        }
    });
}