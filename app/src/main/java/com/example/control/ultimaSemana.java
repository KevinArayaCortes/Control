package com.example.control;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;
import java.util.Map;

public class ultimaSemana extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ultima_semana);

        // Obtener referencias a los TextView de la tabla
        TextView lunesTiempo = findViewById(R.id.lunes_tiempo);
        TextView martesTiempo = findViewById(R.id.martes_tiempo);
        TextView miercolesTiempo = findViewById(R.id.miercoles_tiempo);
        TextView juevesTiempo = findViewById(R.id.jueves_tiempo);
        TextView viernesTiempo = findViewById(R.id.viernes_tiempo);
        TextView sabadoTiempo = findViewById(R.id.sabado_tiempo);
        TextView domingoTiempo = findViewById(R.id.domingo_tiempo);

        // Ejemplo: tiempos de ejercicio en minutos
        Map<String, Integer> tiemposEjercicio = new HashMap<>();
        tiemposEjercicio.put("Lunes", 30);
        tiemposEjercicio.put("Martes", 45);
        tiemposEjercicio.put("Miércoles", 60);
        tiemposEjercicio.put("Jueves", 50);
        tiemposEjercicio.put("Viernes", 40);
        tiemposEjercicio.put("Sábado", 70);
        tiemposEjercicio.put("Domingo", 20);

        // Asignar los tiempos a los TextView
        lunesTiempo.setText(tiemposEjercicio.get("Lunes") + " min");
        martesTiempo.setText(tiemposEjercicio.get("Martes") + " min");
        miercolesTiempo.setText(tiemposEjercicio.get("Miércoles") + " min");
        juevesTiempo.setText(tiemposEjercicio.get("Jueves") + " min");
        viernesTiempo.setText(tiemposEjercicio.get("Viernes") + " min");
        sabadoTiempo.setText(tiemposEjercicio.get("Sábado") + " min");
        domingoTiempo.setText(tiemposEjercicio.get("Domingo") + " min");
    }

    public void volver(View v) {
        Intent i = new Intent(this, Desafio.class);
        startActivity(i);
    }

}
