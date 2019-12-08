package com.alim.freefire.gamershub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.MenuItem;
import android.widget.TextView;

import com.alim.freefire.gamershub.Adapter.PagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView title = findViewById(R.id.main_title);
        viewPager = findViewById(R.id.view_pager);
        bottomNavigationView = findViewById(R.id.navigation_view);
        viewPager.setAdapter(new PagerAdapter.ViewPagerAdapter(getSupportFragmentManager()));

        TextPaint paint = title.getPaint();
        float width = paint.measureText(title.getText().toString());
        Shader textShader = new LinearGradient(0, 0, width, title.getTextSize(),
                new int[]{getResources().getColor(R.color.colorGradusBlue)
                        ,getResources().getColor(R.color.colorViolet)},
                null, Shader.TileMode.CLAMP);
        title.getPaint().setShader(textShader);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.home:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.trending:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.live:
                                viewPager.setCurrentItem(2);
                                break;
                            case R.id.settings:
                                viewPager.setCurrentItem(3);
                                break;
                        }
                        return true;
                    }
                });
    }
}
