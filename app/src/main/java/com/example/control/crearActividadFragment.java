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

import java.util.HashSet;
import java.util.Set;

public class crearActividadFragment extends Fragment {

    private static final String PREFERENCES_NAME = "actividadPreferences";
    private static final String KEY_ACTIVIDADES = "actividades";
    private static final String KEY_TIEMPOS = "tiempos";

    private EditText editTextTiempo, editTextActividad;
    private SharedPreferences sharedPreferences;

    public crearActividadFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_crear_actividad, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextTiempo = view.findViewById(R.id.editTextTiempo);
        editTextActividad = view.findViewById(R.id.editTextActividad);

        sharedPreferences = requireActivity().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);

        editTextTiempo.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) return;

                String currentText = s.toString();
                String cleanText = currentText.replace(":", "");

                if (cleanText.length() > 2) {
                    cleanText = cleanText.substring(0, 2) + ":" + cleanText.substring(2);
                }

                isUpdating = true;
                editTextTiempo.setText(cleanText);
                editTextTiempo.setSelection(cleanText.length());
                isUpdating = false;
            }

            @Override
            public void afterTextChanged(Editable s) {
                String currentText = editTextTiempo.getText().toString();
                if (!currentText.matches("^([01]\\d|2[0-3]):([0-5]\\d)$")) {
                    editTextTiempo.setError("Formato de tiempo inválido (HH:mm)");
                }
            }
        });

        Button btnAceptar = view.findViewById(R.id.btnAceptarActividad);
        btnAceptar.setOnClickListener(v -> {
            String tiempoIngresado = editTextTiempo.getText().toString();
            String actividadIngresada = editTextActividad.getText().toString();

            if (!actividadIngresada.isEmpty() && tiempoIngresado.matches("^([01]\\d|2[0-3]):([0-5]\\d)$")) {
                guardarDatosEnSharedPreferences(actividadIngresada, tiempoIngresado);
                Toast.makeText(getActivity(), "Datos guardados", Toast.LENGTH_SHORT).show();

                // Regresa al fragmento anterior (ActividadFragment)
                requireActivity().getSupportFragmentManager().popBackStack();
            } else {
                Toast.makeText(getActivity(), "Ingrese una actividad y un tiempo válido (HH:mm)", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void guardarDatosEnSharedPreferences(String actividad, String tiempo) {
        Set<String> actividades = new HashSet<>(sharedPreferences.getStringSet(KEY_ACTIVIDADES, new HashSet<>()));
        Set<String> tiempos = new HashSet<>(sharedPreferences.getStringSet(KEY_TIEMPOS, new HashSet<>()));

        actividades.add(actividad);
        tiempos.add(tiempo);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(KEY_ACTIVIDADES, actividades);
        editor.putStringSet(KEY_TIEMPOS, tiempos);
        editor.apply();
    }
}
