package dev.jhon0206.temansys.config;

import dev.jhon0206.temansys.exception.DataAccessException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private final String url;
    private final String user;
    private final String password;

    private DatabaseConnection() {
        Properties props = new Properties();
        try (InputStream in = DatabaseConnection.class.getResourceAsStream("/db.properties")) {
            if (in == null) {
                throw new DataAccessException("db.properties not found in classpath", null);
            }
            props.load(in);
        } catch (IOException e) {
            throw new DataAccessException("Cannot read db.properties", e);
        }
        this.url = env("DB_URL", props.getProperty("db.url"));
        this.user = env("DB_USER", props.getProperty("db.user"));
        this.password = env("DB_PASSWORD", props.getProperty("db.password", ""));
    }

    private static final class Holder {
        private static final DatabaseConnection INSTANCE = new DatabaseConnection();
    }

    public static DatabaseConnection getInstance() {
        return Holder.INSTANCE;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    private static String env(String name, String fallback) {
        String value = System.getenv(name);
        return (value != null && !value.isBlank()) ? value : fallback;
    }
}
