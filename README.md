# Basic API

## Description:
This is a skeleton for a basic api project

Technologies:

    Java 17
    Spring Boot 3.3.3
    JUnit 5.
    Liquibase
    Swagger

Prerequisites:

    Java Development Kit (JDK) [17]
    Maven


## Build the project
mvn clean install


## Running the application with Maven
mvn spring-boot:run



## Running tests with Maven
mvn test


Swagger UI:
Access Swagger UI at http://localhost:8080/swagger-ui/index.html to explore the API documentation.

Database setup:

The project was configured to use h2 database. This can be changed as necessary.
Liquibase will handle database schema creation and migrations. Ensure you have the necessary database credentials configured in your application.properties or application.yml file.

