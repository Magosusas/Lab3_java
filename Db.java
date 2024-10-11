import java.sql.SQLException;
import java.sql.*;

public class Db {
    private Connection connection;

    // Підключення до бази даних
    public Db(String url, String user, String password) throws SQLException {
        this.connection = DriverManager.getConnection(url, user, password);
    }

    // Перевірка логіна та пароля
    public boolean authenticatePlayer(String username, String password) {
        String query = "SELECT * FROM players WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Якщо знайдено користувача, повертаємо true
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Закриття з'єднання з базою даних
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}