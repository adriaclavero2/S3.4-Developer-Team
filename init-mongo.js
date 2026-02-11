db = db.getSibilingDB(process.env.MONGO_INIT_DATABASE || 'calendar_app');

db.createUser({
    user: 'app_user',
    pwd: 'app_password',
    roles: [{role: 'readWrite', db: process.env.MONGO_INIT_DATABASE}]
});

db.createCollection('health_check');
db.health_check.insertOne({status: 'ok', timestamp: new Date()});

print('MongoDB correctly initialized.');