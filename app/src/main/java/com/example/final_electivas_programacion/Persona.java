package com.example.final_electivas_programacion;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Persona {

    //private int numero; // Id_Persona
    private int dni;
    private String nombre;
    private String apellido;
    private String usuario;
    private String contrasenia;
    private ArrayList<Reserva> reservas;
    private Boolean esAdmin;

    public Persona(/*int nro,*/ int doc, String nom, String apell, String user,
                   String contra, Boolean admin){
        //numero = nro;
        dni = doc;
        nombre = nom;
        apellido = apell;
        usuario = user;
        contrasenia = contra;
        reservas = new ArrayList<Reserva>();
        esAdmin = admin;
    }
/*
    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }
    
 */

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public ArrayList<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(ArrayList<Reserva> reservas) {
        this.reservas = reservas;
    }

    public Boolean getEsAdmin() {
        return esAdmin;
    }

    public void setEsAdmin(Boolean esAdmin) {
        this.esAdmin = esAdmin;
    }


    public String getNombreCompleto(){return nombre + " " + apellido;}
}

