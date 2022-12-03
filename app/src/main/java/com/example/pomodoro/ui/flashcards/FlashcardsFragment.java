package com.example.pomodoro.ui.flashcards;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.example.pomodoro.R;
import com.example.pomodoro.adapters.GroupAdapter;
import com.example.pomodoro.structures.Flashcard;
import com.example.pomodoro.structures.FlashcardGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FlashcardsFragment extends Fragment {
    GroupAdapter ga;
    Button new_group;
    Button new_flashcard;
    GridView gv;

    private void displayDialog(int layoutFile, boolean isGroup) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(layoutFile);

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        if (isGroup) {
            builder.setTitle("Add new group");
            builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int id) {
                    EditText name = (EditText) ((AlertDialog) dialogInterface).findViewById(R.id.dialog_group_name);
                    String text = String.valueOf(name.getText());
                    text = text.trim();

                    if (text.length() > 0) {
                        ga.add(new FlashcardGroup(text, 0));
                        Flashcard.addGroup(text);
                        ga.notifyDataSetChanged();
                    }
                }
            });
        }

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_flashcards, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ga = new GroupAdapter(getContext());

        Activity a = getActivity();
        FloatingActionButton fab = a.findViewById(R.id.flashcard_float);
        gv = a.findViewById(R.id.group_container);

        new_group = a.findViewById(R.id.button);
        new_flashcard = a.findViewById(R.id.button2);

        gv.setOnItemLongClickListener(((adapterView, view, i, l) -> {
            FlashcardGroup fg =ga.getItem(i);
            Flashcard.removeGroup(fg.getName());
            ga.remove(fg);
            ga.notifyDataSetChanged();
            return true;
        }
        ));
        gv.setOnItemClickListener((adapterView, view, i, l) -> {

            FlashcardListFragment atf = new FlashcardListFragment(ga.getItem(i).getName());
            getParentFragmentManager().
                    beginTransaction().
                    replace(R.id.nav_host_fragment_content_navigation, atf).addToBackStack(atf.toString()).commit();
        });

        fab.setOnClickListener(v -> {
            int vis = 0;
            if (new_group.getVisibility() == View.VISIBLE) vis = View.INVISIBLE;
            new_group.setVisibility(vis);
            new_flashcard.setVisibility(vis);
        });

        new_flashcard.setOnClickListener(v -> {
            new_flashcard.setVisibility(View.INVISIBLE);
            new_group.setVisibility(View.INVISIBLE);
        });

        new_group.setOnClickListener(v -> {
            new_flashcard.setVisibility(View.INVISIBLE);
            new_group.setVisibility(View.INVISIBLE);
            displayDialog(R.layout.new_group_dialog, true);
        });

        gv.setAdapter(ga);
    }
}