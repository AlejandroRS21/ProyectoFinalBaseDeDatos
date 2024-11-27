package com.mycompany.proyectofinalbasededatos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


public class Matricula  {
    private static final Scanner sc = new Scanner(System.in);
    private int codMatricula,codAsignatura;
    private String dni;

    // Constructor
    public Matricula(int codMatric, String dni, int codAsig){
        this.codMatricula = codMatric;
        this.dni = dni;
        this.codAsignatura = codAsig;
    }
    // Getter
    public String getDni() {
        return dni;
    }
    public int getCodMatricula() {
        return codMatricula;
    }
    public int getCodAsignatura() {
        return codAsignatura;
    }
    // Setter
    public void setCodMatricula(int codMatricula) {
        this.codMatricula = codMatricula;
    }
    public void setDni(String dni) {
        this.dni = dni;
    }
    public void setCodAsignatura(int codAsignatura) {
        this.codAsignatura = codAsignatura;
    }

    public static void introducirMatricula() {
        try {
            System.out.println("Introduce el DNI del alumno:");
            String dni = sc.nextLine().trim();
            if (!alumnoExiste(dni)) {
                System.out.println("El alumno con DNI " + dni + " no existe.");
                return;
            }

            System.out.println("Introduce el código de asignatura:");
            int codAsignatura = Integer.parseInt(sc.nextLine().trim());
            if (!asignaturaExiste(codAsignatura)) {
                System.out.println("La asignatura con código " + codAsignatura + " no existe.");
                return;
            }

            if (matriculaExiste(dni, codAsignatura)) {
                System.out.println("El alumno con DNI " + dni + " ya está matriculado en la asignatura " + codAsignatura + ".");
                return;
            }

            System.out.println("Introduce el código de matrícula:");
            int codMatricula = Integer.parseInt(sc.nextLine().trim());
            insertarMatricula(codMatricula, dni, codAsignatura);
        } catch (NumberFormatException e) {
            System.out.println("Error: Introduce un número válido.");
        } catch (SQLException e) {
            System.out.println("Error al interactuar con la base de datos: " + e.getMessage());
        }
    }

    private static boolean alumnoExiste(String dni) throws SQLException {
        String query = "SELECT COUNT(*) FROM alumnos WHERE DNI = ?";
        try (PreparedStatement stmt = ConexionBD.connection.prepareStatement(query)) {
            stmt.setString(1, dni);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    private static boolean asignaturaExiste(int codAsignatura) throws SQLException {
        String query = "SELECT COUNT(*) FROM asignaturas WHERE CodigoAsignatura = ?";
        try (PreparedStatement stmt = ConexionBD.connection.prepareStatement(query)) {
            stmt.setInt(1, codAsignatura);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    private static boolean matriculaExiste(String dni, int codAsignatura) throws SQLException {
        String query = "SELECT COUNT(*) FROM matriculas WHERE DNI = ? AND CodigoAsignatura = ?";
        try (PreparedStatement stmt = ConexionBD.connection.prepareStatement(query)) {
            stmt.setString(1, dni);
            stmt.setInt(2, codAsignatura);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    private static void insertarMatricula(int codMatricula, String dni, int codAsignatura) throws SQLException {
        String query = "INSERT INTO matriculas (CodigoMatricula, DNI, CodigoAsignatura) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = ConexionBD.connection.prepareStatement(query)) {
            stmt.setInt(1, codMatricula);
            stmt.setString(2, dni);
            stmt.setInt(3, codAsignatura);
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Matrícula registrada con éxito.");
            } else {
                System.out.println("Error al registrar la matrícula.");
            }
        }
    }
}
