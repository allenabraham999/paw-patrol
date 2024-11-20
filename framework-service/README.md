# framework-service Module Overview

## Overview
The `framework-service` module implements the main server functionalities using Java Spring Boot and Maven. This is where the application logic resides.

## Prerequisites
- Java Development Kit (JDK) 21 or higher
- Maven

## Getting Started

### Server Setup
1. **Configuration**:
    - Application properties can be configured with profiles. Local properties should not be checked into the repository. Use `application-local.properties` in your local system to keep all environment configurations.
    - Ensure the necessary configurations are set in the `application.properties` or respective profile files.

2. **Running the Server**:
    - Build the entire project using Maven:
      ```sh
      mvn clean install
      ```
    - Then run the server using:
      ```sh
      mvn spring-boot:run
      ```

### Dependencies
- Check the `pom.xml` file for all necessary dependencies.

## Usage

### Creating New Controllers
- When creating new controllers, ensure that a success response returns:
  ```java
  ResponseEntity<FrameworkBaseResponse>
- Use the @APIResult annotation in all controllers to standardize API responses.


Please feel free to contact for further clarifications if needed.