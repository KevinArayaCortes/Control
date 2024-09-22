package com.example.control;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class ActividadFisica extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_fisica);

        // Configurar el botón para crear un nuevo temporizador
        findViewById(R.id.btn_crear_nuevo).setOnClickListener(v -> {
            Intent intent = new Intent(ActividadFisica.this, crearTempos.class);
            startActivityForResult(intent, 1);  // Iniciar crearTempos esperando un resultado
        });
    }

    // Método para recibir el resultado de crearTempos
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Obtener los datos enviados de crearTempos
            String nombre = data.getStringExtra("nombre");
            String tiempo = data.getStringExtra("tiempo");

            // Agregar el nuevo temporizador al fragmento tablaTempos
            agregarTemporizador(nombre, tiempo);
        }
    }

    // Método para agregar un temporizador al fragmento tablaTempos
    private void agregarTemporizador(String nombre, String tiempo) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        tablaTempos fragment = (tablaTempos) fragmentManager.findFragmentById(R.id.fragmentContainerView2);

        if (fragment != null && fragment.isAdded()) {
            fragment.agregarFilaTemporizador(nombre, tiempo);
        } else {
            Toast.makeText(this, "El fragmento no está disponible.", Toast.LENGTH_SHORT).show();
        }
    }
}
