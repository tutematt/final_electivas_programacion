package com.example.final_electivas_programacion;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;
import java.util.Random;

public class PantallaPago extends AppCompatActivity {
    private static final String CHANNEL_ID = "canal";
    private PendingIntent pendingIntent;
    String codigo_reserva, codigo_tarifa;
    float descuento, total;
    int cantPasajeros;
    DataBase bd;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_pago);
        codigo_reserva = getIntent().getStringExtra("codigo_reserva");
        codigo_tarifa = getIntent().getStringExtra("codigo_tarifa");
        descuento = getIntent().getFloatExtra("descuento", 0);
        cantPasajeros = getIntent().getIntExtra("cant_pasajeros", 0);
        total = getIntent().getFloatExtra("total", 0);
        bd = new DataBase(PantallaPago.this);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                try {
                    Random generadorAleatorios = new Random();
                    int numeroAleatorio = 1+generadorAleatorios.nextInt(999999999);
                    int idTarifa = bd.buscarTarifa(codigo_tarifa);
                    bd.guardarPago(idTarifa, descuento, cantPasajeros, total, "efectivo", numeroAleatorio);
                    int idReserva = bd.buscarIdReserva(codigo_reserva);
                    int idPago = bd.buscarIdPago(numeroAleatorio);
                    bd.actualizarReservaPago(idPago, idReserva);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        enviarNotificacion("Gracias por volar con UM-AIRLINES. Código de reserva: "+ codigo_reserva);
                        //enviarNotificacion("Código de reserva: "+ codigo_reserva);
                    }
                    Toast.makeText(PantallaPago.this, "Se ha corfirmado el pago. Muchas gracias por volar con nosotros.", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(PantallaPago.this, MainActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                catch (Exception e)
                {
                    e.getMessage();
                }
            }
        }, 5000);   //5 seconds

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
                .setSmallIcon(R.drawable.um)
                .setContentTitle("LAUM-AIRLINES")
                .setContentText(pass)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);
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
}
