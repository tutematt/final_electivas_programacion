package com.example.final_electivas_programacion;

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

public class PantallaReservarVuelo extends AppCompatActivity {
    private static final String[] metodoDePago = {"Tarjeta", "Efectivo"};
    Button realizarReserva, cancelarReserva;
    AutoCompleteTextView autoCompleteMetodoDePago;
    TextInputLayout registrarPasajeros, layoutVuelo, layoutOrigen, layoutDestino, layoutFechaIda, layoutFechaVuelta, layoutHoraIda,
            layoutHoraVuelta, layoutDescuento,layoutPrecioAPagar, layoutPrecioTotal, layoutCantPasajeros;
    int hour, minute, cantPasajeros;

    boolean reservar_vuelo = false;
    String codigoVuelo = "";
    DataBase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservar_vuelo);

        reservar_vuelo = getIntent().getBooleanExtra("reservar_vuelo", false);
        codigoVuelo = getIntent().getStringExtra("codigo_vuelo");
        cantPasajeros = getIntent().getIntExtra("cant_pasajeros",0);
        setearBotones();
        setearSeleccionUsuario();
        completarComboMetodoDePago();
        db = new DataBase(PantallaReservarVuelo.this);
        buscarVuelo();
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
                layoutFechaIda.getEditText().setText(partesFecha[0]);
                layoutHoraIda.getEditText().setText(partesFecha[1]);
                String fechaDestino = cursor.getString(2);
                partesFecha = fechaDestino.split(" ");
                layoutFechaVuelta.getEditText().setText(partesFecha[0]);
                layoutHoraVuelta.getEditText().setText(partesFecha[1]);

                layoutCantPasajeros.getEditText().setText(String.valueOf(cantPasajeros) );
                layoutPrecioTotal.getEditText().setText("XXX");
                layoutDescuento.getEditText().setText("XYY");
                layoutPrecioAPagar.getEditText().setText("YYY");
            }
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

}
