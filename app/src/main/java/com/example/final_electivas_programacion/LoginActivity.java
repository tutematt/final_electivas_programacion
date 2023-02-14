package com.example.final_electivas_programacion;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
//import com.example.afinal.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Calendar;


public class LoginActivity extends AppCompatActivity {

    Button login;
    Button crearUsuario;
    EditText usuario;
    EditText password;
    Cursor data;
    Integer idPersona;
    Boolean esAdmin; // REVISAR COMO MANEJAR VARIABLES "GLOBALES" PARA SABER SI ES ADMIN O NO

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_login);

        DataBase admin = new DataBase(this, null);
        //this.databaseExists();

        login = findViewById(R.id.buttonLogin);
        crearUsuario = findViewById(R.id.buttonCrearUsuario);
        usuario = findViewById(R.id.editTextUsuario);
        password = findViewById(R.id.editTextPassword);
    }

    public void login(View vista) {
        Persona p = this.validarUsuario(vista); // valido que complete los campos requeridos
        if(p != null){
            idPersona = p.getDni();
            Intent intent;
            if(p.getEsAdmin()){
                intent = new Intent(vista.getContext(), AdminActivity.class);
            }
            else{
                intent = new Intent(vista.getContext(), MainActivity.class);
            }
            intent.putExtra("idPersona", idPersona);
            startActivity(intent);
        }
    }

    public void registrarse(View vista) {
        Intent intent = new Intent(vista.getContext(), CrearUsuarioActivity.class);
        startActivity(intent);
    }

    public Persona validarUsuario(View vista) {
        Persona p = null;
        if (usuario.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
            Toast.makeText(this, "Debe ingresar un Usuario y una Contraseña", Toast.LENGTH_SHORT).show();
        }
        else{
            DataBase admin = new DataBase(this, null);
            p = admin.buscarPersona(usuario.getText().toString(), password.getText().toString());
            if(p == null){
                Toast.makeText(this, "Usuario y/o Contraseña invalidos", Toast.LENGTH_SHORT).show();
            }
        }
        return p;
    }
}
