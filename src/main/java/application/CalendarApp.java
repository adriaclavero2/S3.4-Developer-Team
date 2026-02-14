package application;

public class CalendarApp {
    public static void main(String[] args) {
        System.out.println();
        try {
            // Esto dispara todo el sistema de builders y config que hiciste
            var db = infrastructure.mongo.connection.MongoDBConnection.getDatabase();

            // Si llegamos aquí, la conexión es real
            System.out.println("✅ CONECTADO AL CONTENEDOR DOCKER");
            System.out.println("🔌 Trabajando en la DB: " + db.getName());

            // Una pequeña prueba extra: listar colecciones
            for (String name : db.listCollectionNames()) {
                System.out.println("📁 Colección encontrada: " + name);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
