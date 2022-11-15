package com.example.pomodoro.structures;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pomodoro.R;


public class TaskAdapter extends ArrayAdapter<Task> {
    public TaskAdapter(Context context) {
        super(context, R.layout.task_list_item, Task.getTasks());
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Task t = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_list_item, parent, false);
        }

        int priority = t.getPriority();

        TextView label = convertView.findViewById(R.id.task_title);
        ImageView img2 = convertView.findViewById(R.id.image_2);
        ImageView img3 = convertView.findViewById(R.id.image_3);

        if (priority == 0) {
            img2.setAlpha(0.5F);
            img3.setAlpha(0.5F);
        }
        else if (priority == 1) {
            img3.setAlpha(0.5F);
        }
        else {
            img2.setAlpha(1.0F);
            img3.setAlpha(1.0F);
        }

        label.setText(t.getTitle());

        return convertView;
    }
}
