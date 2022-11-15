package com.example.pomodoro.ui.tasks;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pomodoro.databinding.FragmentTasksBinding;
import com.example.pomodoro.structures.Task;
import com.example.pomodoro.structures.TaskAdapter;

import java.io.FileInputStream;
import java.util.ArrayList;

public class TasksFragment extends Fragment {

    FragmentTasksBinding tb;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        tb = FragmentTasksBinding.inflate(getLayoutInflater());
        View view = tb.getRoot();
        //here data must be an instance of the class MarsDataProvider
        // load tasks from internal storage

        TaskAdapter ta = new TaskAdapter(getContext());
        tb.taskList.setAdapter(ta);

        ArrayList<Task> tasks = Task.getTasks();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}