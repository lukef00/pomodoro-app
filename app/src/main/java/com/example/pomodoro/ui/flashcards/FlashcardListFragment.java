package com.example.pomodoro.ui.flashcards;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pomodoro.R;
import com.example.pomodoro.adapters.FlashcardAdapter;
import com.example.pomodoro.structures.Flashcard;
import com.example.pomodoro.structures.Task;
import com.example.pomodoro.ui.tasks.AddTaskFragment;

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

        lv.setOnItemLongClickListener((adapterView, view1, i, l) -> {
            final Dialog d = new Dialog(getActivity());
            d.requestWindowFeature(Window.FEATURE_NO_TITLE);
            d.setContentView(R.layout.flashcard_bottom_dialog);

            d.findViewById(R.id.eFlashcard).setOnClickListener((v) -> {
                Flashcard t = fa.getItem(i);
                d.dismiss();

                AddFlashcardFragment atf = new AddFlashcardFragment(t, fa);
                getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_navigation, atf).addToBackStack(atf.toString()).commit();
            });

            d.findViewById(R.id.fRemoveLL).setOnClickListener((v) -> {
                Flashcard.removeFlashcard(fa.getItem(i));
                d.dismiss();
                fa.notifyDataSetChanged();


            });

            d.show();
            d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            d.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            d.getWindow().setGravity(Gravity.BOTTOM);
            return true;
        });


        lv.setAdapter(fa);
    }
}