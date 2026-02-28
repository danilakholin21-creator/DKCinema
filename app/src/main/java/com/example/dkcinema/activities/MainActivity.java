package com.example.dkcinema.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
    private Chip chipAll;
    private MovieAdapter.OnItemClickListener itemClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getInstance(this);
        sessionManager = new SessionManager(this);

        setSupportActionBar(findViewById(R.id.toolbar));

        recyclerView = findViewById(R.id.recyclerViewMovies);
        chipGroupGenres = findViewById(R.id.chipGroupGenres);
        chipAll = findViewById(R.id.chipAll);

        // Убираем галочку у чипа "Все"
        chipAll.setCheckedIconVisible(false);

        itemClickListener = movie -> {
            Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
            intent.putExtra("movie_id", movie.getId());
            startActivity(intent);
        };

        new Thread(() -> {
            DatabaseInitializer.populateMovies(MainActivity.this);
            List<Movie> movies = db.movieDao().getAllMovies();
            runOnUiThread(() -> {
                allMovies = movies;
                setupAdapter(allMovies);
                setupGenreChips();
                chipAll.setChecked(true);
            });
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshMovies();
    }

    private void refreshMovies() {
        new Thread(() -> {
            List<Movie> updatedMovies = db.movieDao().getAllMovies();
            runOnUiThread(() -> {
                allMovies = updatedMovies;
                if (adapter != null) {
                    adapter.updateData(allMovies);
                }
            });
        }).start();
    }

    private void setupAdapter(List<Movie> movies) {
        adapter = new MovieAdapter(movies, itemClickListener);
        recyclerView.setAdapter(adapter);
    }

    private void setupGenreChips() {
        // Удаляем ранее динамически добавленные чипы (кроме chipAll)
        for (int i = 0; i < chipGroupGenres.getChildCount(); i++) {
            View child = chipGroupGenres.getChildAt(i);
            if (child != chipAll) {
                chipGroupGenres.removeView(child);
                i--;
            }
        }

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
            chip.setCheckedIconVisible(false); // Убираем галочку
            chipGroupGenres.addView(chip);
        }

        chipGroupGenres.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                adapter.updateData(allMovies);
            } else {
                Chip selectedChip = findViewById(checkedIds.get(0));
                if (selectedChip == chipAll) {
                    adapter.updateData(allMovies);
                } else {
                    String selectedGenre = selectedChip.getText().toString();
                    List<Movie> filtered = new ArrayList<>();
                    for (Movie movie : allMovies) {
                        if (movie.getGenre().equals(selectedGenre)) {
                            filtered.add(movie);
                        }
                    }
                    adapter.updateData(filtered);
                }
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