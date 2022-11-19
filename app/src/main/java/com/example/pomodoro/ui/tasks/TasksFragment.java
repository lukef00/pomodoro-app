package com.example.pomodoro.ui.tasks;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pomodoro.R;
import com.example.pomodoro.databinding.FragmentTasksBinding;
import com.example.pomodoro.structures.Task;
import com.example.pomodoro.structures.TaskAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TasksFragment extends Fragment {

    FragmentTasksBinding tb;
    FloatingActionButton fab;
    TaskAdapter ta;
    int selected_item;
    boolean selected_status;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        tb = FragmentTasksBinding.inflate(getLayoutInflater());
        View view = tb.getRoot();
        ta = new TaskAdapter(getContext());
        tb.taskList.setAdapter(ta);

        ListView lv = view.findViewById(R.id.task_list);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selected_item = i;

                final Dialog d = new Dialog(getActivity());
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.setContentView(R.layout.bottom_nav);
                TextView ts = d.findViewById(R.id.task_status);
                ImageView sv = d.findViewById(R.id.task_status_img);

                selected_status = Task.getStatus(selected_item);
                if (selected_status) {
                    ts.setText(R.string.unmark_task_label);
                    sv.setImageResource(R.drawable.ic_baseline_remove_done_24);
                }
                else {
                    ts.setText(R.string.mark_task_label);
                    sv.setImageResource(R.drawable.ic_baseline_done_24);
                }

                d.findViewById(R.id.removeLL).setOnClickListener((v) -> {
                    Task.removeTask(selected_item);
                    d.dismiss();
                    ta.notifyDataSetChanged();
                });

                d.findViewById(R.id.mcLL).setOnClickListener((v) -> {
                    selected_status = !selected_status;
                    Task.setStatus(selected_status, selected_item);
                    d.dismiss();
                    ta.notifyDataSetChanged();

                });

                d.show();
                d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                d.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                d.getWindow().setGravity(Gravity.BOTTOM);
            }
        });

        ArrayList<Task> tasks = Task.getTasks();
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTaskFragment atf = new AddTaskFragment();
                getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_navigation, atf).addToBackStack(atf.toString()).commit();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

}