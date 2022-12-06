package com.example.pomodoro.ui.timer;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.pomodoro.R;

public class TimerFragment extends Fragment {

    private final int POMODORO_LENGTH = 25 * 60 * 1000;
    private final int SHORT_BREAK_LENGTH = 5 * 60 * 1000;
    private final int LONG_BREAK_LENGTH = 25 * 60 * 1000;

    private int POMODORO_COUNT = 0;
    private boolean POMODORO_RUNNING = false;

    // notification and CountDownTimer

    // UI elements
    private Button pomodoro_startButton;
    private TextView pomodoro_TextView;
    private TextView timer_TextView;


    @SuppressLint("DefaultLocale")
    private void getElements(View view) {
        this.pomodoro_startButton = view.findViewById(R.id.button_pomodoro_start);
        this.pomodoro_TextView = view.findViewById(R.id.pomodoro_num);
        this.timer_TextView = view.findViewById(R.id.timer);
        this.pomodoro_TextView.setText(String.format("%d out of 4", POMODORO_COUNT));
    }

    private void updateWidgets(long milliseconds) {
        if (milliseconds > 0) {
            int seconds_left = (int) (milliseconds / 1000);
            int minutes_left = seconds_left / 60;
            seconds_left = seconds_left % 60;

            @SuppressLint("DefaultLocale") String formatted_data = String.format("%02d:%02d", minutes_left, seconds_left);
            timer_TextView.setText(formatted_data);
        }

    }

    @SuppressLint("DefaultLocale")
    private void startPomodoro() {
        POMODORO_COUNT++;
        pomodoro_TextView.setText(String.format("%d out of 4", POMODORO_COUNT));

        new CountDownTimer(POMODORO_LENGTH, 1000) {
            public void onTick(long millisUntilFinished) {
                updateWidgets(millisUntilFinished);
            }

            @Override
            public void onFinish() {
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
                    // TODO add some indication that session has ended, congratulate user
                    pomodoro_startButton.setVisibility(View.VISIBLE);
                    POMODORO_COUNT = 0;
                    POMODORO_RUNNING = false;
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