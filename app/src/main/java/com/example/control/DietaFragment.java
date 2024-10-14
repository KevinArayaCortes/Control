package com.example.control;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DietaFragment extends Fragment {

    public DietaFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dieta, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Cargar el fragmento tablaDieta en el fragmentContainerView2
        Fragment tablaDietaFragment = new tablaDieta();
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView2, tablaDietaFragment)
                .commit();

        // Navegar a crearDietaFragment al presionar el botón
        view.findViewById(R.id.crearDietaButton).setOnClickListener(v -> {
            Fragment crearDietaFragment = new crearDietaFragment();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contenedor, crearDietaFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }
}
