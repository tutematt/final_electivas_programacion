package com.example.final_electivas_programacion;

public class Tarifa {

    private int nro; //ID_TARIFA
    private String codigo; //Lo uso para comparar PRIMERA / TURISTA
    private String nombre;
    private double precio;

    public Tarifa(int num, String cod, String nom, double prec){
        nro = num;
        codigo = cod;
        nombre = nom;
        precio = prec;
    }

    public int getNro() {
        return nro;
    }

    public void setNro(int nro) {
        this.nro = nro;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @Override
    public String toString(){
        return ("Nro.: "+ nro + " - Tipo: " + nombre.toUpperCase() + " - Precio: $ " + precio);
    }
}
