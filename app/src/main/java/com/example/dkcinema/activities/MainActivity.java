package com.example.dkcinema.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dkcinema.R;
import com.example.dkcinema.adapters.MovieAdapter;
import com.example.dkcinema.database.AppDatabase;
import com.example.dkcinema.models.Movie;
import com.example.dkcinema.utils.DatabaseInitializer;
import com.example.dkcinema.utils.SessionManager;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private List<Movie> allMovies = new ArrayList<>();
    private AppDatabase db;
    private SessionManager sessionManager;
    private ChipGroup chipGroupGenres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getInstance(this);
        sessionManager = new SessionManager(this);

        setSupportActionBar(findViewById(R.id.toolbar));

        recyclerView = findViewById(R.id.recyclerViewMovies);
        chipGroupGenres = findViewById(R.id.chipGroupGenres);

        new Thread(() -> {
            DatabaseInitializer.populateMovies(MainActivity.this);

            List<Movie> movies = db.movieDao().getAllMovies();

            runOnUiThread(() -> {
                allMovies = movies;
                setupAdapter(allMovies);
                setupGenreChips();
            });
        }).start();
    }

    private void setupAdapter(List<Movie> movies) {
        adapter = new MovieAdapter(movies, movie -> {
            Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
            intent.putExtra("movie_id", movie.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }

    private void setupGenreChips() {
        List<String> genres = new ArrayList<>();
        for (Movie movie : allMovies) {
            if (!genres.contains(movie.getGenre())) {
                genres.add(movie.getGenre());
            }
        }
        for (String genre : genres) {
            Chip chip = new Chip(this);
            chip.setText(genre);
            chip.setCheckable(true);
            chip.setClickable(true);
            chipGroupGenres.addView(chip);
        }

        chipGroupGenres.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                adapter = new MovieAdapter(allMovies, null);
                recyclerView.setAdapter(adapter);
            } else {
                Chip selectedChip = findViewById(checkedIds.get(0));
                String selectedGenre = selectedChip.getText().toString();
                List<Movie> filtered = new ArrayList<>();
                for (Movie movie : allMovies) {
                    if (movie.getGenre().equals(selectedGenre)) {
                        filtered.add(movie);
                    }
                }
                adapter = new MovieAdapter(filtered, null);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_profile) {
            if (sessionManager.isLoggedIn()) {
                startActivity(new Intent(this, ProfileActivity.class));
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}