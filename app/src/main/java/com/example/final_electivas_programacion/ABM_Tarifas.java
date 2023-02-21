package com.example.final_electivas_programacion;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class ABM_Tarifas extends AppCompatActivity {
    Button btnEnviar, btnPrimera, btnTurista;
    TextInputLayout nombreTarifa, precioTarifa;
    float precio;
    DataBase bd;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_tarifas);

        bd = new DataBase(ABM_Tarifas.this);

        btnEnviar = findViewById(R.id.ButtonEnviarPT);
        btnTurista = findViewById(R.id.ButtonTuristaPT);
        btnPrimera = findViewById(R.id.ButtonPrimeraPT);
        nombreTarifa = findViewById(R.id.layoutNombrePT);
        precioTarifa = findViewById(R.id.layoutPrecioPT);
        btnEnviar.setOnClickListener(view -> {
            enviarTarifa();
        });

    }

    private void enviarTarifa() {
        precio = Float.parseFloat(precioTarifa.getEditText().getText().toString().replace(",", "."));
        if(bd.crearTarifa(nombreTarifa.getEditText().getText().toString(), nombreTarifa.getEditText().getText().toString(), precio))
            Toast.makeText(this, "Tarifa creada correctamente", Toast.LENGTH_LONG).show();

    }
}
