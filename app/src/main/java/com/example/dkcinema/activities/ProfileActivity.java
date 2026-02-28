package com.example.dkcinema.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.dkcinema.R;
import com.example.dkcinema.adapters.ProfilePagerAdapter;
import com.example.dkcinema.database.AppDatabase;
import com.example.dkcinema.models.User;
import com.example.dkcinema.utils.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private CircleImageView avatar;
    private TextView username;
    private Button buttonChangeAvatar, buttonLogout;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private MaterialToolbar toolbar;
    private AppDatabase db;
    private SessionManager sessionManager;
    private User currentUser;
    private static final int PICK_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        db = AppDatabase.getInstance(this);
        sessionManager = new SessionManager(this);

        if (!sessionManager.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        int userId = sessionManager.getUserId();

        initViews();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        new Thread(() -> {
            currentUser = db.userDao().getUserById(userId);
            if (currentUser == null) {
                runOnUiThread(() -> {
                    sessionManager.logout();
                    Toast.makeText(ProfileActivity.this, "Ошибка загрузки профиля", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                    finish();
                });
                return;
            }

            runOnUiThread(() -> {
                loadUserData();
                setupTabs();
                setupButtons();
            });
        }).start();
    }

    private void initViews() {
        avatar = findViewById(R.id.profile_avatar);
        username = findViewById(R.id.profile_username);
        buttonChangeAvatar = findViewById(R.id.button_change_avatar);
        buttonLogout = findViewById(R.id.button_logout);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        toolbar = findViewById(R.id.toolbar);
    }

    private void loadUserData() {
        if (currentUser == null) return;
        username.setText(currentUser.getUsername());
        String avatarPath = currentUser.getAvatarPath();
        if (avatarPath != null && !avatarPath.isEmpty()) {
            if (avatarPath.startsWith("drawable/")) {
                String resName = avatarPath.replace("drawable/", "");
                int resId = getResources().getIdentifier(resName, "drawable", getPackageName());
                if (resId != 0) {
                    avatar.setImageResource(resId);
                } else {
                    avatar.setImageResource(R.drawable.ic_default_avatar);
                }
            } else {
                Glide.with(this).load(new File(avatarPath)).into(avatar);
            }
        } else {
            avatar.setImageResource(R.drawable.ic_default_avatar);
        }
    }

    private void setupTabs() {
        ProfilePagerAdapter adapter = new ProfilePagerAdapter(this, currentUser.getId());
        viewPager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) tab.setText("Оцененные");
            else tab.setText("Хочу посмотреть");
        }).attach();
    }

    private void setupButtons() {
        buttonChangeAvatar.setOnClickListener(v -> showAvatarChangeDialog());
        buttonLogout.setOnClickListener(v -> {
            sessionManager.logout();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void showAvatarChangeDialog() {
        String[] options = {"Выбрать из галереи", "Выбрать стандартный"};
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Сменить аватар")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(gallery, PICK_IMAGE);
                    } else {
                        showDefaultAvatarsDialog();
                    }
                }).show();
    }

    private void showDefaultAvatarsDialog() {
        String[] avatars = {"avatar1", "avatar2", "avatar3", "avatar4", "avatar5"};
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Выберите аватар")
                .setItems(avatars, (dialog, which) -> {
                    String selected = "drawable/" + avatars[which];
                    new Thread(() -> {
                        currentUser.setAvatarPath(selected);
                        db.userDao().update(currentUser);
                        runOnUiThread(() -> loadUserData());
                    }).start();
                }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                new Thread(() -> {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(imageUri);
                        File avatarDir = new File(getFilesDir(), "avatars");
                        if (!avatarDir.exists()) avatarDir.mkdirs();
                        File avatarFile = new File(avatarDir, "user_" + currentUser.getId() + ".jpg");
                        FileOutputStream outputStream = new FileOutputStream(avatarFile);
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = inputStream.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, length);
                        }
                        outputStream.close();
                        inputStream.close();

                        currentUser.setAvatarPath(avatarFile.getAbsolutePath());
                        db.userDao().update(currentUser);
                        runOnUiThread(() -> loadUserData());
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(() -> Toast.makeText(ProfileActivity.this, "Ошибка сохранения", Toast.LENGTH_SHORT).show());
                    }
                }).start();
            }
        }
    }
}