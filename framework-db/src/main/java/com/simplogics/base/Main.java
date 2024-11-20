package com.simplogics.base;

import org.flywaydb.core.Flyway;

public class Main {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Please provide DB_URL, DB_USERNAME, and DB_PASSWORD.");
            return;
        }

        String dbUrl = args[0];
        String dbUsername = args[1];
        String dbPassword = args[2];

        Flyway flyway = Flyway.configure()
                .dataSource(dbUrl, dbUsername, dbPassword)
                .load();
        flyway.migrate();

        System.out.println("Migration completed successfully.");
    }
}