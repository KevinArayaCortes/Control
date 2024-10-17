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

public class crearActividadFragment extends Fragment {

    private EditText editTextTiempo, editTextActividad;
    private Button btnAceptar;
    private FirebaseFirestore db;
    private String deviceId;

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
        btnAceptar = view.findViewById(R.id.btnAceptarActividad);

        // Inicializa Firestore y DeviceId
        db = FirebaseFirestore.getInstance();
        deviceId = DeviceIdManager.getDeviceId(requireContext());

        editTextTiempo.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) return;

                String currentText = s.toString().replace(":", "");
                if (currentText.length() > 4) {
                    currentText = currentText.substring(0, 4);
                }

                if (currentText.length() >= 3) {
                    currentText = currentText.substring(0, 2) + ":" + currentText.substring(2);
                }

                isUpdating = true;
                editTextTiempo.setText(currentText);
                editTextTiempo.setSelection(currentText.length());
                isUpdating = false;
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnAceptar.setOnClickListener(v -> {
            String tiempoIngresado = editTextTiempo.getText().toString();
            String actividadIngresada = editTextActividad.getText().toString();

            if (!actividadIngresada.isEmpty() && tiempoIngresado.matches("^([0-5]?\\d):([0-5]\\d)$")) {
                guardarDatosEnFirestore(actividadIngresada, tiempoIngresado);
            } else {
                Toast.makeText(getActivity(), "Ingrese una actividad y un tiempo válido (mm:ss)", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void guardarDatosEnFirestore(String actividad, String tiempo) {
        Map<String, Object> actividadFisica = new HashMap<>();
        actividadFisica.put("actividad", actividad);
        actividadFisica.put("tiempo", tiempo);

        db.collection("ActividadFisica")
                .document(deviceId)
                .collection("Actividades")
                .add(actividadFisica)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getContext(), "Actividad guardada con éxito", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al guardar la actividad", Toast.LENGTH_SHORT).show();
                });
    }
}
