package com.mycompany.proyectofinalbasededatos;

import java.io.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import static com.mycompany.proyectofinalbasededatos.ConexionBD.connection;

public class Alumno  {
    private static final Scanner sc = new Scanner(System.in);
    private static  String dni,nombre,fechaNac,direccion;

    public static void introducirAlumno() {
        boolean datosValidos = true;
        String query;
        System.out.println("Introduce los datos del alumno:");
        System.out.println("DNI:");
        dni = sc.nextLine();
        if (!comprobarDNI(dni)) {
            datosValidos = false;
        }
        if (datosValidos) {
            System.out.println("Nombre del Alumno:");
            nombre = sc.nextLine();
            if (!comprobarNombre(nombre)) {
                datosValidos = false;
            }
        }
        if (datosValidos) {
            System.out.println("Fecha de nacimiento:");
            fechaNac = sc.nextLine();
        }
        if (datosValidos) {
            System.out.println("Dirección:");
            direccion = sc.nextLine();
        }
        //Una vez comprobados todos los datos, se insertan en la base de datos
        if (datosValidos) {
            query = "INSERT INTO alumnos (DNI, NombreCompleto, FechaNacimiento, Direccion) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, dni);
                statement.setString(2, nombre);
                statement.setString(3, fechaNac);
                statement.setString(4, direccion);
                statement.executeUpdate();
                System.out.println("Alumno registrado con éxito.");
            } catch (SQLException e) {
                System.err.println("Error al registrar al alumno: " + e.getMessage());
            }
        } else {
            System.out.println("No se ha registrado al alumno debido a errores en los datos.");
        }
    }

    private static boolean comprobarDNI(String dni){
        boolean resultado = true;
        String query = "SELECT COUNT(*) FROM alumnos WHERE DNI = ?";

        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, dni);
            ResultSet rs = st.executeQuery();

        if(rs.next() && rs.getInt(1) > 0){
            System.out.println("El DNI ya esta registrado en la base de datos");
            resultado = false;
        }

        } catch (Exception e) {
            e.printStackTrace();
            resultado = false;
        }
        return resultado;
    }

    private static boolean comprobarNombre(String nombre){
        boolean datosValidos = true;
        boolean resultado = true;
        String query = "SELECT COUNT(*) FROM alumnos WHERE NombreCompleto = ?";

        if(datosValidos){  //Comprueba que el nombre no este en blanco
            if (nombre == null || nombre.trim().isEmpty()){
                System.out.println("El nombre no puede estar vacio");
                resultado = false;
                datosValidos = false;
            }
        }
        if(datosValidos){ //Comprueba que el nombre no exista ya en la base de datos
            try (PreparedStatement st = connection.prepareStatement(query)) {
                st.setString(1, nombre);
                ResultSet rs = st.executeQuery();
                if(rs.next() && rs.getInt(1) > 0){
                    System.out.println("El nombre ya existe en la base de datos");
                    resultado = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultado;
        }

    public static String mostrarAlumno(String dni) {
        StringBuilder sb = new StringBuilder();
        String consultaAlumno = "SELECT DNI, NombreCompleto, FechaNacimiento, Direccion " +
                "FROM Alumnos WHERE TRIM(UPPER(DNI)) = ?";
        try (PreparedStatement stmtAlumno = connection.prepareStatement(consultaAlumno);) {
            stmtAlumno.setString(1, dni);
            try (ResultSet rsAlumno = stmtAlumno.executeQuery()) {
                if (rsAlumno.next()) {

                    String id = rsAlumno.getString("DNI");
                    String nombre = rsAlumno.getString("NombreCompleto");
                    String fechaNac = rsAlumno.getString("FechaNacimiento");
                    String direccion = rsAlumno.getString("Direccion");


                    sb.append("Datos del Alumno:\n")
                            .append("DNI: ").append(id).append("\n")
                            .append("Nombre: ").append(nombre).append("\n")
                            .append("Fecha de nacimiento: ").append(fechaNac).append("\n")
                            .append("Dirección: ").append(direccion).append("\n");
                } else {
                    sb.append("No se encontró un alumno con el DNI: ").append(dni);
                    return sb.toString();
                }
            }
        } catch (SQLException e) {
            sb.append("Error al buscar el alumno: ").append(e.getMessage());
        }
        return sb.toString();
    }

    public static void volcarDatosAlumnos() {
        String dni, nombre, nombreArchivo, datosAlumno;
        File f;
        String consultaAlumnos = "SELECT DNI, NombreCompleto FROM Alumnos";

        try (Statement stmt = ConexionBD.connection.createStatement();
             ResultSet rs = stmt.executeQuery(consultaAlumnos)) {
            while (rs.next()) {
                dni = rs.getString("DNI");
                nombre = rs.getString("NombreCompleto");
                nombreArchivo = nombre.replaceAll("\\s+", "_") + ".txt"; // Reemplazar espacios con guiones bajos
                datosAlumno = obtenerAsignaturasAlumno(dni);
                f = new File(nombreArchivo);
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(f))) {
                    writer.write("Nombre: " + nombre + "\n");
                    writer.write("Asignaturas:\n" + datosAlumno);
                    writer.flush();
                    System.out.println("Datos del alumno " + nombre + " volcados en el archivo " + nombreArchivo);
                } catch (IOException e) {
                    System.err.println("Error al crear el archivo para el alumno " + nombre + ": " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar los datos de los alumnos: " + e.getMessage());
        }
    }

    private static String obtenerAsignaturasAlumno(String dni) {
        StringBuilder asignaturas = new StringBuilder();
        String consultaAsignaturas = "SELECT a.NombreAsignatura FROM matriculas m " +
                "JOIN asignaturas a ON m.CodigoAsignatura = a.CodigoAsignatura " +
                "WHERE m.DNI = ?";
        try (PreparedStatement stmt = ConexionBD.connection.prepareStatement(consultaAsignaturas)) {
            stmt.setString(1, dni);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    asignaturas.append("- ").append(rs.getString("NombreAsignatura")).append("\n");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar las asignaturas del alumno con DNI " + dni + ": " + e.getMessage());
        }
        return asignaturas.toString();
    }
}