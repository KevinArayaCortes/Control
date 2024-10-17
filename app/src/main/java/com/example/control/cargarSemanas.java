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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class cargarSemanas extends Fragment {

    private FirebaseFirestore db;
    private String semana;
    private String deviceId;
    private static final String TAG = "cargarSemanas";

    public cargarSemanas(String semana, String deviceId) {
        this.semana = semana;
        this.deviceId = deviceId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cargar_semanas, container, false);

        db = FirebaseFirestore.getInstance();

        // Obtener referencias a los TextViews
        TextView lunesTiempo = view.findViewById(R.id.lunes_tiempo);
        TextView martesTiempo = view.findViewById(R.id.martes_tiempo);
        TextView miercolesTiempo = view.findViewById(R.id.miercoles_tiempo);
        TextView juevesTiempo = view.findViewById(R.id.jueves_tiempo);
        TextView viernesTiempo = view.findViewById(R.id.viernes_tiempo);
        TextView sabadoTiempo = view.findViewById(R.id.sabado_tiempo);
        TextView domingoTiempo = view.findViewById(R.id.domingo_tiempo);

        // Recuperar los tiempos desde Firestore para la semana y el dispositivo actuales
        cargarTiemposDesdeFirestore(lunesTiempo, martesTiempo, miercolesTiempo, juevesTiempo, viernesTiempo, sabadoTiempo, domingoTiempo);

        return view;
    }

    private void cargarTiemposDesdeFirestore(TextView lunes, TextView martes, TextView miercoles, TextView jueves, TextView viernes, TextView sabado, TextView domingo) {
        // Cambiar a la colección que almacena los tiempos diarios por semana
        db.collection("tiemposDiarios")
                .document(deviceId)
                .collection(semana)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        Log.d(TAG, "No se encontraron documentos para la semana " + semana);
                        // Opcional: Muestra un mensaje en los TextView si no hay datos
                        lunes.setText("0:00");
                        martes.setText("0:00");
                        miercoles.setText("0:00");
                        jueves.setText("0:00");
                        viernes.setText("0:00");
                        sabado.setText("0:00");
                        domingo.setText("0:00");
                    } else {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String dia = document.getId();
                            Long tiempoTotal = document.getLong("tiempo_total");

                            Log.d(TAG, "Día: " + dia + " | Tiempo total: " + tiempoTotal);

                            // Asignar el tiempo al TextView correspondiente
                            if (tiempoTotal != null) {
                                String tiempo = convertirSegundosATiempo(tiempoTotal);
                                switch (dia) {
                                    case "lunes":
                                        lunes.setText(tiempo);
                                        break;
                                    case "martes":
                                        martes.setText(tiempo);
                                        break;
                                    case "miércoles":
                                        miercoles.setText(tiempo);
                                        break;
                                    case "jueves":
                                        jueves.setText(tiempo);
                                        break;
                                    case "viernes":
                                        viernes.setText(tiempo);
                                        break;
                                    case "sábado":
                                        sabado.setText(tiempo);
                                        break;
                                    case "domingo":
                                        domingo.setText(tiempo);
                                        break;
                                    default:
                                        Log.w(TAG, "Día no reconocido: " + dia);
                                        break;
                                }
                            } else {
                                Log.w(TAG, "El campo 'tiempo_total' no existe para el día " + dia);
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al recuperar los datos de Firestore: ", e);
                });
    }

    // Método para convertir segundos a formato mm:ss
    private String convertirSegundosATiempo(long segundos) {
        long minutos = segundos / 60;
        long seg = segundos % 60;
        return String.format("%02d:%02d", minutos, seg);
    }
}
