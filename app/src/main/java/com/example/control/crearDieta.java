package com.example.control;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Set;

public class crearDieta extends AppCompatActivity {

    private EditText editTextTime;
    private EditText editTextNombre;
    private SharedPreferences sharedPreferences;
    private static final String PREFERENCES_NAME = "dietaPreferences";
    private static final String KEY_NOMBRES = "nombres";
    private static final String KEY_HORAS = "horas";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crear_dieta);

        // Referencias a los EditTexts
        editTextTime = findViewById(R.id.editTextText4);
        editTextNombre = findViewById(R.id.editTextText3);

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);

        // Agregar TextWatcher para la hora
        editTextTime.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) return;

                String currentText = s.toString();
                String cleanText = currentText.replace(":", ""); // Quita los dos puntos

                if (cleanText.length() > 4) {
                    cleanText = cleanText.substring(0, 4); // Limitar a 4 números
                }

                if (cleanText.length() >= 2) {
                    cleanText = cleanText.substring(0, 2) + ":" + cleanText.substring(2); // Formato "HH:mm"
                }

                isUpdating = true;
                editTextTime.setText(cleanText);
                editTextTime.setSelection(cleanText.length()); // Colocar el cursor al final
                isUpdating = false;
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Verificar formato de hora válido
                String currentText = editTextTime.getText().toString();
                if (!currentText.matches("^([01]\\d|2[0-3]):([0-5]\\d)$")) {
                    editTextTime.setError("Formato de hora inválido (HH:mm)");
                }
            }
        });

        // Acción del botón Aceptar para guardar los datos en SharedPreferences
        Button btnAceptar = findViewById(R.id.button8);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String horaIngresada = editTextTime.getText().toString();
                String nombreIngresado = editTextNombre.getText().toString();

                if (!nombreIngresado.isEmpty() && !horaIngresada.isEmpty() && horaIngresada.matches("^([01]\\d|2[0-3]):([0-5]\\d)$")) {
                    guardarDatosEnSharedPreferences(nombreIngresado, horaIngresada);
                    Toast.makeText(crearDieta.this, "Datos guardados", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(crearDieta.this, "Ingrese un nombre y una hora válida (HH:mm)", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Método para guardar el nombre y la hora en SharedPreferences
    private void guardarDatosEnSharedPreferences(String nombre, String hora) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Obtener los valores existentes de nombres y horas
        Set<String> nombres = sharedPreferences.getStringSet(KEY_NOMBRES, new HashSet<>());
        Set<String> horas = sharedPreferences.getStringSet(KEY_HORAS, new HashSet<>());

        // Agregar el nuevo nombre y la nueva hora
        nombres.add(nombre);
        horas.add(hora);

        // Guardar los nuevos valores en SharedPreferences
        editor.putStringSet(KEY_NOMBRES, nombres);
        editor.putStringSet(KEY_HORAS, horas);
        editor.apply();
    }

    public void volver(View v) {
        Intent i = new Intent(this, Dieta.class);
        startActivity(i);
    }
}
