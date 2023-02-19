package com.example.final_electivas_programacion;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;

public class PantallaVuelos extends AppCompatActivity {
    DataBase admin;
    private static final String[] aviones = {"AIR-BUS500", "AIR-BUS700", "AIRBUS800"};
    private static final String[] origen = {"Aeropuerto Internacional Ezeiza (EZE)"};
    private static final String[] destino = {"Aeropuerto Internacional Roma (ROM)"};
    AutoCompleteTextView autoCompleteAvion, autoCompleteOrigen, autoCompleteDestino;
    Button seleccionarFechaIda, seleccionarFechaVuelta, btnEnviar, btnVolver;
    TextInputLayout layoutCodigo, layoutAvion, layoutOrigen, layoutDestino, layoutPorcRestriccion;
    String avionStr, codigoStr, origenStr, destinoStr, horaStr, restriccionStr, horaDesStr;

    Button timeButton, timeButtonVuelta;
    int hour, minute;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_vuelo);
        admin = new DataBase(PantallaVuelos.this);
        setearBotones();
        completarDatePicker();
        completarDatePickerDestino();
        completarComboAviones();
        completarComboOrigen();
        completarComboDestino();
        editarVuelo();

        layoutCodigo.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });

        layoutPorcRestriccion.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });

        btnEnviar.setOnClickListener(view -> {
            if(getIntent().hasExtra("editar"))
                actualizarVuelo();
            else
                crearVuelo();
        });

        /*Back Menu Configuracion*/
        btnVolver = (Button) findViewById(R.id.buttonVolverAV);
        btnVolver.setOnClickListener(view -> volver());


    }

    private void actualizarVuelo() {
        try{
            String fechaOrigen = seleccionarFechaIda.getText().toString();
            String horarioOrigen = timeButton.getText().toString();
            String fechaDestino = seleccionarFechaVuelta.getText().toString();
            String horarioDestino = timeButtonVuelta.getText().toString();
            if((fechaOrigen.isEmpty() || horarioOrigen.isEmpty()) && (fechaDestino.isEmpty() || horarioDestino.isEmpty()))
                Toast.makeText(this, "Hubo un error, vuelva a intentar más tarde.", Toast.LENGTH_SHORT).show();
            else
            {
                // FALTA validar que no se de de alta el mismo avion en el misma fecha

                String fechaYHora = fechaOrigen+" "+horarioOrigen;
                String fechaYHoraDestino = fechaDestino+" "+horarioDestino;
                admin.actualizarVuelo(fechaYHora, getIntent().getStringExtra("codigo_vuelo"), fechaYHoraDestino);
                Toast.makeText(this, "Vuelo actualizado correctamente.", Toast.LENGTH_SHORT).show();
                this.volver();
            }
        }
        catch (Exception ex){
            Toast.makeText(this, "Hubo un error, vuelva a intentar más tarde.", Toast.LENGTH_SHORT).show();
        }
    }

    private void editarVuelo() {
        if(getIntent().hasExtra("editar"))
        {
            inhabilitarCampos();
            layoutCodigo.getEditText().setText(getIntent().getStringExtra("codigo_vuelo"));
            Cursor cursor = admin.buscarVuelo(getIntent().getStringExtra("codigo_vuelo"));
            if(cursor.getCount()!=0)
            {
                if(cursor.moveToFirst())
                {
                    int cant = cursor.getCount();
                    int cantRestriccion = cursor.getInt(0);
                    String fechaOrigen = cursor.getString(1);
                    String fechaDestino = cursor.getString(2);
                    String[] partesFecha = fechaOrigen.split(" ");
                    layoutPorcRestriccion.getEditText().setText(String.valueOf(cantRestriccion));
                    seleccionarFechaIda.setText(partesFecha[0]);
                    timeButton.setText(partesFecha[1]);
                    partesFecha = fechaDestino.split(" ");
                    seleccionarFechaVuelta.setText(partesFecha[0]);
                    timeButtonVuelta.setText(partesFecha[1]);
                }

            }
            btnEnviar.setText("Actualizar");
        }
    }

    private void inhabilitarCampos() {
        layoutAvion.setEnabled(false);
        layoutCodigo.setEnabled(false);
        layoutOrigen.setEnabled(false);
        layoutDestino.setEnabled(false);
        layoutPorcRestriccion.setEnabled(false);
    }

    public void popTimePicker(View view)
    {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, selectedHour, selectedMinute) -> {
            hour = selectedHour;
            minute = selectedMinute;
            timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d",hour, minute));
        };

        // int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, /*style,*/ onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Horario de Salida:");
        timePickerDialog.show();
    }

    public void popTimePickerDestino(View view)
    {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, selectedHour, selectedMinute) -> {
            hour = selectedHour;
            minute = selectedMinute;
            timeButtonVuelta.setText(String.format(Locale.getDefault(), "%02d:%02d",hour, minute));
        };

        // int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, /*style,*/ onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Horario de Llegada:");
        timePickerDialog.show();
    }

    private void setearBotones() {
        layoutCodigo = findViewById(R.id.layoutCodigoAV);
        layoutAvion = findViewById(R.id.layoutAvionAV);
        layoutOrigen = findViewById(R.id.layoutOrigenAV);
        layoutDestino = findViewById(R.id.layoutDestinoAV);
        layoutPorcRestriccion = findViewById(R.id.layoutPorcentRestriccionAV);
        autoCompleteAvion = findViewById(R.id.autoCompleteAvionAV);
        autoCompleteOrigen = findViewById(R.id.autoCompleteOrigenAV);
        autoCompleteDestino = findViewById(R.id.autoCompleteDestinoAV);
        seleccionarFechaIda = findViewById(R.id.buttonFechaVueloAV);
        seleccionarFechaVuelta = findViewById(R.id.buttonFechaVueloDestinoAV);
        timeButton = findViewById(R.id.timeButton);
        timeButtonVuelta = findViewById(R.id.timeButtonDestinoAV);
        btnEnviar = (Button) findViewById(R.id.buttonConfirmAV);
    }

    private void completarComboAviones() {
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(PantallaVuelos.this, R.layout.activity_items_list, aviones);
        autoCompleteAvion.setAdapter(itemAdapter);
        autoCompleteAvion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.getItemAtPosition(i).toString();
                //layoutTrivias.getEditText().setText((String)adapterView.getItemAtPosition(i));
            }
        });

    }

    private void completarComboOrigen() {
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(PantallaVuelos.this, R.layout.activity_items_list, origen);
        autoCompleteOrigen.setAdapter(itemAdapter);
        autoCompleteOrigen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.getItemAtPosition(i).toString();
                //layoutTrivias.getEditText().setText((String)adapterView.getItemAtPosition(i));
            }
        });
        if(getIntent().hasExtra("editar"))
            autoCompleteOrigen.setText(itemAdapter.getItem(0));
    }

    private void completarComboDestino() {
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(PantallaVuelos.this, R.layout.activity_items_list, destino);
        autoCompleteDestino.setAdapter(itemAdapter);
        autoCompleteDestino.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.getItemAtPosition(i).toString();
                //layoutTrivias.getEditText().setText((String)adapterView.getItemAtPosition(i));
            }
        });
        if(getIntent().hasExtra("editar"))
            autoCompleteDestino.setText(itemAdapter.getItem(0));
    }

    void completarDatePicker(){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker().setTitleText ("Seleccionar Fecha de Vuelo") .setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build();
        seleccionarFechaIda.setOnClickListener(view -> {
            datePicker.show(getSupportFragmentManager(), "Material_Date_Picker");
            datePicker.addOnPositiveButtonClickListener((MaterialPickerOnPositiveButtonClickListener<Long>) selection -> {
                calendar.setTimeInMillis(selection);
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                String formattedDate  = format.format(calendar.getTime());
                seleccionarFechaIda.setText(formattedDate);
            });
        });

    }

    void completarDatePickerDestino()
    {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        MaterialDatePicker datePickerD = MaterialDatePicker.Builder.datePicker().setTitleText ("Seleccionar Fecha de Vuelo") .setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build();
        seleccionarFechaVuelta.setOnClickListener(view -> {
            datePickerD.show(getSupportFragmentManager(), "Material_Date_Picker");
            datePickerD.addOnPositiveButtonClickListener((MaterialPickerOnPositiveButtonClickListener<Long>) selection -> {
                calendar.setTimeInMillis(selection);
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                String formattedDate  = format.format(calendar.getTime());
                seleccionarFechaVuelta.setText(formattedDate);
            });
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void crearVuelo(){
        try{
            if(this.validar()){
                // FALTA validar que no se de de alta el mismo avion en el misma fecha
                String fechaYHora = seleccionarFechaIda.getText().toString() + " " + horaStr;
                String fechaYHoraDestino = seleccionarFechaVuelta.getText().toString() + " " + horaDesStr;
                int capacidad  = this.calcularCapacidad();
                String avion = autoCompleteAvion.getText().toString();
                admin.crearVuelo(codigoStr, origenStr, destinoStr, fechaYHora, capacidad, Double.parseDouble(restriccionStr), avion, fechaYHoraDestino);
                Toast.makeText(this, "Vuelo creado correctamente.", Toast.LENGTH_SHORT).show();
                this.volver();
            }
        }
        catch (Exception ex){
            Toast.makeText(this, "Hubo un error, vuelva a intentar más tarde.", Toast.LENGTH_SHORT).show();
        }

    }
    private boolean validar(){
        avionStr = layoutAvion.getEditText().getText().toString();
        codigoStr = layoutCodigo.getEditText().getText().toString();
        origenStr = layoutOrigen.getEditText().getText().toString();
        destinoStr =  layoutDestino.getEditText().getText().toString();
        horaStr = timeButton.getText().toString();
        horaDesStr = timeButtonVuelta.getText().toString();
        restriccionStr = layoutPorcRestriccion.getEditText().getText().toString();
        String fechaO  = seleccionarFechaIda.getText().toString();
        String fechaD  = seleccionarFechaVuelta.getText().toString();
        if(avionStr.isEmpty() || codigoStr.isEmpty() || origenStr.isEmpty() || destinoStr.isEmpty()
                && (horaStr.isEmpty() || horaStr.equals( "Partida")) && (horaDesStr.isEmpty() || horaDesStr != "Destino") || restriccionStr.isEmpty()
                && (fechaO.isEmpty() || fechaO != "Fecha") && (fechaD.isEmpty() || fechaD != "Fecha")){
            Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_LONG).show();
            return false;
        }

        // FALTA: Validar que la fecha seleccionada sea mayor a hoy

        return true;
    }
    public int calcularCapacidad(){
        int capacidad = 340;
        double porc = Double.parseDouble(restriccionStr);
        double restringidos = (capacidad * (int) Math.round(porc)) / 100;
        capacidad = (int) (capacidad - restringidos);
        return capacidad;
    }

    private void volver() {
        Intent i = new Intent(PantallaVuelos.this, ABM_Vuelo.class);
        startActivity(i);
    }

}
