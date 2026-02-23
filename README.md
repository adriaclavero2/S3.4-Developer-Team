<div align="center">

# 📅 S3.4-Developer-Team - Agenda

**Desarrollado por:**
[Federico Praticò](https://github.com/federicopratico) • [Adrià Clavero Monteagudo](https://github.com/adriaclavero2) • [Federico Cantore](https://github.com/FedEx8525)

*(IT Academy Java Bootcamp - Proyecto Colaborativo)*

---

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-%234ea94b.svg?style=for-the-badge&logo=mongodb&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)
![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)
</div>

---

## 📖 Introducción

Bienvenido a nuestro **Proyecto**, una Agenda desarrollada en Java para la gestión de Tareas. Este proyecto ha sido diseñado bajo principios de **Clean Architecture**, permitiendo una total independencia entre la lógica de negocio y los motores de persistencia.  Además está pensada para la escalabilidad y la implementación de dos dominios más como note y event.

---

## 🏗️ Arquitectura del Proyecto

Nuestra aplicación se basa en una separación estricta de responsabilidades, facilitando el mantenimiento y la escalabilidad mediante una estructura organizada por **features** y **capas**.

### Jerarquía de Directorios

```
S3.4_Developer-Team
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── application
│   │   │   │   ├── config
│   │   │   │   └── menu
│   │   │   │     
│   │   │   ├── common
│   │   │   │   ├── config
│   │   │   │   │   └── builders
│   │   │   │   ├── exception   
│   │   │   │   ├── persistance   
│   │   │   │   └── utils   
│   │   │   │ 
│   │   │   ├── event 
│   │   │   │   ├── cli
│   │   │   │   ├── model   
│   │   │   │   ├── repository   
│   │   │   │   └── service 
│   │   │   │ 
│   │   │   ├── infrastructure 
│   │   │   │   ├── mongo
│   │   │   │   │     ├── connection
│   │   │   │   │     └── dao
│   │   │   │   └── MySQL
│   │   │   │
│   │   │   ├── note 
│   │   │   │   ├── cli
│   │   │   │   ├── model   
│   │   │   │   ├── repository   
│   │   │   │   └── service
│   │   │   │
│   │   │   └── task 
│   │   │       ├── applicacion
│   │   │       ├── dto   
│   │   │       ├── enums   
│   │   │       ├── mapper
│   │   │       ├── model
│   │   │       │   └── builderSteps
│   │   │       ├── repository
│   │   │       └── service 
│   │   │       
│   │   └── resources   
│   │         
│   └── test  
│       ├── application
│       │   └── menu 
│       │  
│       ├── infrastructure
│       │   └── mongo 
│       │       └── dao 
│       └── task
│           ├── mapper 
│           ├── model 
│           ├── repository
│           └── service
└── target
```
# 🚀 Funcionalidades Clave

### 🛠️ Gestión de Tareas (Task Module)
El módulo de tareas es el núcleo del sistema y destaca por su robustez:

* **Step Builder Pattern**: Implementado en `task.model.builderSteps`. Garantiza que cada objeto `Task` se construya siguiendo un flujo lógico obligatorio, evitando estados inconsistentes.
* **Lifecycle**: Control de estados (En progreso, Completada), prioridades y fechas de vencimiento.
* **Desacoplamiento**: Uso intensivo de **DTOs** y **Mappers** para separar el modelo de base de datos de la representación en la consola.

### 📅 Eventos y Notas
* **Eventos**: Gestión de actividades con fechas programadas y persistencia dedicada.
* **Notas**: Sistema ágil para almacenar información textual rápida.

---

## 💾 Persistencia Híbrida (Infrastructure)
### 💾 Persistencia Flexbile y Extensible

Actualmente, el sistema utiliza **MongoDB** como motor principal, aprovechando una persistencia **No-SQL** basada en documentos para gestionar estructuras de datos flexibles y dinámicas.

Lo más destacado de nuestra implementación es el nivel de desacoplamiento:
* **Abstracción Total**: Gracias al uso de interfaces en la capa `repository` y `DAO`, la lógica de negocio en la capa `service` es completamente agnóstica al motor de base de datos.
* **Escalabilidad**: Esta arquitectura permite integrar nuevos sistemas de persistencia (como MySQL o PostgreSQL) de forma transparente, sin necesidad de modificar el código existente en las capas superiores.
---

## 🛠️ Patrones de Diseño Aplicados
Para asegurar la calidad del software (**Clean Code**), hemos implementado:

1.  **Fluent/Step Builder**: Ubicado en `common.config.builders` y `task.model.builderSteps` para una creación de objetos legible y segura.
2.  **DAO (Data Access Object)**: Localizado en la capa de `infrastructure` para encapsular el acceso a datos.
3.  **Dependency Inversion**: Los servicios dependen de abstracciones, permitiendo intercambiar MySQL por Mongo sin tocar una sola línea de lógica de negocio.
4.  **Custom Exceptions**: Gestión de errores centralizada en `common.exception` para un feedback claro al usuario.

---
## 🛠️ Tecnologías utilizadas

| Tecnología   | Uso                               |
|:-------------|:----------------------------------|
| **Java 24**  | Lógica de negocio                 |
| **IntelliJ** | IDE de programación               |
| **Maven**    | Gestión del proyecto              |
| **MongoDB**  | Persistencia de datos NoSQL       |
| **Docker**   | Contenerización de bases de datos |
| **JUnit 5**  | Testing                           |
| **GitHub**   | Trabajo con Gitflow               |

## 🚦 Instalación y Ejecución

### Pasos para empezar
1.  **Levantar la infraestructura (Docker):**
    ```bash
    docker-compose up -d
    ```
2.  **Compilar el proyecto:**
    ```bash
    mvn clean install
    ```
3.  **Ejecutar la aplicación CLI:**
    ```bash
    java -jar target/S3.4_Developer-Team.jar
    ```

---

## 🧪 Estrategia de Testing

La fiabilidad es un pilar fundamental. Nuestra suite en `src/test` incluye:

* **Unit Tests**: Validación de `services` y `mappers` de forma aislada.
* **Infrastructure Tests**: Pruebas de integración para asegurar la conexión correcta con **Mongo** y **MySQL**.
* **Builder Validation**: Tests específicos para confirmar que el **Builder** lanza excepciones ante datos obligatorios ausentes.

Para ejecutar las pruebas:

```bash
mvn test
```
**Nota del Equipo:** Este proyecto ha sido desarrollado como parte del bootcamp de Java, enfocándose en la escalabilidad y el uso de patrones de diseño avanzados para entornos profesionales.