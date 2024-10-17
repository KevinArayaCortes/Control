package com.example.control;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class HistorialFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historial, container, false);

        Button semana1Btn = view.findViewById(R.id.button5);
        Button semana2Btn = view.findViewById(R.id.button6);
        Button semana3Btn = view.findViewById(R.id.button12);
        Button semana4Btn = view.findViewById(R.id.button13);

        semana1Btn.setOnClickListener(v -> cargarFragmentoSemana("semana1"));
        semana2Btn.setOnClickListener(v -> cargarFragmentoSemana("semana2"));
        semana3Btn.setOnClickListener(v -> cargarFragmentoSemana("semana3"));
        semana4Btn.setOnClickListener(v -> cargarFragmentoSemana("semana4"));

        return view;
    }

    private void cargarFragmentoSemana(String semana) {
        Fragment fragment;

        switch (semana) {
            case "semana1":
                fragment = new cargarSemanas();  // Aquí cargamos el fragmento de la semana 1
                break;
            case "semana2":
                fragment = new cargarSemanas2(); // Fragmento de la semana 2
                break;
            case "semana3":
                fragment = new cargarSemanas3(); // Fragmento de la semana 3
                break;
            case "semana4":
                fragment = new cargarSemanas4(); // Fragmento de la semana 4
                break;
            default:
                fragment = new cargarSemanas();  // Por defecto semana 1
        }

        // Realizar la transacción para reemplazar el contenido del contenedor en MainActivity
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contenedor, fragment);  // Aquí usamos el ID del FrameLayout en MainActivity
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
