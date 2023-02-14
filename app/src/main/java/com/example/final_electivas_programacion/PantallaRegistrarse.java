package com.example.final_electivas_programacion;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PantallaRegistrarse extends AppCompatActivity {
    Cursor data;
    Button volver;
    Button crearUsuario;
    EditText usuario;
    EditText password;
    EditText nombre;
    EditText apellido;
    EditText dni;
    int idPersona = 0;

    String userStr;
    String passStr;
    String nameStr;
    String surnameStr;
    String docStr;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_registro);

        volver = findViewById(R.id.buttonSalir);
        crearUsuario = findViewById(R.id.buttonCrearUsuario);
        usuario = findViewById(R.id.editTextUsuario);
        password = findViewById(R.id.editTextPassword);
        nombre = findViewById(R.id.editTextNombre);
        apellido = findViewById(R.id.editTextApellido);
        dni = findViewById(R.id.editTextNumberDNI);
    }

    public void crearUsuario() {
        Boolean ok = this.validarCampos();
        if(ok){
            try{
                DataBase admin = new DataBase(this);
                Persona p = admin.buscarPersonaPorDni(Integer.parseInt(docStr));
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
                        this.ingresar(p);
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
        userStr = usuario.getText().toString();
        passStr = password.getText().toString();
        nameStr = nombre.getText().toString();
        surnameStr = apellido.getText().toString();
        docStr = dni.getText().toString();
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
