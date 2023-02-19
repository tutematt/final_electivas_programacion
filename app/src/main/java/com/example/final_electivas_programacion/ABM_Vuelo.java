package com.example.final_electivas_programacion;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ABM_Vuelo extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton add_vuelo,volver;
    DataBase db;
    ArrayList<String> ids_vuelos, codigos_vuelos, fechasDestino_vuelos, fechasOrigen_vuelos, horasDestino_vuelos, horasOrigen_vuelos, precios_vuelos;
    AdapterVueloAdmin customAdapter;
    int i =0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abm_vuelos);
        recyclerView = findViewById(R.id.recycleViewABMvuelos);
        add_vuelo = findViewById(R.id.floatingButtonAddVuelo);
        add_vuelo.setOnClickListener((View.OnClickListener) view -> {
            Intent i = new Intent(ABM_Vuelo.this, PantallaVuelos.class);
            startActivity(i);
        });

        volver = findViewById(R.id.floatingButtonTriviaVuelo);
        volver.setOnClickListener((View.OnClickListener) view -> {
            Intent i = new Intent(ABM_Vuelo.this, AdminActivity.class);
            startActivity(i);
        });

        db = new DataBase(ABM_Vuelo.this);
        ids_vuelos = new ArrayList<>();
        codigos_vuelos = new ArrayList<>();
        fechasDestino_vuelos = new ArrayList<>();
        fechasOrigen_vuelos = new ArrayList<>();
        horasDestino_vuelos = new ArrayList<>();
        horasOrigen_vuelos = new ArrayList<>();
        precios_vuelos = new ArrayList<>();

        mostrarVuelos();

        customAdapter = new AdapterVueloAdmin(ABM_Vuelo.this, this, codigos_vuelos, ids_vuelos, "admin", "");
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ABM_Vuelo.this));
    }

    private void mostrarVuelos() {
        Cursor cursor = db.traerVuelos();
        if(cursor.getCount() == 0)
            Toast.makeText(this, "No hay vuelos para mostrar.", Toast.LENGTH_SHORT).show();
        else
        {
            while(cursor.moveToNext())
            {
                i++;
                ids_vuelos.add(String.valueOf(i));
                codigos_vuelos.add(cursor.getString(0));
            }
        }

    }
}
