# framework-db Module Overview

## Overview
The `framework-db` module is exclusively used for database migrations using Flyway. Flyway is an open-source database migration tool that allows developers to manage and track database schema changes in a versioned manner.

## Prerequisites
- Java Development Kit (JDK) 21 or higher
- Maven

## Getting Started

### Database Setup
1. **Initial Database Creation**:
    - The initial database creation scripts are available in `resources/db/setup`.
    - Run these scripts to create the initial database schema.

2. **Configuration**:
    - Ensure the database configurations are correctly set in the `pom.xml` file. Note that any changes made to the `pom.xml` file for local setup should not be checked into the repository.
    - You can specify database configuration via command-line arguments while running Flyway migrate:
      ```sh
      mvn flyway:migrate -DDB_URL=<url> -DDB_USERNAME=<name> -DDB_PASSWORD=<password>
      ```

### Migration Process
- The database migration scripts are stored in the `db/migrations` directory.
- To run the migrations, use the following Maven command:
  ```sh
  mvn clean compile flyway:migrate
