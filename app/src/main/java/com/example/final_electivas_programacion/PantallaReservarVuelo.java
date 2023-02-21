package com.example.final_electivas_programacion;

import static java.time.temporal.ChronoUnit.DAYS;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class PantallaReservarVuelo extends AppCompatActivity {
    private static final String[] metodoDePago = {"Tarjeta", "Efectivo"};
    Button realizarReserva, cancelarReserva;
    AutoCompleteTextView autoCompleteMetodoDePago;
    TextInputLayout registrarPasajeros, layoutVuelo, layoutOrigen, layoutDestino, layoutFechaIda, layoutFechaVuelta, layoutHoraIda,
            layoutHoraVuelta, layoutDescuento,layoutPrecioAPagar, layoutPrecioTotal, layoutCantPasajeros;
    int hour, minute, cantPasajeros;
    float precio;
    float precioDescuento = 0;

    boolean reservar_vuelo = false;
    String codigoVuelo = "";
    DataBase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservar_vuelo);

        reservar_vuelo = getIntent().getBooleanExtra("reservar_vuelo", false);
        codigoVuelo = getIntent().getStringExtra("codigo_vuelo");
        precio = Float.parseFloat(getIntent().getStringExtra("precio_vuelo"));

        setearBotones();
        setearSeleccionUsuario();
        completarComboMetodoDePago();
        db = new DataBase(PantallaReservarVuelo.this);
        buscarVuelo();
        realizarReserva.setOnClickListener(view -> {
            pago();
        });
    }

    private void pago() {
        Intent i = new Intent(this, PantallaPago.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void setearSeleccionUsuario() {
    }

    private void buscarVuelo(){
        db.buscarVuelo(codigoVuelo);


        Cursor cursor = db.buscarVuelo(codigoVuelo);
        if(cursor.getCount()!=0)
        {
            if(cursor.moveToFirst())
            {
                int cant = cursor.getCount();
                int cantRestriccion = cursor.getInt(0);
                layoutDestino.getEditText().setText("ROMA");
                layoutOrigen.getEditText().setText("EZEIZA");
                layoutVuelo.getEditText().setText(codigoVuelo);

                String fechaOrigen = cursor.getString(1);
                String[] partesFecha = fechaOrigen.split(" ");
                int diasDiferencia = calcularFecha(partesFecha[0]);
                calcularDescuento(diasDiferencia);
                layoutFechaIda.getEditText().setText(partesFecha[0]);
                layoutHoraIda.getEditText().setText(partesFecha[1]);
                String fechaDestino = cursor.getString(2);
                partesFecha = fechaDestino.split(" ");
                layoutFechaVuelta.getEditText().setText(partesFecha[0]);
                layoutHoraVuelta.getEditText().setText(partesFecha[1]);
                layoutCantPasajeros.getEditText().setText(getIntent().getStringExtra("cant_pasajeros"));
                layoutPrecioTotal.getEditText().setText("$ "+String.valueOf(precio));
                layoutDescuento.getEditText().setText("$ "+String.valueOf(precioDescuento));
                layoutPrecioAPagar.getEditText().setText("$ "+String.valueOf(precio-precioDescuento));
            }
        }
    }

    private void calcularDescuento(int diasDiferencia) {
        if(diasDiferencia >= 150)
            precioDescuento = diasDiferencia-(diasDiferencia*75/100);
        else
        {
            precioDescuento = (float) (diasDiferencia*(precio*0.5/100));
        }
    }

    private void setearBotones() {
        registrarPasajeros = findViewById(R.id.ButtonRegistrarPasajerosPRV);
        realizarReserva = findViewById(R.id.ButtonReservarPRV);
        cancelarReserva = findViewById(R.id.ButtonCancelarReservaPRV);
        autoCompleteMetodoDePago = findViewById(R.id.autoCompleteMetodoPagoPRV);

        if (reservar_vuelo == true){
            realizarReserva.setVisibility(View.VISIBLE);
            cancelarReserva.setVisibility(View.GONE);
        } else{
            realizarReserva.setVisibility(View.GONE);
            cancelarReserva.setVisibility(View.VISIBLE);
        }
        layoutVuelo = findViewById(R.id.LinearLayoutVueloPRV);
        layoutOrigen = findViewById(R.id.LineaLayoutOrigenPRV);
        layoutDestino = findViewById(R.id.LineaLayoutDestinoPSV);
        layoutFechaIda= findViewById(R.id.LinearLayoutFechaIdaPRV);
        layoutHoraIda= findViewById(R.id.LinearLayoutHorarioIdaPRV);
        layoutFechaVuelta= findViewById(R.id.LinearLayoutFechaVueltaPRV);
        layoutHoraVuelta= findViewById(R.id.LinearLayoutHorarioVueltaPRV);
        layoutCantPasajeros= findViewById(R.id.LinearLayoutPasajerosPRV);
        layoutPrecioTotal= findViewById(R.id.LinearLayoutPrecioTotalPRV);
        layoutDescuento= findViewById(R.id.LinearLayoutDescuentoPRV);
        layoutPrecioAPagar= findViewById(R.id.LinearLayoutPrecioaPagarPRV);

    }
    private void completarComboMetodoDePago() {
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(PantallaReservarVuelo.this, R.layout.activity_items_list, metodoDePago);
        autoCompleteMetodoDePago.setAdapter(itemAdapter);
        autoCompleteMetodoDePago.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.getItemAtPosition(i).toString();
                //layoutTrivias.getEditText().setText((String)adapterView.getItemAtPosition(i));
            }
        });
        if(getIntent().hasExtra("Seleccionar Metodo de Pago"))
            autoCompleteMetodoDePago.setText(itemAdapter.getItem(0));
    }

    public String getDiferencia(Date fechaInicial, Date fechaFinal){

        long diferencia = fechaFinal.getTime() - fechaInicial.getTime();

        long segsMilli = 1000;
        long minsMilli = segsMilli * 60;
        long horasMilli = minsMilli * 60;
        long diasMilli = horasMilli * 24;

        long diasTranscurridos = diferencia / diasMilli;
        diferencia = diferencia % diasMilli;

        long horasTranscurridos = diferencia / horasMilli;
        diferencia = diferencia % horasMilli;

        long minutosTranscurridos = diferencia / minsMilli;
        diferencia = diferencia % minsMilli;

        long segsTranscurridos = diferencia / segsMilli;

        return "diasTranscurridos: " + diasTranscurridos + " , horasTranscurridos: " + horasTranscurridos +
                " , minutosTranscurridos: " + minutosTranscurridos + " , segsTranscurridos: " + segsTranscurridos;


    }

    public int calcularFecha(String s) {
        int dias=0;
        try {
            //usamos SimpleDateFormat
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            //creamos una variable date con la fecha inicial
            Date fechaInicial = dateFormat.parse(obtenerFechaConFormato("dd-MM-yyyy", "GMT-3"));
            //variable date con la fecha final
            Date fechaFinal = dateFormat.parse(s);
            //calculamos los días restando la inicial de la final y dividiendolo entre los milisegundos que tiene un día y almacenamos el resultado en la variable entera
            dias = (int) ((fechaFinal.getTime() - fechaInicial.getTime()) / 86400000);
            //se imprime el resultado
            System.out.println("Hay " + dias + " dias de diferencia");

        }catch(Exception e)
        {

        }
        return dias;
    }

    public static String obtenerFechaConFormato(String formato, String zonaHoraria) {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat(formato);
        sdf.setTimeZone(TimeZone.getTimeZone(zonaHoraria));
        return sdf.format(date);
    }

}
