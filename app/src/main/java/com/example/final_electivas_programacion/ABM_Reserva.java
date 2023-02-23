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

public class ABM_Reserva extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton add_vuelo,volver;
    DataBase db;
    ArrayList<String> ids_reserva, codigos_reserva, fechasDestino_vuelos, fechasOrigen_vuelos, horasDestino_vuelos, horasOrigen_vuelos, precios_vuelos;
    AdapterReservaAdmin customAdapter;
    int i =0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abm_reserva);

        recyclerView = findViewById(R.id.recycleViewABMreservas);

        volver = findViewById(R.id.floatingButtonReservaVolver);
        volver.setOnClickListener((View.OnClickListener) view -> {
            volver();
        });

        db = new DataBase(ABM_Reserva.this);
        ids_reserva = new ArrayList<>();
        codigos_reserva = new ArrayList<>();
        /*fechasDestino_vuelos = new ArrayList<>();
        fechasOrigen_vuelos = new ArrayList<>();
        horasDestino_vuelos = new ArrayList<>();
        horasOrigen_vuelos = new ArrayList<>();
        precios_vuelos = new ArrayList<>();*/

        mostrarReservas();

        customAdapter = new AdapterReservaAdmin(ABM_Reserva.this, this, ids_reserva, codigos_reserva);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ABM_Reserva.this));
    }

    void mostrarReservas()
    {
        Cursor cursor = db.traerReservas();
        if(cursor.getCount() == 0)
            Toast.makeText(this, "No hay reservas para mostrar.", Toast.LENGTH_SHORT).show();
        else
        {
            while(cursor.moveToNext())
            {
                i++;
                ids_reserva.add(String.valueOf(i));
                codigos_reserva.add(cursor.getString(1));
            }
        }
    }

    public void volver() {
        Intent intent = new Intent(this, AdminActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
