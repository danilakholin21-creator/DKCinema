package com.example.dkcinema.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dkcinema.R;
import com.example.dkcinema.activities.MovieDetailActivity;
import com.example.dkcinema.adapters.MovieAdapter;
import com.example.dkcinema.database.AppDatabase;
import com.example.dkcinema.models.Movie;
import com.example.dkcinema.models.WatchLater;

import java.util.ArrayList;
import java.util.List;

public class WatchLaterFragment extends Fragment {
    private static final String ARG_USER_ID = "user_id";
    private int userId;
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private AppDatabase db;

    public static WatchLaterFragment newInstance(int userId) {
        WatchLaterFragment fragment = new WatchLaterFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getInt(ARG_USER_ID);
        }
        db = AppDatabase.getInstance(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        new Thread(() -> {
            List<WatchLater> watchList = db.watchLaterDao().getWatchLaterByUser(userId);
            List<Movie> movies = new ArrayList<>();
            for (WatchLater wl : watchList) {
                Movie m = db.movieDao().getMovieById(wl.getMovieId());
                if (m != null) movies.add(m);
            }

            requireActivity().runOnUiThread(() -> {
                adapter = new MovieAdapter(movies, movie -> {
                    Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                    intent.putExtra("movie_id", movie.getId());
                    startActivity(intent);
                });
                recyclerView.setAdapter(adapter);
            });
        }).start();

        return view;
    }
}