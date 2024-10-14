package com.example.control;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class tempoDientesFragment extends Fragment {

    private TextView timerTextView;
    private Button startButton;
    private CountDownTimer countDownTimer;
    private boolean timerRunning;
    private boolean alarmPlaying;
    private long timeLeftInMillis = 180000; // 3 minutos
    private final long initialTimeInMillis = 180000; // Guardamos el tiempo inicial para restaurarlo
    private MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tempo_dientes, container, false);

        timerTextView = view.findViewById(R.id.textView_timer);
        startButton = view.findViewById(R.id.button_start);

        // Inicializar el MediaPlayer con el sonido de alarma
        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.alarma);
        alarmPlaying = false;

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerRunning) {
                    stopTimer();
                } else if (alarmPlaying) {
                    stopAlarm(); // Detener la alarma si está sonando
                } else {
                    startTimer();
                }
            }
        });

        // Actualizamos la vista del temporizador al iniciar el fragmento
        updateTimer();

        return view;
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
                startButton.setText(getString(R.string.comenzar));

                // Reproducir la alarma
                if (mediaPlayer != null) {
                    mediaPlayer.start();
                    alarmPlaying = true;
                    startButton.setText(getString(R.string.detener_alarma)); // Cambiar el texto del botón
                }
            }
        }.start();

        timerRunning = true;
        startButton.setText(getString(R.string.pausa));
    }

    private void stopTimer() {
        countDownTimer.cancel();
        timerRunning = false;
        startButton.setText(getString(R.string.comenzar));
    }

    private void updateTimer() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
        timerTextView.setText(timeLeftFormatted);
    }

    private void stopAlarm() {
        if (mediaPlayer != null && alarmPlaying) {
            mediaPlayer.pause();
            mediaPlayer.seekTo(0); // Reiniciar el sonido de alarma para la próxima vez
            alarmPlaying = false;

            // Restablecer el tiempo inicial
            timeLeftInMillis = initialTimeInMillis;
            updateTimer(); // Actualizamos la vista del temporizador

            startButton.setText(getString(R.string.comenzar)); // Volver al estado inicial del botón
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
