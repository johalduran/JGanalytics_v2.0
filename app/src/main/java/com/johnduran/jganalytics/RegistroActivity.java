package com.johnduran.jganalytics;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegistroActivity extends AppCompatActivity {

    private String correo, contrasena, repcontrasena;
    EditText eCorreo, eContrasena, eRepContrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        eCorreo = (EditText) findViewById(R.id.eCorreo);
        eContrasena = (EditText) findViewById(R.id.eContrasena);
        eRepContrasena = (EditText) findViewById(R.id.eRepContrasena);
    }

    public void registrar(View view) {
        correo = eCorreo.getText().toString();
        contrasena = eContrasena.getText().toString();
        repcontrasena = eRepContrasena.getText().toString();
        if(correo.isEmpty() || contrasena.isEmpty() || repcontrasena.isEmpty()){
            Toast.makeText(this, "Faltan campos por llenar", Toast.LENGTH_SHORT).show();


        }else {
            if (!(correo.contains("@") && correo.contains("."))){
                Toast.makeText(this, "Debe introducir un correo válido", Toast.LENGTH_SHORT).show();

            }else {
                if(contrasena.equals(repcontrasena)){
                    Intent intent = new Intent();
                    intent.putExtra("correo", correo);
                    intent.putExtra("contrasena", contrasena);
                    setResult(RESULT_OK, intent);
                    finish();
                }else{
                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();

                }
            }
        }



    }
}

