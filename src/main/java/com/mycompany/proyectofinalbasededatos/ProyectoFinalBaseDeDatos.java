/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.proyectofinalbasededatos;





import java.sql.PreparedStatement;

import java.sql.SQLException;

import java.util.Scanner;


import static com.mycompany.proyectofinalbasededatos.ConexionBD.connection;

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
                    datosAlumno =Alumno.mostrarAlumno(dni);
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
        String[] tablas = {"MatriculasAsignaturas", "Matriculas", "Alumnos", "Asignaturas"};
        String desactivarRestricciones = "SET FOREIGN_KEY_CHECKS = 0";
        String activarRestricciones = "SET FOREIGN_KEY_CHECKS = 1";
        try {


            try (PreparedStatement stmDesactivar = connection.prepareStatement(desactivarRestricciones)) {
                stmDesactivar.executeUpdate();
            }


            for (String tabla : tablas) {
                String query = "DELETE FROM " + tabla;
                try (PreparedStatement stmBorrar = connection.prepareStatement(query)) {
                    stmBorrar.executeUpdate();
                    System.out.println("Datos borrados de la tabla: " + tabla);
                } catch (SQLException e) {
                    System.err.println("Error al borrar los datos de la tabla " + tabla + ": " + e.getMessage());
                }
            }



            try (PreparedStatement stmActivar = connection.prepareStatement(activarRestricciones)) {
                stmActivar.executeUpdate();
            }

            System.out.println("Todos los datos han sido borrados de la base de datos.");
        } catch (SQLException e) {
            System.err.println("Error al borrar los datos: " + e.getMessage());
        }
    }

}