package com.example.control;

import com.example.control.utils.DeviceIdManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
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
import java.util.List;
import java.util.ArrayList;

public class tablaDieta extends Fragment {

    private TableLayout tableLayoutHorarios;
    private FirebaseFirestore db;
    private List<String> dietaIds;
    private String deviceId;

    public tablaDieta() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tabla_dieta, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializa DeviceIdManager y obtiene el ID único del dispositivo
        deviceId = DeviceIdManager.getDeviceId(requireContext());

        // Inicializa Firestore
        db = FirebaseFirestore.getInstance();

        // Inicializa las listas
        dietaIds = new ArrayList<>();

        // Inicializa el TableLayout
        tableLayoutHorarios = view.findViewById(R.id.tableLayoutHorarios);

        // Cargar dietas
        cargarDietas();
    }

    private void cargarDietas() {
        // Filtrar dietas por deviceId
        db.collection("Dieta")
                .whereEqualTo("deviceId", deviceId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Limpia la tabla antes de agregar nuevas filas
                        tableLayoutHorarios.removeViews(1, tableLayoutHorarios.getChildCount() - 1); // Mantener la fila del encabezado
                        dietaIds.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String nombre = document.getString("nombre");
                            String hora = document.getString("hora");
                            String dietaId = document.getId();

                            // Crear una nueva fila en la tabla
                            TableRow tableRow = new TableRow(getContext());

                            // Crear TextView para el nombre
                            TextView nombreTextView = new TextView(getContext());
                            nombreTextView.setText(nombre);
                            nombreTextView.setTextColor(getResources().getColor(R.color.white));
                            nombreTextView.setPadding(8, 8, 8, 8);
                            nombreTextView.setTextSize(18);

                            // Crear TextView para la hora
                            TextView horaTextView = new TextView(getContext());
                            horaTextView.setText(hora);
                            horaTextView.setTextColor(getResources().getColor(R.color.white));
                            horaTextView.setPadding(8, 8, 8, 8);
                            horaTextView.setTextSize(18);

                            // Añadir los TextViews a la fila
                            tableRow.addView(nombreTextView);
                            tableRow.addView(horaTextView);

                            // Añadir un listener para manejar el clic en la fila
                            tableRow.setOnClickListener(v -> {
                                editarDietaFragment fragment = editarDietaFragment.newInstance(nombre, hora, dietaId);
                                requireActivity().getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.contenedor, fragment) // Cambiado de fragment_container a contenedor
                                        .addToBackStack(null)
                                        .commit();
                            });

                            // Añadir la fila a la tabla
                            tableLayoutHorarios.addView(tableRow);

                            // Añadir el ID de la dieta a la lista
                            dietaIds.add(dietaId);
                        }
                    }
                });
    }
}
