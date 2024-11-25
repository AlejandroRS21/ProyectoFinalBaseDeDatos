package com.mycompany.proyectofinalbasededatos;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import static com.mycompany.proyectofinalbasededatos.ConexionBD.connection;

public class Asignatura  {
    private static final Scanner sc = new Scanner(System.in);

    private int codAsignatura;
    private String nombreAsignatura;


    public Asignatura(int codAsig,String nombreAsig) {
        this.codAsignatura = codAsig;
        this.nombreAsignatura = nombreAsig;
    }

    public int getCodAsignatura() {
        return codAsignatura;
    }
    public String getNombreAsignatura() {
        return nombreAsignatura;
    }

    public void setCodAsignatura(int codAsignatura) {
        this.codAsignatura = codAsignatura;
    }
    public void setNombreAsignatura(String nombreAsignatura) {
        this.nombreAsignatura = nombreAsignatura;
    }

    public static void introducirAsignatura() {
        String codAsignatura;
        String nombreAsig = "";
        boolean datosValidos = true;



        System.out.println("Introduce el código de asignatura:");
        codAsignatura = sc.nextLine().trim();

        // Verificar que el código de asignatura no esté vacío
        if (codAsignatura.isEmpty()) {
            System.out.println("El código de asignatura no puede estar vacío.");
            datosValidos = false;
        }

        // Verificar que el código de asignatura no exista ya en la base de datos
        if (datosValidos && comprobarCodigoAsignaturaExistente(codAsignatura)) {
            System.out.println("El código de asignatura ya está registrado en la base de datos.");
            datosValidos = false;
        }


        if (datosValidos) {
            System.out.println("Introduce el nombre de la asignatura:");
            nombreAsig = sc.nextLine().trim();

            if (nombreAsig.isEmpty()) {
                System.out.println("El nombre de la asignatura no puede estar vacío.");
                datosValidos = false;
            }
        }


        if (datosValidos) {
            String consulta = "INSERT INTO Asignaturas (CodigoAsignatura, NombreAsignatura) VALUES (?, ?)";

            try (PreparedStatement stmt = connection.prepareStatement(consulta)) {
                stmt.setString(1, codAsignatura);
                stmt.setString(2, nombreAsig);
                stmt.executeUpdate(); // Ejecutar la inserción

                System.out.println("Asignatura registrada correctamente.");
            } catch (SQLException e) {
                System.out.println("Error al registrar la asignatura: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("No se ha registrado la asignatura debido a datos inválidos.");
        }
    }

    // Método para comprobar si ya existe el código de asignatura en la base de datos
    private static boolean comprobarCodigoAsignaturaExistente(String codAsignatura) {
        boolean resultado = false;
        String query = "SELECT COUNT(*) FROM Asignaturas WHERE CodigoAsignatura = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, codAsignatura);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                resultado = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }


}
