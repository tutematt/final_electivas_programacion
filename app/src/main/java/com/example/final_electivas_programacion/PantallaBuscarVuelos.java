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

public class PantallaBuscarVuelos extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton add_vuelo,volver;
    DataBase db;
    ArrayList<String> opciones_id, opciones_codigo, opciones_origen, opciones_destino, opciones_fecha, opciones_hora;
    AdapterVueloAdmin customAdapter;
    int i =0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_search_vuelos);
        recyclerView = findViewById(R.id.recycleViewABMvuelos);

        volver = findViewById(R.id.floatingButtonVuelosVolver);
        volver.setOnClickListener((View.OnClickListener) view -> {
            Intent i = new Intent(PantallaBuscarVuelos.this, MainActivity.class);
            startActivity(i);
        });

        db = new DataBase(PantallaBuscarVuelos.this);
        opciones_id = new ArrayList<>();
        opciones_codigo = new ArrayList<>();
        opciones_origen = new ArrayList<>();
        opciones_destino = new ArrayList<>();
        opciones_fecha = new ArrayList<>();
        opciones_hora = new ArrayList<>();

        mostrarVuelos();

        customAdapter = new AdapterVueloAdmin(PantallaBuscarVuelos.this, this, opciones_codigo, opciones_id, "user", "", new ArrayList(), new ArrayList(),new ArrayList(), "");
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(PantallaBuscarVuelos.this));
    }

    private void mostrarVuelos() {
        Cursor cursor = db.traerVuelos();
        if(cursor.getCount() == 0)
            Toast.makeText(this, "No hay vuelos para mostrar esa fecha.", Toast.LENGTH_SHORT).show();
        else
        {
            while(cursor.moveToNext())
            {
                i++;
                opciones_id.add(String.valueOf(i));
                opciones_codigo.add(cursor.getString(0));

            }
        }
    }
}
