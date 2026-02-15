package infrastructure.mongo.connection;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import common.config.DBType;
import common.config.DatabaseConfig;

public class MongoDBConnection {
    private static volatile MongoDatabase database;
    private static MongoClient mongoClient;

    private MongoDBConnection() {}

    public static MongoDatabase getDatabase() {
        if (database == null) {
            synchronized ( (MongoDBConnection.class)) {
                if (database == null) {
                    try {
                        String connectionString = DatabaseConfig.getConnectionString(DBType.MONGO);
                        String dbName = DatabaseConfig.getDbName(DBType.MONGO);

                        mongoClient = MongoClients.create(connectionString);
                        database = mongoClient.getDatabase(dbName);

                        System.out.println("Successful connection to MongoDB: " + dbName);
                    } catch (Exception e) {
                        throw new RuntimeException("Error connecting to MongoDB: ", e);
                    }
                }
            }
        }
        return database;
    }

    // .close() method is missing
    public static void close() {
        if(mongoClient != null) {
            mongoClient.close();
            System.out.println("Connection successfully close.");
        }
    }
}
