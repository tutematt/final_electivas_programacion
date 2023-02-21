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

public class PantallaPago extends AppCompatActivity {
    private static final String CHANNEL_ID = "canal";
    private PendingIntent pendingIntent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_pago);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    enviarNotificacion("Gracias por volar con UM-AIRLINES. Ya tienes disponible tu reserva.");
                }
                Toast.makeText(PantallaPago.this, "Se ha corfirmado el pago. Muchas gracias por volar con nosotros.", Toast.LENGTH_LONG).show();
                Intent i = new Intent(PantallaPago.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
}
