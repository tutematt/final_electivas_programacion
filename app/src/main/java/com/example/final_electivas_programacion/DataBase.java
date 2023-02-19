package com.example.final_electivas_programacion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ELECTIVA_FINAL.db";
    private static final int DATABASE_VERSION = 4;

    public DataBase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase dataBase) {
        // IF NOT EXIST CREATE DATABASE
        dataBase.execSQL("CREATE TABLE TARIFA(ID_TARIFA int primary key, CODE text, NAME text, PRICE float)");
        dataBase.execSQL("CREATE TABLE PAGO(ID_PAGO int primary key, DateTime DATE, ID_TARIFA int, DISCOUNT float, NUMBER_PASSENGERS int, TOTAL_AMOUNT float /*, PAYMENT_TYPE text*/ )");
        dataBase.execSQL("CREATE TABLE VUELO(ID_VUELO int primary key, CODE text, ORIGIN text, ARRIVAL text, DATE_ORIGIN text, DATE_ARRIVAL text, CAPABILITY int, RESTRICTION double, AVION text)");
        // FALTA tabla intermedia para asociar asientos al vuelo ASIENTOS_X_VUELO
        dataBase.execSQL("CREATE TABLE ASIENTO(ID_ASIENTO int primary key, LINE text, NUMBER int, STATUS int, ID_TARIFA int)");
        dataBase.execSQL("CREATE TABLE RESERVA(ID_RESERVA int primary key, ID_VUELO int, ID_PAGO int, IS_CANCEL boolean, ID_PASAJERO int)");
        //tabla intermedia entre los la reserva y los tickets
        //dataBase.execSQL("CREATE TABLE ASIENTOS_RESERVAS(id int primary key, id_reserva int, id_asiento int)");
        dataBase.execSQL("CREATE TABLE PERSONA(ID_PERSONA int primary key, DNI int, NAME text, SURNAME text, USERNAME text, PASSWORD text, IS_ADMIN boolean)");
        // tabla intermedia para asociar las reservas a la persona
        dataBase.execSQL("CREATE TABLE TICKET(ID_TICKET int primary key, ID_VUELO int, ID_PERSONA int, ISVALID boolean)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase dataBase, int var1, int var2) {
        dataBase.execSQL("DROP TABLE IF EXISTS TARIFA");
        dataBase.execSQL("DROP TABLE IF EXISTS PAGO");
        dataBase.execSQL("DROP TABLE IF EXISTS VUELO");
        dataBase.execSQL("DROP TABLE IF EXISTS RESERVA");
        dataBase.execSQL("DROP TABLE IF EXISTS PERSONA");
        dataBase.execSQL("DROP TABLE IF EXISTS TICKET");
        dataBase.execSQL("DROP TABLE IF EXISTS ASIENTO");
        onCreate(dataBase);
    }

    public void validarUsuariosAdmin() {
        try {

            SQLiteDatabase dataBase = this.getWritableDatabase();
            String q = "INSERT INTO PERSONA(DNI, USERNAME, PASSWORD, SURNAME, IS_ADMIN) SELECT * FROM (SELECT 9999 AS dni, 'admin' as username, '1234' as password, 'user' as surname, 1 as is_admin) AS X WHERE NOT EXISTS (SELECT * FROM PERSONA WHERE DNI=X.dni)";
            dataBase.execSQL( q );
        } catch (Exception exception) {
            exception.getMessage();
        }
    }

    // region Personas
    public void crearPersona(String user, String pass, String name, String surname, Integer dni) {
        try {
            SQLiteDatabase dataBase = this.getWritableDatabase();
            ContentValues campos = new ContentValues();
            campos.put("DNI", dni);             // 1
            campos.put("USERNAME", user);       // 2
            campos.put("PASSWORD", pass);       // 3
            campos.put("NAME", name);           // 4
            campos.put("SURNAME", surname);     // 5
            campos.put("IS_ADMIN", false);      // 6
            dataBase.insert("PERSONA", null, campos);
            dataBase.close();
        } catch (Exception exception) {
            exception.getMessage();
        }
    }

    public Persona buscarPersona(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Persona p = null;
        String q = "SELECT * FROM PERSONA WHERE USERNAME = " + "'" + username + "' AND PASSWORD = " + "'" + password + "';";
        Cursor cursor = db.rawQuery(q, null);
        if (cursor.moveToFirst()) {
            int id, doc;
            String nom, apell, user, pass;
            Boolean esAdmin;
            //id = cursor.getInt(0);
            doc = cursor.getInt(1);
            nom = cursor.getString(2);
            apell = cursor.getString(3);
            user = cursor.getString(4);
            pass = cursor.getString(5);
            String valor = cursor.getString(6);
            if(cursor.getString(6).equals("1"))
                esAdmin = true;
            else
                esAdmin = false;
            //esAdmin = Boolean.parseBoolean(cursor.getString(6));
            p = new Persona(/*id, */ doc, nom, apell, user, pass, esAdmin);
        }
        return p;
    }

    public Persona buscarPersonaPorDni(Integer dni) {
        SQLiteDatabase db = this.getWritableDatabase();
        Persona p = null;
        String q = "SELECT * FROM PERSONA WHERE DNI = " + dni + ";";
        Cursor cursor = db.rawQuery(q, null);
        if (cursor.moveToFirst()) {
            int id, doc;
            String nom, apell, user, pass;
            Boolean esAdmin;
            // id = cursor.getInt(0);
            doc = cursor.getInt(1);
            nom = cursor.getString(2);
            apell = cursor.getString(3);
            user = cursor.getString(4);
            pass = cursor.getString(5);
            esAdmin = Boolean.parseBoolean(cursor.getString(6));
            p = new Persona(/*id,*/ doc, nom, apell, user, pass, esAdmin);
        }
        return p;
    }

    public Persona buscarPersonaPorUser(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        Persona p = null;
        String q = "SELECT * FROM PERSONA WHERE USERNAME = " + "'" + username + "';";
        Cursor cursor = db.rawQuery(q, null);
        if (cursor.moveToFirst()) {
            int id, doc;
            String nom, apell, user, pass;
            Boolean esAdmin;
            id = cursor.getInt(0);
            doc = cursor.getInt(1);
            nom = cursor.getString(2);
            apell = cursor.getString(3);
            user = cursor.getString(4);
            pass = cursor.getString(5);
            esAdmin = Boolean.parseBoolean(cursor.getString(6));
            p = new Persona(/*id,*/ doc, nom, apell, user, pass, esAdmin);
        }
        return p;
    }

    public void actualizarPersona(Integer dni, String username, String pass, String name, String surname) {
        SQLiteDatabase dataBase = this.getWritableDatabase();
        dataBase.execSQL("UPDATE PERSONA SET USERNMAE = " + "'" + username + "', PASSWORD = " + "'" + pass + "', NAME = " + "'" + name + "', SURNAME = " + "'" + surname + "' WHERE DNI = " + dni + ";", null);
    }

    // endregion

    // region Vuelos
    public Cursor traerVuelos() {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor data = database.rawQuery("SELECT CODE FROM VUELO", null);
        return data;
    }

    public void crearVuelo(String code, String origin, String arrival, String date, int capability, double restriction, String avion, String fechaYHoraDestino) {
        SQLiteDatabase dataBase = this.getWritableDatabase();
        ContentValues campos = new ContentValues();
        campos.put("CODE", code);               // 1
        campos.put("ORIGIN", origin);           // 2
        campos.put("ARRIVAL", arrival);         // 3
        campos.put("DATE_ORIGIN", date);               // 4
        campos.put("CAPABILITY", capability);   // 5
        campos.put("RESTRICTION", restriction); // 6
        campos.put("AVION", avion);
        campos.put("DATE_ARRIVAL", fechaYHoraDestino);
        dataBase.insert("VUELO", null, campos);
        dataBase.close();
    }

    public Cursor buscarVuelo(String codigo_vuelo) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor data = database.rawQuery("SELECT RESTRICTION, DATE_ORIGIN, DATE_ARRIVAL FROM VUELO WHERE CODE="+"'"+codigo_vuelo+"'", null);
        return data;
    }

    public void actualizarVuelo(String fechaYHora, String codigo_vuelo, String fechayHoraDestino) {
        SQLiteDatabase dataBase = this.getWritableDatabase();
        String q = "UPDATE VUELO SET DATE_ORIGIN="+"'"+fechaYHora+"', DATE_ARRIVAL="+"'"+fechayHoraDestino+"' WHERE CODE="+"'"+codigo_vuelo+"'";
        dataBase.execSQL( q );
    }

    public Cursor traerVuelosxFecha(String fechaDesde, String fechaHasta) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor data = database.rawQuery("SELECT CODE, DATE_ORIGIN, DATE_ARRIVAL  FROM VUELO WHERE date(substr(DATE_ORIGIN, 7,4) || '-' || substr(DATE_ORIGIN,4,2) || '-' || substr(DATE_ORIGIN,1,2)) ="+"'"+fechaDesde+"' AND date(substr(DATE_ARRIVAL, 7,4) || '-' || substr(DATE_ARRIVAL,4,2) || '-' || substr(DATE_ARRIVAL,1,2))="+"'"+fechaHasta+"'", null);
        return data;
    }

   /*
    public Vuelo buscarVueloPorAvionYFecha(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        Persona v = null;
        String q = "SELECT * FROM Vuelo WHERE AVION = " + "'" + FECHA + "';";
        Cursor cursor = db.rawQuery(q, null);
        if (cursor.moveToFirst()) {
            int capacidad;
            double restriccion;
            String codigo, origen, destino;
            codigo = cursor.getString(0);
            doc = cursor.getInt(1);
            nom = cursor.getString(2);
            apell = cursor.getString(3);
            user = cursor.getString(4);
            pass = cursor.getString(5);
            esAdmin = Boolean.parseBoolean(cursor.getString(6));
            v = new Vuelo(doc, nom, apell, user, pass, esAdmin);
        }
        return v;
    }
    */


    // endregion

}
