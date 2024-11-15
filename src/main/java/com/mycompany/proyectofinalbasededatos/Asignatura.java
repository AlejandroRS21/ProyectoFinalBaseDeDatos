package com.mycompany.proyectofinalbasededatos;

import java.io.Serializable;
import java.util.Scanner;

public class Asignatura implements Serializable {
    private static final Scanner sc = new Scanner(System.in);

    private int codAsignatura;
    private String nombreAsignatura;

    // Constructor
    public Asignatura(int codAsig,String nombreAsig) {
        this.codAsignatura = codAsig;
        this.nombreAsignatura = nombreAsig;
    }
    // Getters
    public int getCodAsignatura() {
        return codAsignatura;
    }
    public String getNombreAsignatura() {
        return nombreAsignatura;
    }
    //Setters
    public void setCodAsignatura(int codAsignatura) {
        this.codAsignatura = codAsignatura;
    }
    public void setNombreAsignatura(String nombreAsignatura) {
        this.nombreAsignatura = nombreAsignatura;
    }

    public static void introducirAsignatura(){

    }
}
