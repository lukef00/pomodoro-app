package com.example.pomodoro.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.pomodoro.R;
import com.example.pomodoro.structures.Task;

public class TaskAdapter extends ArrayAdapter<Task> {
    public TaskAdapter(Context context) {
        super(context, R.layout.task_list_item, Task.getTasks());
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_list_item, parent, false);
        }

        ImageView im = convertView.findViewById(R.id.task_img);
        Task t = getItem(position);
        TextView label = convertView.findViewById(R.id.task_title);
        RatingBar rbar = convertView.findViewById(R.id.ratingBar);

        if (t.isFinished()) {
            int color = convertView.getResources().getColor(R.color.color11);
            label.setPaintFlags(label.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            label.setTextColor(color);
        }
        else {
            int color = convertView.getResources().getColor(R.color.color0);
            label.setPaintFlags(label.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            label.setTextColor(color);
        }

        if (t.getDescription() != null) {
            im.setVisibility(View.VISIBLE);
        }
        else im.setVisibility(View.INVISIBLE);

        rbar.setRating(t.getPriority() + 1);
        label.setText(t.getTitle());
        return convertView;
    }
}
