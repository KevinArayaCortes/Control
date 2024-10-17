package com.example.control;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.firestore.FirebaseFirestore;

public class cargarSemanas2 extends Fragment {

    private FirebaseFirestore db;
    private String semana;
    private String deviceId;

    public cargarSemanas2(String semana, String deviceId) {
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

        // Recuperar los tiempos desde Firestore para la semana 2
        cargarTiemposDesdeFirestore(lunesTiempo, martesTiempo, miercolesTiempo, juevesTiempo, viernesTiempo, sabadoTiempo, domingoTiempo);

        return view;
    }

    private void cargarTiemposDesdeFirestore(TextView lunes, TextView martes, TextView miercoles, TextView jueves, TextView viernes, TextView sabado, TextView domingo) {
        db.collection("semanas")
                .document(deviceId)
                .collection(semana)
                .document("tiempo")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Asignar los tiempos a los TextViews correspondientes
                        Long tiempoTotal = documentSnapshot.getLong("tiempo_total");
                        if (tiempoTotal != null) {
                            String tiempo = tiempoTotal + " horas"; // Ajustar el formato según sea necesario
                            lunes.setText(tiempo); // En este caso, todos los días mostrarán el mismo tiempo total
                            martes.setText(tiempo);
                            miercoles.setText(tiempo);
                            jueves.setText(tiempo);
                            viernes.setText(tiempo);
                            sabado.setText(tiempo);
                            domingo.setText(tiempo);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    // Manejar errores en la recuperación de datos
                });
    }
}
