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

public class tablaDieta extends Fragment {

    private static final String PREFERENCES_NAME = "dietaPreferences";
    private static final String KEY_NOMBRES = "nombres";
    private static final String KEY_HORAS = "horas";

    private SharedPreferences sharedPreferences;
    private TableLayout tableLayout;
    private Set<String> nombres;
    private Set<String> horas;

    public tablaDieta() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tabla_dieta, container, false);

        sharedPreferences = requireActivity().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        tableLayout = rootView.findViewById(R.id.tableLayoutHorarios);

        cargarDatosEnTabla();
        return rootView;
    }

    private void cargarDatosEnTabla() {
        nombres = sharedPreferences.getStringSet(KEY_NOMBRES, new HashSet<>());
        horas = sharedPreferences.getStringSet(KEY_HORAS, new HashSet<>());

        if (nombres != null && horas != null && !nombres.isEmpty() && !horas.isEmpty()) {
            List<String> nombresList = new ArrayList<>(nombres);
            List<String> horasList = new ArrayList<>(horas);
            int minLength = Math.min(nombresList.size(), horasList.size());

            for (int i = 0; i < minLength; i++) {
                agregarFilaATabla(nombresList.get(i), horasList.get(i));
            }
        }
    }


    private void agregarFilaATabla(final String nombre, final String hora) {
        TableRow row = new TableRow(getContext());
        row.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView textViewNombre = new TextView(getContext());
        textViewNombre.setText(nombre);
        textViewNombre.setTextColor(getResources().getColor(R.color.white));
        row.addView(textViewNombre);

        TextView textViewHora = new TextView(getContext());
        textViewHora.setText(hora);
        textViewHora.setTextColor(getResources().getColor(R.color.white));
        row.addView(textViewHora);

        row.setOnClickListener(v -> abrirEditarDietaFragment(nombre, hora));

        tableLayout.addView(row);
    }

    private void abrirEditarDietaFragment(String nombre, String hora) {
        editarDietaFragment fragment = new editarDietaFragment();
        Bundle args = new Bundle();
        args.putString("nombre", nombre);
        args.putString("hora", hora);
        fragment.setArguments(args);

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.contenedor, fragment)
                .addToBackStack(null)
                .commit();
    }
}
