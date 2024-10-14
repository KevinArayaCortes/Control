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

import java.util.HashSet;
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

        // Inicializa SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);

        // Inicializa la tabla
        tableLayout = rootView.findViewById(R.id.tableLayoutHorarios);

        // Carga los datos guardados y llena la tabla
        cargarDatosEnTabla();

        return rootView;
    }

    private void cargarDatosEnTabla() {
        nombres = sharedPreferences.getStringSet(KEY_NOMBRES, new HashSet<>());
        horas = sharedPreferences.getStringSet(KEY_HORAS, new HashSet<>());

        // Verifica que los sets no estén vacíos
        if (nombres != null && horas != null && !nombres.isEmpty() && !horas.isEmpty()) {
            String[] nombresArray = nombres.toArray(new String[0]);
            String[] horasArray = horas.toArray(new String[0]);

            // Asegúrate de que ambos arrays tengan el mismo tamaño
            int minLength = Math.min(nombresArray.length, horasArray.length);

            for (int i = 0; i < minLength; i++) {
                agregarFilaATabla(nombresArray[i], horasArray[i]);
            }
        }
    }


    private void agregarFilaATabla(final String nombre, final String hora) {
        TableRow row = new TableRow(getContext());
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        // Crea las celdas
        TextView textViewNombre = new TextView(getContext());
        textViewNombre.setText(nombre);
        textViewNombre.setTextColor(getResources().getColor(R.color.white)); // Color blanco
        row.addView(textViewNombre);

        TextView textViewHora = new TextView(getContext());
        textViewHora.setText(hora);
        textViewHora.setTextColor(getResources().getColor(R.color.white)); // Color blanco
        row.addView(textViewHora);

        // Agrega la fila a la tabla
        tableLayout.addView(row);
    }
}
