module.exports = {

    mongoClient: null,
    app: null,

    init: function (app, mongoClient) {
        this.mongoClient = mongoClient;
        this.app = app;
    },

    getFriendships: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("social");
            const collectionName = 'friendships';
            const friendshipsCollection = database.collection(collectionName);
            return await friendshipsCollection.find(filter, options).toArray();
        } catch (error) {
            throw (error);
        }
    },

    getFriendshipsPg: async function (filter, options, page) {
        try {
            const limit = 4;
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("social");
            const collectionName = 'friendships';
            const friendshipsCollection = database.collection(collectionName);
            const friendshipsCollectionCount = await friendshipsCollection.count();
            const cursor = friendshipsCollection.find(filter, options).skip((page - 1) * limit).limit(limit)
            const friendships = await cursor.toArray();
            return {friendships: friendships, total: friendshipsCollectionCount};
        } catch (error) {
            throw (error);
        }
    },

    findFriendship: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("social");
            const collectionName = 'friendships';
            const friendshipsCollection = database.collection(collectionName);
            return await friendshipsCollection.findOne(filter, options);
        } catch (error) {
            throw (error);
        }
    },

    insertFriendship: async function (friendship) {
        try{
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("social");
            const collectionName = 'friendships';
            const friendshipsCollection = database.collection(collectionName);
            const result = await friendshipsCollection.insertOne(friendship);
            return result.insertedId;
        } catch(error) {
            throw (error);
        }
    },

    deleteFriendship: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("social");
            const collectionName = 'friendships';
            const friendshipsCollection = database.collection(collectionName);
            return await friendshipsCollection.deleteOne(filter, options);
        } catch (error) {
            throw (error);
        }
    },

};