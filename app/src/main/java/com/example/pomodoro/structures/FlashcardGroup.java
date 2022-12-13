package com.example.pomodoro.structures;

import java.io.IOException;

public class FlashcardGroup {
    private String name;
    private int size;

    public FlashcardGroup(String name, int size) {
        this.name = name;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        try {
            Flashcard.getFlashcardsFromGroup(this.name).forEach(f -> {
                f.setGroup(this.name);
            });
            Flashcard.serialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getSize() {
        return size;
    }
}
