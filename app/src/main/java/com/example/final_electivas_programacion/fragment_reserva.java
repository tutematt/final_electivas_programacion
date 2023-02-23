package com.example.final_electivas_programacion;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class fragment_reserva extends Fragment {
    RecyclerView recyclerView;
    FloatingActionButton add_vuelo,volver;
    DataBase db;
    ArrayList<String> ids_reserva, codigos_reserva, fechasDestino_vuelos, fechasOrigen_vuelos, horasDestino_vuelos, horasOrigen_vuelos, precios_vuelos;
    AdapterReservaAdmin customAdapter;
    int i =0;
    View inflatedView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.inflatedView = inflater.inflate(R.layout.fragment_reserva, container, false);

        recyclerView = inflatedView.findViewById(R.id.recycleResultadosReservasUser);

        db = new DataBase(getActivity());
        ids_reserva = new ArrayList<>();
        codigos_reserva = new ArrayList<>();

        mostrarReservas();

        customAdapter = new AdapterReservaAdmin(getActivity(), getActivity(), ids_reserva, codigos_reserva);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return inflatedView;
    }

    void mostrarReservas()
    {
        Globales global = new Globales();
        int dni = global.getDniPersona();
        Persona p = db.buscarPersonaPorDni(dni);
        int numero = p.getNumero();
        Cursor cursor = db.traerReservasxPersona(numero);
        if(cursor.getCount() <= 0)
            Toast.makeText(getActivity(), "No hay reservas para mostrar.", Toast.LENGTH_SHORT).show();
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

}