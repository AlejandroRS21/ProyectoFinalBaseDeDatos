package com.mycompany.proyectofinalbasededatos;

import java.io.Serializable;
import java.util.Scanner;

public class Alumno implements Serializable {
    private static final Scanner sc = new Scanner(System.in);

    private String dni;
    private String nombreCompleto;
    private String fechaNac;
    private String direccion;


    // Constructor
    public Alumno(String dni, String nombreCompleto, String fechaNac, String direccion) {
        this.dni = dni;
        this.nombreCompleto = nombreCompleto;
        this.fechaNac = fechaNac;
        this.direccion = direccion;
    }

    // Getter
    public String getDni() {
        return dni;
    }
    public String getFechaNac() {
        return fechaNac;
    }
    public String getDireccion() {
        return direccion;
    }
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    // Setter
    public void setDni(String dni) {
        this.dni = dni;
    }
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }
    public void setFechaNac(String fechaNac) {
        this.fechaNac = fechaNac;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}