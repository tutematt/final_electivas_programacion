package com.example.final_electivas_programacion;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;

import com.google.android.material.textfield.TextInputLayout;

public class PantallaRecuperarContrasenia extends AppCompatActivity {
    Cursor data;
    private static final String CHANNEL_ID = "canal";
    private PendingIntent pendingIntent;
    Button btnVolver, btnRecuperar;
    TextInputLayout dni;
    int idPersona = 0;
    DataBase admin = new DataBase(PantallaRecuperarContrasenia.this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_recuperar_contrasenia);
        setearBotones();
        ocultarTecladoInputs();
        btnRecuperar.setOnClickListener(view -> {
            recuperar();
        });
        btnVolver.setOnClickListener(view -> {
            volver();
        });
    }

    private void ocultarTecladoInputs() {
        dni.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });
    }

    private void setearBotones() {
        btnVolver = findViewById(R.id.ButtonVolverPRC);
        btnRecuperar = findViewById(R.id.ButtonRecuperarPRC);
        dni = findViewById(R.id.layoutDNIPRC);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void recuperar() {
        try {
            if (validarCampos()) {
                String dniiii = dni.getEditText().getText().toString();
                Persona p = admin.buscarPersonaPorDni(Integer.parseInt(dniiii));
                if (p != null) {
                    String pass = p.getContrasenia();
                    if (pass == null) {   // estaba en el padron pero no se registró en la app
                        Toast.makeText(this, "No se encontró ningun usuario asociado a su DNI.", Toast.LENGTH_SHORT).show();
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {   // valido la version del sdk
                            this.enviarNotificacion(pass);
                            Toast.makeText(this, "Aguarda un momento y te llegará una notificación con tu contraseña.", Toast.LENGTH_LONG).show();
                            this.volver();
                        }
                        else{
                            Toast.makeText(this, "Lo sentimos, no pudimos recuperar tu contraseña.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(this, "No se encontró ningun usuario asociado a su DNI.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Debe ingresar su DNI.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Hubo un error, vuelva a intentar más tarde.", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean validarCampos() {
        if (dni.getEditText().getText().toString().isEmpty())
            return false;
        else
            return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void enviarNotificacion(String pass){
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "NEW", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);
        showNotification(pass);
    }

    @SuppressLint("MissingPermission")
    public void showNotification(String pass) {
        setPendingIntent(LoginActivity.class);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_login_24)
                .setContentTitle("LAUM - Recuperación de Contraseña")
                .setContentText("Tu contraseña es: " + pass)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent); //Cuando el user haga click en la notificacion, lanza la nueva actividad
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());
        managerCompat.notify(1, builder.build());
    }

    public void setPendingIntent(Class<?> clsActivity){
        Intent intent = new Intent(this, clsActivity);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(clsActivity);
        stackBuilder.addNextIntent(intent);
        pendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_IMMUTABLE);
    }

    public void volver() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
