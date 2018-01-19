package no.pederyo.dataaccess;

import no.pederyo.modell.Bruker;

import java.sql.*;

public class BrukerEAO {

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

    public static void insertBruker(String name) {
        String sql = "INSERT INTO bruker(name) VALUES(?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static Bruker getBruker(String name){
        String sql = "SELECT * FROM bruker WHERE name = ?";
        Bruker b = null;
        try (Connection conn = connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            // set the value
            pstmt.setString(1, name);
            //
            ResultSet rs  = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                b = new Bruker(rs.getInt("id"),
                        rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return b;
    }


}
