package com.example.control;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class tablaDieta extends Fragment {

    public tablaDieta() {
        // Constructor público vacío
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Infla el layout del fragmento con la tabla de horarios
        return inflater.inflate(R.layout.fragment_tabla_dieta, container, false);
    }
}
