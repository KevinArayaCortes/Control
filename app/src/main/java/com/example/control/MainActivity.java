package com.example.control;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        }

    public void higienico(View v){
        Intent i = new Intent(this,Higienico.class);
        startActivity(i);
    }

    public void actividad(View v){
        Intent i = new Intent(this,ActividadFisica.class);
        startActivity(i);
    }

    public void dieta(View v){
        Intent i = new Intent(this,Dieta.class);
        startActivity(i);
    }

    public void hobbie(View v){
        Intent i = new Intent(this,Hobbie.class);
        startActivity(i);
    }

    public void Historial(View v){
        Intent i = new Intent(this,Historial.class);
        startActivity(i);
    }

    public void Desafio(View v){
        Intent i = new Intent(this,Desafio.class);
        startActivity(i);
    }

}
