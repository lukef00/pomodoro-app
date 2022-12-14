package com.example.pomodoro.ui.tasks;

import android.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pomodoro.R;
import com.example.pomodoro.databinding.FragmentTasksBinding;
import com.example.pomodoro.structures.Task;
import com.example.pomodoro.adapters.TaskAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TasksFragment extends Fragment {

    FragmentTasksBinding tb;
    FloatingActionButton fab;
    TaskAdapter ta;
    ImageView image;
    TextView text;

    int selected_item;
    boolean selected_status;

    private void updateUI() {
        if (ta.getCount() == 0) {
            image.setAlpha(.7F);
            text.setAlpha(.8F);
        } else {
            image.setAlpha(0.0F);
            text.setAlpha(0.0F);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        tb = FragmentTasksBinding.inflate(getLayoutInflater());
        View view = tb.getRoot();
        image = view.findViewById(R.id.imageView3);
        text = view.findViewById(R.id.emptyListLabel);
        ta = new TaskAdapter(getContext());
        tb.taskList.setAdapter(ta);

        ListView lv = view.findViewById(R.id.task_list);
        updateUI();

        lv.setOnItemClickListener((adapterView, view1, i, l) -> {
            Task f = ta.getItem(i);
            if (f.getDescription() != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(f.getDescription()).setTitle(f.getTitle());
                builder.setPositiveButton(R.string.close, (dialog, id) -> dialog.dismiss());

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        lv.setOnItemLongClickListener((adapterView, view1, i, l) -> {
            selected_item = i;

            final Dialog d = new Dialog(getActivity());
            d.requestWindowFeature(Window.FEATURE_NO_TITLE);
            d.setContentView(R.layout.task_bottom_dialog);
            TextView ts = d.findViewById(R.id.task_status);
            ImageView sv = d.findViewById(R.id.task_status_img);

            selected_status = Task.getStatus(selected_item);
            if (selected_status) {
                ts.setText(R.string.unmark_task_label);
                sv.setImageResource(R.drawable.ic_baseline_remove_done_24);
            } else {
                ts.setText(R.string.mark_task_label);
                sv.setImageResource(R.drawable.ic_baseline_done_24);
            }

            d.findViewById(R.id.removeLL).setOnClickListener((v) -> {
                Task.removeTask(selected_item);
                d.dismiss();
                ta.notifyDataSetChanged();
                updateUI();
            });

            d.findViewById(R.id.editLL).setOnClickListener((v) -> {
                Task t = ta.getItem(i);
                d.dismiss();

                AddTaskFragment atf = new AddTaskFragment(t, ta);
                getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_navigation, atf).addToBackStack(atf.toString()).commit();
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
            return true;
        });

        ArrayList<Task> tasks = Task.getTasks();
        final int[] num = {0};
        tasks.forEach((task -> num[0] += task.isFinished() ? 0 : 1));
        if (num[0] > 0) {
            Toast.makeText(getContext(), String.format("%d %s left to go", num[0], num[0] == 1 ? "task" : "tasks"), Toast.LENGTH_LONG).show();

        }
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(view12 -> {
            AddTaskFragment atf = new AddTaskFragment();
            getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_navigation, atf).addToBackStack(atf.toString()).commit();
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

}