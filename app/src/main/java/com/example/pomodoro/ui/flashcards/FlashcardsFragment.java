package com.example.pomodoro.ui.flashcards;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.pomodoro.R;
import com.example.pomodoro.adapters.GroupAdapter;

public class FlashcardsFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_flashcards, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        GroupAdapter ga = new GroupAdapter(getContext());
        GridView gv = getActivity().findViewById(R.id.group_container);
        gv.setOnItemClickListener((adapterView, view, i, l) -> {
            FlashcardListFragment atf = new FlashcardListFragment();
            getParentFragmentManager().
                    beginTransaction().
                    replace(R.id.nav_host_fragment_content_navigation, atf).addToBackStack(atf.toString()).commit();
        });
        gv.setAdapter(ga);

    }
}