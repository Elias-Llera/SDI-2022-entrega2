module.exports = {

    mongoClient: null,
    app: null,

    init: function (app, mongoClient) {
        this.mongoClient = mongoClient;
        this.app = app;
    },

    getPosts: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("social");
            const collectionName = 'posts';
            const postsCollection = database.collection(collectionName);
            return await postsCollection.find(filter, options).toArray();
        } catch (error) {
            throw (error);
        }
    },

    getPostsPg: async function (filter, options, page) {
        try {
            const limit = 4;
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("social");
            const collectionName = 'posts';
            const postsCollection = database.collection(collectionName);
            const postsCollectionCount = await postsCollection.count();
            const cursor = postsCollection.find(filter, options).skip((page - 1) * limit).limit(limit)
            const posts = await cursor.toArray();
            return {posts: posts, total: postsCollectionCount};
        } catch (error) {
            throw (error);
        }
    },

    findPost: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("social");
            const collectionName = 'posts';
            const postsCollection = database.collection(collectionName);
            return await postsCollection.findOne(filter, options);
        } catch (error) {
            throw (error);
        }
    },

    insertPost: async function (post) {
        try{
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("social");
            const collectionName = 'posts';
            const postsCollection = database.collection(collectionName);
            const result = await postsCollection.insertOne(post);
            return result.insertedId;
        } catch(error) {
            throw (error);
        }
    },

    deletePost: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("social");
            const collectionName = 'posts';
            const postsCollection = database.collection(collectionName);
            return await postsCollection.deleteOne(filter, options);
        } catch (error) {
            throw (error);
        }
    },

};