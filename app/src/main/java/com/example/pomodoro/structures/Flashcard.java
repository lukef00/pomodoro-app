package com.example.pomodoro.structures;

import android.util.JsonReader;
import android.util.JsonToken;
import android.util.JsonWriter;

import androidx.annotation.NonNull;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Flashcard {
    String group;
    String title;
    String answer;
    int totalAnswers;
    int correctAnswers;
    LocalDateTime lastAnswer;

    private static HashMap<String, ArrayList<Flashcard>> flashcards = new HashMap<>();
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public Flashcard(String title, String answer) {
        this.title = title;
        this.answer = answer;
        this.totalAnswers = 0;
        this.correctAnswers = 0;
        this.group = "ungrouped";
        this.lastAnswer = null;
    }
    public Flashcard(String group, String title, String answer) {
        this(title, answer);
        this.group = group;
    }
    public Flashcard(String group, String title, String answer, int totalAnswers, int correctAnswers, LocalDateTime lastAnswer) {
        this(group, title, answer);
        this.totalAnswers = totalAnswers;
        this.correctAnswers = correctAnswers;
        this.lastAnswer = lastAnswer;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getGroup() {
        return group;
    }
    public void setGroup(String group) {
        this.group = group;
    }
    public String getAnswer() {
        return answer;
    }
    public void setAnswer(String answer) {
        this.answer = answer;
    }
    public int getTotalAnswers() {
        return totalAnswers;
    }
    public void setTotalAnswers(int totalAnswers) {
        this.totalAnswers = totalAnswers;
    }
    public int getCorrectAnswers() {
        return correctAnswers;
    }
    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }
    public String getLastAnswer() {
        return lastAnswer != null ? lastAnswer.toString() : null;
    }
    public void setLastAnswer(LocalDateTime lastAnswer) {
        this.lastAnswer = lastAnswer;
    }
    public void increaseCorrectAnswers(int amount) { this.correctAnswers += amount;
    }

    public static ArrayList<FlashcardGroup> getGroups() {
        ArrayList<FlashcardGroup> temp = new ArrayList<>();
        flashcards.forEach((group_name, data) -> {
            temp.add(new FlashcardGroup(group_name, data.size()));
        });
        return temp;
    }

    public static void addGroup(String groupName) {
        Flashcard.flashcards.put(groupName, new ArrayList<>());
    }
    public static void removeGroup(String groupName) {
        Flashcard.flashcards.remove(groupName);
    }

    public void addFlashcard(Flashcard newFlash) {
        String group = newFlash.getGroup();
        Flashcard.flashcards.computeIfAbsent(group, k -> new ArrayList<>()).add(newFlash);
    }

    public static ArrayList<Flashcard> getFlashcardsFromGroup(String groupName) {
        return flashcards.get(groupName);
    }

//    public static ArrayList<Flashcard> getFlashcards() { return Flashcard.getFlashcards(); }
//    public static Flashcard getFlashcard(int pos) { return Flashcard.flashcards.get(pos); }
//    public static void addFlashcard(Flashcard t) { Flashcard.flashcards.add(t);}
//    public static void removeFlashcard(int pos) { Flashcard.flashcards.remove(pos);}

    // loading data from file into static ArrayList
    public static void deserialize(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        HashMap<String, ArrayList<Flashcard>> nl = new HashMap<>();

        try {
            nl = parseFlashcards(reader);
        } catch (EOFException e) {
            e.printStackTrace();
        }
        finally {
            reader.close();
            Flashcard.flashcards = nl == null ? new HashMap<>() : nl;
        }
    }
    private static HashMap<String, ArrayList<Flashcard>> parseFlashcards(JsonReader reader) throws IOException {
        HashMap<String, ArrayList<Flashcard>> flash = new HashMap<>();
        ArrayList<Flashcard> grouped_flashcards;
        String group_name;
        reader.beginObject();
        while (reader.hasNext()) {
            group_name = reader.nextName();
            grouped_flashcards = new ArrayList<>();

            reader.beginArray();

            while (reader.hasNext()) {
                grouped_flashcards.add(parseFlashcard(reader, group_name));
            }

            reader.endArray();
            flash.put(group_name, grouped_flashcards);
        }
        reader.endObject();
        return flash;
    }
    private static Flashcard parseFlashcard(JsonReader reader, String group_name) throws IOException {
        String title = null;
        String answer = null;
        int totalAnswers = 0;
        int correctAnswers = 0;
        String dateTemplate;
        LocalDateTime lastAnswer = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("title")) {
                title = reader.nextString();
            } else if (name.equals("answer") ) {
                answer = reader.nextString();
            } else if (name.equals("totalAnswers") ) {
                totalAnswers = reader.nextInt();
            }
            else if (name.equals("correctAnswers") ) {
                correctAnswers = reader.nextInt();
            }
            else if (name.equals("lastAnswer") && reader.peek() != JsonToken.NULL) {
                dateTemplate = reader.nextString();
                lastAnswer = LocalDateTime.parse(dateTemplate);
            }
            else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Flashcard(group_name, title, answer, totalAnswers, correctAnswers, lastAnswer);
    }

    // saving data from static ArrayList to json file on device
    public static void serialize(OutputStream fos) throws IOException {
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, "UTF-8"));
        writer.setIndent("  ");
        writer.beginObject();
        Flashcard.flashcards.forEach( (group_name, al) -> {

            try {
                writer.name(group_name);
                writer.beginArray();

                al.forEach(fc -> {
                    try {
                        serializeFlashcard(writer, fc);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                writer.endArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        writer.endObject();
        writer.close();
    }
    private static void serializeFlashcard(@NonNull JsonWriter writer, Flashcard t) throws IOException {
        writer.beginObject();
        writer.name("title").value(t.getTitle());
        writer.name("answer").value(t.getAnswer());
        writer.name("totalAnswers").value(t.getTotalAnswers());
        writer.name("correctAnswers").value(t.getCorrectAnswers());

        String la = t.getLastAnswer();
        if (la != null) writer.name("lastAnswer").value(la);
        else writer.name("lastAnswer").nullValue();

        writer.endObject();
    }

};