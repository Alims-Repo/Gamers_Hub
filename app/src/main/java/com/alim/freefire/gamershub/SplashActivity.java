package com.alim.freefire.gamershub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class SplashActivity extends AppCompatActivity {

    int PROGRESS = 75;
    ProgressBar loading;
    ImageView splash_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        loading = findViewById(R.id.loading_progress);

        splash_logo = findViewById(R.id.splash_logo);

        progressChanger();
    }

    private void progressChanger() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (PROGRESS<100) {
                    PROGRESS++;
                    loading.setProgress(PROGRESS);
                    progressChanger();
                } else {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(SplashActivity.this, splash_logo, "app_logo");
                    intent.putExtra("NAME","DATA");
                    startActivity(intent, options.toBundle());
                }
            }
        },50);
    }
}
