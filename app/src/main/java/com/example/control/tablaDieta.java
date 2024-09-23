package com.example.control;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Set;
import java.util.HashSet;

public class tablaDieta extends Fragment {

    private static final String PREFERENCES_NAME = "dietaPreferences";
    private static final String KEY_NOMBRES = "nombres";
    private static final String KEY_HORAS = "horas";
    private SharedPreferences sharedPreferences;
    private TableLayout tableLayout;
    private Set<String> nombres;
    private Set<String> horas;

    public tablaDieta() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tabla_dieta, container, false);

        sharedPreferences = getActivity().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);

        tableLayout = rootView.findViewById(R.id.tableLayoutHorarios);

        cargarDatosEnTabla();

        return rootView;
    }

    // Método para cargar los datos guardados y mostrarlos en la tabla
    private void cargarDatosEnTabla() {
        nombres = sharedPreferences.getStringSet(KEY_NOMBRES, new HashSet<>());
        horas = sharedPreferences.getStringSet(KEY_HORAS, new HashSet<>());

        // Si hay nombres y horas guardados, cargar las filas
        if (nombres != null && horas != null && !nombres.isEmpty() && !horas.isEmpty()) {
            String[] nombresArray = nombres.toArray(new String[0]);
            String[] horasArray = horas.toArray(new String[0]);

            for (int i = 0; i < nombresArray.length; i++) {
                agregarFilaATabla(nombresArray[i], horasArray[i]);
            }
        }
    }

    // Método para agregar una fila a la tabla
    private void agregarFilaATabla(final String nombre, final String hora) {
        TableRow row = new TableRow(getContext());
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        // Columna de nombre
        TextView textViewNombre = new TextView(getContext());
        textViewNombre.setText(nombre);
        textViewNombre.setTextColor(getResources().getColor(android.R.color.white)); // Color de texto blanco
        row.addView(textViewNombre);

        // Columna de la hora
        TextView textViewHora = new TextView(getContext());
        textViewHora.setText(hora);
        textViewHora.setTextColor(getResources().getColor(android.R.color.white)); // Color de texto blanco
        row.addView(textViewHora);

        // Crear un LinearLayout para organizar el botón de eliminar
        LinearLayout accionesLayout = new LinearLayout(getContext());
        accionesLayout.setOrientation(LinearLayout.HORIZONTAL);

        // Definir LayoutParams para el botón de eliminar y añadir márgenes
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonParams.setMargins(8, 0, 8, 8); // Margen alrededor del botón

        // Botón Eliminar
        Button btnEliminar = new Button(getContext());
        btnEliminar.setBackgroundColor(getResources().getColor(R.color.light_blue)); // Color desde colors.xml
        btnEliminar.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_delete, 0, 0, 0); // Ícono de eliminar
        accionesLayout.addView(btnEliminar, buttonParams); // Añadir botón con margen

        // Agregar el LinearLayout a la fila
        row.addView(accionesLayout);

        // Agregar la nueva fila a la tabla
        tableLayout.addView(row);

        // Eliminar la fila al hacer clic en el botón Eliminar
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Remover la fila de la tabla
                tableLayout.removeView(row);

                // Remover el nombre y la hora de SharedPreferences
                nombres.remove(nombre);
                horas.remove(hora);

                // Guardar los cambios en SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putStringSet(KEY_NOMBRES, nombres);
                editor.putStringSet(KEY_HORAS, horas);
                editor.apply();
            }
        });
    }
}
