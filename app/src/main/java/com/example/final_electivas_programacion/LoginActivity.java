package com.example.final_electivas_programacion;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;


public class LoginActivity extends AppCompatActivity {

    Button login, crearUsuario, recuperarContrasenia;
    Cursor data;
    Integer idPersona; // REVISAR COMO MANEJAR VARIABLES "GLOBALES" PARA SABER SI ES ADMIN O NO
    Boolean esAdmin; // REVISAR COMO MANEJAR VARIABLES "GLOBALES" PARA SABER SI ES ADMIN O NO

    /*Layout*/
    TextInputLayout layoutUser, layoutPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_login);

        DataBase admin = new DataBase(LoginActivity.this);
        admin.validarUsuariosAdmin();
        //this.databaseExists();
        setearBotones();

        layoutUser.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });
        layoutPass.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });
        login.setOnClickListener(view -> {
            login();
        });
        crearUsuario.setOnClickListener(view -> {
            registrarse();
        });
        recuperarContrasenia.setOnClickListener(view -> {
            recuperarContrasenia();
        });

    }

    private void setearBotones() {
        login = findViewById(R.id.ButtonIniciarSesionPL);
        crearUsuario = findViewById(R.id.ButtonRegistrarsePL);;
        recuperarContrasenia = findViewById(R.id.buttonRecuperarContraseña);
        layoutUser = findViewById(R.id.layoutUsuarioPL);
        layoutPass = findViewById(R.id.layoutPasswordPL);
    }

    public void login() {
        Persona p = validarUsuario(); // valido que complete los campos requeridos
        if(p != null){
            idPersona = p.getDni();
            Intent intent;
            if(p.getEsAdmin()){
                intent = new Intent(this, AdminActivity.class);
                intent.putExtra("esAdmin", true);
            }
            else
                intent = new Intent(this, MainActivity.class);
            intent.putExtra("idPersona", idPersona);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    public void registrarse() {
        Intent i = new Intent(this, PantallaRegistrarse.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public Persona validarUsuario() {
        Persona p = null;
        if (layoutUser.getEditText().getText().toString().isEmpty() || layoutPass.getEditText().getText().toString().isEmpty()){
            Toast.makeText(this, "Debe ingresar un Usuario y una Contraseña", Toast.LENGTH_SHORT).show();
        }
        else{
            DataBase admin = new DataBase(this);
            p = admin.buscarPersona(layoutUser.getEditText().getText().toString(), layoutPass.getEditText().getText().toString());
            if(p == null){
                Toast.makeText(this, "Usuario y/o Contraseña invalidos", Toast.LENGTH_SHORT).show();
            }
        }
        return p;
    }

    public void recuperarContrasenia() {
        Intent i = new Intent(this, PantallaRecuperarContrasenia.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
