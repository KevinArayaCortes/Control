package com.example.control;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.control.utils.DeviceIdManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.SetOptions;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class HistorialFragment extends Fragment {

    private FirebaseFirestore db;

    public HistorialFragment() {
        // Constructor vacío requerido
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        calcularYGuardarTiempoDiario();
    }

    private void calcularYGuardarTiempoDiario() {
        String deviceId = DeviceIdManager.getDeviceId(requireContext());
        String semanaActual = obtenerSemanaActual(); // Método para obtener el nombre de la semana
        Map<String, Integer> tiemposDiarios = new HashMap<>();

        // Inicializar los tiempos para cada día
        tiemposDiarios.put("lunes", 0);
        tiemposDiarios.put("martes", 0);
        tiemposDiarios.put("miércoles", 0);
        tiemposDiarios.put("jueves", 0);
        tiemposDiarios.put("viernes", 0);
        tiemposDiarios.put("sábado", 0);
        tiemposDiarios.put("domingo", 0);

        // Obtener actividades del Firestore
        db.collection("ActividadFisica")
                .document(deviceId)
                .collection("Actividades")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String tiempo = document.getString("tiempo");
                            String diaActividad = obtenerDiaDeLaActividad(); // Método para obtener el día de la actividad

                            // Convertir tiempo a segundos
                            int tiempoEnSegundos = convertirTiempoASegundos(tiempo);
                            // Acumular el tiempo en el día correspondiente
                            tiemposDiarios.put(diaActividad, tiemposDiarios.get(diaActividad) + tiempoEnSegundos);
                        }
                        // Guardar los tiempos acumulados en Firestore
                        guardarTiemposDiarios(tiemposDiarios, deviceId, semanaActual);
                    } else {
                        Log.w("Firestore", "Error al obtener actividades.", task.getException());
                    }
                });
    }

    private void guardarTiemposDiarios(Map<String, Integer> tiemposDiarios, String deviceId, String semanaActual) {
        for (Map.Entry<String, Integer> entry : tiemposDiarios.entrySet()) {
            String dia = entry.getKey();
            int tiempoTotal = entry.getValue();

            // Referencia al documento correspondiente al día de la semana
            db.collection("tiemposDiarios")
                    .document(deviceId)
                    .collection(semanaActual)
                    .document(dia)
                    .set(Map.of("tiempo_total", FieldValue.increment(tiempoTotal)), SetOptions.merge())
                    .addOnSuccessListener(aVoid -> {
                        Log.d("Firestore", "Tiempo diario actualizado para " + dia + ": " + tiempoTotal);
                    })
                    .addOnFailureListener(e -> {
                        Log.w("Firestore", "Error al actualizar el tiempo diario para " + dia, e);
                    });
        }
    }

    private int convertirTiempoASegundos(String tiempo) {
        String[] partes = tiempo.split(":");
        if (partes.length == 2) {
            int minutos = Integer.parseInt(partes[0]);
            int segundos = Integer.parseInt(partes[1]);
            return minutos * 60 + segundos; // Convertir a segundos
        }
        return 0; // En caso de que no esté en el formato esperado
    }

    private String obtenerSemanaActual() {
        // Lógica para obtener la semana actual, por ejemplo: "Semana_1"
        Calendar calendar = Calendar.getInstance();
        int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
        return "Semana_" + weekOfYear; // Nombre de la semana
    }

    private String obtenerDiaDeLaActividad() {
        // Implementa la lógica para determinar el día de la actividad
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                return "lunes";
            case Calendar.TUESDAY:
                return "martes";
            case Calendar.WEDNESDAY:
                return "miércoles";
            case Calendar.THURSDAY:
                return "jueves";
            case Calendar.FRIDAY:
                return "viernes";
            case Calendar.SATURDAY:
                return "sábado";
            case Calendar.SUNDAY:
                return "domingo";
            default:
                return ""; // En caso de error
        }
    }
}
