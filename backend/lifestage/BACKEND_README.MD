# Backend run guide for Lifestage

## Prerequisites
- Java Development Kit (JDK) version 21 or higher
- Maven 3.6 or higher (or use the included Maven wrapper and write `./mvnw` instead of `mvn` in the commands below)

## Running the Backend
1. Clone the repository and navigate to the backend directory:
```bash
cd backend/lifestage
```

2. Run the application:
```bash
mvn spring-boot:run
```
This will start the Spring Boot application on the default port (8080). You can access the API endpoints at `http://localhost:8080`.
