package com.example.control;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Hobbie extends AppCompatActivity {

    private EditText editTextTime;
    private boolean isUpdating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hobbie);

        editTextTime = findViewById(R.id.editTextTime);

        // Agregar TextWatcher para formatear el tiempo en mm:ss
        editTextTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) {
                    isUpdating = false;
                    return;
                }

                String input = s.toString().replace(":", ""); // Eliminar los ':'
                if (input.length() >= 3) {
                    String minutes = input.substring(0, 2);
                    String seconds = input.substring(2);

                    isUpdating = true;
                    editTextTime.setText(minutes + ":" + seconds);
                    editTextTime.setSelection(editTextTime.getText().length()); // Mover el cursor al final
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void aceptarTiempo(View v) {
        String timeInput = editTextTime.getText().toString();

        // Validar que esté en formato mm:ss
        if (!timeInput.isEmpty() && timeInput.contains(":")) {
            String[] timeParts = timeInput.split(":");
            int minutes = Integer.parseInt(timeParts[0]);
            int seconds = Integer.parseInt(timeParts[1]);

            long timeInMillis = (minutes * 60 + seconds) * 1000; // Convertir a milisegundos

            // Enviar el tiempo ingresado al otro Activity
            Intent intent = new Intent(this, tempoPersonalizado.class);
            intent.putExtra("TIEMPO", timeInMillis);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Por favor, ingrese un tiempo válido en formato mm:ss", Toast.LENGTH_SHORT).show();
        }
    }

    public void inicio(View v) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
