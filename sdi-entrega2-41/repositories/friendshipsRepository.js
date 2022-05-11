module.exports = {

    mongoClient: null,
    app: null,

    init: function (app, mongoClient) {
        this.mongoClient = mongoClient;
        this.app = app;
    },

    /**
     *
     * @param filter
     * @param options
     * @returns {Promise<*>}
     */
    getFriendships: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("entrega2");
            const collectionName = 'friendships';
            const friendshipsCollection = database.collection(collectionName);
            return await friendshipsCollection.find(filter, options).toArray();
        } catch (error) {
            throw (error);
        }
    },

    /**
     *
     * @param filter
     * @param options
     * @param page
     * @returns {Promise<{total: *, friendships: *}>}
     */
    getFriendshipsPg: async function (filter, options, page) {
        try {
            const limit = 4;
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("entrega2");
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

    /**
     *
     * @param filter
     * @param options
     * @returns {Promise<*>}
     */
    findFriendship: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("entrega2");
            const collectionName = 'friendships';
            const friendshipsCollection = database.collection(collectionName);
            return await friendshipsCollection.findOne(filter, options);
        } catch (error) {
            throw (error);
        }
    },

    /**
     *
     * @param friendship
     * @returns {Promise<any>}
     */
    insertFriendship: async function (friendship) {
        try{
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("entrega2");
            const collectionName = 'friendships';
            const friendshipsCollection = database.collection(collectionName);
            const result = await friendshipsCollection.insertOne(friendship);
            return result.insertedId;
        } catch(error) {
            throw (error);
        }
    },

    /**
     *
     * @param filter
     * @param options
     * @returns {Promise<*>}
     */
    deleteFriendship: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("entrega2");
            const collectionName = 'friendships';
            const friendshipsCollection = database.collection(collectionName);
            return await friendshipsCollection.deleteOne(filter, options);
        } catch (error) {
            throw (error);
        }
    },

    /**
     *
     * @param newFriendship
     * @param filter
     * @param options
     * @returns {Promise<*>}
     */
    updateFriendship: async function(newFriendship, filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("entrega2");
            const collectionName = 'friendships';
            const friendshipCollection = database.collection(collectionName);
            return await friendshipCollection.updateOne(filter, {$set: newFriendship}, options);
        } catch (error) {
            throw (error);
        }
    },

    /**
     */
    resetFriendships: async function (friendships) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("entrega2");
            const collectionName = 'friendships';
            const friendshipsCollection = database.collection(collectionName);
            await friendshipsCollection.remove({});
            await friendshipsCollection.insertMany(friendships);
            return true;
        } catch(error){
            throw error;
        }
    }

};