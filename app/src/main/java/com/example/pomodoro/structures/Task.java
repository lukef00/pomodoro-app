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
import java.util.ArrayList;
import java.util.Collections;

public class Task implements Comparable<Task>{
    private String title;
    private String description;
    private int priority;
    private boolean finished;

    private static ArrayList<Task> tasks = new ArrayList<>();

    public Task(String title, int priority) {
        this.title = title;
        this.priority = priority;
        this.finished = false;
    }
    public Task(String title, String description, int priority, boolean finished) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.finished = finished;
    }
    public Task(@NonNull String title, @NonNull String description, int priority) {
        this.title = title.trim();
        this.description = description.trim();
        this.priority = priority;
    }

    public static void serialize(OutputStream fos) throws IOException {
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, "UTF-8"));
        writer.setIndent("  ");
        writer.beginArray();
        Task.tasks.stream().forEach((task) -> {
            try {
                serializeTask(writer, task);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        writer.endArray();
        writer.close();
    }
    private static void serializeTask(@NonNull JsonWriter writer, Task t) throws IOException {
        writer.beginObject();
        writer.name("title").value(t.getTitle());
        writer.name("priority").value(t.getPriority());
        writer.name("finished").value(t.isFinished());
        String desc = t.getDescription();
        if (desc != null) writer.name("description").value(desc);
        else writer.name("description").nullValue();
        writer.endObject();
    }

    public static void deserialize(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        ArrayList<Task> nl = null;

        try {
            nl = parseTasks(reader);
        } catch (EOFException e) {}
        finally {
            reader.close();
            Task.tasks = nl;
        }
        Task.sortTasks();
    }

    private static void sortTasks() {
        Collections.sort(Task.tasks);
    }


    private static ArrayList<Task> parseTasks(JsonReader reader) throws IOException {
        ArrayList<Task> tasks = new ArrayList<Task>();

        reader.beginArray();
        while (reader.hasNext()) {
            tasks.add(parseTask(reader));
        }
        reader.endArray();
        return tasks;
    }
    private static Task parseTask(JsonReader reader) throws IOException {
       String title = null;
       String description = null;
       boolean finished = false;
       int priority = 0;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("title")) {
                title = reader.nextString();
            } else if (name.equals("description") && reader.peek() != JsonToken.NULL) {
                description = reader.nextString();
            } else if (name.equals("priority") ) {
                priority = reader.nextInt();
            } else if (name.equals("finished") ) {
                finished = reader.nextBoolean();
            }
            else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Task(title, description, priority, finished);
    }

    public static ArrayList<Task> getTasks() { return Task.tasks; }
    public static Task getTask(int pos) { return Task.tasks.get(pos); }
    public static void addTask(Task t) { Task.tasks.add(t); Task.sortTasks();}
    public static void removeTask(int pos) { Task.tasks.remove(pos); Task.sortTasks(); }
    public static void setStatus(boolean newStatus, int taskPos) {
        Task.tasks.get(taskPos).setFinished(newStatus); Task.sortTasks();
    }
    public static boolean getStatus(int taskPos) { return Task.tasks.get(taskPos).isFinished();}

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getPriority() {
        return priority;
    }
    public void setPriority(int priority) {
        this.priority = priority;
    }
    public boolean isFinished() {
        return finished;
    }
    public void setFinished(boolean finished) {
        this.finished = finished;
    }
    public String toString() {
        return String.format("Task(title: %s, description: %s, priority: %d, finished: %b)",
                getTitle(), getDescription(), getPriority(), isFinished());
    }

    @Override
    public int compareTo(Task task) {
        // by status
        int mine = isFinished() ? 1 : 0;
        int their = task.isFinished() ? 1: 0;
        int status_compare = mine - their;
        if (status_compare != 0) return status_compare;
        // by priority
        int priority_compare = task.getPriority() - this.getPriority();
        if (priority_compare != 0) return priority_compare;

        // alphabetically
        return this.getTitle().compareTo(task.getTitle());
    }
}
