package com.mycompany.proyectofinalbasededatos;

import org.mariadb.jdbc.Connection;
import org.mariadb.jdbc.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static String usuarioActual;
    private static final String URL = "jdbc:mariadb://localhost:3306/gestionacademica";
    private static final String USER = "root";
    private static final String PASSWORD = "admin";
    private static Statement statement;

    static Connection connection;

    public static void conectar() throws ClassNotFoundException, SQLException {
        Class.forName("org.mariadb.jdbc.Driver");
        connection = (Connection) DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void desconectar() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
