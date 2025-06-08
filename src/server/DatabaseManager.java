package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String URL = "jdbc:postgresql://localhost:5432/studs";
    private static final String USER = "s465500";
    private static final String PASSWORD = "RgmYBjsjqC9odMSL";

    private Connection connection;

    public DatabaseManager() throws SQLException {
        this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
    }


    public Connection getConnection() {
        return connection;
    }

}

