package com.example.final_electivas_programacion;

import android.app.Activity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;

public class AgregarVuelo extends AppCompatActivity {
    DataBase admin = new DataBase(AgregarVuelo.this);
    private static final String[] aviones = {"AIR-BUS500", "AIR-BUS700", "AIRBUS800"};
    private static final String[] origen = {"Aeropuerto Internacional Ezeiza (EZE)"};
    private static final String[] destino = {"Aeropuerto Internacional Roma (ROM)"};
    AutoCompleteTextView autoCompleteAvion, autoCompleteOrigen, autoCompleteDestino;
    Button seleccionarFecha, btnEnviar, btnVolver;
    TextInputLayout layoutCodigo, layoutHorario;

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

        layoutHorario.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });

        btnEnviar = (Button) findViewById(R.id.buttonConfirmAV);
        btnEnviar.setOnClickListener(view -> {
            guardarVuelo();
        });

        /*Back Menu Configuracion*/
        btnVolver = (Button) findViewById(R.id.buttonVolverAV);
        btnVolver.setOnClickListener(view -> volver());
    }

    private void guardarVuelo() {
    }

    private void setearBotones() {
        layoutCodigo = findViewById(R.id.layoutCodigoAV);
        layoutHorario = findViewById(R.id.layoutHorarioIdaAV);
        autoCompleteAvion = findViewById(R.id.autoCompleteAvionAV);
        autoCompleteOrigen = findViewById(R.id.autoCompleteOrigenAV);
        autoCompleteDestino = findViewById(R.id.autoCompleteDestinoAV);
        seleccionarFecha = findViewById(R.id.buttonFechaVueloAV);
    }

    private void volver() {
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
}
