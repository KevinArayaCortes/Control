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

        // Cargar el fragmento tablaActividad en el fragmentContainerViewActividad
        Fragment tablaActividadFragment = new tablaActividad();
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerViewActividad, tablaActividadFragment)
                .commit();

        // Navegar a crearActividadFragment al presionar el botón
        view.findViewById(R.id.crearActividadButton).setOnClickListener(v -> {
            Fragment crearActividadFragment = new crearActividadFragment();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contenedor, crearActividadFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }
}
