package com.example.control;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.control.utils.DeviceIdManager;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class tablaDieta extends Fragment {

    private TableLayout tableLayoutHorarios;
    private FirebaseFirestore db;
    private String deviceId; // Para almacenar el ID único del dispositivo

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tabla_dieta, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Encuentra el TableLayout en la vista
        tableLayoutHorarios = view.findViewById(R.id.tableLayoutHorarios);

        // Inicializa Firebase Firestore
        db = FirebaseFirestore.getInstance();
        deviceId = DeviceIdManager.getDeviceId(requireContext()); // Obtener el ID único del dispositivo

        // Cargar datos desde Firestore y agregarlos a la tabla
        cargarDatosDesdeFirestore();
    }

    private void cargarDatosDesdeFirestore() {
        // Referencia a la colección 'Dieta' en Firestore
        CollectionReference dietaRef = db.collection("Dieta");

        // Obtener documentos de la colección donde el campo deviceId coincida
        dietaRef.whereEqualTo("deviceId", deviceId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Iterar sobre los documentos de Firestore y agregar filas a la tabla
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String nombre = document.getString("nombre");
                    String hora = document.getString("hora");

                    // Agregar una nueva fila con los datos
                    agregarFilaATabla(nombre, hora, document.getId());
                }
            } else {
                Toast.makeText(getContext(), "Error al cargar datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void agregarFilaATabla(String nombreDieta, String horaDieta, String documentoId) {
        // Crear una nueva fila
        TableRow nuevaFila = new TableRow(getContext());

        // Crear la columna "Nombre"
        TextView nombre = new TextView(getContext());
        nombre.setText(nombreDieta);
        nombre.setTextColor(getResources().getColor(R.color.white));
        nombre.setPadding(8, 8, 8, 8);

        // Crear la columna "Hora"
        TextView hora = new TextView(getContext());
        hora.setText(horaDieta);
        hora.setTextColor(getResources().getColor(R.color.white));
        hora.setPadding(8, 8, 8, 8);

        // Añadir las celdas a la fila
        nuevaFila.addView(nombre);
        nuevaFila.addView(hora);

        // Añadir la fila al TableLayout
        tableLayoutHorarios.addView(nuevaFila);

        // Configurar un listener para cuando se presione una fila
        nuevaFila.setOnClickListener(v -> {
            abrirEditarDietaFragment(nombreDieta, horaDieta, documentoId);
        });
    }

    private void abrirEditarDietaFragment(String nombre, String hora, String documentoId) {
        // Crear una nueva instancia de editarDietaFragment con los datos pasados
        editarDietaFragment fragment = editarDietaFragment.newInstance(nombre, hora, documentoId);

        // Reemplazar el fragmento en el FrameLayout 'contenedor' del MainActivity
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contenedor, fragment); // Aquí se usa el FrameLayout del MainActivity
        transaction.addToBackStack(null); // Para que se pueda volver atrás
        transaction.commit();
    }
}
