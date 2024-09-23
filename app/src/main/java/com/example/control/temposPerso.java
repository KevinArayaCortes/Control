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

public class temposPerso extends Fragment {

    private static final String ARG_TIEMPO = "tiempo";
    private CountDownTimer countDownTimer;
    private boolean isRunning = false;
    private MediaPlayer mediaPlayer;

    private TextView textViewTimer;
    private Button buttonStart;

    public static temposPerso newInstance(String tiempo) {
        temposPerso fragment = new temposPerso();
        Bundle args = new Bundle();
        args.putString(ARG_TIEMPO, tiempo);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tempos_perso, container, false);

        textViewTimer = view.findViewById(R.id.textView_timer);
        buttonStart = view.findViewById(R.id.button_start);

        String tiempo = getArguments() != null ? getArguments().getString(ARG_TIEMPO) : "00:00";
        textViewTimer.setText(tiempo);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarTemporizador(tiempo);
            }
        });

        return view;
    }

    private void iniciarTemporizador(String tiempo) {
        String[] partes = tiempo.split(":");
        int minutos = Integer.parseInt(partes[0]);
        int segundos = Integer.parseInt(partes[1]);

        long tiempoEnMilisegundos = (minutos * 60 + segundos) * 1000;

        if (!isRunning) {
            countDownTimer = new CountDownTimer(tiempoEnMilisegundos, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    int minutosRestantes = (int) (millisUntilFinished / 1000) / 60;
                    int segundosRestantes = (int) (millisUntilFinished / 1000) % 60;
                    String tiempoRestante = String.format("%02d:%02d", minutosRestantes, segundosRestantes);
                    textViewTimer.setText(tiempoRestante);
                }

                @Override
                public void onFinish() {
                    textViewTimer.setText("00:00");
                    mediaPlayer = MediaPlayer.create(getContext(), R.raw.alarma);
                    mediaPlayer.start();
                }
            }.start();

            isRunning = true;
        }
    }
}