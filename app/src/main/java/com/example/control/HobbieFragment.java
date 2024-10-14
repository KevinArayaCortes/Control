package com.example.control;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class HobbieFragment extends Fragment {

    private EditText editTextTime;
    private boolean isUpdating = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hobbie, container, false);

        editTextTime = view.findViewById(R.id.editTextTime);
        Button buttonAceptar = view.findViewById(R.id.buttonAceptar);

        // Configurar el listener para el botón Aceptar
        buttonAceptar.setOnClickListener(v -> aceptarTiempo());

        // Agregar TextWatcher para formatear el tiempo en mm:ss
        editTextTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) {
                    isUpdating = false;
                    return;
                }

                String input = s.toString().replace(":", "");
                if (input.length() >= 3) {
                    String minutes = input.substring(0, 2);
                    String seconds = input.substring(2);

                    isUpdating = true;
                    editTextTime.setText(minutes + ":" + seconds);
                    editTextTime.setSelection(editTextTime.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    private void aceptarTiempo() {
        String timeInput = editTextTime.getText().toString();

        // Validar formato mm:ss
        if (!timeInput.isEmpty() && timeInput.contains(":")) {
            String[] timeParts = timeInput.split(":");
            int minutes = Integer.parseInt(timeParts[0]);
            int seconds = Integer.parseInt(timeParts[1]);

            long timeInMillis = (minutes * 60 + seconds) * 1000; // Convertir a milisegundos

            // Crear instancia del fragmento tempoPersonalizadoFragment
            tempoPersonalizadoFragment tempoFragment = new tempoPersonalizadoFragment();
            Bundle args = new Bundle();
            args.putLong("TIEMPO", timeInMillis);
            tempoFragment.setArguments(args);

            // Reemplazar el fragmento en el contenedor del MainActivity
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contenedor, tempoFragment) // Asegurar que el ID sea correcto
                    .addToBackStack(null) // Permitir regresar al fragmento anterior
                    .commit();
        } else {
            Toast.makeText(getActivity(), "Ingrese un tiempo válido en formato mm:ss", Toast.LENGTH_SHORT).show();
        }
    }
}
