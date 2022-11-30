package com.example.pomodoro.ui.timer;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
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

    private final String CHANNEL_ID = "9000";
    private final int NOTIFICATION_ID = 1;

    private final int POMODORO_LENGTH = 25 * 60 * 1000;
    private final int SHORT_BREAK_LENGTH = 5 * 60 * 1000;
    private final int LONG_BREAK_LENGTH = 25 * 60 * 1000;

    private int POMODORO_COUNT = 0;
    private boolean POMODORO_RUNNING = false;

    // notification and CountDownTimer
    private NotificationManagerCompat notificationManager;
    private NotificationCompat.Builder builder;

    // UI elements
    private Button pomodoro_startButton;
    private TextView pomodoro_TextView;
    private TextView timer_TextView;



    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        CharSequence name = getString(R.string.channel_name);
        String description = getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    @SuppressLint("DefaultLocale")
    private void getElements(View view) {
        this.pomodoro_startButton = view.findViewById(R.id.button_pomodoro_start);
        this.pomodoro_TextView = view.findViewById(R.id.pomodoro_num);
        this.timer_TextView = view.findViewById(R.id.timer);
        this.pomodoro_TextView.setText(String.format("%d out of 4", POMODORO_COUNT));
    }

    private void displayNotification() {
        this.builder = new NotificationCompat.Builder(getActivity(), CHANNEL_ID)
                .setSmallIcon(R.drawable.tomato)
                .setContentText(timer_TextView.getText())
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        this.notificationManager = NotificationManagerCompat.from(getActivity());
        this.notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void updateWidgets(long milliseconds) {
        if (milliseconds > 0) {
            int seconds_left = (int) (milliseconds / 1000);
            int minutes_left = seconds_left / 60;
            seconds_left = seconds_left % 60;

            @SuppressLint("DefaultLocale") String formatted_data = String.format("%02d:%02d", minutes_left, seconds_left);
            builder.setContentText(formatted_data);
            timer_TextView.setText(formatted_data);
        }

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    @SuppressLint("DefaultLocale")
    private void startPomodoro() {
        POMODORO_COUNT++;
        pomodoro_TextView.setText(String.format("%d out of 4", POMODORO_COUNT));
        builder.setContentTitle("Session going on");



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
        builder.setContentTitle("Session going on");

        new CountDownTimer(longBreak ? LONG_BREAK_LENGTH : SHORT_BREAK_LENGTH, 1000) {
            public void onTick(long millisUntilFinished) {
                updateWidgets(millisUntilFinished);
            }

            public void onFinish() {
                if (POMODORO_COUNT <= 3) startPomodoro();
                else {
                    // TODO add some indication that session has ended, congratulate user
                    builder.setContentTitle("Congratulations, you finished this session");
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
        createNotificationChannel();

        if (POMODORO_RUNNING) pomodoro_startButton.setVisibility(View.INVISIBLE);

        pomodoro_startButton.setOnClickListener(v -> {
            // first of all we have to hide start button and show notification
            POMODORO_RUNNING = true;
            pomodoro_startButton.setVisibility(View.INVISIBLE);
            displayNotification();
            startPomodoro();
        });
    }

}