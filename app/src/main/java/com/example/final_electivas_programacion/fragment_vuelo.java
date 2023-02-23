package com.example.final_electivas_programacion;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class fragment_vuelo extends Fragment {
    private static final String[] origen = {"Aeropuerto Internacional Ezeiza (EZE)"};
    private static final String[] destino = {"Aeropuerto Internacional Roma (ROM)"};
    AutoCompleteTextView autoCompleteOrigen, autoCompleteDestino;

    Button seleccionarFechaIda, seleccionarFechaVuelta, btnBuscar;
    FloatingActionButton btnVolver;
    View inflatedView = null;
    TextInputLayout cantPasajeros;
    RecyclerView recyclerView;
    DataBase db;
    ArrayList<String> ids_vuelos, codigos_vuelos, fechasDestino_vuelos, fechasOrigen_vuelos, horasDestino_vuelos, horasOrigen_vuelos, precios_vuelos;
    AdapterVueloAdmin customAdapter;
    int i =0;
    String fechaDesde ="", fechaHasta ="", tarifa="Turista";
    SwitchMaterial materialSwitch;
    boolean seleccionoFecha = true;
    RadioGroup radioTipoTarifa;

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

        cantPasajeros.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });


        btnBuscar.setOnClickListener(view -> {
            if(validarCampos())
                buscarVuelo();

        });

        btnVolver.setOnClickListener(view -> {
            volver();
        });

        materialSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            boolean on = isChecked;
            if(on)
            {
                seleccionarFechaIda.setVisibility(View.GONE);
                seleccionarFechaVuelta.setVisibility(View.GONE);
                seleccionoFecha = false;
            }
            else
            {
                seleccionarFechaIda.setVisibility(View.VISIBLE);
                seleccionarFechaVuelta.setVisibility(View.VISIBLE);
                seleccionoFecha = true;
            }
        });

        radioTipoTarifa = inflatedView.findViewById(R.id.radioGroupClaseTicket);
        radioTipoTarifa.setOnCheckedChangeListener((radioGroup, i) -> {
            RadioButton rb=inflatedView.findViewById(i);
            tarifa= rb.getText().toString();
        });
        return inflatedView;
    }

    private void volver() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    private boolean validarCampos() {
        if(autoCompleteOrigen.getText().toString().isEmpty() || autoCompleteDestino.getText().toString().isEmpty() || cantPasajeros.getEditText().getText().toString().isEmpty())
        {
            Toast.makeText(getActivity(), "Por favor, complete todos los campos.", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(seleccionarFechaVuelta.isShown() && seleccionarFechaIda.isShown())
        {
            if(seleccionarFechaIda.getText().toString().isEmpty() || seleccionarFechaIda.getText().toString().equals( "DESDE") || (seleccionarFechaVuelta.getText().toString().isEmpty() || seleccionarFechaVuelta.getText().toString().equals( "HASTA")))
                Toast.makeText(getActivity(), "Por favor, complete campos de FECHA.", Toast.LENGTH_LONG).show();
        }

        return true;
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void setearBotones() {

        cantPasajeros = inflatedView.findViewById(R.id.layoutCantidadPasajerosPPV);
        autoCompleteOrigen = inflatedView.findViewById(R.id.autoCompleteOrigenPPV);
        autoCompleteDestino = inflatedView.findViewById(R.id.autoCompleteDestinoPPV);
        seleccionarFechaIda = inflatedView.findViewById(R.id.buttonFechaDesdePPV);
        seleccionarFechaVuelta = inflatedView.findViewById(R.id.buttonFechaHastaPPV);
        btnBuscar = inflatedView.findViewById(R.id.ButtonBuscarPPV);
        materialSwitch = inflatedView.findViewById(R.id.switch_fechas);
        btnVolver = inflatedView.findViewById(R.id.floatingButtonVolverFV);
    }


    void buscarVuelo()
    {
        cambiarLayout();
        iniciarListadoVuelos();
        mostrarVuelos();
        iniciarAdapter();
        LinearLayout fragmento2 = inflatedView.findViewById(R.id.layouBusquedaVuelo);
        fragmento2.setVisibility(View.VISIBLE);

    }

    private void iniciarAdapter() {
        String cantidad_pasajeros = cantPasajeros.getEditText().getText().toString();
        customAdapter = new AdapterVueloAdmin(getActivity(), getActivity(), codigos_vuelos, ids_vuelos, "user", cantidad_pasajeros, fechasOrigen_vuelos, fechasDestino_vuelos, precios_vuelos, tarifa);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void cambiarLayout() {
        LinearLayout fragmento = inflatedView.findViewById(R.id.linearLayoutFV);
        getActivity().overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_left);
        fragmento.setVisibility(View.GONE);
    }

    private void iniciarListadoVuelos(){
        recyclerView = inflatedView.findViewById(R.id.recycleViewResultadoVuelos);
        ids_vuelos = new ArrayList<>();
        codigos_vuelos = new ArrayList<>();
        fechasDestino_vuelos = new ArrayList<>();
        fechasOrigen_vuelos = new ArrayList<>();
        horasDestino_vuelos = new ArrayList<>();
        horasOrigen_vuelos = new ArrayList<>();
        precios_vuelos = new ArrayList<>();
    }

    private void mostrarVuelos() {

        Cursor cursor;
        if(seleccionoFecha)
            cursor = db.traerVuelosxFecha(fechaDesde, fechaHasta, tarifa);
        else
            cursor = db.traerVuelossinFecha(tarifa);
        if(cursor.getCount() == 0)
            Toast.makeText(getActivity(), "No hay vuelos para mostrar.", Toast.LENGTH_SHORT).show();
        else
        {
            while(cursor.moveToNext())
            {
                if(cursor.getInt(1) >= Integer.parseInt(cantPasajeros.getEditText().getText().toString()))
                {
                    i++;
                    ids_vuelos.add(String.valueOf(i));
                    codigos_vuelos.add(cursor.getString(0));
                    fechasOrigen_vuelos.add(cursor.getString(3));
                    fechasDestino_vuelos.add(cursor.getString(4));
                    precios_vuelos.add(cursor.getString(2));
                }

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