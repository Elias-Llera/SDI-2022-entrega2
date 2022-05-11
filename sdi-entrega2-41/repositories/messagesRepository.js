module.exports = {

    mongoClient: null,
    app: null,

    init: function (app, mongoClient) {
        this.mongoClient = mongoClient;
        this.app = app;
    },

    getMessages: async function (filter,options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("entrega2");
            const collectionName = 'messages';
            const messagesCollection = database.collection(collectionName);
            return await messagesCollection.find(filter, options).toArray();
        } catch (error) {
            throw (error);
        }
    },

    insertMessage: async function (message) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("entrega2");
            const collectionName = 'messages';
            const messagesCollection = database.collection(collectionName);
            return await messagesCollection.insertOne(message);
        } catch (error) {
            throw (error)
        }
    },

    /**
     */
    resetMessages: async function (messages) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("entrega2");
            const collectionName = 'messages';
            const messagesCollection = database.collection(collectionName);
            await messagesCollection.remove({});
            for (let message of messages){
                await messagesCollection.insertOne(message);
            }
            return true;
        } catch(error){
            throw error;
        }
    }
}