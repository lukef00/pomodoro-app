package com.example.pomodoro.ui.flashcards;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.pomodoro.R;
import com.example.pomodoro.adapters.FlashcardAdapter;
import com.example.pomodoro.structures.Flashcard;
import com.example.pomodoro.structures.FlashcardGroup;

import java.util.ArrayList;

public class AddFlashcardFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    ArrayList<String> group_names = new ArrayList<>();

    Flashcard temp;
    FlashcardAdapter fa;

    String selected_group;
    String title;
    String answer;

    // UI elements
    Spinner group_spinner;
    EditText flashcard_title_field;
    EditText flashcard_answer_field;
    Button flashcard_submit;

    public AddFlashcardFragment() {
        // Required empty public constructor
    }

    public AddFlashcardFragment(Flashcard f, FlashcardAdapter fa) {
        temp = f;
        this.fa = fa;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<FlashcardGroup> fg = Flashcard.getGroups();
        if (fg.size() == 0) {
            fg = new ArrayList<>();
            fg.add(new FlashcardGroup("ungroupped", 0));
        }
        fg.forEach((f) -> group_names.add(f.getName()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_flashcard, container, false);
    }

    public void
    onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = getView();
        group_spinner = v.findViewById(R.id.add_flashcard_spinner);

        flashcard_title_field = v.findViewById(R.id.add_flashcard_title);
        flashcard_answer_field = v.findViewById(R.id.add_flashcard_answer);
        flashcard_submit = v.findViewById(R.id.add_flashcard_submit);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, group_names);
        group_spinner.setAdapter(adapter);
        group_spinner.setOnItemSelectedListener(this);

        flashcard_submit.setOnClickListener(x -> {
            title = String.valueOf(flashcard_title_field.getText()).trim();
            answer = String.valueOf(flashcard_answer_field.getText()).trim();

            if (temp != null) {
                if (title.length() > 0 && answer.length() > 0) {
                    temp.setAnswer(answer);
                    temp.setTitle(title);
                    fa.notifyDataSetChanged();
                    getParentFragmentManager().popBackStack();
                }
            } else {

                if (title.length() > 0 && answer.length() > 0) {
                    Flashcard nf = new Flashcard(selected_group, title, answer);
                    Flashcard.addFlashcard(nf);
                    getParentFragmentManager().popBackStack();
                }
            }

        });

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selected_group = (String) adapterView.getSelectedItem();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}