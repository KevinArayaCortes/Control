package com.example.control;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Historial extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_historial);
    }

    public void volver(View v) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void cargar(View v) {
        Intent i = new Intent(this, Semanas.class);
        startActivity(i);
    }

}