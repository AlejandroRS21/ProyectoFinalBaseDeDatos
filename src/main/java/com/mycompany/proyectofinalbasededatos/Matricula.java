package com.mycompany.proyectofinalbasededatos;

import java.io.Serializable;
import java.util.Scanner;

public class Matricula implements Serializable {
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

    public static void introducirMatricula(){

    }
    public static boolean existeMatricula(){

        return false;
    }


}
