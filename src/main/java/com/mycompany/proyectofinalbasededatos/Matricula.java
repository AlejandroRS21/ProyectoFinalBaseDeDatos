package com.mycompany.proyectofinalbasededatos;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import static com.mycompany.proyectofinalbasededatos.ConexionBD.connection;

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
        int codMatricula = 0;
        String dni;
        int codAsignatura = 0;
        boolean datosValidos = true;


        System.out.println("Introduce DNI del alumno:");
        dni = sc.nextLine().trim();
        if (!dni.matches("\\d{8}[A-Za-z]")) {
            System.out.println("El DNI debe tener el formato correcto (8 dígitos seguidos de una letra).");
            datosValidos = false;
        }

        // Verificar si el alumno ya tiene una matrícula
        if (datosValidos) {
            String consultaVerificarMatricula = "SELECT CodigoMatricula FROM Matriculas WHERE DNI = ?";
            try (PreparedStatement stmVerificarMatricula = connection.prepareStatement(consultaVerificarMatricula)) {
                stmVerificarMatricula.setString(1, dni);

                try (ResultSet rs = stmVerificarMatricula.executeQuery()) {
                    if (rs.next()) {
                        // Si el alumno ya tiene una matrícula, obtener el código de matrícula existente
                        codMatricula = rs.getInt("CodigoMatricula");
                        System.out.println("El alumno ya tiene la matrícula con código: " + codMatricula);
                    } else {
                        // Si no tiene matrícula, se le asigna una nueva matrícula
                        System.out.println("Introduce código de matrícula:");
                        if (!sc.hasNextInt()) {
                            System.out.println("El código de matrícula debe ser un número entero.");
                            datosValidos = false;
                            sc.nextLine();
                        } else {
                            codMatricula = sc.nextInt();
                            sc.nextLine();
                        }

                        // Insertar la nueva matrícula
                        String consultaInsertarMatricula = "INSERT INTO Matriculas (CodigoMatricula, DNI) VALUES (?, ?)";
                        try (PreparedStatement stmInsertarMatricula = connection.prepareStatement(consultaInsertarMatricula)) {
                            stmInsertarMatricula.setInt(1, codMatricula);
                            stmInsertarMatricula.setString(2, dni);
                            stmInsertarMatricula.executeUpdate();
                            System.out.println("Nueva matrícula creada para el alumno con DNI: " + dni);
                        } catch (SQLException e) {
                            System.err.println("Error al insertar la matrícula: " + e.getMessage());
                            datosValidos = false;
                        }
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error al verificar la matrícula: " + e.getMessage());
                datosValidos = false;
            }
        }


        if (datosValidos) {
            System.out.println("Introduce código de asignatura:");
            if (!sc.hasNextInt()) {
                System.out.println("El código de asignatura debe ser un número entero.");
                datosValidos = false;
                sc.nextLine();
            } else {
                codAsignatura = sc.nextInt();
                sc.nextLine();
            }
        }

        // Verificar si el alumno ya está matriculado en esa asignatura
        if (datosValidos) {
            String consultaVerificarAsignatura = "SELECT COUNT(*) FROM MatriculasAsignaturas WHERE CodigoMatricula = ? AND CodigoAsignatura = ?";
            try (PreparedStatement stmVerificarAsignatura = connection.prepareStatement(consultaVerificarAsignatura)) {
                stmVerificarAsignatura.setInt(1, codMatricula);
                stmVerificarAsignatura.setInt(2, codAsignatura);

                try (ResultSet rs = stmVerificarAsignatura.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        System.out.println("El alumno ya está matriculado en esta asignatura.");
                        datosValidos = false;
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error al verificar la asignatura: " + e.getMessage());
                datosValidos = false;
            }
        }

        // Insertar la asignatura en la tabla intermedia si no está matriculado
        if (datosValidos) {
            String consultaInsertarRelacion = "INSERT INTO MatriculasAsignaturas (CodigoMatricula, CodigoAsignatura) VALUES (?, ?)";
            try (PreparedStatement stmInsertarRelacion = connection.prepareStatement(consultaInsertarRelacion)) {
                stmInsertarRelacion.setInt(1, codMatricula);
                stmInsertarRelacion.setInt(2, codAsignatura);

                int filasAfectadas = stmInsertarRelacion.executeUpdate();
                if (filasAfectadas > 0) {
                    System.out.println("Asignatura asociada a la matrícula con éxito.");
                } else {
                    System.out.println("Error: No se pudo asociar la asignatura a la matrícula.");
                }
            } catch (SQLException e) {
                System.err.println("Error al asociar la asignatura: " + e.getMessage());
            }
        } else {
            System.out.println("No se pudo registrar la asignatura debido a errores en los datos.");
        }
    }








}
