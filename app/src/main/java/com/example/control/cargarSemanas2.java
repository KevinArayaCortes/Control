package com.example.control;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.control.R;

public class cargarSemanas2 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cargar_semanas2, container, false);

        // Obtener referencias a los TextViews
        TextView lunesTiempo = view.findViewById(R.id.lunes_tiempo);
        TextView martesTiempo = view.findViewById(R.id.martes_tiempo);
        TextView miercolesTiempo = view.findViewById(R.id.miercoles_tiempo);
        TextView juevesTiempo = view.findViewById(R.id.jueves_tiempo);
        TextView viernesTiempo = view.findViewById(R.id.viernes_tiempo);
        TextView sabadoTiempo = view.findViewById(R.id.sabado_tiempo);
        TextView domingoTiempo = view.findViewById(R.id.domingo_tiempo);

        // Establecer los valores en los TextViews
        lunesTiempo.setText("6 horas");
        martesTiempo.setText("7 horas");
        miercolesTiempo.setText("3 horas");
        juevesTiempo.setText("4 horas");
        viernesTiempo.setText("5 horas");
        sabadoTiempo.setText("2 horas");
        domingoTiempo.setText("6 horas");

        return view;
    }
}
