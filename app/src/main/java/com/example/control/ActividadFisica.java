package com.example.control;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class ActividadFisica extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_fisica);
    }

    public void menu(View v){
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }

}
