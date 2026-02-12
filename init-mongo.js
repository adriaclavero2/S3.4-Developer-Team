db = db.getSiblingDB(process.env.MONGO_INITDB_DATABASE || 'calendar_app');

db.createUser({
    user: 'app_user',
    pwd: 'app_password',
    roles: [{
        role: 'readWrite',
        db: process.env.MONGO_INITDB_DATABASE || 'calendar_app'
    }]
});

db.createCollection('health_check');
db.createCollection('tasks');
db.createCollection('notes');
db.createCollection('events');

db.health_check.insertOne({
    status: 'ok',
    timestamp: new Date(),
    message: 'Database successfully initialized'});

print('MongoDB successfully initialized.');