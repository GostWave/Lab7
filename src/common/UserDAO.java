package common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private final Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public Integer register(String login, String password) throws SQLException {
        String sql = "INSERT INTO users (login, password_hash) VALUES (?, ?) RETURNING id";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, login);
            stmt.setString(2, PasswordHasher.hash(password));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            } else {
                return null;
            }
        } catch (SQLException e) {
            return null;
        }
    }


    public Integer authenticate(String login, String password) throws SQLException {
        String sql = "SELECT id, password_hash FROM users WHERE login = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                if (storedHash.equals(PasswordHasher.hash(password))) {
                    return rs.getInt("id");
                }
            }
        }

        return null;
    }
}
