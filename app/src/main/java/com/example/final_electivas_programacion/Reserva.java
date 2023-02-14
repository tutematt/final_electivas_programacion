package com.example.final_electivas_programacion;

import java.util.ArrayList;
import java.util.Date;

public class Reserva {

    private int codigo; //ID_RESERVA
    private Boolean estaCancelado;
    private Vuelo vuelo;
    private Pago pago;
    //private Persona pasajero;
    //private ArrayList<Asiento> asientos;
    private ArrayList<Ticket> tickets;  // 1 POR CADA PASAJERO

    public Reserva(int cod, Boolean cancel, Vuelo v, Pago p, ArrayList<Ticket> t){
        codigo = cod;
        estaCancelado = cancel;
        vuelo = v;
        pago = p;
        tickets = t;
    }

    public void setCodigo(int cod){
        codigo = cod;
    }

    public int getCodigo(){
        return (codigo);
    }

    public void setPago(Pago p){
        pago = p;
    }

    public Pago getPago(){
        return (pago);
    }

    public void setVuelo(Vuelo v){
        vuelo = v;
    }

    public Vuelo getVuelo(){
        return (vuelo);
    }

    public ArrayList<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(ArrayList<Ticket> tickets) {
        this.tickets = tickets;
    }

    @Override
    public String toString(){
        return ("Código: "+ codigo + "- Vuelo: " + vuelo.getOrigen() + " - " + vuelo.getDestino());
    }

}

