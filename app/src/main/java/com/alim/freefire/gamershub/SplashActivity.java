package com.alim.freefire.gamershub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.alim.freefire.gamershub.DataBase.AppSettings;

public class SplashActivity extends AppCompatActivity {

    int PROGRESS = 0;
    ProgressBar loading;
    boolean pause = false;
    ImageView splash_logo;
    AppSettings appSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        appSettings = new AppSettings(this);
        if (appSettings.getTheme()==1) {
            setTheme(R.style.AppThemeDark);
        } else if (appSettings.getTheme()==2) {
            setTheme(R.style.AppTheme);
        } else {
            switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration.UI_MODE_NIGHT_YES:
                    setTheme(R.style.AppThemeDark);
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                    setTheme(R.style.AppTheme);
                    break;
            }
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        loading = findViewById(R.id.loading_progress);

        splash_logo = findViewById(R.id.splash_logo);

        progressChanger();
    }

    private void progressChanger() {
        final Handler handler = new Handler();
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
        },10);
    }

    @Override
    protected void onPause() {
        super.onPause();
        pause = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (pause) {
            //finish();
        }
    }
}
