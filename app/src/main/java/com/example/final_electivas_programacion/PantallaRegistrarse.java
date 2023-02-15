package com.example.final_electivas_programacion;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class PantallaRegistrarse extends AppCompatActivity {
    Cursor data;
    Button btnVolver, btnCrearUsuario;
    TextInputLayout usuario, password, nombre, apellido, dni;
    int idPersona = 0;
    String userStr="", passStr="", nameStr="", surnameStr="", docStr="";
    DataBase admin = new DataBase(PantallaRegistrarse.this);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_registro);
        setearBotones();
        ocultarTecladoInputs();

        btnCrearUsuario.setOnClickListener(view -> {
            crearUsuario();
        });

        btnVolver.setOnClickListener(view -> {
            volver();
        });

    }

    private void ocultarTecladoInputs() {
        usuario.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });
        password.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });
        nombre.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });
        apellido.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });
        dni.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });
    }

    private void setearBotones() {
        btnVolver = findViewById(R.id.ButtonVolverPR);
        btnCrearUsuario = findViewById(R.id.ButtonRegistrarsePR);
        usuario = findViewById(R.id.layoutUsuarioPR);
        password = findViewById(R.id.layoutPasswordPR);
        nombre = findViewById(R.id.layoutNombrePR);
        apellido = findViewById(R.id.layoutApellidoPR);
        dni = findViewById(R.id.layoutDNIPR);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void crearUsuario() {
        Boolean ok = validarCampos();
        int prueba = Integer.parseInt(docStr);
        if(ok){
            Persona p = admin.buscarPersonaPorDni(Integer.parseInt(docStr));
            try{
                if(p != null){
                    String user = p.getUsuario();
                    if(user != ""){
                        Toast.makeText(this, "Ya existe un usuario con ese DNI.", Toast.LENGTH_SHORT).show();
                    }
                    else{  // estaba en el padron pero no se registró en la app
                        admin.actualizarPersona(Integer.parseInt(docStr), userStr, passStr, nameStr, surnameStr);
                        this.ingresar(p);
                    }
                }
                else{
                    p = admin.buscarPersonaPorUser(userStr);
                    if(p != null){
                        Toast.makeText(this, "El nombre de usuario ya existe.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        admin.crearPersona(userStr, passStr, nameStr, surnameStr, Integer.parseInt(docStr));
                        // crear la persona en el array de personas ??
                        p = new Persona(Integer.parseInt(docStr), nameStr, surnameStr, userStr, passStr, false);
                        ingresar(p);
                    }
                }
            }
            catch (Exception ex){
                Toast.makeText(this, "Hubo un error, vuelva a intentar más tarde.", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
        }
    }
    public boolean validarCampos() {
        userStr = usuario.getEditText().getText().toString();
        passStr = password.getEditText().getText().toString();
        nameStr = nombre.getEditText().getText().toString();
        surnameStr = apellido.getEditText().getText().toString();
        docStr = dni.getEditText().getText().toString();
        if(userStr.isEmpty() || passStr.isEmpty() || nameStr.isEmpty() || surnameStr.isEmpty() || docStr.isEmpty()){
            return false;
        }
        else{
            return true;
        }
    }

    public void ingresar(Persona p){
        Toast.makeText(this, "¡Bienvenido" + userStr + "!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent (this, MainActivity.class);
        intent.putExtra("idPersona", p.getDni());
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void volver() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
