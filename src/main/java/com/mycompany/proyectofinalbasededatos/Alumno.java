package com.mycompany.proyectofinalbasededatos;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

import static com.mycompany.proyectofinalbasededatos.ConexionBD.connection;

public class Alumno implements Serializable {

    private static final Scanner sc = new Scanner(System.in);
    private static  String dni,nombre,fechaNac,direccion;

    public static void introducirAlumno(){
        System.out.println("Introduce los datos del alumno a introducir:");
        System.out.println("DNI:");
        dni = sc.nextLine();
        System.out.println("Nombre del Alumno");
        nombre = sc.nextLine();
        System.out.println("Fecha de nacimiento:");
        fechaNac = sc.nextLine();
        System.out.println("Direccion:");
        direccion = sc.nextLine();

        String query = "INSERT INTO alumnos (DNI, nombre, fechaNac, direccion) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, dni);
            statement.setString(2, nombre);
            statement.setString(3, fechaNac);
            statement.setString(4, direccion);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void volcar(){

    }
    public static void mostrar() {

    }

}