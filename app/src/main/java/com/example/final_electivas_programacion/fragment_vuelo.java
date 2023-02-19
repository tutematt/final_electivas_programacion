package com.example.final_electivas_programacion;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;

public class fragment_vuelo extends Fragment {
    private static final String[] origen = {"Aeropuerto Internacional Ezeiza (EZE)"};
    private static final String[] destino = {"Aeropuerto Internacional Roma (ROM)"};
    AutoCompleteTextView autoCompleteOrigen, autoCompleteDestino;

    Button seleccionarFechaIda, seleccionarFechaVuelta, btnBuscar;
    View inflatedView = null;
    TextInputLayout cantPasajeros;
    RecyclerView recyclerView;
    DataBase db;
    ArrayList<String> ids_vuelos, codigos_vuelos, fechasDestino_vuelos, fechasOrigen_vuelos, horasDestino_vuelos, horasOrigen_vuelos, precios_vuelos;
    AdapterVueloAdmin customAdapter;
    int i =0;
    String fechaDesde ="", fechaHasta ="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflatedView = inflater.inflate(R.layout.fragment_vuelo, container, false);

        setearBotones();
        completarComboOrigen();
        completarComboDestino();
        completarDatePicker();
        completarDatePickerDestino();
        db = new DataBase(getActivity());


        btnBuscar.setOnClickListener(view -> {

            /*LinearLayout fragmento = inflatedView.findViewById(R.id.linearLayoutFV);
            getActivity().overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_left);
            fragmento.setVisibility(View.GONE);*/
            buscarVuelo();
            /*
                Fragment secondFrag = new fragment_reserva();
                FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction() ;
                fm.replace(R.id.PantallaMenuPrincipal,secondFrag).commit();*/

        });

        // Inflate the layout for this fragment
        return inflatedView;
    }

    private void setearBotones() {

        cantPasajeros = inflatedView.findViewById(R.id.layoutCantidadPasajerosPPV);
        autoCompleteOrigen = inflatedView.findViewById(R.id.autoCompleteOrigenPPV);
        autoCompleteDestino = inflatedView.findViewById(R.id.autoCompleteDestinoPPV);
        seleccionarFechaIda = inflatedView.findViewById(R.id.buttonFechaDesdePPV);
        seleccionarFechaVuelta = inflatedView.findViewById(R.id.buttonFechaHastaPPV);
        btnBuscar = inflatedView.findViewById(R.id.ButtonBuscarPPV);
    }


    void buscarVuelo() {
        LinearLayout fragmento = inflatedView.findViewById(R.id.linearLayoutFV);
        getActivity().overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_left);
        fragmento.setVisibility(View.GONE);
        recyclerView = inflatedView.findViewById(R.id.recycleViewResultadoVuelos);
        ids_vuelos = new ArrayList<>();
        codigos_vuelos = new ArrayList<>();
        fechasDestino_vuelos = new ArrayList<>();
        fechasOrigen_vuelos = new ArrayList<>();
        horasDestino_vuelos = new ArrayList<>();
        horasOrigen_vuelos = new ArrayList<>();
        precios_vuelos = new ArrayList<>();
        mostrarVuelos();
        customAdapter = new AdapterVueloAdmin(getActivity(), getActivity(), codigos_vuelos, ids_vuelos, "user");
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        LinearLayout fragmento2 = inflatedView.findViewById(R.id.layouBusquedaVuelo);
        fragmento2.setVisibility(View.VISIBLE);

    }

    private void mostrarVuelos() {

        Cursor cursor = db.traerVuelosxFecha(fechaDesde, fechaHasta);
        if(cursor.getCount() == 0)
            Toast.makeText(getActivity(), "No hay vuelos para mostrar.", Toast.LENGTH_SHORT).show();
        else
        {
            while(cursor.moveToNext())
            {
                i++;
                ids_vuelos.add(String.valueOf(i));
                codigos_vuelos.add(cursor.getString(0));
                fechasOrigen_vuelos.add(cursor.getString(1));
                fechasDestino_vuelos.add(cursor.getString(2));
                //precios_vuelos.add(cursor.getString(3));
            }
        }
    }

    private void completarComboOrigen() {
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(getActivity(), R.layout.activity_items_list, origen);
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
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(getActivity(), R.layout.activity_items_list, destino);
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
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Seleccionar Fecha").setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build();
        seleccionarFechaIda.setOnClickListener(view -> {
            datePicker.show(getFragmentManager(), "Material_Date_Picker");
            datePicker.addOnPositiveButtonClickListener((MaterialPickerOnPositiveButtonClickListener<Long>) selection -> {
                calendar.setTimeInMillis(selection);
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat formatDB = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate  = format.format(calendar.getTime());
                seleccionarFechaIda.setText(formattedDate);
                fechaDesde = formatDB.format(calendar.getTime());
            });

        });
        seleccionarFechaVuelta.setOnClickListener(view -> {
            datePicker.show(getFragmentManager(), "Material_Date_Picker");
            datePicker.addOnPositiveButtonClickListener((MaterialPickerOnPositiveButtonClickListener<Long>) selection -> {
                calendar.setTimeInMillis(selection);
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat formatDB = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate  = format.format(calendar.getTime());
                seleccionarFechaVuelta.setText(formattedDate);
                fechaHasta = formatDB.format(calendar.getTime());
                });

        });
    }

    void completarDatePickerDestino(){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        MaterialDatePicker datePickerDestino = MaterialDatePicker.Builder.datePicker().setTitleText("Seleccionar Fecha").setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build();
        seleccionarFechaVuelta.setOnClickListener(view -> {
            datePickerDestino.show(getFragmentManager(), "Material_Date_Picker");
            datePickerDestino.addOnPositiveButtonClickListener((MaterialPickerOnPositiveButtonClickListener<Long>) selection -> {
                calendar.setTimeInMillis(selection);
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat formatDB = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate  = format.format(calendar.getTime());
                seleccionarFechaVuelta.setText(formattedDate);
                fechaHasta = formatDB.format(calendar.getTime());
            });

        });
    }



}