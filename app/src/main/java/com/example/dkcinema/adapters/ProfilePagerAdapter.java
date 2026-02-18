package com.example.dkcinema.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.dkcinema.fragments.RatedMoviesFragment;
import com.example.dkcinema.fragments.WatchLaterFragment;

public class ProfilePagerAdapter extends FragmentStateAdapter {
    private int userId;

    public ProfilePagerAdapter(@NonNull FragmentActivity fragmentActivity, int userId) {
        super(fragmentActivity);
        this.userId = userId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return RatedMoviesFragment.newInstance(userId);
        } else {
            return WatchLaterFragment.newInstance(userId);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}