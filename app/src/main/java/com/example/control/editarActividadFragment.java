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
import androidx.fragment.app.FragmentTransaction;
import java.util.HashMap;
import java.util.Map;

public class editarActividadFragment extends Fragment {

    private EditText etEditarActividad, etEditarTiempo;
    private Button btnAceptarEditar, btnBorrarActividad, btnComenzar;
    private FirebaseFirestore db;
    private String deviceId, actividadId;

    public editarActividadFragment() {
        // Constructor vacío requerido
    }

    public static editarActividadFragment newInstance(String actividad, String tiempo, String actividadId) {
        editarActividadFragment fragment = new editarActividadFragment();
        Bundle args = new Bundle();
        args.putString("actividad", actividad);
        args.putString("tiempo", tiempo);
        args.putString("actividadId", actividadId);
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

        etEditarActividad = view.findViewById(R.id.etEditarActividad);
        etEditarTiempo = view.findViewById(R.id.etEditarTiempo);
        btnAceptarEditar = view.findViewById(R.id.btnAceptarEditarActividad);
        btnBorrarActividad = view.findViewById(R.id.btnBorrarActividad);
        btnComenzar = view.findViewById(R.id.btnComenzar);

        db = FirebaseFirestore.getInstance();
        deviceId = DeviceIdManager.getDeviceId(requireContext());

        if (getArguments() != null) {
            String actividad = getArguments().getString("actividad");
            String tiempo = getArguments().getString("tiempo");
            actividadId = getArguments().getString("actividadId");

            etEditarActividad.setText(actividad);
            etEditarTiempo.setText(tiempo);
        }

        etEditarTiempo.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) return;

                String input = s.toString().replace(":", "");
                if (input.length() > 4) {
                    input = input.substring(0, 4);
                }

                if (input.length() >= 3) {
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

        btnAceptarEditar.setOnClickListener(v -> guardarCambios());
        btnBorrarActividad.setOnClickListener(v -> borrarActividad());

        // Configurar el evento click para comenzar el temporizador
        btnComenzar.setOnClickListener(v -> {
            // Obtener el tiempo ingresado en el EditText etEditarTiempo
            String tiempoInput = etEditarTiempo.getText().toString();

            // Convertir el tiempo en milisegundos (asegúrate de que el formato del input sea válido)
            long tiempoEnMilisegundos = convertirTiempoAMilisegundos(tiempoInput);

            // Crear el nuevo fragmento tempoPersonalizadoFragment
            tempoPersonalizadoFragment fragment = new tempoPersonalizadoFragment();

            // Pasar el tiempo como argumento
            Bundle bundle = new Bundle();
            bundle.putLong("TIEMPO", tiempoEnMilisegundos);
            fragment.setArguments(bundle);

            // Reemplazar el fragmento en el contenedor principal
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.contenedor, fragment);
            transaction.addToBackStack(null);  // Opcional, para poder volver atrás
            transaction.commit();
        });
    }

    // Función auxiliar para convertir tiempo en formato "MM:SS" a milisegundos
    private long convertirTiempoAMilisegundos(String tiempo) {
        String[] partes = tiempo.split(":");
        int minutos = Integer.parseInt(partes[0]);
        int segundos = Integer.parseInt(partes[1]);
        return (minutos * 60 + segundos) * 1000;
    }

    private void guardarCambios() {
        String nuevaActividad = etEditarActividad.getText().toString();
        String nuevoTiempo = etEditarTiempo.getText().toString();

        if (nuevaActividad.isEmpty() || nuevoTiempo.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> actividadActualizada = new HashMap<>();
        actividadActualizada.put("actividad", nuevaActividad);
        actividadActualizada.put("tiempo", nuevoTiempo);

        db.collection("ActividadFisica")
                .document(deviceId)
                .collection("Actividades")
                .document(actividadId)
                .update(actividadActualizada)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Actividad actualizada con éxito", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al actualizar la actividad", Toast.LENGTH_SHORT).show();
                });
    }

    private void borrarActividad() {
        db.collection("ActividadFisica")
                .document(deviceId)
                .collection("Actividades")
                .document(actividadId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Actividad eliminada con éxito", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al eliminar la actividad", Toast.LENGTH_SHORT).show();
                });
    }
}
