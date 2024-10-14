package com.example.control;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class tablaActividad extends Fragment {

    private static final String PREFERENCES_NAME = "actividadPreferences";
    private static final String KEY_ACTIVIDADES = "actividades";
    private static final String KEY_TIEMPOS = "tiempos";

    private SharedPreferences sharedPreferences;
    private TableLayout tableLayout;
    private Set<String> actividades;
    private Set<String> tiempos;

    public tablaActividad() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tabla_actividad, container, false);

        sharedPreferences = requireActivity().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        tableLayout = rootView.findViewById(R.id.tableLayoutActividades);

        cargarDatosEnTabla();
        return rootView;
    }

    private void cargarDatosEnTabla() {
        actividades = sharedPreferences.getStringSet(KEY_ACTIVIDADES, new HashSet<>());
        tiempos = sharedPreferences.getStringSet(KEY_TIEMPOS, new HashSet<>());

        if (actividades != null && tiempos != null && !actividades.isEmpty() && !tiempos.isEmpty()) {
            List<String> actividadesList = new ArrayList<>(actividades);
            List<String> tiemposList = new ArrayList<>(tiempos);
            int minLength = Math.min(actividadesList.size(), tiemposList.size());

            for (int i = 0; i < minLength; i++) {
                agregarFilaATabla(actividadesList.get(i), tiemposList.get(i));
            }
        }
    }

    private void agregarFilaATabla(final String actividad, final String tiempo) {
        TableRow row = new TableRow(getContext());
        row.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView textViewActividad = new TextView(getContext());
        textViewActividad.setText(actividad);
        textViewActividad.setTextColor(getResources().getColor(R.color.white));
        row.addView(textViewActividad);

        TextView textViewTiempo = new TextView(getContext());
        textViewTiempo.setText(tiempo);
        textViewTiempo.setTextColor(getResources().getColor(R.color.white));
        row.addView(textViewTiempo);

        row.setOnClickListener(v -> abrirEditarActividadFragment(actividad, tiempo));

        tableLayout.addView(row);
    }

    private void abrirEditarActividadFragment(String actividad, String tiempo) {
        editarActividadFragment fragment = new editarActividadFragment();
        Bundle args = new Bundle();
        args.putString("actividad", actividad);
        args.putString("tiempo", tiempo);
        fragment.setArguments(args);

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.contenedor, fragment)
                .addToBackStack(null)
                .commit();
    }
}
