package com.example.control;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class DesafioFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflar el layout para este fragmento
        View view = inflater.inflate(R.layout.fragment_desafio, container, false);

        // Configurar el botón para cargar el fragmento ultimaSemana
        Button verButton = view.findViewById(R.id.button);
        verButton.setOnClickListener(v -> cargarUltimaSemana());

        return view;
    }

    private void cargarUltimaSemana() {
        // Crear una nueva instancia del fragmento ultimaSemana
        ultimaSemana fragment = new ultimaSemana();

        // Obtener el FragmentManager y comenzar la transacción
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Reemplazar el contenido del contenedor con el nuevo fragmento
        fragmentTransaction.replace(R.id.contenedor, fragment);
        fragmentTransaction.addToBackStack(null); // Permite volver al fragmento anterior
        fragmentTransaction.commit(); // Finalizar la transacción
    }
}
