package com.example.control;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Semanas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_semanas);

        // Si el estado guardado es nulo, cargamos el fragmento
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView3, new cargarSemanas())
                    .commit();
        }
    }

    public void volver(View v) {
        Intent i = new Intent(this, Historial.class);
        startActivity(i);
    }
}
