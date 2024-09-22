package com.example.control;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class crearTempos extends Fragment {

    private EditText etNombre;
    private EditText etTiempo;
    private OnGuardarTemporizadorListener callback;

    // Interface para comunicar el resultado a la actividad contenedora
    public interface OnGuardarTemporizadorListener {
        void onGuardarTemporizador(String nombre, String tiempo);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            callback = (OnGuardarTemporizadorListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " debe implementar OnGuardarTemporizadorListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflar el layout del fragmento
        View view = inflater.inflate(R.layout.fragment_crear_tempos, container, false);

        etNombre = view.findViewById(R.id.et_nombre);
        etTiempo = view.findViewById(R.id.et_tiempo);

        Button btnGuardar = view.findViewById(R.id.btn_guardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarTemporizador();
            }
        });

        return view;
    }

    private void guardarTemporizador() {
        String nombre = etNombre.getText().toString();
        String tiempo = etTiempo.getText().toString();

        // Pasar los datos de vuelta a la actividad que contiene el fragmento
        if (callback != null) {
            callback.onGuardarTemporizador(nombre, tiempo);
        }
    }
}
