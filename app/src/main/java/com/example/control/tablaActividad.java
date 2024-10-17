package com.example.control;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.control.utils.DeviceIdManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class tablaActividad extends Fragment {

    private TableLayout tableLayout;
    private FirebaseFirestore db;
    private String deviceId;

    public tablaActividad() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar el layout definido en el XML proporcionado
        View rootView = inflater.inflate(R.layout.fragment_tabla_actividad, container, false);

        // Inicializa el Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Obtén el ID del dispositivo desde la clase DeviceIdManager
        deviceId = DeviceIdManager.getDeviceId(requireContext());

        // Referencia del TableLayout
        tableLayout = rootView.findViewById(R.id.tableLayoutActividades);

        // Carga los datos desde Firestore y llena la tabla
        cargarActividadesDeFirestore();

        return rootView;
    }

    private void cargarActividadesDeFirestore() {
        // Cargar actividades desde Firestore para el dispositivo actual
        db.collection("ActividadFisica")
                .document(deviceId)
                .collection("Actividades")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Itera sobre los resultados obtenidos
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String actividad = document.getString("actividad");
                            String tiempo = document.getString("tiempo");
                            String actividadId = document.getId();  // Obtiene el ID del documento
                            agregarFilaATabla(actividad, tiempo, actividadId);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    // Manejar errores de carga de Firestore
                });
    }

    private void agregarFilaATabla(final String actividad, final String tiempo, final String actividadId) {
        // Crear una nueva fila de la tabla
        TableRow row = new TableRow(getContext());
        row.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        // Crear TextView para la actividad
        TextView textViewActividad = new TextView(getContext());
        textViewActividad.setText(actividad);
        textViewActividad.setTextColor(getResources().getColor(R.color.white));
        textViewActividad.setPadding(8, 8, 8, 8);
        row.addView(textViewActividad);

        // Crear TextView para el tiempo
        TextView textViewTiempo = new TextView(getContext());
        textViewTiempo.setText(tiempo);
        textViewTiempo.setTextColor(getResources().getColor(R.color.white));
        textViewTiempo.setPadding(8, 8, 8, 8);
        row.addView(textViewTiempo);

        // Añadir la fila a la tabla
        tableLayout.addView(row);

        // Agregar click listener para editar actividad al hacer clic en una fila
        row.setOnClickListener(v -> abrirEditarActividadFragment(actividad, tiempo, actividadId));
    }

    private void abrirEditarActividadFragment(String actividad, String tiempo, String actividadId) {
        editarActividadFragment fragment = editarActividadFragment.newInstance(actividad, tiempo, actividadId);

        // Reemplazar el fragmento actual con el fragmento de edición en el contenedor principal
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.contenedor, fragment)
                .addToBackStack(null)
                .commit();
    }
}
