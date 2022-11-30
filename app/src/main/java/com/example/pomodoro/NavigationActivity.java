package com.example.pomodoro;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;

import com.example.pomodoro.structures.Flashcard;
import com.example.pomodoro.structures.Task;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pomodoro.databinding.ActivityNavigationBinding;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Objects;

public class NavigationActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            FileOutputStream fos = openFileOutput("tasks.json", Context.MODE_PRIVATE);
            FileOutputStream fos2 = openFileOutput("flashcards.json", Context.MODE_PRIVATE);
            Task.serialize(fos);
            Flashcard.serialize(fos2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {

        FragmentManager oChildFragmentManager = Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_navigation)).getChildFragmentManager();
        if(oChildFragmentManager.getBackStackEntryCount() > 1){
            oChildFragmentManager.popBackStack();
            return;
        }

        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            FileInputStream fos = openFileInput("tasks.json");
            FileInputStream fos2= openFileInput("flashcards.json");
            Task.deserialize(fos);
            Flashcard.deserialize(fos2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        com.example.pomodoro.databinding.ActivityNavigationBinding binding = ActivityNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarNavigation.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_timer, R.id.nav_timer, R.id.nav_tasks, R.id.nav_flashcard)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {

        FragmentManager oChildFragmentManager = Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_navigation)).getChildFragmentManager();
        if(oChildFragmentManager.getBackStackEntryCount() > 1){
            oChildFragmentManager.popBackStack();
            return true;
        }

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }
}