import java.sql.*;

public class DatabaseManager {

    // Thông tin kết nối cơ sở dữ liệu
    public static final String URL = "jdbc:mysql://localhost:3306/GuessNumberGame";  // Địa chỉ và tên cơ sở dữ liệu
    private static final String USER = "root";  // Tên người dùng MySQL của bạn
    private static final String PASSWORD = "2725";  // Mật khẩu MySQL của bạn

    // Phương thức kiểm tra kết nối
    public static void testConnection() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            if (conn != null) {
                System.out.println("Kết nối thành công!");
            }
        } catch (SQLException e) {
            System.out.println("Lỗi kết nối: " + e.getMessage());
        }
    }

    // Phương thức lưu lịch sử trò chơi vào cơ sở dữ liệu
    public static void saveGameHistory(String playerName, String result, int attempts) {
        // Câu lệnh SQL để lưu lịch sử trò chơi vào bảng game_history
        String query = "INSERT INTO game_history (player_name, result, attempts) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Thiết lập giá trị cho các tham số trong câu truy vấn
            stmt.setString(1, playerName);  // Tên người chơi
            stmt.setString(2, result);      // Kết quả của trò chơi (Win/Lose)
            stmt.setInt(3, attempts);       // Số lần đoán

            // Thực thi câu truy vấn
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Lịch sử trò chơi đã được lưu thành công!");
            } else {
                System.out.println("Không thể lưu lịch sử trò chơi.");
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi lưu lịch sử trò chơi: " + e.getMessage());
        }
    }

    // Phương thức lấy lịch sử trò chơi của người chơi theo tên
    public static void getPlayerHistory(String playerName) {
        String query = "SELECT * FROM game_history WHERE player_name = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Thiết lập tham số cho câu truy vấn
            stmt.setString(1, playerName);

            // Thực thi câu truy vấn và lấy kết quả
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String result = rs.getString("result");
                int attempts = rs.getInt("attempts");
                Timestamp date = rs.getTimestamp("date");

                // In ra thông tin lịch sử trò chơi
                System.out.println("ID: " + id + ", Player: " + playerName + ", Result: " + result +
                        ", Attempts: " + attempts + ", Date: " + date);
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy lịch sử trò chơi: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Kiểm tra kết nối cơ sở dữ liệu
        testConnection();

        // Ví dụ lưu lịch sử trò chơi
        saveGameHistory("Alice", "Win", 7);
        saveGameHistory("Bob", "Lose", 10);

        // Lấy và in ra lịch sử trò chơi của người chơi
        getPlayerHistory("Alice");
    }
}
