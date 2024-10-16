package com.example.control;

import com.example.control.utils.DeviceIdManager;
import com.google.firebase.firestore.FirebaseFirestore;
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

import java.util.HashMap;
import java.util.Map;

public class crearDietaFragment extends Fragment {

    private EditText etNombre, etHora;
    private Button btnGuardar;
    private FirebaseFirestore db;
    private String deviceId; // Para almacenar el ID del dispositivo

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

        // Inicializa las vistas
        etNombre = view.findViewById(R.id.etNombre);
        etHora = view.findViewById(R.id.etHora);
        btnGuardar = view.findViewById(R.id.btnGuardar);

        // Inicializa Firestore
        db = FirebaseFirestore.getInstance();
        deviceId = DeviceIdManager.getDeviceId(requireContext()); // Obtener el deviceId

        // Formato automático de la hora
        etHora.addTextChangedListener(new TextWatcher() {
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
                etHora.setText(input);
                etHora.setSelection(input.length());
                isUpdating = false;
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Configura el botón Guardar
        btnGuardar.setOnClickListener(v -> guardarDieta());
    }

    private void guardarDieta() {
        String nombre = etNombre.getText().toString();
        String hora = etHora.getText().toString();

        if (nombre.isEmpty() || hora.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crea un nuevo documento para la dieta
        Map<String, Object> dieta = new HashMap<>();
        dieta.put("nombre", nombre);
        dieta.put("hora", hora);
        dieta.put("deviceId", deviceId); // Guardar el deviceId junto con la dieta

        // Usa el deviceId para referenciar el documento del dispositivo
        db.collection("Dieta") // Colección principal de Dieta
                .add(dieta)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getContext(), "Dieta guardada con éxito", Toast.LENGTH_SHORT).show();
                    // Regresa al fragmento anterior (tablaDieta)
                    requireActivity().getSupportFragmentManager().popBackStack();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al guardar la dieta", Toast.LENGTH_SHORT).show();
                });
    }
}
