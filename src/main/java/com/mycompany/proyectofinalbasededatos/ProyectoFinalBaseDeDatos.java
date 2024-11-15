/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.proyectofinalbasededatos;

import java.util.Scanner;

public class ProyectoFinalBaseDeDatos {
    private static final Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        int opcion;

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

            //Comprobar si la entrada es un numero
            while (!sc.hasNextInt()) {
                System.out.println("Por favor, introduce un número válido:");
                sc.next(); // Limpiar la entrada incorrecta
            }

            opcion = sc.nextInt();
            sc.nextLine();
            switch (opcion) {
                case 1:
                    break;
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                default:
                    System.out.print("Numero incorrecto vuelva a escribir uno");
            }
        } while (opcion != 7);
        sc.close();
    }
}