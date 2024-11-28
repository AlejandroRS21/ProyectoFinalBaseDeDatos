package com.mycompany.proyectofinalbasededatos;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class ProyectoFinalBaseDeDatos {
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        int opcion;
        String dni;
        String datosAlumno;

        do {
            System.out.println("""
                    __________________________________________
                    MENU
                    1.Introduce alumno
                    2.Introduce matricula
                    3.Introduce asignatura
                    4.Mostrar informacion alumno
                    5.Volcar todos los alumnos a fichero.
                    6.Borrar todo
                    7.Salir
                    ___________________________________________
                    """);


            while (!sc.hasNextInt()) {
                System.out.println("Por favor, introduce un número válido:");
                sc.next();

            }

            ConexionBD.conectar();
            opcion = sc.nextInt();
            sc.nextLine();
            switch (opcion) {
                case 1 -> Alumno.introducirAlumno();
                case 2 -> Matricula.introducirMatricula();
                case 3 -> Asignatura.introducirAsignatura();
                case 4 -> {
                    System.out.println("Introduce DNI del alumno");
                    dni = sc.nextLine();
                    datosAlumno = Alumno.mostrarAlumno(dni);
                    System.out.println(datosAlumno);
                }
                case 5 -> Alumno.volcarDatosAlumnos();
                case 6 -> borrar();
                case 7 -> System.out.println("Saliendo");
                default -> System.out.print("Numero incorrecto vuelva a escribir uno");
            }
        } while (opcion != 7);
        sc.close();
        ConexionBD.desconectar();
    }

    public static void borrar() {
        int opcion = sc.nextInt();

        System.out.println("¿Qué desea borrar?");
        System.out.println("1. Todos los archivos .txt");
        System.out.println("2. Vaciar la base de datos");
        System.out.print("Seleccione una: ");
        switch (opcion) {
            case 1 -> borrarArchivosTxt();
            case 2 -> vaciarBaseDeDatos();
            default -> System.out.println("Opción no válida. No se ha realizado ninguna acción.");
        }
    }

    private static void borrarArchivosTxt() {
        File directorioActual = new File(".");
        File[] archivos = directorioActual.listFiles((_, name) -> name.endsWith(".txt"));
        if (archivos != null) {
            for (File archivo : archivos) {
                if (archivo.delete()) {
                    System.out.println("Archivo eliminado: " + archivo.getName());
                } else {
                    System.out.println("No se pudo eliminar el archivo: " + archivo.getName());
                }
            }
        } else {
            System.out.println("No se encontraron archivos .txt para eliminar.");
        }
    }

    private static void vaciarBaseDeDatos() {
        String[] tablas = {"matriculas", "alumnos", "asignaturas"};
        String desactivarRestricciones = "SET FOREIGN_KEY_CHECKS = 0";
        String activarRestricciones = "SET FOREIGN_KEY_CHECKS = 1";

        try {
            try (PreparedStatement stmtDesactivar = ConexionBD.connection.prepareStatement(desactivarRestricciones)) {
                stmtDesactivar.executeUpdate();
            }
            for (String tabla : tablas) {
                String query = "DELETE FROM " + tabla;
                try (PreparedStatement stmt = ConexionBD.connection.prepareStatement(query)) {
                    stmt.executeUpdate();
                    System.out.println("Datos borrados de la tabla: " + tabla);
                } catch (SQLException e) {
                    System.err.println("Error al borrar datos de la tabla " + tabla + ": " + e.getMessage());
                }
            }
            try (PreparedStatement stmtActivar = ConexionBD.connection.prepareStatement(activarRestricciones)) {
                stmtActivar.executeUpdate();
            }
            System.out.println("Todos los datos han sido borrados de la base de datos.");
        } catch (SQLException e) {
            System.err.println("Error al vaciar la base de datos: " + e.getMessage());
        }
    }
}