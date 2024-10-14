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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class editarDietaFragment extends Fragment {

    private static final String PREFERENCES_NAME = "dietaPreferences";
    private static final String KEY_NOMBRES = "nombres";
    private static final String KEY_HORAS = "horas";

    private EditText etEditarNombre, etEditarHora;
    private Button btnAceptarEditar;
    private SharedPreferences sharedPreferences;
    private int filaSeleccionada;

    public editarDietaFragment() {
        // Constructor vacío requerido
    }

    public static editarDietaFragment newInstance(String nombre, String hora, int index) {
        editarDietaFragment fragment = new editarDietaFragment();
        Bundle args = new Bundle();
        args.putString("nombre", nombre);
        args.putString("hora", hora);
        args.putInt("index", index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_editar_dieta, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializa las vistas
        etEditarNombre = view.findViewById(R.id.etEditarNombre);
        etEditarHora = view.findViewById(R.id.etEditarHora);
        btnAceptarEditar = view.findViewById(R.id.btnAceptarEditar);
        Button btnBorrar = view.findViewById(R.id.btnBorrar);

        // Inicializa SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);

        // Recupera los datos enviados y muestra en los inputs
        if (getArguments() != null) {
            String nombre = getArguments().getString("nombre");
            String hora = getArguments().getString("hora");
            filaSeleccionada = getArguments().getInt("index");

            etEditarNombre.setText(nombre);
            etEditarHora.setText(hora);
        }

        // Formato automático de la hora
        etEditarHora.addTextChangedListener(new TextWatcher() {
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
                etEditarHora.setText(input);
                etEditarHora.setSelection(input.length());
                isUpdating = false;
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Configura el botón Aceptar
        btnAceptarEditar.setOnClickListener(v -> guardarCambios());

        // Configura el botón Borrar
        btnBorrar.setOnClickListener(v -> borrarFila());
    }

    private void guardarCambios() {
        List<String> nombres = new ArrayList<>(sharedPreferences.getStringSet(KEY_NOMBRES, new HashSet<>()));
        List<String> horas = new ArrayList<>(sharedPreferences.getStringSet(KEY_HORAS, new HashSet<>()));

        if (filaSeleccionada >= nombres.size() || filaSeleccionada >= horas.size()) {
            Toast.makeText(getContext(), "Error: Índice fuera de rango", Toast.LENGTH_SHORT).show();
            return;
        }

        // Actualiza los datos
        nombres.set(filaSeleccionada, etEditarNombre.getText().toString());
        horas.set(filaSeleccionada, etEditarHora.getText().toString());

        // Guarda los cambios en SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(KEY_NOMBRES, new HashSet<>(nombres));
        editor.putStringSet(KEY_HORAS, new HashSet<>(horas));
        editor.apply();

        Toast.makeText(getContext(), "Cambios guardados", Toast.LENGTH_SHORT).show();

        // Regresa al fragmento anterior (tablaDieta)
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    private void borrarFila() {
        List<String> nombres = new ArrayList<>(sharedPreferences.getStringSet(KEY_NOMBRES, new HashSet<>()));
        List<String> horas = new ArrayList<>(sharedPreferences.getStringSet(KEY_HORAS, new HashSet<>()));

        if (filaSeleccionada >= nombres.size() || filaSeleccionada >= horas.size()) {
            Toast.makeText(getContext(), "Error: Índice fuera de rango", Toast.LENGTH_SHORT).show();
            return;
        }

        // Eliminar el elemento seleccionado
        nombres.remove(filaSeleccionada);
        horas.remove(filaSeleccionada);

        // Guardar los cambios en SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(KEY_NOMBRES, new HashSet<>(nombres));
        editor.putStringSet(KEY_HORAS, new HashSet<>(horas));
        editor.apply();

        Toast.makeText(getContext(), "Fila eliminada", Toast.LENGTH_SHORT).show();

        // Regresar al fragmento anterior (tablaDieta)
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}
