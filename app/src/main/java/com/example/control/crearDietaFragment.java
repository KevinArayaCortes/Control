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

public class crearDietaFragment extends Fragment {

    private static final String PREFERENCES_NAME = "dietaPreferences";
    private static final String KEY_NOMBRES = "nombres";
    private static final String KEY_HORAS = "horas";

    private EditText editTextTime, editTextNombre;
    private SharedPreferences sharedPreferences;

    public crearDietaFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_crear_dieta, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextTime = view.findViewById(R.id.editTextText4);
        editTextNombre = view.findViewById(R.id.editTextText3);

        sharedPreferences = requireActivity().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);

        editTextTime.addTextChangedListener(new TextWatcher() {
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
                editTextTime.setText(cleanText);
                editTextTime.setSelection(cleanText.length());
                isUpdating = false;
            }

            @Override
            public void afterTextChanged(Editable s) {
                String currentText = editTextTime.getText().toString();
                if (!currentText.matches("^([01]\\d|2[0-3]):([0-5]\\d)$")) {
                    editTextTime.setError("Formato de hora inválido (HH:mm)");
                }
            }
        });

        Button btnAceptar = view.findViewById(R.id.button8);
        btnAceptar.setOnClickListener(v -> {
            String horaIngresada = editTextTime.getText().toString();
            String nombreIngresado = editTextNombre.getText().toString();

            if (!nombreIngresado.isEmpty() && horaIngresada.matches("^([01]\\d|2[0-3]):([0-5]\\d)$")) {
                guardarDatosEnSharedPreferences(nombreIngresado, horaIngresada);
                Toast.makeText(getActivity(), "Datos guardados", Toast.LENGTH_SHORT).show();

                // Regresa al fragmento anterior (DietaFragment)
                requireActivity().getSupportFragmentManager().popBackStack();
            } else {
                Toast.makeText(getActivity(), "Ingrese un nombre y una hora válida (HH:mm)", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void guardarDatosEnSharedPreferences(String nombre, String hora) {
        Set<String> nombres = new HashSet<>(sharedPreferences.getStringSet(KEY_NOMBRES, new HashSet<>()));
        Set<String> horas = new HashSet<>(sharedPreferences.getStringSet(KEY_HORAS, new HashSet<>()));

        nombres.add(nombre);
        horas.add(hora);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(KEY_NOMBRES, nombres);
        editor.putStringSet(KEY_HORAS, horas);
        editor.apply();
    }
}
