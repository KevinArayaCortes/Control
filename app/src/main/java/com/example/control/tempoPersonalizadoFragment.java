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

public class tempoPersonalizadoFragment extends Fragment {

    private TextView timerTextView;
    private Button startButton;
    private CountDownTimer countDownTimer;
    private boolean timerRunning;
    private boolean alarmPlaying;
    private long timeLeftInMillis;
    private long initialTimeInMillis;
    private MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tempo_personalizado, container, false);

        timerTextView = view.findViewById(R.id.textView_timer);
        startButton = view.findViewById(R.id.button_start);

        // Obtener el tiempo desde los argumentos
        if (getArguments() != null) {
            timeLeftInMillis = getArguments().getLong("TIEMPO", 60000); // 1 minuto por defecto
        } else {
            timeLeftInMillis = 60000;
        }
        initialTimeInMillis = timeLeftInMillis;

        // Inicializar MediaPlayer para la alarma
        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.alarma);
        alarmPlaying = false;

        startButton.setOnClickListener(v -> {
            if (alarmPlaying) {
                stopAlarmAndResetTimer();
            } else if (timerRunning) {
                stopTimer();
            } else {
                startTimer();
            }
        });

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
            mediaPlayer.prepareAsync();
        }
        alarmPlaying = false;
        resetTimer();
        startButton.setText("Comenzar");
    }

    private void resetTimer() {
        timeLeftInMillis = initialTimeInMillis;
        updateTimer();
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }
}
