package com.example.final_electivas_programacion;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputLayout;

public class ABM_Tarifas extends AppCompatActivity {
    Button btnEnviar, btnPrimera, btnTurista, btnVolver;
    TextInputLayout precioTarifa;
    String nombreTarifa="";
    float precioTurista, precioPrimera;
    DataBase bd;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_tarifas);

        bd = new DataBase(ABM_Tarifas.this);
        cargarTarifas();

        btnEnviar = findViewById(R.id.ButtonEnviarPT);
        btnVolver = findViewById(R.id.buttonVolverPT);
        btnVolver.setOnClickListener(view -> {
            volver();
        });

        btnTurista = findViewById(R.id.ButtonTuristaPT);
        btnTurista.setOnClickListener(view -> {
            nombreTarifa=btnTurista.getText().toString();
            precioTarifa.getEditText().setText(String.valueOf(precioTurista));

        });
        btnPrimera = findViewById(R.id.ButtonPrimeraPT);
        btnPrimera.setOnClickListener(view -> {
            nombreTarifa=btnPrimera.getText().toString();
            precioTarifa.getEditText().setText(String.valueOf(precioPrimera));
        });
        precioTarifa = findViewById(R.id.layoutPrecioPT);
        btnEnviar.setOnClickListener(view -> {
            if(nombreTarifa.equals(""))
            {
                Toast.makeText(this, "Debe seleccionar un tipo de tarifa para continuar.", Toast.LENGTH_LONG).show();
            }
        });



    }

    private void cargarTarifas()
    {
        Cursor cursor = bd.cargarTarfias();
        if(cursor.getCount()>0)
        {
            while(cursor.moveToNext())
            {
                if(cursor.getString(0)=="Turista")
                    precioTurista = cursor.getFloat(1);
                else
                    precioPrimera = cursor.getFloat(1);
            }
        }
    }

    /*private void enviarTarifa() {
        precio = Float.parseFloat(precioTarifa.getEditText().getText().toString().replace(",", "."));
        if(bd.crearTarifa(nombreTarifa, nombreTarifa, precio))
            Toast.makeText(this, "Tarifa creada correctamente", Toast.LENGTH_LONG).show();
    }*/

    public void volver() {
        Intent intent = new Intent(this, AdminActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
