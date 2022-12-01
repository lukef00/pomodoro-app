package com.example.pomodoro.ui.flashcards;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pomodoro.R;
import com.example.pomodoro.adapters.FlashcardAdapter;
import com.example.pomodoro.structures.Flashcard;

public class FlashcardListFragment extends Fragment {
    private final String group_name;

    public FlashcardListFragment(String group_name) {
        this.group_name = group_name;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_flashcard_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ListView lv = getActivity().findViewById(R.id.flashcard_list);
        FlashcardAdapter fa = new FlashcardAdapter(getContext(), Flashcard.getFlashcardsFromGroup(group_name));

        lv.setOnItemClickListener((adapterView, view, i, l) -> {
            Flashcard f = fa.getItem(i);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(f.getAnswer())
                    .setTitle(f.getTitle());
            builder.setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        });

        lv.setAdapter(fa);
    }
}