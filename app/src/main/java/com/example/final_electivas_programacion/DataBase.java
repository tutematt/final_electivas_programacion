package com.example.final_electivas_programacion;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ELECTIVA_FINAL.db";
    private static final int DATABASE_VERSION = 1;

    public DataBase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase dataBase) {
        dataBase.execSQL("CREATE TABLE TARIFAS(id_tarifa int primary key, codigo text, nombre text, precio float)");
        dataBase.execSQL("CREATE TABLE PAGOS(id_pago int primary key, DateTime fecha, id_tarifa int, descuento float, total float, cant_pasajeros int, tipo_pago text)");
        dataBase.execSQL("CREATE TABLE VUELOS(id_vuelo int primary key, destino text, origen text, fecha_salida dateTime, fecha_llegada dateTime, restriccion double, descuento double)");
        // tabla intermedia para asociar asientos al vuelo
        dataBase.execSQL("CREATE TABLE ASIENTOS(id_asiento int primary key, fila text, estado int, id_tarifa int)");
        dataBase.execSQL("CREATE TABLE RESERVAS(id_reserva int primary key, id_vuelo int, id_pasajero int, id_pago int, cancelado boolean)");
        //tabla intermedia para los asientos de una reserva
        dataBase.execSQL("CREATE TABLE ASIENTOS_RESERVAS(id int primary key, id_reserva int, id_asiento int)");
        dataBase.execSQL("CREATE TABLE PERSONAS(id_pasajero int primary key, nro_documento int, nombre text, apellido text, usuario text, contrasenia text, admin boolean)");
        // tabla intermedia para asociar las reservas a la persona
    }

    @Override
    public void onUpgrade(SQLiteDatabase dataBase, int var1, int var2) {
        dataBase.execSQL("DROP TABLE IF EXISTS TARIFAS");
        dataBase.execSQL("DROP TABLE IF EXISTS PAGOS");
        dataBase.execSQL("DROP TABLE IF EXISTS VUELOS");
        dataBase.execSQL("DROP TABLE IF EXISTS RESERVAS");
        dataBase.execSQL("DROP TABLE IF EXISTS PERSONAS");
        dataBase.execSQL("DROP TABLE IF EXISTS ASIENTOS_RESERVAS");
        onCreate(dataBase);
    }
}
