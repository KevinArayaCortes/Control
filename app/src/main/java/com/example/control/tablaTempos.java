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

public class tablaTempos extends Fragment {

    private TableLayout tableLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tabla_tempos, container, false);
        tableLayout = view.findViewById(R.id.tableLayout_temporizadores);
        return view;
    }

    // Método para agregar una nueva fila a la tabla de temporizadores
    public void agregarFilaTemporizador(String nombre, String tiempo) {
        TableRow fila = new TableRow(getContext());

        TextView tvNombre = new TextView(getContext());
        tvNombre.setText(nombre);
        tvNombre.setTextColor(getResources().getColor(R.color.white));

        TextView tvTiempo = new TextView(getContext());
        tvTiempo.setText(tiempo);
        tvTiempo.setTextColor(getResources().getColor(R.color.white));

        // Agrega las celdas a la fila
        fila.addView(tvNombre);
        fila.addView(tvTiempo);

        // Agrega la fila a la tabla
        tableLayout.addView(fila);
    }
}
