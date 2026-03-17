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

## Local Database with Docker (PostgreSQL)

### Prerequisites

- Docker Desktop

### Start the database

From the root of the repository, run:

```bash
docker compose up -d
```

If you have problems connecting to the database, you probably have a native PostgreSQL service running on your computer. Open a terminal with administrator rights and run: 
```bash
Stop-Service -Name postgresql-x64-18
```
To confirm that the service was stopped, run:
```bash
Get-Service -Name postgresql-x64-18 
```
## Code formatting

This project uses [Spotless](https://github.com/diffplug/spotless) with [Google Java Format](https://github.com/google/google-java-format) to enforce consistent code formatting.

- To check formatting, run: `mvn spotless:check`
- To automatically format the code, run: `mvn spotless:apply`

> Remember to run formatting before committing your code!

The build will fail if formatting violations are detected.
