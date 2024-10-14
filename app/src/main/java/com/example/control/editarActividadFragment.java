package com.example.control;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class editarActividadFragment extends Fragment {

    private static final String PREFERENCES_NAME = "actividadPreferences";
    private static final String KEY_ACTIVIDADES = "actividades";
    private static final String KEY_TIEMPOS = "tiempos";

    private EditText etEditarActividad, etEditarTiempo;
    private Button btnAceptarEditar, btnBorrarActividad, btnComenzar; // Declaración del nuevo botón
    private SharedPreferences sharedPreferences;
    private int filaSeleccionada;

    public editarActividadFragment() {
        // Constructor vacío requerido
    }

    public static editarActividadFragment newInstance(String actividad, String tiempo, int index) {
        editarActividadFragment fragment = new editarActividadFragment();
        Bundle args = new Bundle();
        args.putString("actividad", actividad);
        args.putString("tiempo", tiempo);
        args.putInt("index", index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_editar_actividad, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializa las vistas
        etEditarActividad = view.findViewById(R.id.etEditarActividad);
        etEditarTiempo = view.findViewById(R.id.etEditarTiempo);
        btnAceptarEditar = view.findViewById(R.id.btnAceptarEditarActividad);
        btnBorrarActividad = view.findViewById(R.id.btnBorrarActividad);
        btnComenzar = view.findViewById(R.id.btnComenzar); // Inicializa el botón Comenzar

        // Inicializa SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);

        // Recupera los datos enviados y muestra en los inputs
        if (getArguments() != null) {
            String actividad = getArguments().getString("actividad");
            String tiempo = getArguments().getString("tiempo");
            filaSeleccionada = getArguments().getInt("index");

            etEditarActividad.setText(actividad);
            etEditarTiempo.setText(tiempo);
        }

        // Formato automático del tiempo
        etEditarTiempo.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) return;

                String input = s.toString().replace(":", "");
                if (input.length() > 2) {
                    input = input.substring(0, 2) + ":" + input.substring(2);
                }

                isUpdating = true;
                etEditarTiempo.setText(input);
                etEditarTiempo.setSelection(input.length());
                isUpdating = false;
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Configura el botón Aceptar
        btnAceptarEditar.setOnClickListener(v -> guardarCambios());

        // Configura el botón Borrar
        btnBorrarActividad.setOnClickListener(v -> borrarFila());

        // Configura el botón Comenzar
        btnComenzar.setOnClickListener(v -> {
            String tiempoTexto = etEditarTiempo.getText().toString();
            if (!tiempoTexto.isEmpty()) {
                // Convertir el tiempo ingresado a milisegundos
                long tiempoEnMilisegundos = convertirTiempoAMilisegundos(tiempoTexto);
                if (tiempoEnMilisegundos > 0) {
                    cargarTempoPersonalizadoFragment(tiempoEnMilisegundos);
                }
            } else {
                Toast.makeText(getContext(), "Por favor ingresa un tiempo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para convertir el tiempo en formato mm:ss a milisegundos
    private long convertirTiempoAMilisegundos(String tiempo) {
        String[] partes = tiempo.split(":");
        int minutos = 0;
        int segundos = 0;

        try {
            if (partes.length == 2) {
                minutos = Integer.parseInt(partes[0]);
                segundos = Integer.parseInt(partes[1]);
            } else if (partes.length == 1) {
                minutos = Integer.parseInt(partes[0]);
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Formato de tiempo inválido", Toast.LENGTH_SHORT).show();
            return 0;
        }

        return (minutos * 60 + segundos) * 1000;
    }

    // Método para cargar el tempoPersonalizadoFragment y pasarle el tiempo
    private void cargarTempoPersonalizadoFragment(long tiempo) {
        tempoPersonalizadoFragment fragment = new tempoPersonalizadoFragment();

        // Crear los argumentos
        Bundle args = new Bundle();
        args.putLong("TIEMPO", tiempo);
        fragment.setArguments(args);

        // Reemplazar el fragmento actual por el tempoPersonalizadoFragment
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.contenedor, fragment) // Asegúrate de que el ID 'contenedor' sea correcto
                .addToBackStack(null)
                .commit();
    }

    private void guardarCambios() {
        List<String> actividades = new ArrayList<>(sharedPreferences.getStringSet(KEY_ACTIVIDADES, new HashSet<>()));
        List<String> tiempos = new ArrayList<>(sharedPreferences.getStringSet(KEY_TIEMPOS, new HashSet<>()));

        if (filaSeleccionada >= actividades.size() || filaSeleccionada >= tiempos.size()) {
            Toast.makeText(getContext(), "Error: Índice fuera de rango", Toast.LENGTH_SHORT).show();
            return;
        }

        // Actualiza los datos
        actividades.set(filaSeleccionada, etEditarActividad.getText().toString());
        tiempos.set(filaSeleccionada, etEditarTiempo.getText().toString());

        // Guarda los cambios en SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(KEY_ACTIVIDADES, new HashSet<>(actividades));
        editor.putStringSet(KEY_TIEMPOS, new HashSet<>(tiempos));
        editor.apply();

        Toast.makeText(getContext(), "Cambios guardados", Toast.LENGTH_SHORT).show();

        // Regresa al fragmento anterior
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    private void borrarFila() {
        List<String> actividades = new ArrayList<>(sharedPreferences.getStringSet(KEY_ACTIVIDADES, new HashSet<>()));
        List<String> tiempos = new ArrayList<>(sharedPreferences.getStringSet(KEY_TIEMPOS, new HashSet<>()));

        if (filaSeleccionada >= actividades.size() || filaSeleccionada >= tiempos.size()) {
            Toast.makeText(getContext(), "Error: Índice fuera de rango", Toast.LENGTH_SHORT).show();
            return;
        }

        // Eliminar el elemento seleccionado
        actividades.remove(filaSeleccionada);
        tiempos.remove(filaSeleccionada);

        // Guardar los cambios en SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(KEY_ACTIVIDADES, new HashSet<>(actividades));
        editor.putStringSet(KEY_TIEMPOS, new HashSet<>(tiempos));
        editor.apply();

        Toast.makeText(getContext(), "Fila eliminada", Toast.LENGTH_SHORT).show();

        // Regresar al fragmento anterior
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}
