// 第6章: JDBCによるデータベース連携
import java.sql.*;

public class Chapter06_JDBC {
    public static void main(String[] args) {
        // メモリ上で動作するH2データベース等のURL (実行環境にドライバがないとエラーになります)
        String url = "jdbc:h2:mem:testdb"; 
        String user = "sa";
        String password = "";

        // DriverManager を使用した接続 (ドライバが存在しない場合は SQLException がスローされます)
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {
            
            System.out.println("データベースに接続しました。");
            
            // テーブル作成
            stmt.execute("CREATE TABLE users (id INT PRIMARY KEY, name VARCHAR(50))");
            
            // PreparedStatement を使用した CRUD 操作
            String insertSql = "INSERT INTO users (id, name) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                pstmt.setInt(1, 1);
                pstmt.setString(2, "Alice");
                pstmt.executeUpdate();
            }

            // データの取得
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");
            while (rs.next()) {
                System.out.println("User: " + rs.getInt("id") + ", " + rs.getString("name"));
            }

        } catch (SQLException e) {
            System.out.println("JDBCドライバが見つからない、または接続に失敗しました: " + e.getMessage());
        }
    }
}
