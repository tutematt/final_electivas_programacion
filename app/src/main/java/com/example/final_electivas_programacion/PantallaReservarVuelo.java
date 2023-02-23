package com.example.final_electivas_programacion;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.database.Cursor;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

public class PantallaReservarVuelo extends AppCompatActivity {
    private static final String[] metodoDePago = {"Tarjeta", "Efectivo"};
    Button realizarReserva, cancelarReserva, popupRegistrarPasajero;
    AutoCompleteTextView autoCompleteMetodoDePago;
    TextInputLayout registrarPasajeros, layoutVuelo, layoutOrigen, layoutDestino, layoutFechaIda, layoutFechaVuelta, layoutHoraIda, layoutHoraVuelta, layoutDescuento,layoutPrecioAPagar, layoutPrecioTotal, layoutCantPasajeros;
    int cantPasajeros, pasajeroDni;
    int cantPasajerosRegistradosRestantes = -1;
    float precio, precioDescuento = 0;
    boolean reservar_vuelo, esAdmin;
    String codigoVuelo = "", tipoTarifa ="";
    String pasajeroNombre, pasajeroApellido, codReserva;

    DataBase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservar_vuelo);

        reservar_vuelo = getIntent().getBooleanExtra("reservar_vuelo", false);
        setearBotones();
        db = new DataBase(PantallaReservarVuelo.this);
        esAdmin = getIntent().getBooleanExtra("editar", false);
        codReserva = getIntent().getStringExtra("codigo_reserva");

        if(esAdmin)
        {
            Cursor cursor = db.buscarReserva(codReserva);
            completarReserva(cursor);
            cancelarReserva.setOnClickListener(view -> {
                if(esAdmin)
                    cancelarReserva(cursor);
                else
                    Toast.makeText(this, "Debe comunicarse con atención al cliente para realizar la cancelación de la reserva", Toast.LENGTH_LONG).show();

            });
        }
        else if(!esAdmin && !reservar_vuelo)
        {
            cancelarReserva.setVisibility(View.GONE);
            Cursor cursor = db.buscarReserva(codReserva);
            completarReserva(cursor);
        }
        else if(reservar_vuelo)
        {

            codigoVuelo = getIntent().getStringExtra("codigo_vuelo");
            precio = Float.parseFloat(getIntent().getStringExtra("precio_vuelo"));
            cantPasajeros = Integer.parseInt(getIntent().getStringExtra("cant_pasajeros"));
            tipoTarifa = getIntent().getStringExtra("tarifa");
            setearSeleccionUsuario();
            completarComboMetodoDePago();
            buscarVuelo();
            realizarReserva.setOnClickListener(view -> {
                reservarPasaje();
            });
            popupRegistrarPasajero.setOnClickListener(view -> {
                registrarPasajero();
            });
        }

    }

    private void cancelarReserva(Cursor cursor) {
        if(cursor.getCount()!=0)
        {
            if(cursor.moveToFirst())
            {
                db.desocuparAsientoxVuelo(cursor.getString(0), cursor.getString(8), cursor.getInt(4));
                db.actualizarCapacidad(cursor.getString(0), cursor.getInt(9));
                db.eliminarReserva(codReserva);
                volverCancelar();
            }
        }

    }

    private void volverCancelar() {
        Toast.makeText(this, "Reserva Eliminada con Exito.", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, AdminActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void completarReserva(Cursor cursor) {

        if(cursor.getCount()!=0)
        {
            while(cursor.moveToNext())
            {
                layoutVuelo.getEditText().setText(cursor.getString(0));
                layoutOrigen.getEditText().setText("Ezeiza");
                layoutDestino.getEditText().setText("Roma");
                layoutCantPasajeros.getEditText().setText(cursor.getString(4));
                String fechaOrigen = cursor.getString(2);
                String[] partesFecha = fechaOrigen.split(" ");
                layoutFechaIda.getEditText().setText(partesFecha[0]);
                layoutHoraIda.getEditText().setText(partesFecha[1]);
                String fechaDestino = cursor.getString(1);
                partesFecha = fechaDestino.split(" ");
                layoutFechaVuelta.getEditText().setText(partesFecha[0]);
                layoutHoraVuelta.getEditText().setText(partesFecha[1]);
                String desc = String.valueOf(cursor.getFloat(3));
                layoutDescuento.getEditText().setText(desc);
                float total = cursor.getFloat(5);
                layoutPrecioTotal.getEditText().setText(String.valueOf(total));
                int cantPasajeros = cursor.getInt(4);
                layoutPrecioAPagar.getEditText().setText(String.valueOf(total * cantPasajeros));
                autoCompleteMetodoDePago.setText(cursor.getString(6)+ " - Nro. "+ cursor.getString(7));
                popupRegistrarPasajero.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void reservarPasaje() {
        String metodPag = autoCompleteMetodoDePago.getText().toString();
        if (metodPag.equals("Tarjeta") || metodPag.equals("Efectivo")){
            if (cantPasajerosRegistradosRestantes == 0) {
                buscarPasajeros();
                guardarReserva();
            } else {
                Toast.makeText(this, "Registre todos los pasajeros por favor.", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Seleccione un metodo de pago.", Toast.LENGTH_SHORT).show();
        }
    }

    boolean guardarReserva()
    {
        String codigo_reserva = generarCodigoReserva();
        try
        {
            Globales global = new Globales();
            int dni = global.getDniPersona();
            Persona p = db.buscarPersonaPorDni(dni);
            db.guardarReserva(codigo_reserva, codigoVuelo, false, p.getNumero());
            db.agregarPersonaxReserva(pasajeros);
            db.ocuparAsientoxVuelo(codigoVuelo, tipoTarifa, cantPasajeros);
            pasarApagar(codigo_reserva);
        }
        catch(Exception e)
        {
            e.getMessage();
            Toast.makeText(this, "No se pudo completar el proceso de reserva.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    void pasarApagar(String codigo_reserva)
    {
        Intent i = new Intent(this, PantallaPago.class);
        i.putExtra("codigo_reserva", codigo_reserva);
        i.putExtra("codigo_tarifa", tipoTarifa);
        i.putExtra("descuento", precioDescuento);
        i.putExtra("cant_pasajeros", cantPasajeros);
        i.putExtra("total", precio-precioDescuento);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private String generarCodigoReserva() {
        Random generadorAleatorios = new Random();
        int numeroAleatorio = 1+generadorAleatorios.nextInt();
        return "REV-"+numeroAleatorio;
    }

    private void setearSeleccionUsuario() {
    }

    private void buscarVuelo(){
        Cursor cursor = db.buscarVuelo(codigoVuelo);
        if(cursor.getCount()!=0)
        {
            if(cursor.moveToFirst())
            {
                int cant = cursor.getCount();
                int cantRestriccion = cursor.getInt(0);
                layoutDestino.getEditText().setText("ROMA");
                layoutOrigen.getEditText().setText("EZEIZA");
                layoutVuelo.getEditText().setText(codigoVuelo);

                String fechaOrigen = cursor.getString(1);
                String[] partesFecha = fechaOrigen.split(" ");
                int diasDiferencia = calcularFecha(partesFecha[0]);
                calcularDescuento(diasDiferencia);
                layoutFechaIda.getEditText().setText(partesFecha[0]);
                layoutHoraIda.getEditText().setText(partesFecha[1]);
                String fechaDestino = cursor.getString(2);
                partesFecha = fechaDestino.split(" ");
                layoutFechaVuelta.getEditText().setText(partesFecha[0]);
                layoutHoraVuelta.getEditText().setText(partesFecha[1]);
                layoutCantPasajeros.getEditText().setText(String.valueOf(cantPasajeros));
                layoutPrecioTotal.getEditText().setText("$ "+String.valueOf(precio));
                layoutDescuento.getEditText().setText("$ "+String.valueOf(precioDescuento));
                layoutPrecioAPagar.getEditText().setText("$ "+String.valueOf(((precio-precioDescuento) * cantPasajeros)));
            }
        }
    }

    private void calcularDescuento(int diasDiferencia) {
        if(diasDiferencia >= 150)
            precioDescuento = (precio*75/100);
        else if(diasDiferencia>=1)
        {
            precioDescuento = (float) ((precio*0.5/100)*diasDiferencia);
        }
        else
            precioDescuento = 0;
    }

    private void setearBotones() {
        registrarPasajeros = findViewById(R.id.ButtonRegistrarPasajerosPRV);
        realizarReserva = findViewById(R.id.ButtonReservarPRV);
        cancelarReserva = findViewById(R.id.ButtonCancelarReservaPRV);
        autoCompleteMetodoDePago = findViewById(R.id.autoCompleteMetodoPagoPRV);

        if (reservar_vuelo == true){
            realizarReserva.setVisibility(View.VISIBLE);
            cancelarReserva.setVisibility(View.GONE);
        } else{
            realizarReserva.setVisibility(View.GONE);
            cancelarReserva.setVisibility(View.VISIBLE);
        }
        layoutVuelo = findViewById(R.id.LinearLayoutVueloPRV);
        layoutOrigen = findViewById(R.id.LineaLayoutOrigenPRV);
        layoutDestino = findViewById(R.id.LineaLayoutDestinoPSV);
        layoutFechaIda= findViewById(R.id.LinearLayoutFechaIdaPRV);
        layoutHoraIda= findViewById(R.id.LinearLayoutHorarioIdaPRV);
        layoutFechaVuelta= findViewById(R.id.LinearLayoutFechaVueltaPRV);
        layoutHoraVuelta= findViewById(R.id.LinearLayoutHorarioVueltaPRV);
        layoutCantPasajeros= findViewById(R.id.LinearLayoutPasajerosPRV);
        layoutPrecioTotal= findViewById(R.id.LinearLayoutPrecioTotalPRV);
        layoutDescuento= findViewById(R.id.LinearLayoutDescuentoPRV);
        layoutPrecioAPagar= findViewById(R.id.LinearLayoutPrecioaPagarPRV);
        //*************************
        popupRegistrarPasajero =(Button)findViewById(R.id.popupRegistrarPasajero);
    }
    private void completarComboMetodoDePago() {
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(PantallaReservarVuelo.this, R.layout.activity_items_list, metodoDePago);
        autoCompleteMetodoDePago.setAdapter(itemAdapter);
        autoCompleteMetodoDePago.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.getItemAtPosition(i).toString();
            }
        });
        if(getIntent().hasExtra("Seleccionar Metodo de Pago"))
            autoCompleteMetodoDePago.setText(itemAdapter.getItem(0));
    }

    public int calcularFecha(String s) {
        int dias=0;
        try {
            //usamos SimpleDateFormat
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            //creamos una variable date con la fecha inicial
            Date fechaInicial = dateFormat.parse(obtenerFechaConFormato("dd-MM-yyyy", "GMT-3"));
            //variable date con la fecha final
            Date fechaFinal = dateFormat.parse(s);
            //calculamos los días restando la inicial de la final y dividiendolo entre los milisegundos que tiene un día y almacenamos el resultado en la variable entera
            dias = (int) ((fechaFinal.getTime() - fechaInicial.getTime()) / 86400000);
            //se imprime el resultado
            System.out.println("Hay " + dias + " dias de diferencia");

        }catch(Exception e)
        {
            e.getMessage();
        }
        return dias;
    }

    public static String obtenerFechaConFormato(String formato, String zonaHoraria) {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat(formato);
        sdf.setTimeZone(TimeZone.getTimeZone(zonaHoraria));
        return sdf.format(date);
    }

    final Context context = this; //para el popup
    private List<Persona> pasajeros = new ArrayList<>();
    private void registrarPasajero(){
        cantPasajerosRegistradosRestantes = cantPasajeros;
        popupRegistrarPasajero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(cantPasajerosRegistradosRestantes>0) {
                    LayoutInflater li = LayoutInflater.from(context);
                    View promptsView = li.inflate(R.layout.popup_registrar_pasajero, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            context);
                    alertDialogBuilder.setView(promptsView);
                    MaterialTextView numeroPasajero = (MaterialTextView) promptsView
                            .findViewById(R.id.MaterialTextViewNumeroPasajeroPopupRP);
                    numeroPasajero.setText(String.valueOf(cantPasajerosRegistradosRestantes));
                    final EditText nombreCargado = (EditText) promptsView
                            .findViewById(R.id.EditTextNombrePopupRP);
                    final EditText apellidoCargado = (EditText) promptsView
                            .findViewById(R.id.EditTextApellidoPopupRP);
                    final EditText dniCargado = (EditText) promptsView
                            .findViewById(R.id.EditTextDNIPopupRP);

                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // get user input and set it to result
                                            // edit text
                                            pasajeroNombre = nombreCargado.getText().toString();
                                            pasajeroApellido = apellidoCargado.getText().toString();
                                            pasajeroDni = Integer.parseInt(dniCargado.getText().toString());
                                            cantPasajerosRegistradosRestantes--;
                                            Persona pasajero = new Persona (pasajeroDni,pasajeroNombre, pasajeroApellido, null, null, false );
                                            pasajeros.add(pasajero);        //creo un array de pasajeros para despues agregarlos/buscarlos

                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                }
            }
        });

    }

    private void buscarPasajeros(){
        for (Persona pasajero : pasajeros){ //recorro los pasajeros creados
            pasajeroNombre = pasajero.getNombre();
            pasajeroApellido = pasajero.getApellido();
            pasajeroDni = pasajero.getDni();
            if(buscarPasajeroPorDNI(pasajero.getDni()) == false){//false = no existe registro
                db.crearPasajero(pasajeroNombre, pasajeroApellido, pasajeroDni);
            }
        }
    }
    private boolean buscarPasajeroPorDNI(int dni){
        Persona pasajero = db.buscarPersonaPorDni(dni);
        if (pasajero == null)
            return false; //no existe
        else
            return true; //ya existe uno con ese dni


    }
}
