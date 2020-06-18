package io.skymind.pathmind.bddtests;

import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.*;

public class DbManager {

    private static EnvironmentVariables variables = SystemEnvironmentVariables.createEnvironmentVariables();
    private static String host = variables.getProperty("postgresql.host");
    private static String port = variables.getProperty("postgresql.port");
    private static String db = variables.getProperty("postgresql.db");
    private static String username = variables.getProperty("postgresql.username");
    private static String password = variables.getProperty("postgresql.password");
    private static String url = "jdbc:postgresql://" + host + ":" + port + "/" + db + "";
    private static String pathmindUsername = variables.getProperty("pathmind.username");
    private static String pathmindPassword = variables.getProperty("pathmind.password");
    private static Connection con = null;

    private static void createConnection() {
        try {
            con = DriverManager.getConnection(url, username, password);
            System.out.println("***Database Connected***");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void closeDataBaseConnection() {
        try {
            if (con != null && !con.isClosed()) {
                System.out.println("***Closing Database Connection***");
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createTestUser() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String pw = passwordEncoder.encode(pathmindPassword);
        System.out.println(pw);
        String query = "insert into pathmind_user (email, password, account_type, email_verified_at) values ('" + pathmindUsername + "', '" + pw + "', '0', '2020-02-02 20:24:48.067479') on conflict (email) do nothing;";
        System.out.println("DB query: " + query);

        createConnection();
        try {
            Statement st = con.createStatement();
            st.executeQuery(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            closeDataBaseConnection();
        }
    }
}
