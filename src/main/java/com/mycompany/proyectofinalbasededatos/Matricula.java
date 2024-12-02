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
        String dni,mensaje = null;
        int codMatricula,
                codAsignatura;
        try {
            System.out.println("Introduce el DNI del alumno:");
            dni = sc.nextLine().trim();
            if (!alumnoExiste(dni)) {
                mensaje = "El alumno con DNI " + dni + " no existe.";
            } else{
                System.out.println("Introduce el código de asignatura:");
                codAsignatura = Integer.parseInt(sc.nextLine().trim());
                if (!asignaturaExiste(codAsignatura)) {
                    mensaje = "La asignatura con código " + codAsignatura + " no existe.";
                } else if (matriculaExiste(dni, codAsignatura)) {
                    mensaje = "El alumno con DNI " + dni + " ya está matriculado en la asignatura " + codAsignatura + ".";
                } else {
                    System.out.println("Introduce el código de matrícula:");
                    codMatricula = Integer.parseInt(sc.nextLine().trim());
                    insertarMatricula(codMatricula, dni, codAsignatura);
                }
            }
            if (mensaje != null){
                System.out.println(mensaje);
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Introduce un número válido.");
        } catch (SQLException e) {
            System.out.println("Error al interactuar con la base de datos: " + e.getMessage());
        }
    }

    private static boolean alumnoExiste(String dni) throws SQLException {
        String query = "SELECT COUNT(*) FROM alumnos WHERE DNI = ?";
        boolean existe = false;
        try (PreparedStatement stmt = ConexionBD.connection.prepareStatement(query)) {
            stmt.setString(1, dni);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    existe = rs.getInt(1) > 0;
                }
            }
        }
        return existe;
    }

    private static boolean asignaturaExiste(int codAsignatura) throws SQLException {
        String query = "SELECT COUNT(*) FROM asignaturas WHERE CodigoAsignatura = ?";
        boolean existe = false;
        try (PreparedStatement stmt = ConexionBD.connection.prepareStatement(query)) {
            stmt.setInt(1, codAsignatura);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    existe = rs.getInt(1) > 0;
                }
            }
        }
        return existe;
    }

    private static boolean matriculaExiste(String dni, int codAsignatura) throws SQLException {
        String query = "SELECT COUNT(*) FROM matriculas WHERE DNI = ? AND CodigoAsignatura = ?";
        boolean existe=false;
        try (PreparedStatement stmt = ConexionBD.connection.prepareStatement(query)) {
            stmt.setString(1, dni);
            stmt.setInt(2, codAsignatura);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    existe = rs.getInt(1) > 0;
                }
            }
        }
        return existe;
    }

    private static void insertarMatricula(int codMatricula, String dni, int codAsignatura) throws SQLException {
        String query = "INSERT INTO matriculas (CodigoMatricula, DNI, CodigoAsignatura) VALUES (?, ?, ?)";
        int rowsInserted;
        try (PreparedStatement stmt = ConexionBD.connection.prepareStatement(query)) {
            stmt.setInt(1, codMatricula);
            stmt.setString(2, dni);
            stmt.setInt(3, codAsignatura);
            rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Matrícula registrada con éxito.");
            } else {
                System.out.println("Error al registrar la matrícula.");
            }
        }
    }
}
