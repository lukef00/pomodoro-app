package com.example.pomodoro.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.pomodoro.R;
import com.example.pomodoro.structures.Flashcard;
import com.example.pomodoro.structures.FlashcardGroup;


public class GroupAdapter extends ArrayAdapter<FlashcardGroup> {
    public GroupAdapter(Context context) {
        super(context, R.layout.task_list_item, Flashcard.getGroups());
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.flashcard_group_item, parent, false);
        }

        FlashcardGroup t = getItem(position);


        TextView label = convertView.findViewById(R.id.group_label);
        TextView items = convertView.findViewById(R.id.group_count);
        label.setText(t.getName());
        items.setText(String.format("%d", t.getSize()));

        return convertView;
    }
}
