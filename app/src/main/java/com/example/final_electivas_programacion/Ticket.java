package com.example.final_electivas_programacion;

import java.util.ArrayList;
import java.util.Date;

public class Ticket {

    private int codigo; //ID_TICKET
    private Boolean esValido;
    private Vuelo vuelo;
    private Persona pasajero;

    public Ticket(int cod, Boolean valid, Vuelo v, Persona p){
        codigo = cod;
        esValido = valid;
        vuelo = v;
        pasajero = p;
    }

    public void setCodigo(int cod){
        codigo = cod;
    }
    public int getCodigo(){
        return (codigo);
    }

    public Boolean getValidez(){
        return (esValido);
    }
    public void setCodigo(Boolean valid){
        esValido = valid;   // cancela el ticket si se cancela la reserva
    }

    public void setPasajero(Persona p){
        pasajero = p;
    }
    public Persona getPasajero(){
        return (pasajero);
    }

    public void setVuelo(Vuelo v){
        vuelo = v;
    }
    public Vuelo getVuelo(){
        return (vuelo);
    }

    @Override
    public String toString(){
        return ("Código: "+ codigo + "- Vuelo: " + vuelo.getOrigen() + " - " + vuelo.getDestino() + "- Pasajero: " + pasajero.getDni() + " - " + pasajero.getNombreCompleto());
    }


}

