package com.example.dkcinema.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dkcinema.R;
import com.example.dkcinema.adapters.ReviewAdapter;
import com.example.dkcinema.database.AppDatabase;
import com.example.dkcinema.models.Movie;
import com.example.dkcinema.models.Review;
import com.example.dkcinema.models.User;
import com.example.dkcinema.models.WatchLater;
import com.example.dkcinema.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailActivity extends AppCompatActivity {
    private int movieId;
    private Movie movie;
    private AppDatabase db;
    private SessionManager sessionManager;
    private int currentUserId = -1;
    private Review existingUserReview;

    private ImageView poster;
    private TextView title, genre, year, description;
    private RatingBar ratingBar;
    private Button buttonWatchLater;
    private LinearLayout layoutAddReview;
    private RatingBar ratingBarUser;
    private EditText editTextComment;
    private Button buttonSubmitReview;
    private Button buttonEditReview;
    private RecyclerView recyclerViewReviews;
    private ReviewAdapter reviewAdapter;
    private List<Review> reviews = new ArrayList<>();
    private List<User> reviewUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        db = AppDatabase.getInstance(this);
        sessionManager = new SessionManager(this);
        if (sessionManager.isLoggedIn()) {
            currentUserId = sessionManager.getUserId();
        }

        movieId = getIntent().getIntExtra("movie_id", -1);
        if (movieId == -1) {
            Toast.makeText(this, "Ошибка: фильм не найден", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();

        new Thread(() -> {
            movie = db.movieDao().getMovieById(movieId);
            if (movie == null) {
                runOnUiThread(() -> {
                    Toast.makeText(MovieDetailActivity.this, "Фильм не найден", Toast.LENGTH_SHORT).show();
                    finish();
                });
                return;
            }

            if (currentUserId != -1) {
                existingUserReview = db.reviewDao().getUserReviewForMovie(currentUserId, movieId);
            }

            List<Review> loadedReviews = db.reviewDao().getReviewsForMovie(movieId);
            List<User> loadedUsers = new ArrayList<>();
            for (Review r : loadedReviews) {
                User u = db.userDao().getUserById(r.getUserId());
                if (u != null) loadedUsers.add(u);
            }

            boolean isInWatchLater = false;
            if (currentUserId != -1) {
                isInWatchLater = db.watchLaterDao().isInWatchLater(currentUserId, movieId);
            }

            final boolean finalIsInWatchLater = isInWatchLater;
            final List<Review> finalLoadedReviews = loadedReviews;
            final List<User> finalLoadedUsers = loadedUsers;

            runOnUiThread(() -> {
                loadMovieData();
                reviews = finalLoadedReviews;
                reviewUsers = finalLoadedUsers;
                setupReviewAdapter();
                buttonWatchLater.setText(finalIsInWatchLater ? "Убрать из списка" : "Хочу посмотреть");
                setupWatchLaterButton();
                setupReviewSection();
            });
        }).start();
    }

    private void initViews() {
        poster = findViewById(R.id.movie_poster);
        title = findViewById(R.id.movie_title);
        genre = findViewById(R.id.movie_genre);
        year = findViewById(R.id.movie_year);
        description = findViewById(R.id.movie_description);
        ratingBar = findViewById(R.id.movie_rating_bar);
        buttonWatchLater = findViewById(R.id.button_watch_later);
        layoutAddReview = findViewById(R.id.layout_add_review);
        ratingBarUser = findViewById(R.id.ratingBar_user);
        editTextComment = findViewById(R.id.editText_comment);
        buttonSubmitReview = findViewById(R.id.button_submit_review);
        buttonEditReview = findViewById(R.id.button_edit_review);
        recyclerViewReviews = findViewById(R.id.recyclerView_reviews);
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadMovieData() {
        if (movie == null) return;
        title.setText(movie.getTitle());
        genre.setText(movie.getGenre());
        year.setText(String.valueOf(movie.getReleaseYear()));
        description.setText(movie.getDescription());
        ratingBar.setRating((float) movie.getAverageRating());

        Glide.with(this)
                .load(movie.getPosterUrl())
                .placeholder(R.drawable.ic_placeholder)
                .into(poster);
    }

    private void setupReviewAdapter() {
        reviewAdapter = new ReviewAdapter(reviews, reviewUsers);
        recyclerViewReviews.setAdapter(reviewAdapter);
    }

    private void setupWatchLaterButton() {
        buttonWatchLater.setOnClickListener(v -> {
            if (currentUserId == -1) {
                Toast.makeText(this, "Войдите, чтобы добавить в список", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                return;
            }
            buttonWatchLater.setEnabled(false);
            new Thread(() -> {
                boolean isIn = db.watchLaterDao().isInWatchLater(currentUserId, movieId);
                if (isIn) {
                    db.watchLaterDao().delete(currentUserId, movieId);
                    runOnUiThread(() -> {
                        buttonWatchLater.setText("Хочу посмотреть");
                        Toast.makeText(MovieDetailActivity.this, "Удалено из списка", Toast.LENGTH_SHORT).show();
                        buttonWatchLater.setEnabled(true);
                    });
                } else {
                    WatchLater wl = new WatchLater(currentUserId, movieId, System.currentTimeMillis());
                    db.watchLaterDao().insert(wl);
                    runOnUiThread(() -> {
                        buttonWatchLater.setText("Убрать из списка");
                        Toast.makeText(MovieDetailActivity.this, "Добавлено в список", Toast.LENGTH_SHORT).show();
                        buttonWatchLater.setEnabled(true);
                    });
                }
            }).start();
        });
    }

    private void setupReviewSection() {
        if (currentUserId != -1) {
            if (existingUserReview != null) {
                layoutAddReview.setVisibility(View.GONE);
                buttonEditReview.setVisibility(View.VISIBLE);
                buttonEditReview.setOnClickListener(v -> {
                    ratingBarUser.setRating(existingUserReview.getRating());
                    editTextComment.setText(existingUserReview.getComment());
                    layoutAddReview.setVisibility(View.VISIBLE);
                    buttonEditReview.setVisibility(View.GONE);
                    buttonSubmitReview.setText("Обновить отзыв");
                });
            } else {
                layoutAddReview.setVisibility(View.VISIBLE);
                buttonEditReview.setVisibility(View.GONE);
                buttonSubmitReview.setText("Оставить отзыв");
            }

            buttonSubmitReview.setOnClickListener(v -> {
                int rating = (int) ratingBarUser.getRating();
                if (rating == 0) {
                    Toast.makeText(MovieDetailActivity.this, "Поставьте оценку", Toast.LENGTH_SHORT).show();
                    return;
                }
                String comment = editTextComment.getText().toString().trim();

                new Thread(() -> {
                    if (existingUserReview != null) {
                        // Обновляем существующий отзыв
                        existingUserReview.setRating(rating);
                        existingUserReview.setComment(comment);
                        existingUserReview.setTimestamp(System.currentTimeMillis());
                        db.reviewDao().update(existingUserReview);
                    } else {
                        // Создаём новый отзыв
                        Review review = new Review(currentUserId, movieId, rating, comment, System.currentTimeMillis());
                        db.reviewDao().insert(review);
                    }

                    double avg = db.reviewDao().getAverageRatingForMovie(movieId);
                    movie.setAverageRating(avg);
                    db.movieDao().update(movie);

                    runOnUiThread(() -> {
                        Toast.makeText(MovieDetailActivity.this,
                                existingUserReview != null ? "Отзыв обновлён" : "Спасибо за отзыв!",
                                Toast.LENGTH_SHORT).show();
                        layoutAddReview.setVisibility(View.GONE);
                        buttonEditReview.setVisibility(View.VISIBLE);
                        refreshReviews();
                    });
                }).start();
            });
        } else {
            layoutAddReview.setVisibility(View.GONE);
            buttonEditReview.setVisibility(View.GONE);
        }
    }

    private void refreshReviews() {
        new Thread(() -> {
            List<Review> newReviews = db.reviewDao().getReviewsForMovie(movieId);
            List<User> newUsers = new ArrayList<>();
            for (Review r : newReviews) {
                User u = db.userDao().getUserById(r.getUserId());
                if (u != null) newUsers.add(u);
            }
            runOnUiThread(() -> {
                reviews = newReviews;
                reviewUsers = newUsers;
                reviewAdapter = new ReviewAdapter(reviews, reviewUsers);
                recyclerViewReviews.setAdapter(reviewAdapter);
                ratingBar.setRating((float) movie.getAverageRating());
            });
        }).start();
    }
}