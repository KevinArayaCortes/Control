package com.example.control;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class tempoPersonalizado extends AppCompatActivity {

    private TextView timerTextView;
    private Button startButton;
    private CountDownTimer countDownTimer;
    private boolean timerRunning;
    private boolean alarmPlaying;
    private long timeLeftInMillis;
    private long initialTimeInMillis;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tempo_personalizado);

        timerTextView = findViewById(R.id.textView_timer);
        startButton = findViewById(R.id.button_start);

        // Obtener el tiempo ingresado en la otra actividad
        Intent intent = getIntent();
        timeLeftInMillis = intent.getLongExtra("TIEMPO", 60000); // Tiempo por defecto de 1 minuto
        initialTimeInMillis = timeLeftInMillis; // Guardar tiempo inicial para reiniciar

        // Inicializar MediaPlayer con un sonido de alarma
        mediaPlayer = MediaPlayer.create(this, R.raw.alarma);
        alarmPlaying = false;

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alarmPlaying) {
                    stopAlarmAndResetTimer();
                } else if (timerRunning) {
                    stopTimer();
                } else {
                    startTimer();
                }
            }
        });

        updateTimer();
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                startButton.setText("Detener alarma");
                startAlarm();
            }
        }.start();

        timerRunning = true;
        startButton.setText("Pausar");
    }

    private void stopTimer() {
        countDownTimer.cancel();
        timerRunning = false;
        startButton.setText("Comenzar");
    }

    private void updateTimer() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
        timerTextView.setText(timeLeftFormatted);
    }

    private void startAlarm() {
        mediaPlayer.start();
        alarmPlaying = true;
    }

    private void stopAlarmAndResetTimer() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.prepareAsync(); // Para estar listo para otra reproducción
        }
        alarmPlaying = false;
        resetTimer();
        startButton.setText("Comenzar");
    }

    private void resetTimer() {
        timeLeftInMillis = initialTimeInMillis; // Reiniciar tiempo a su valor inicial
        updateTimer();
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }

    public void hobbie(View v) {
        Intent i = new Intent(this, Hobbie.class);
        startActivity(i);
    }
}
