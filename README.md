# Agile Express

Agile Express is a comprehensive backend system for an agile project management tool, built with Java and the Spring Boot framework. It provides a robust, scalable, and feature-rich foundation for managing projects, sprints, tasks, and users, following Clean Architecture and Domain-Driven Design principles.

The system incorporates a wide range of modern technologies, including multiple authentication methods, an event-driven architecture with a message broker, and a distributed search engine, all containerized for easy setup and deployment.

## Key Features

*   **Project Management**: Full CRUD capabilities for projects, including management of details like name, description, duration, and team composition.
*   **Sprint & Task Management**: Create and manage sprints within projects. Add, update, delete, and assign tasks. Move tasks between the backlog and sprints.
*   **Role-Based Access Control (RBAC)**: Fine-grained permissions with pre-defined roles (`ADMIN`, `MANAGER`, `TEAM_LEAD`, `MEMBER`) enforced via AOP.
*   **Multi-Provider Authentication**:
    *   **LDAP**: Authenticate users against an OpenLDAP directory.
    *   **Google OAuth2**: Secure sign-in using Google accounts.
    *   **JWT Security**: Stateless authentication using JSON Web Tokens with support for refresh tokens to maintain user sessions securely.
*   **Event-Driven Architecture**: Uses RabbitMQ to handle asynchronous operations, such as updating the search index when projects or tasks are modified, ensuring a decoupled and resilient system.
*   **Advanced Search**: Integrated with MongoDB to provide fast, full-text search capabilities across all projects and tasks.
*   **Caching**: Leverages Redis for caching frequently accessed data, improving application performance and response times.
*   **Scheduled Notifications**: A scheduled service sends daily email reminders for sprint-related events.
*   **API Documentation**: Comprehensive and interactive API documentation provided via OpenAPI (Swagger).

## Tech Stack

*   **Backend**: Java 17, Spring Boot 3
*   **Databases**:
    *   **PostgreSQL**: Primary relational database for core application data.
    *   **MongoDB**: Document database for the search index.
*   **Authentication**: Spring Security, LDAP, Google OAuth2, JWT
*   **Messaging**: RabbitMQ
*   **Caching**: Redis
*   **Containerization**: Docker, Docker Compose
*   **Build Tool**: Gradle
*   **API Documentation**: SpringDoc (OpenAPI 3)

## Getting Started

### Prerequisites

*   Docker
*   Docker Compose

### Running the Environment

The entire application stack, including databases and message brokers, is containerized and can be launched with a single command.

1.  **Clone the repository:**
    ```sh
    git clone https://github.com/sinandogans/agile-express.git
    cd agile-express
    ```

2.  **Start all services using Docker Compose:**
    ```sh
    docker-compose up -d
    ```
    This command will build and start the following services:
    *   `postgres`: PostgreSQL database on port `5440`.
    *   `mongo`: MongoDB database on port `27017`.
    *   `redis`: Redis cache on port `6379`.
    *   `rabbitmq`: RabbitMQ message broker on ports `5672` (AMQP) and `15672` (Management UI).
    *   `openldap`: OpenLDAP server for user authentication on port `389`.
    *   `phpldapadmin`: A web interface for managing the LDAP server on port `6443`.

    The LDAP server is automatically populated with test users from the `ldap/user.ldif` file on its first run.

3.  **Run the Spring Boot Application:**
    You can run the application either from your IDE by running the `AgileExpressApplication` class or by using the Gradle wrapper:
    ```sh
    ./gradlew bootRun
    ```
    The application will start on `http://localhost:8080` and connect to the services running in Docker.

### Accessing Services

*   **Agile Express API**: `http://localhost:8080`
*   **Swagger API Docs**: `http://localhost:8080/swagger-ui.html`
*   **RabbitMQ Management**: `http://localhost:15672` (user: `admin`, pass: `password123`)
*   **phpLDAPadmin**: `http://localhost:6443`

## Project Structure

The project follows a layered architecture to separate concerns and improve maintainability.

*   `domain`: Contains the core business logic, aggregates (Project, Task), enums, and domain-specific exceptions. This layer has no external dependencies.
*   `application`: Defines application services, DTOs, repository interfaces, and business exceptions. It orchestrates the domain layer to perform application-specific use cases.
*   `infrastructure`: Provides concrete implementations for the interfaces defined in the application layer. This includes database repositories (JPA, MongoDB), security configurations, message queue publishers/consumers, and other external service integrations.
*   `webapi`: The outermost layer, containing REST controllers that expose the application's functionality via an HTTP API. It also includes OpenAPI configurations.

## API Overview

The API is secured with JWT and provides endpoints for managing all aspects of the application. Refer to the Swagger UI for detailed documentation on all available endpoints.

*   `/api/auth/**`: Endpoints for user authentication, including LDAP login, Google login, and token refreshing.
*   `/api/projects/**`: Endpoints for CRUD operations on projects, sprints, and tasks.
*   `/api/users/**`: Endpoint to retrieve a list of all users from the LDAP directory.
*   `/api/search`: A unified endpoint for searching across projects and tasks.
