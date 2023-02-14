package com.example.final_electivas_programacion;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Asiento {

    private int nro;
    private String fila;
    private Integer estado; // 0 = LIBRE, 1 = OCUPADO, 2 = RESTRINGIDO
    private Tarifa tarifa;

    public Asiento(int num, String row, Integer state, Tarifa price){
        nro = num;
        fila = row;
        estado = state;
        tarifa = price;
    }

    public int getNro() {
        return nro;
    }

    public void setNro(int nro) {
        this.nro = nro;
    }

    public String getFila() {
        return fila;
    }

    public void setFila(String fila) {
        this.fila = fila;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Tarifa getTarifa() {
        return tarifa;
    }

    public void setTarifa(Tarifa tarifa) {
        this.tarifa = tarifa;
    }

    @Override
    public String toString(){
        return ("Asiento: "+ fila.toUpperCase() + nro + " - Estado: " + estado);
    }
}

