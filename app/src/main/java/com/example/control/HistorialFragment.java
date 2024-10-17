package com.example.control;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.control.utils.DeviceIdManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar el layout del fragmento historial
        return inflater.inflate(R.layout.fragment_historial, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance();
        calcularYGuardarTiempoDiario();

        // Configura los eventos de los botones
        Button buttonSemana1 = view.findViewById(R.id.button5);
        buttonSemana1.setOnClickListener(v -> {
            cargarSemanaFragment("Semana_1");  // Cargar fragmento de la semana 1
        });

        Button buttonSemana2 = view.findViewById(R.id.button6);
        buttonSemana2.setOnClickListener(v -> {
            cargarSemanaFragment("Semana_2");  // Cargar fragmento de la semana 2
        });

        Button buttonSemana3 = view.findViewById(R.id.button12);
        buttonSemana3.setOnClickListener(v -> {
            cargarSemanaFragment("Semana_3");  // Cargar fragmento de la semana 3
        });

        Button buttonSemana4 = view.findViewById(R.id.button13);
        buttonSemana4.setOnClickListener(v -> {
            cargarSemanaFragment("Semana_4");  // Cargar fragmento de la semana 4
        });
    }

    // Método para cargar el fragmento de la semana seleccionada
    private void cargarSemanaFragment(String semana) {
        String deviceId = DeviceIdManager.getDeviceId(requireContext());  // Obtiene el ID del dispositivo
        cargarSemanas fragmentSemana = new cargarSemanas(semana, deviceId);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.contenedor, fragmentSemana)
                .addToBackStack(null)  // Añadir a la pila para poder volver atrás
                .commit();
    }

    private void calcularYGuardarTiempoDiario() {
        String deviceId = DeviceIdManager.getDeviceId(requireContext());
        String semanaActual = obtenerSemanaActual(); // Obtener la semana actual (de 1 a 4)
        Map<String, Integer> tiemposDiarios = new HashMap<>();

        // Inicializar los tiempos para cada día
        tiemposDiarios.put("lunes", 0);
        tiemposDiarios.put("martes", 0);
        tiemposDiarios.put("miércoles", 0);
        tiemposDiarios.put("jueves", 0);
        tiemposDiarios.put("viernes", 0);
        tiemposDiarios.put("sábado", 0);
        tiemposDiarios.put("domingo", 0);

        // Verificar si ya se ha actualizado hoy
        SharedPreferences prefs = requireContext().getSharedPreferences("HistorialFragment", Context.MODE_PRIVATE);
        String fechaHoy = obtenerFechaActual(); // Obtener la fecha de hoy en formato String
        String ultimaFecha = prefs.getString("ultimaActualizacion", "");

        if (fechaHoy.equals(ultimaFecha)) {
            // Ya se ha actualizado hoy, no hacer nada
            Log.d("HistorialFragment", "El tiempo ya ha sido sumado hoy.");
            return;
        }

        // Obtener actividades del Firestore
        db.collection("ActividadFisica")
                .document(deviceId)
                .collection("Actividades")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String tiempo = document.getString("tiempo");
                            String diaActividad = obtenerDiaDeLaActividad();

                            // Convertir tiempo a segundos
                            int tiempoEnSegundos = convertirTiempoASegundos(tiempo);
                            tiemposDiarios.put(diaActividad, tiemposDiarios.get(diaActividad) + tiempoEnSegundos);
                        }
                        // Guardar los tiempos acumulados en Firestore
                        guardarTiemposDiarios(tiemposDiarios, deviceId, semanaActual);

                        // Actualizar la última fecha de actualización en SharedPreferences
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("ultimaActualizacion", fechaHoy);
                        editor.apply();
                    } else {
                        Log.w("Firestore", "Error al obtener actividades.", task.getException());
                    }
                });
    }

    private void guardarTiemposDiarios(Map<String, Integer> tiemposDiarios, String deviceId, String semanaActual) {
        for (Map.Entry<String, Integer> entry : tiemposDiarios.entrySet()) {
            String dia = entry.getKey();
            int tiempoTotal = entry.getValue();

            // Guardar en Firestore, sobrescribiendo los datos cada mes
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
            return minutos * 60 + segundos;
        }
        return 0;
    }

    private String obtenerSemanaActual() {
        Calendar calendar = Calendar.getInstance();
        int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
        // Limitar el número de semana de 1 a 4
        int semanaDelMes = ((calendar.get(Calendar.DAY_OF_MONTH) - 1) / 7) + 1; // Calcula la semana del mes
        return "Semana_" + (semanaDelMes > 4 ? 1 : semanaDelMes); // Volver a Semana_1 si es mayor a 4
    }

    private String obtenerDiaDeLaActividad() {
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
                return "";
        }
    }

    private String obtenerFechaActual() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Los meses empiezan en 0
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return year + "-" + month + "-" + day; // Devuelve la fecha en formato "YYYY-MM-DD"
    }
}
