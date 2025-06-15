package server.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String URL = "jdbc:postgresql://localhost:5432/lab7";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1";
//


    private Connection connection;

    public DatabaseManager() throws SQLException {
        this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
    }


    public Connection getConnection() {
        return connection;
    }

}

