package com.example.final_electivas_programacion;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;

import java.sql.Date;

public class AgregarVuelo extends AppCompatActivity {
    DataBase admin = new DataBase(AgregarVuelo.this);
    private static final String[] aviones = {"AIR-BUS500", "AIR-BUS700", "AIRBUS800"};
    private static final String[] origen = {"Aeropuerto Internacional Ezeiza (EZE)"};
    private static final String[] destino = {"Aeropuerto Internacional Roma (ROM)"};
    AutoCompleteTextView autoCompleteAvion, autoCompleteOrigen, autoCompleteDestino;
    Button seleccionarFecha, btnEnviar, btnVolver;
    TextInputLayout layoutCodigo, layoutAvion, layoutOrigen, layoutDestino, layoutPorcRestriccion;
    String avionStr, codigoStr, origenStr, destinoStr, horaStr, restriccionStr;

    Button timeButton;
    int hour, minute;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_vuelo);
        setearBotones();
        completarDatePicker();
        completarComboAviones();
        completarComboOrigen();
        completarComboDestino();

        layoutCodigo.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });

        btnEnviar = (Button) findViewById(R.id.buttonConfirmAV);
        btnEnviar.setOnClickListener(view -> {
            crearVuelo();
        });

        /*Back Menu Configuracion*/
        btnVolver = (Button) findViewById(R.id.buttonVolverAV);
        btnVolver.setOnClickListener(view -> volver());


    }
    public void popTimePicker(View view)
    {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                hour = selectedHour;
                minute = selectedMinute;
                timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d",hour, minute));
            }
        };

        // int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, /*style,*/ onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    private void setearBotones() {
        layoutCodigo = findViewById(R.id.layoutCodigoAV);
        layoutAvion = findViewById(R.id.layoutAvionAV);
        layoutOrigen = findViewById(R.id.layoutOrigenAV);
        layoutDestino = findViewById(R.id.layoutDestinoAV);
        layoutPorcRestriccion = findViewById(R.id.layoutPorcentRestriccionAV);
        autoCompleteAvion = findViewById(R.id.autoCompleteAvionAV);
        autoCompleteOrigen = findViewById(R.id.autoCompleteOrigenAV);
        autoCompleteDestino = findViewById(R.id.autoCompleteDestinoAV);
        seleccionarFecha = findViewById(R.id.buttonFechaVueloAV);
        timeButton = findViewById(R.id.timeButton);
    }

    private void completarComboAviones() {
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(AgregarVuelo.this, R.layout.activity_items_list, aviones);
        autoCompleteAvion.setAdapter(itemAdapter);
        autoCompleteAvion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.getItemAtPosition(i).toString();
                //layoutTrivias.getEditText().setText((String)adapterView.getItemAtPosition(i));
            }
        });
    }

    private void completarComboOrigen() {
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(AgregarVuelo.this, R.layout.activity_items_list, origen);
        autoCompleteOrigen.setAdapter(itemAdapter);
        autoCompleteOrigen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.getItemAtPosition(i).toString();
                //layoutTrivias.getEditText().setText((String)adapterView.getItemAtPosition(i));
            }
        });
    }

    private void completarComboDestino() {
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(AgregarVuelo.this, R.layout.activity_items_list, destino);
        autoCompleteDestino.setAdapter(itemAdapter);
        autoCompleteDestino.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.getItemAtPosition(i).toString();
                //layoutTrivias.getEditText().setText((String)adapterView.getItemAtPosition(i));
            }
        });
    }

    void completarDatePicker(){
        MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker().setTitleText ("Seleccionar Fecha de Vuelo") .setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build();
        seleccionarFecha.setOnClickListener(view -> {
            datePicker.show(getSupportFragmentManager(), "Material_Date_Picker");
            datePicker.addOnPositiveButtonClickListener(selection -> seleccionarFecha.setText(datePicker.getHeaderText()));
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void crearVuelo(){
        try{
            if(this.validar()){
                // FALTA validar que no se de de alta el mismo avion en el misma fecha
                String fechaYHora = seleccionarFecha.getText().toString() + " " + horaStr;
                int capacidad  = this.calcularCapacidad();
                admin.crearVuelo(codigoStr, origenStr, destinoStr, fechaYHora, capacidad, Double.parseDouble(restriccionStr));
                Toast.makeText(this, "Vuelo creado correctamente.", Toast.LENGTH_SHORT).show();
                this.volver();
            }
        }
        catch (Exception ex){
            Toast.makeText(this, "Hubo un error, vuelva a intentar más tarde.", Toast.LENGTH_SHORT).show();
        }

    }
    private boolean validar(){
        avionStr = layoutAvion.getEditText().getText().toString();
        codigoStr = layoutCodigo.getEditText().getText().toString();
        origenStr = layoutOrigen.getEditText().getText().toString();
        destinoStr =  layoutDestino.getEditText().getText().toString();
        horaStr = timeButton.getText().toString();
        restriccionStr = layoutPorcRestriccion.getEditText().getText().toString();
        String fecha  = seleccionarFecha.getText().toString();
        if(avionStr.isEmpty() || codigoStr.isEmpty() || origenStr.isEmpty() || destinoStr.isEmpty()
                || (horaStr.isEmpty() && horaStr != "Horario Partida") || restriccionStr.isEmpty()
                || (fecha.isEmpty() && fecha != "Fecha")){
            Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_LONG).show();
            return false;
        }

        // FALTA: Validar que la fecha seleccionada sea mayor a hoy

        return true;
    }
    public int calcularCapacidad(){
        int capacidad = 340;
        double porc = Double.parseDouble(restriccionStr);
        double restringidos = (capacidad * (int) Math.round(porc)) / 100;
        capacidad = (int) (capacidad - restringidos);
        return capacidad;
    }

    private void volver() {
        Intent i = new Intent(AgregarVuelo.this, ABM_Vuelo.class);
        startActivity(i);
    }

}
