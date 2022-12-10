package com.example.pomodoro.ui.tasks;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.pomodoro.R;
import com.example.pomodoro.adapters.TaskAdapter;
import com.example.pomodoro.databinding.FragmentAddTaskBinding;
import com.example.pomodoro.structures.Task;

import java.io.IOException;


public class AddTaskFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    Task temp = null;
    TaskAdapter adapter;
    String title;
    String description;
    int priority;


    public AddTaskFragment(Task t, TaskAdapter adap) {
        temp = t;
        adapter = adap;
    }

    TextView title_input;
    TextView description_input;

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        priority = pos;
    }

    public void onNothingSelected(AdapterView<?> parent) {
    }

    public AddTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentAddTaskBinding tb = FragmentAddTaskBinding.inflate(getLayoutInflater());
        View view = tb.getRoot();
        Spinner spinner = view.findViewById(R.id.priority_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.priority_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        title_input = view.findViewById(R.id.add_task_title);
        description_input = view.findViewById(R.id.add_task_description);

        if (temp != null) {
            title_input.setText(this.temp.getTitle());
            description_input.setText(this.temp.getDescription());
            spinner.setSelection(this.temp.getPriority());
        }

        view.findViewById(R.id.add_task_btn).setOnClickListener((v) -> createTask());
        return view;
    }

    private void createTask() {
        title = title_input.getText().toString();
        description = description_input.getText().toString();
        title = title.trim();

        if (title.length() > 0) {
            Task t;

            if (temp != null) {
                temp.setTitle(title);
                if (description.length() > 0) temp.setDescription(description);
                temp.setPriority(priority);
                try {
                    Task.serialize();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Task.sortTasks();
                adapter.notifyDataSetChanged();
            } else {
                if (description.length() == 0) {
                    description = null;

                    t = new Task(title, priority);
                } else t = new Task(title, description, priority);
                Task.addTask(t);
            }


            getParentFragmentManager().popBackStack();
        }

    }
}