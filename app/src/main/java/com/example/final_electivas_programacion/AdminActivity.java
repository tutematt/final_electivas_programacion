package com.example.final_electivas_programacion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {
    Button btnVuelos, btnSalir;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_admin);
        setearBotones();

        btnVuelos.setOnClickListener(view -> {
            abmVuelos();
        });
        btnSalir.setOnClickListener(view -> {
            salir();
        });
    }

    private void abmVuelos() {
        Intent i = new Intent(this, ABM_Vuelo.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void salir() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void setearBotones() {
        btnVuelos = findViewById(R.id.buttonVuelosPA);
        btnSalir = findViewById(R.id.buttonSalirPMP);
    }
}
