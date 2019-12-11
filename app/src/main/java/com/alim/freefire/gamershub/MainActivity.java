package com.alim.freefire.gamershub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.MenuItem;
import android.widget.TextView;
import com.alim.freefire.gamershub.Adapter.PagerAdapter;
import com.alim.freefire.gamershub.DataBase.AppSettings;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    TextView title;
    AppSettings appSettings;
    ViewPager viewPager;
    BottomNavigationView bottomNavigationView;

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
        setContentView(R.layout.activity_main);

        title = findViewById(R.id.main_title);
        viewPager = findViewById(R.id.view_pager);
        bottomNavigationView = findViewById(R.id.navigation_view);
        viewPager.setAdapter(new PagerAdapter.ViewPagerAdapter(getSupportFragmentManager()));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                TextColor(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        int pos = 0;
                        switch (menuItem.getItemId()) {
                            case R.id.home:
                                pos = 0;
                                break;
                            case R.id.trending:
                                pos = 1;
                                break;
                            case R.id.live:
                                pos = 2;
                                break;
                            case R.id.settings:
                                pos = 3;
                                break;
                        }
                        viewPager.setCurrentItem(pos);
                        TextColor(pos);
                        return true;
                    }
                });
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        if (bundle.getString("FROM","").equals("SETTINGS"))  {
            viewPager.setCurrentItem(3);
            TextColor(3);
        } else
            TextColor(0);
    }

    private void TextColor(int pos) {
        String text = "";
        switch (pos) {
            case 0:
                text = "Gamers Hub";
                break;
            case 1:
                text = "Trending";
                break;
            case 2:
                text = "Live";
                break;
            case 3:
                text = "Settings";
                break;
        }
        title.setText(text);
        TextPaint paint = title.getPaint();
        float width = paint.measureText(title.getText().toString());
        Shader textShader = new LinearGradient(0, 0, width, title.getTextSize(),
                new int[]{getResources().getColor(R.color.colorGradusBlue)
                        ,getResources().getColor(R.color.colorViolet)},
                null, Shader.TileMode.CLAMP);
        title.getPaint().setShader(textShader);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Exit")
                .setIcon(getResources().getDrawable(R.drawable.ic_garena))
                .setMessage("Sure want to exit ?")
                .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.super.onBackPressed();
                    }
                }).show();
    }
}