package com.example.control;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class editarDietaFragment extends Fragment {

    private EditText etEditarNombre, etEditarHora;
    private Button btnAceptarEditar;
    private FirebaseFirestore db;
    private String dietaId;  // Para almacenar el ID del documento de Firestore

    public editarDietaFragment() {
        // Constructor vacío requerido
    }

    public static editarDietaFragment newInstance(String nombre, String hora, String id) {
        editarDietaFragment fragment = new editarDietaFragment();
        Bundle args = new Bundle();
        args.putString("nombre", nombre);
        args.putString("hora", hora);
        args.putString("id", id);  // Pasar el ID del documento desde Firestore
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

        // Inicializa Firestore
        db = FirebaseFirestore.getInstance();

        // Recupera los datos enviados y muestra en los inputs
        if (getArguments() != null) {
            String nombre = getArguments().getString("nombre");
            String hora = getArguments().getString("hora");
            dietaId = getArguments().getString("id");  // Obtener el ID del documento

            // Log para verificar el ID de la dieta
            Log.d("DietaFragment", "ID del documento de la dieta: " + dietaId);

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

        // Configura el botón Aceptar para guardar los cambios en Firestore
        btnAceptarEditar.setOnClickListener(v -> guardarCambios());

        // Configura el botón Borrar para eliminar el documento de Firestore
        btnBorrar.setOnClickListener(v -> borrarDieta());
    }

    private void guardarCambios() {
        // Verificar el ID del documento antes de intentar actualizar
        if (dietaId == null || dietaId.isEmpty()) {
            Toast.makeText(getContext(), "Error: ID de la dieta no válido", Toast.LENGTH_SHORT).show();
            return;
        }

        DocumentReference dietaRef = db.collection("Dieta").document(dietaId);

        // Actualizar el documento en Firestore
        dietaRef.update("nombre", etEditarNombre.getText().toString(),
                        "hora", etEditarHora.getText().toString())
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Cambios guardados", Toast.LENGTH_SHORT).show();
                    // Regresa al fragmento anterior (tablaDieta)
                    requireActivity().getSupportFragmentManager().popBackStack();
                })
                .addOnFailureListener(e -> {
                    // Mostrar un mensaje más detallado en caso de error
                    Toast.makeText(getContext(), "Error al guardar los cambios: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d("FirestoreUpdateError", "Error al actualizar documento: " + e.getMessage());
                });
    }

    private void borrarDieta() {
        // Verificar el ID del documento antes de intentar eliminar
        if (dietaId == null || dietaId.isEmpty()) {
            Toast.makeText(getContext(), "Error: ID de la dieta no válido", Toast.LENGTH_SHORT).show();
            return;
        }

        DocumentReference dietaRef = db.collection("Dieta").document(dietaId);

        // Eliminar el documento de Firestore
        dietaRef.delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Dieta eliminada", Toast.LENGTH_SHORT).show();
                    // Regresa al fragmento anterior (tablaDieta)
                    requireActivity().getSupportFragmentManager().popBackStack();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al eliminar la dieta: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d("FirestoreDeleteError", "Error al eliminar documento: " + e.getMessage());
                });
    }
}
