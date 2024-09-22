package com.example.control;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;

public class cargarSemanas extends Fragment {

    public cargarSemanas() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cargar_semanas, container, false);

        // Obtener referencias a los TextView de la tabla
        TextView lunesTiempo = view.findViewById(R.id.lunes_tiempo);
        TextView martesTiempo = view.findViewById(R.id.martes_tiempo);
        TextView miercolesTiempo = view.findViewById(R.id.miercoles_tiempo);
        TextView juevesTiempo = view.findViewById(R.id.jueves_tiempo);
        TextView viernesTiempo = view.findViewById(R.id.viernes_tiempo);
        TextView sabadoTiempo = view.findViewById(R.id.sabado_tiempo);
        TextView domingoTiempo = view.findViewById(R.id.domingo_tiempo);

        // Ejemplo de datos de ejercicio (en minutos)
        Map<String, Integer> tiemposEjercicio = new HashMap<>();
        tiemposEjercicio.put("Lunes", 30);
        tiemposEjercicio.put("Martes", 45);
        tiemposEjercicio.put("Miércoles", 60);
        tiemposEjercicio.put("Jueves", 50);
        tiemposEjercicio.put("Viernes", 40);
        tiemposEjercicio.put("Sábado", 70);
        tiemposEjercicio.put("Domingo", 20);

        // Asignar tiempos a los TextView
        lunesTiempo.setText(tiemposEjercicio.get("Lunes") + " min");
        martesTiempo.setText(tiemposEjercicio.get("Martes") + " min");
        miercolesTiempo.setText(tiemposEjercicio.get("Miércoles") + " min");
        juevesTiempo.setText(tiemposEjercicio.get("Jueves") + " min");
        viernesTiempo.setText(tiemposEjercicio.get("Viernes") + " min");
        sabadoTiempo.setText(tiemposEjercicio.get("Sábado") + " min");
        domingoTiempo.setText(tiemposEjercicio.get("Domingo") + " min");

        return view;
    }
}
