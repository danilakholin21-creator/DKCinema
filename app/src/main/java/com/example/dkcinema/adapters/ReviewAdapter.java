package com.example.dkcinema.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dkcinema.R;
import com.example.dkcinema.models.Review;
import com.example.dkcinema.models.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private List<Review> reviews;
    private List<User> users;

    public ReviewAdapter(List<Review> reviews, List<User> users) {
        this.reviews = reviews;
        this.users = users;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);
        String username = "Unknown";
        for (User u : users) {
            if (u.getId() == review.getUserId()) {
                username = u.getUsername();
                break;
            }
        }
        holder.username.setText(username);
        holder.ratingBar.setRating(review.getRating());
        holder.comment.setText(review.getComment());
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        holder.date.setText(sdf.format(new Date(review.getTimestamp())));
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView username, comment, date;
        RatingBar ratingBar;

        ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.review_username);
            ratingBar = itemView.findViewById(R.id.review_rating);
            comment = itemView.findViewById(R.id.review_comment);
            date = itemView.findViewById(R.id.review_date);
        }
    }
}