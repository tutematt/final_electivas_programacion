package com.example.final_electivas_programacion;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class fragment_vuelo extends Fragment {
    private static final String[] origen = {"Aeropuerto Internacional Ezeiza (EZE)"};
    private static final String[] destino = {"Aeropuerto Internacional Roma (ROM)"};
    AutoCompleteTextView autoCompleteOrigen, autoCompleteDestino;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vuelo, container, false);
    }

/*
    private void completarComboOrigen() {
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(fragment_vuelo.this, R.layout.activity_items_list, origen);
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
    }*/

}