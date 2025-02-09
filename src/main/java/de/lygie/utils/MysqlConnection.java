package de.lygie.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlConnection {

    public Connection getConnection() throws SQLException {


        String url = "jdbc:mysql://localhost:3306/dsmetest";
        String username = "lygie";
        String password = "lygie";

       // DriverManager.registerDriver(new com.mysql.jdbc.Driver ());


        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Database connected!");
            return connection;

        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }

    }

}
