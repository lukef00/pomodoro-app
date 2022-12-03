package com.example.pomodoro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.pomodoro.R;
import com.example.pomodoro.structures.Flashcard;

import java.util.ArrayList;

public class FlashcardAdapter extends ArrayAdapter<Flashcard> {
    public FlashcardAdapter(Context context, ArrayList<Flashcard> flashcardGroup) {
        super(context, R.layout.flashcard_list_item, flashcardGroup);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.flashcard_list_item, parent, false);
        }
        Flashcard t = getItem(position);
        TextView label = convertView.findViewById(R.id.flashcard_title);
        label.setText(t.getTitle());
        return convertView;
    }
}
