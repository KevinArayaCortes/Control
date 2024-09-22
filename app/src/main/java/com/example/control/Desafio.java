package com.example.control;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Desafio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_desafio2);
    }

    public void desafio(View v){
        Intent i = new Intent(this,ultimaSemana.class);
        startActivity(i);
    }

    public void menu(View v){
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }

}