package com.example.final_electivas_programacion;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class fragment_config extends Fragment {
    Button btnSalir;
    View inflatedView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflatedView = inflater.inflate(R.layout.fragment_config, container, false);

        btnSalir = inflatedView.findViewById(R.id.ButtonSalirSesionPFG);
        btnSalir.setOnClickListener(view -> {
            salir();
        });
        // Inflate the layout for this fragment
        return inflatedView;
    }

    private void salir() {
        Intent i = new Intent(getActivity(), LoginActivity.class);
        startActivity(i);
        //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}