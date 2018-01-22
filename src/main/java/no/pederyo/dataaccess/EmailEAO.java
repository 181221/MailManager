package no.pederyo.dataaccess;

import java.sql.*;

public class EmailEAO {

    public static final String DRIVER = "jdbc:sqlite:";

    private static Connection connect() {
        // SQLite connection string
        String url = DRIVER + "mailmanager.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void getAlleMailer(int brukerId){
        String sql = "SELECT * FROM email e join bruker b on e.bruker_id = b.id WHERE e.id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){
            // set the value
            pstmt.setInt(1, brukerId);
            ResultSet rs  = pstmt.executeQuery();
            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("id"));
                System.out.println(rs.getString("username"));
                System.out.println(rs.getInt("bruker_id"));

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void insertEmail(String username, String passord, String salt, int mailtype, int bruker_id) {
        String sql = "INSERT INTO email (username, passord, salt, mailtype, bruker_id) VALUES (?,?,?,?,?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, passord);
            pstmt.setString(3, salt);
            pstmt.setInt(4, mailtype);
            pstmt.setInt(5, bruker_id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
