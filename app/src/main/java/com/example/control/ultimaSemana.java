package com.example.control;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.control.utils.DeviceIdManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ultimaSemana extends Fragment {

    private FirebaseFirestore db;
    private static final String TAG = "ultimaSemana";
    private String[] semanas = {"Semana_4", "Semana_3", "Semana_2", "Semana_1"}; // Orden inverso para obtener la última semana

    public ultimaSemana() {
        // Constructor vacío requerido
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ultima_semana, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance();

        // Obtener el ID del dispositivo
        String deviceId = DeviceIdManager.getDeviceId(requireContext());


        // Recuperar los tiempos de la última semana guardada
        cargarTiemposUltimaSemana(deviceId, view);
    }

    private void cargarTiemposUltimaSemana(String deviceId, View view) {
        // Iterar sobre las semanas en orden inverso para encontrar la última semana guardada
        for (String semana : semanas) {
            db.collection("tiemposDiarios")
                    .document(deviceId)
                    .collection(semana)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            // Si encontramos datos para esta semana, cargamos los tiempos
                            Log.d(TAG, "Datos encontrados para " + semana);
                            mostrarTiemposEnTabla(task.getResult(), view);
                            return;  // Detener el bucle después de encontrar la última semana
                        } else {
                            Log.d(TAG, "No se encontraron datos para " + semana);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error al recuperar datos de la semana " + semana, e);
                    });
        }
    }

    private void mostrarTiemposEnTabla(QuerySnapshot semanaData, View view) {
        // Obtener referencias a los TextViews
        TextView lunesTiempo = view.findViewById(R.id.lunes_tiempo);
        TextView martesTiempo = view.findViewById(R.id.martes_tiempo);
        TextView miercolesTiempo = view.findViewById(R.id.miercoles_tiempo);
        TextView juevesTiempo = view.findViewById(R.id.jueves_tiempo);
        TextView viernesTiempo = view.findViewById(R.id.viernes_tiempo);
        TextView sabadoTiempo = view.findViewById(R.id.sabado_tiempo);
        TextView domingoTiempo = view.findViewById(R.id.domingo_tiempo);

        // Recorrer los documentos de la semana y asignar los tiempos a los TextViews
        for (QueryDocumentSnapshot document : semanaData) {
            Long tiempoTotal = document.getLong("tiempo_total");
            String tiempo = convertirSegundosATiempo(tiempoTotal != null ? tiempoTotal : 0);

            switch (document.getId()) {
                case "lunes":
                    lunesTiempo.setText(tiempo);
                    break;
                case "martes":
                    martesTiempo.setText(tiempo);
                    break;
                case "miércoles":
                    miercolesTiempo.setText(tiempo);
                    break;
                case "jueves":
                    juevesTiempo.setText(tiempo);
                    break;
                case "viernes":
                    viernesTiempo.setText(tiempo);
                    break;
                case "sábado":
                    sabadoTiempo.setText(tiempo);
                    break;
                case "domingo":
                    domingoTiempo.setText(tiempo);
                    break;
                default:
                    Log.w(TAG, "Día no reconocido: " + document.getId());
                    break;
            }
        }
    }

    // Método para convertir segundos a formato mm:ss
    private String convertirSegundosATiempo(long segundos) {
        long minutos = segundos / 60;
        long seg = segundos % 60;
        return String.format("%02d:%02d", minutos, seg);
    }
}
