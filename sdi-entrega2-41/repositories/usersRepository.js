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
    findUser: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("entrega2");
            const collectionName = 'users';
            const usersCollection = database.collection(collectionName);
            const user = await usersCollection.findOne(filter, options);
            return user;
        } catch (error) {
            throw (error);
        }
    },


    /**
     * @param filter
     * @param options
     * @returns {Promise<*>}
     */
    getUsers: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("entrega2");
            const collectionName = 'users';
            const usersCollection = database.collection(collectionName);
            return await usersCollection.find(filter, options).toArray();
        } catch (error) {
            throw (error);
        }
    },

    /**
     * @param filter
     * @param options
     * @param page
     * @returns {Promise<{total: *, users: *}>}
     */
    getUsersPg: async function (filter, options, page){
        try {
            const limit = 5;
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("entrega2");
            const collectionName = 'users';
            const usersCollection = database.collection(collectionName);
            const usersCollectionCount = await usersCollection.find(filter, options).count();
            const cursor = usersCollection.find(filter, options).skip((page - 1) * limit).limit(limit)
            const users = await cursor.toArray();
            return {users: users, total: usersCollectionCount };
        } catch (error) {
            throw (error);
        }
    },

    getUsers: async function (filter, options){
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("entrega2");
            const collectionName = 'users';
            const c = database.collection(collectionName);
            const usersCollection = await c.find(filter, options);
            const users = await usersCollection.toArray();
            return {users: users };
        } catch (error) {
            throw (error);
        }
    },

    /**
     *
     * @param user
     * @returns {Promise<any>}
     */
    insertUser: async function (user) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("entrega2");
            const collectionName = 'users';
            const usersCollection = database.collection(collectionName);
            const result = await usersCollection.insertOne(user);
            return result.insertedId;
        } catch (error) {
            throw (error);
        }
    },

    deleteUser: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("entrega2");
            const collectionName = 'users';
            const usersCollection = database.collection(collectionName);
            const result = await usersCollection.deleteOne(filter, options);
            return result;
        } catch (error) {
            throw (error);
        }
    },

    /**
     * Metodo para reestablecer los usuarios a los predeterminados. Usado para el testeo
     * @param usuarios  usuarios base que dejar en la aplicacion
     * @param funcionCallback true si no hay errores, false si los hay
     */
    resetUsers: async function (users) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("entrega2");
            const collectionName = 'users';
            const usersCollection = database.collection(collectionName);
            await usersCollection.remove({});
            for (let user of users){
                await usersCollection.insertOne(user);
            }
            return true;
        } catch(error){
            throw error;
        }
    }
};