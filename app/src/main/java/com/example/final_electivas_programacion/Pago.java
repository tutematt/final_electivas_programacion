package com.example.final_electivas_programacion;

import java.time.LocalDateTime;

public class Pago {
    private int nro;  //ID_PAGO
    private LocalDateTime fecha;
    private Tarifa tarifa;
    private float descuento; //DEPENDIENDO EL ENUNCIADO
    private float totalAPagar;
    private int cantPasajeros;
    private String TipoDePago; // UTILIZAR HERENCIA

    public Pago(int num, LocalDateTime fech, Tarifa t, float desc,
                float total, int cant, String tipo){
        nro = num;
        fecha = fech;
        tarifa = t;
        descuento = desc;
        totalAPagar = total;
        cantPasajeros = cant;
        TipoDePago = tipo;
    }

    public int getNro() {
        return nro;
    }

    public void setNro(int nro) {
        this.nro = nro;
    }

    public Tarifa getTarifa() {
        return tarifa;
    }

    public void setTarifa(Tarifa tarifa) {
        this.tarifa = tarifa;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public float getDescuento() {
        return descuento;
    }

    public void setDescuento(float descuento) {
        this.descuento = descuento;
    }

    public float getTotalAPagar() {
        return totalAPagar;
    }

    public void setTotalAPagar(float totalAPagar) {
        this.totalAPagar = totalAPagar;
    }

    public int getCantPasajeros() {
        return cantPasajeros;
    }

    public void setCantPasajeros(int cantPasajeros) {
        this.cantPasajeros = cantPasajeros;
    }

    public String getTipoDePago() {
        return TipoDePago;
    }

    public void setTipoDePago(String tipoDePago) {
        TipoDePago = tipoDePago;
    }

    @Override
    public String toString(){
        return ("Nro.: "+ nro + " - Importe: $" + totalAPagar);
    }
}
