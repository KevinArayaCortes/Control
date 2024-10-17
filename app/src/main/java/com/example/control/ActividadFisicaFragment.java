package com.example.control;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ActividadFisicaFragment extends Fragment {

    public ActividadFisicaFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_actividad_fisica, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Cargar tablaActividad en el contenedor del fragmento
        Fragment tablaActividad = new tablaActividad();
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerViewActividad, tablaActividad)
                .commit();

        // Navegar al fragmento de creación de actividad al presionar el botón
        view.findViewById(R.id.crearActividadButton).setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contenedor, new crearActividadFragment())
                    .addToBackStack(null)
                    .commit();
        });
    }
}
