package com.example.control;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Higienico extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_higienico);
        }

    public void ducha(View v){
        Intent i = new Intent(this, tempoDucha.class);
        startActivity(i);
    }

    public void dientes(View v){
        Intent i = new Intent(this, tempoDientes.class);
        startActivity(i);
    }

    public void inicio(View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

}