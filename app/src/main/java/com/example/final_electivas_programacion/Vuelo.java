package com.example.final_electivas_programacion;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Vuelo {

    private int numeroVuelo;
    private String codigo;
    private String destino;
    private String origen;
    private ArrayList<Asiento> asientos; //DEPENDE DE LA CAPACIDAD Y DE LA RESTRICCION
    private int capacidadVuelo;

    private int diaSalidaVuelo;
    private int mesSalidaVuelo;
    private int añoSalidaVuelo;
    private String horaSalidaVuelo;
    //private double tarifa1raClase;
    //private double tarifaClaseTurista;
    private double porcRestrinccion;
    //private double porcDesc


    public Vuelo(int num, String cod, String to, String from,
                              int exitDay, int exitMonth, int exitYear, String hour, int capacity, double pRestric){
        numeroVuelo = num;
        codigo = cod;
        destino = to;
        origen = from;
        asientos = new ArrayList<Asiento>();
        capacidadVuelo = capacity;
        diaSalidaVuelo = exitDay;
        mesSalidaVuelo = exitMonth;
        añoSalidaVuelo = exitYear;
        horaSalidaVuelo = hour;
        porcRestrinccion = pRestric;
    }

    public void setNumero(int num){
        numeroVuelo = num;
    }

    public int getNumero(){
        return (numeroVuelo);
    }

    public void setCodigo(String cod){
        codigo = cod;
    }

    public String getCodigo(){
        return (codigo);
    }

    public void setDestino(String d){
        destino = d;
    }

    public String getDestino(){
        return (destino);
    }

    public void setOrigen(String o){
        origen = o;
    }

    public String getOrigen(){
        return (origen);
    }

    public void setAsientos(ArrayList<Asiento> a){
        asientos = a;
    }
    public void setAsiento(Asiento a){
        asientos.add(a);
    }

    public ArrayList<Asiento> getAsientos(){
        return (asientos);
    }

    public void setFechaIda(int d, int m, int a){
        diaSalidaVuelo = d;
        mesSalidaVuelo = m;
        añoSalidaVuelo = a;
    }
    public String getFecha(){
        return diaSalidaVuelo + "/" + mesSalidaVuelo + "/" + añoSalidaVuelo;
    }

    public String getFechaYHora(){
        return diaSalidaVuelo + "/" + mesSalidaVuelo + "/" + añoSalidaVuelo + " " + horaSalidaVuelo + " hs.";
    }

    public int getDiaSalidaVuelo(){
        return (diaSalidaVuelo);
    }

    public int getMesSalidaVuelo(){
        return (mesSalidaVuelo);
    }

    public int getAñoSalidaVuelo(){
        return (añoSalidaVuelo);
    }

    public String getHora(){
        return (horaSalidaVuelo);
    }

    public void setPorcentajeRestric(double r){
        porcRestrinccion = r;
    }
    public double getPorcentajeRestric(){
        return (porcRestrinccion);
    }

    public void setCapacidadVuelo(int c){
        capacidadVuelo = c;
    }

    public int getCapacidadVuelo(){
        return capacidadVuelo;
    };


    @Override
    public String toString(){
        return ("Codigo: "+ codigo + " ORIGEN: " + origen + " - DESTINO: " + destino);
    }

}

