package com.example.pomodoro.ui.timer;


import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.pomodoro.R;

import java.io.IOException;

public class TimerFragment extends Fragment {

    private final int POMODORO_LENGTH = 25 * 60 * 1000;
    private final int SHORT_BREAK_LENGTH = 5 * 60 * 1000;
    private final int LONG_BREAK_LENGTH = 25 * 60 * 1000;

    private int POMODORO_COUNT = 0;
    private boolean POMODORO_RUNNING = false;
    private static MediaPlayer player;

    // UI elements
    private Button pomodoro_startButton;
    private TextView pomodoro_TextView;
    private TextView timer_TextView;
    private ImageView image;


    @SuppressLint("DefaultLocale")
    private void getElements(View view) {
        this.pomodoro_startButton = view.findViewById(R.id.button_pomodoro_start);
        this.pomodoro_TextView = view.findViewById(R.id.pomodoro_num);
        this.timer_TextView = view.findViewById(R.id.timer);
        this.image = view.findViewById(R.id.imageView);
        this.pomodoro_TextView.setText(String.format("%d out of 4", POMODORO_COUNT));
    }

    private void updateWidgets(long milliseconds) {
            int seconds_left = (int) (milliseconds / 1000);
            int minutes_left = seconds_left / 60;
            seconds_left = seconds_left % 60;

            @SuppressLint("DefaultLocale") String formatted_data = String.format("%02d:%02d", minutes_left, seconds_left);
            timer_TextView.setText(formatted_data);
    }

    private void changePlayerTrack(int track) {
        if (player != null) {
            player.release();
        }
        player = MediaPlayer.create(getContext(), track);
        player.start();
    }

    @SuppressLint("DefaultLocale")
    private void startPomodoro() {
        POMODORO_COUNT++;
        image.setImageResource(R.drawable.tomato);
        pomodoro_TextView.setText(String.format("%d out of 4", POMODORO_COUNT));
        changePlayerTrack(R.raw.lofi);

        new CountDownTimer(POMODORO_LENGTH, 1000) {
            public void onTick(long millisUntilFinished) {
                updateWidgets(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                image.setImageResource(R.drawable.break_img);
                changePlayerTrack(POMODORO_COUNT >= 4 ? R.raw.pike : R.raw.familiar);
                startBreak(POMODORO_COUNT >= 4);
            }
        }.start();
    }

    private void startBreak(boolean longBreak) {


        new CountDownTimer(longBreak ? LONG_BREAK_LENGTH : SHORT_BREAK_LENGTH, 1000) {
            public void onTick(long millisUntilFinished) {
                updateWidgets(millisUntilFinished);
            }

            public void onFinish() {
                if (POMODORO_COUNT <= 3) startPomodoro();
                else {
                    pomodoro_startButton.setVisibility(View.VISIBLE);
                    POMODORO_COUNT = 0;
                    POMODORO_RUNNING = false;
                    player.release();
                    updateWidgets(0);
                }
            }
        }.start();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_timer, container, false);
        getElements(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (POMODORO_RUNNING) pomodoro_startButton.setVisibility(View.INVISIBLE);

        pomodoro_startButton.setOnClickListener(v -> {
            // first of all we have to hide start button and show notification
            POMODORO_RUNNING = true;
            pomodoro_startButton.setVisibility(View.INVISIBLE);
            startPomodoro();
        });
    }

}
