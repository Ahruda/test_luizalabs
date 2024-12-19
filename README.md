# Test LuizaLabs


In this project, I used a clean architecture model, following some design patterns such as Command for Use Cases, Builder for object creation, and some Object-Oriented principles like dependency inversion.

The project was built with Spring Boot using Gradle. I utilized Jacoco to generate code coverage reports and Docker for containerization.

## Prerequisites

Before starting, make sure you have Docker installed on your machine.

You don't need Gradle installed on your machine.
There is already an executable in the project root to build the artifact.

## Steps to Run the Application
The Jacoco reports are already generated during the application's build.

1. **Compiling the Project**

   ```bash
   ./gradlew :infrastructure:bootjar

2. **Building and Starting the Application with Docker**

   ```bash
   docker compose build

   docker compose up

## Docs
Swagger:
http://localhost:8080/swagger-ui/index.html

Code Coverage reports: in path
orders/build/reports/jacoco/test/index.html
