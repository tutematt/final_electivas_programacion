package com.example.final_electivas_programacion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ELECTIVA_FINAL.db";
    private static final int DATABASE_VERSION = 14;

    public DataBase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase dataBase) {
        dataBase.execSQL("CREATE TABLE TARIFA(ID_TARIFA INTEGER PRIMARY KEY AUTOINCREMENT, CODE text, NAME text, PRICE float)");
        dataBase.execSQL("CREATE TABLE PAGO(ID_PAGO INTEGER PRIMARY KEY AUTOINCREMENT, DateTime DATE, ID_TARIFA int, DISCOUNT float, NUMBER_PASSENGERS int, TOTAL_AMOUNT float, PAYMENT_TYPE text, NUMBER_CONFIRM int)");
        dataBase.execSQL("CREATE TABLE VUELO(ID_VUELO INTEGER PRIMARY KEY AUTOINCREMENT, CODE text, ORIGIN text, ARRIVAL text, DATE_ORIGIN text, DATE_ARRIVAL text, CAPABILITY int, RESTRICTION double, AVION text)");
        dataBase.execSQL("CREATE TABLE ASIENTO_VUELO(ID_ASIENTO_VUELO INTEGER PRIMARY KEY AUTOINCREMENT, ID_VUELO int, CODE_ASIENTO text, ESTADO text)");
        dataBase.execSQL("CREATE TABLE ASIENTO(ID_ASIENTO INTEGER PRIMARY KEY AUTOINCREMENT, LINE text, NUMBER int, CODE text, ID_TARIFA int)");
        dataBase.execSQL("CREATE TABLE RESERVA(ID_RESERVA INTEGER PRIMARY KEY AUTOINCREMENT, CODE text, CODE_VUELO text, ID_PAGO int, IS_CANCEL boolean, ID_PASAJERO int)");
        dataBase.execSQL("CREATE TABLE RESERVA_PERSONA(ID_RESERVA_PERSONA INTEGER PRIMARY KEY AUTOINCREMENT, CODE_RESERVA, DNI_PERSONA)");
        dataBase.execSQL("CREATE TABLE PERSONA(ID_PERSONA INTEGER PRIMARY KEY AUTOINCREMENT, DNI int, NAME text, SURNAME text, USERNAME text, PASSWORD text, IS_ADMIN boolean)");
        dataBase.execSQL("CREATE TABLE TICKET(ID_TICKET INTEGER PRIMARY KEY AUTOINCREMENT, CODE text, ID_PERSONA int, ISVALID boolean)");
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
        dataBase.execSQL("DROP TABLE IF EXISTS ASIENTO_VUELO");
        dataBase.execSQL("DROP TABLE IF EXISTS RESERVA_PERSONA");
        onCreate(dataBase);
    }

    public void validarUsuariosAdmin() {
        try {

            SQLiteDatabase dataBase = this.getWritableDatabase();
            String q = "INSERT INTO PERSONA(DNI, USERNAME, PASSWORD, SURNAME, IS_ADMIN) SELECT * FROM (SELECT 9999 AS dni, 'admin' as username, '1234' as password, 'user' as surname, 1 as is_admin) AS X WHERE NOT EXISTS (SELECT * FROM PERSONA WHERE DNI=X.dni)";
            dataBase.execSQL( q );
            crearTarifa("Turista", "Turista", 220000);
                crearTarifa("Primera", "Primera", 338000);
        } catch (Exception exception) {
            exception.getMessage();
        }
    }

    //region tarifas
    void crearTarifa(String code, String name, float price)
    {
        try {
            SQLiteDatabase dataBase = this.getWritableDatabase();
            String q = "INSERT INTO TARIFA(CODE, NAME, PRICE) SELECT * FROM (SELECT " + "'" + code + "' AS CODE, " + "'" + name + "' as username, " + "" + price + " as price) AS X WHERE NOT EXISTS (SELECT * FROM TARIFA WHERE CODE=X.CODE)";
            dataBase.execSQL(q);
        } catch (Exception exception) {
            exception.getMessage();
        }
    }

    //endregion

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

    public void crearPasajero(String name, String surname, Integer dni) {
        try {
            SQLiteDatabase dataBase = this.getWritableDatabase();
            ContentValues campos = new ContentValues();
            campos.put("DNI", dni);             // 1
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
        String q = "UPDATE PERSONA SET USERNAME = " + "'" + username + "', PASSWORD = " + "'" + pass + "', NAME = " + "'" + name + "', SURNAME = " + "'" + surname + "' WHERE DNI = " + dni + ";";
        dataBase.execSQL( q );
    }

    // endregion

    // region Vuelos
    public Cursor traerVuelos() {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor data = database.rawQuery("SELECT CODE, DATE_ORIGIN, DATE_ARRIVAL FROM VUELO", null);
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
        Cursor data = database.rawQuery("SELECT RESTRICTION, DATE_ORIGIN, DATE_ARRIVAL, ID_VUELO, AVION FROM VUELO WHERE CODE="+"'"+codigo_vuelo+"'", null);
        return data;
    }

    public void actualizarVuelo(String fechaYHora, String codigo_vuelo, String fechayHoraDestino) {
        SQLiteDatabase dataBase = this.getWritableDatabase();
        String q = "UPDATE VUELO SET DATE_ORIGIN="+"'"+fechaYHora+"', DATE_ARRIVAL="+"'"+fechayHoraDestino+"' WHERE CODE="+"'"+codigo_vuelo+"'";
        dataBase.execSQL( q );
    }

    public Cursor traerVuelosxFecha(String fechaDesde, String fechaHasta, String tarifa) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor data = database.rawQuery("SELECT V.CODE, COUNT(AV.ESTADO) AS ASIENTOS_DISPONIBLES, T.PRICE, V.DATE_ORIGIN, V.DATE_ARRIVAL " +
                "FROM VUELO AS V " +
                "JOIN ASIENTO_VUELO AS AV ON V.ID_VUELO=AV.ID_VUELO " +
                "JOIN ASIENTO AS A ON A.CODE=AV.CODE_ASIENTO " +
                "JOIN TARIFA AS T ON T.ID_TARIFA=A.ID_TARIFA " +
                "WHERE date(substr(V.DATE_ORIGIN, 7,4) || '-' || substr(V.DATE_ORIGIN,4,2) || '-' || substr(V.DATE_ORIGIN,1,2)) ="+"'"+fechaDesde+"' " +
                "AND date(substr(V.DATE_ARRIVAL, 7,4) || '-' || substr(V.DATE_ARRIVAL,4,2) || '-' || substr(V.DATE_ARRIVAL,1,2))="+"'"+fechaHasta+"'" +
                "AND V.CAPABILITY>0 AND AV.ESTADO='Disponible' AND T.NAME="+"'"+tarifa+"' " +
                "GROUP BY V.CODE, V.DATE_ORIGIN, V.DATE_ARRIVAL, AV.ESTADO, T.PRICE " +
                "ORDER BY V.CODE", null);
        return data;
    }

    public Cursor traerVuelossinFecha(String tarifa) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor data = database.rawQuery("SELECT V.CODE, COUNT(AV.ESTADO) AS ASIENTOS_DISPONIBLES, T.PRICE, V.DATE_ORIGIN, V.DATE_ARRIVAL " +
                "FROM VUELO AS V " +
                "JOIN ASIENTO_VUELO AS AV ON V.ID_VUELO=AV.ID_VUELO " +
                "JOIN ASIENTO AS A ON A.CODE=AV.CODE_ASIENTO " +
                "JOIN TARIFA AS T ON T.ID_TARIFA=A.ID_TARIFA " +
                "WHERE V.CAPABILITY>0 AND AV.ESTADO='Disponible' AND T.NAME="+"'"+tarifa+"' " +
                "GROUP BY V.CODE, V.DATE_ORIGIN, V.DATE_ARRIVAL, AV.ESTADO, T.PRICE " +
                "ORDER BY V.CODE", null);
        return data;
    }



    public void crearAsientos(int cant) {
        SQLiteDatabase db = this.getWritableDatabase();
        String q = "INSERT INTO ASIENTO(LINE,NUMBER,CODE,ID_TARIFA) SELECT * FROM ( ";
        String values = "";
        int porcentajePrimera = cant*10/100;
        int porcentajeTurista = cant*90/100;
        for(int i=0;i<porcentajePrimera;i++)
        {
            values+="SELECT"+"'"+i+"'AS LINE,"+"'"+i+"' AS NUMBER, "+"'P"+i+"' AS CODE, 1 AS ID_TARIFA\n UNION\n ";
        }
        q+=values;
        values="";
        for(int i=0;i<porcentajeTurista;i++)
        {
            if(porcentajeTurista-1==i)
                values+="SELECT"+"'"+i+"'AS LINE,"+"'"+i+"' AS NUMBER,"+"'T"+i+"' AS CODE, 2 AS ID_TARIFA\n";
            else
                values+="SELECT"+"'"+i+"'AS LINE,"+"'"+i+"' AS NUMBER,"+"'T"+i+"' AS CODE, 2 AS ID_TARIFA\n UNION\n ";
        }
        q+=values;
        q+=") AS T WHERE NOT EXISTS (SELECT * FROM ASIENTO WHERE T.CODE=CODE AND T.ID_TARIFA=ID_TARIFA)";
        db.execSQL(q);
    }

    public void crearAsientosxVuelo(int cant, int id_vuelo) {
        SQLiteDatabase db = this.getWritableDatabase();
        String q = "INSERT INTO ASIENTO_VUELO(ID_VUELO,CODE_ASIENTO,ESTADO) SELECT * FROM ( ";
        String values = "";
        int porcentajePrimera = cant*10/100;
        int porcentajeTurista = cant*90/100;
        for(int i=1;i<=porcentajePrimera;i++)
        {
            values+="SELECT"+"'"+id_vuelo+"'AS ID_VUELO,"+"'P"+i+"' AS CODE_ASIENTO, 'Disponible' AS ESTADO\n UNION\n ";
        }
        q+=values;
        values="";
        for(int i=0;i<=porcentajeTurista;i++)
        {
            if(porcentajeTurista==i)
                values+="SELECT"+"'"+id_vuelo+"'AS ID_VUELO,"+"'T"+i+"' AS CODE_ASIENTO, 'Disponible' AS ESTADO\n";
            else
                values+="SELECT"+"'"+id_vuelo+"'AS ID_VUELO,"+"'T"+i+"' AS CODE_ASIENTO, 'Disponible' AS ESTADO\n UNION\n ";
        }
        q+=values;
        q+=") AS T WHERE NOT EXISTS (SELECT * FROM ASIENTO_VUELO WHERE ID_VUELO=T.ID_VUELO AND CODE_ASIENTO=T.CODE_ASIENTO)";
        db.execSQL(q);
    }

    //region reserva
    public Cursor traerReservas() {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor data = database.rawQuery("SELECT ID_RESERVA, CODE FROM RESERVA WHERE IS_CANCEL = false", null);
        return data;
    }

    public int guardarReserva(String code, String codeVuelo, boolean cancel, int idPasajero)
    {
        SQLiteDatabase dataBase = this.getWritableDatabase();
        ContentValues campos = new ContentValues();
        campos.put("CODE", code);
        campos.put("CODE_VUELO", codeVuelo);
        campos.put("IS_CANCEL", cancel);
        campos.put("ID_PASAJERO", idPasajero);
        long insertID = dataBase.insert("RESERVA", null, campos);
        dataBase.close();
        return (int) insertID;
    }

    public void actualizarReservaPago(int idPago, int idReserva)
    {
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues registro = new ContentValues();
        registro.put("ID_PAGO", idPago);

        db.update("RESERVA", registro, "ID_RESERVA =?",new String[]{String.valueOf(idReserva)});
    }
    public Cursor buscarReserva(String code) { //Busco la reserva con el IdReserva
        //ERROR     Caused by: android.database.sqlite.SQLiteException: no such column: Reserva.idReserva (code 1 SQLITE_ERROR): , while compiling: SELECT CODE, CODE_VUELO, ID_PASAJERO FROM RESERVA WHERE Reserva.idReserva = idReserva
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor data = database.rawQuery("SELECT V.CODE, V.DATE_ARRIVAL, V.DATE_ORIGIN, P.DISCOUNT, P.NUMBER_PASSENGERS, P.TOTAL_AMOUNT, P.PAYMENT_TYPE, P.NUMBER_CONFIRM, T.NAME, V.CAPABILITY " +
                "FROM RESERVA AS R JOIN PAGO AS P ON R.ID_PAGO = P.ID_PAGO " +
                "JOIN TARIFA AS T ON T.ID_TARIFA=P.ID_TARIFA " +
                "JOIN VUELO AS V ON V.CODE=R.CODE_VUELO WHERE R.CODE="+"'"+code+"'", null);
        return data;
    }
    public void agregarPersonaxReserva(List<Persona> pasajeros)
    {

    }

    //region asiento_vuelos
    public void ocuparAsientoxVuelo(String codeVuelo, String tipoTarifa, int cantPasajeros)
    {
        try
        {
            SQLiteDatabase dataBase = this.getWritableDatabase();
            String q = "UPDATE ASIENTO_VUELO SET ESTADO='Ocupado' " +
                    "WHERE ID_ASIENTO_VUELO IN (SELECT ID_ASIENTO_VUELO FROM VUELO AS V " +
                    "JOIN ASIENTO_VUELO AS AV ON V.ID_VUELO=AV.ID_VUELO " +
                    "JOIN ASIENTO AS A ON AV.CODE_ASIENTO=A.CODE " +
                    "JOIN TARIFA AS T ON T.ID_TARIFA=A.ID_TARIFA " +
                    "WHERE V.CODE="+"'"+codeVuelo+"'  AND T.NAME="+"'"+tipoTarifa+"'  AND AV.ESTADO='Disponible' LIMIT "+""+cantPasajeros+" ) ";
            dataBase.execSQL( q );
        }catch (Exception exception) {
            exception.getMessage();
        }
    }

    public void desocuparAsientoxVuelo(String codeVuelo, String tipoTarifa, int cantPasajeros)
    {
        try
        {
            SQLiteDatabase dataBase = this.getWritableDatabase();
            String q = "UPDATE ASIENTO_VUELO SET ESTADO='Disponible' " +
                    "WHERE ID_ASIENTO_VUELO IN (SELECT ID_ASIENTO_VUELO FROM VUELO AS V " +
                    "JOIN ASIENTO_VUELO AS AV ON V.ID_VUELO=AV.ID_VUELO " +
                    "JOIN ASIENTO AS A ON AV.CODE_ASIENTO=A.CODE " +
                    "JOIN TARIFA AS T ON T.ID_TARIFA=A.ID_TARIFA " +
                    "WHERE V.CODE="+"'"+codeVuelo+"'  AND T.NAME="+"'"+tipoTarifa+"'  AND AV.ESTADO='Ocupado' LIMIT "+""+cantPasajeros+" ) ";
            dataBase.execSQL( q );
        }catch (Exception exception) {
            exception.getMessage();
        }
    }

    public void actualizarCapacidad(String codeVuelo, int cantPasajeros)
    {
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues registro = new ContentValues();
        registro.put("CAPABILITY", cantPasajeros);

        db.update("VUELO", registro, "CODE =?",new String[]{String.valueOf(codeVuelo)});
    }
    //endregion

    //endregion

    //region pago

    public int guardarPago(int idTarifa, float discount, int cantPasajeros, float totalPrice, String payment, int nroConfirm)
    {
        SQLiteDatabase dataBase = this.getWritableDatabase();
        ContentValues campos = new ContentValues();
        campos.put("ID_TARIFA", idTarifa);
        campos.put("NUMBER_PASSENGERS", cantPasajeros);
        campos.put("DISCOUNT", discount);
        campos.put("TOTAL_AMOUNT", totalPrice);
        campos.put("PAYMENT_TYPE", payment);
        campos.put("NUMBER_CONFIRM", nroConfirm);
        long insertID = dataBase.insert("PAGO", null, campos);
        dataBase.close();
        return (int) insertID;
    }
    //endregion

    public Cursor cargarTarfias() {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor data = database.rawQuery("SELECT NAME, PRICE FROM TARIFA", null);
        return data;
    }

    public int buscarIdReserva(String codigo_reserva) {
        SQLiteDatabase database = this.getWritableDatabase();
        int id=0;
        Cursor data = database.rawQuery("SELECT ID_RESERVA FROM RESERVA WHERE CODE="+"'"+codigo_reserva+"'", null);
        if(data.moveToFirst())
            id = data.getInt(0);
        return id;
    }

    public void eliminarReserva(String code)
    {
        SQLiteDatabase dataBase = this.getWritableDatabase();
        ContentValues campos = new ContentValues();
        campos.put("IS_CANCEL", true);
        dataBase.update("RESERVA", campos, "CODE=?",new String[]{code});
    }

    public int buscarIdPago(int numeroAleatorio ) {
        SQLiteDatabase database = this.getWritableDatabase();
        int id=0;
        Cursor data = database.rawQuery("SELECT ID_PAGO FROM PAGO WHERE NUMBER_CONFIRM="+"'"+numeroAleatorio+"'", null);
        if(data.moveToFirst())
            id = data.getInt(0);
        return id;
    }

    public int buscarTarifa(String codigo_tarifa) {
        SQLiteDatabase database = this.getWritableDatabase();
        int id=0;
        Cursor data = database.rawQuery("SELECT ID_TARIFA FROM TARIFA WHERE CODE="+"'"+codigo_tarifa+"'", null);
        if(data.moveToFirst())
            id = data.getInt(0);
        return id;
    }

    public Cursor traerReservasxPersona(int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor data = database.rawQuery("SELECT ID_RESERVA, CODE FROM RESERVA WHERE IS_CANCEL = false AND ID_PASAJERO ="+"'"+ id +"'", null);
        return data;
    }
    // endregion

}
