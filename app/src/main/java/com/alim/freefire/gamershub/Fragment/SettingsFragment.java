package com.alim.freefire.gamershub.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.alim.freefire.gamershub.DataBase.AppSettings;
import com.alim.freefire.gamershub.MainActivity;
import com.alim.freefire.gamershub.R;
import com.alim.freefire.gamershub.SplashActivity;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class SettingsFragment extends Fragment {

    Chip auto, on, off;
    AppSettings appSettings;
    ChipGroup darkMode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        appSettings = new AppSettings(getActivity());
        darkMode = rootView.findViewById(R.id.dark_mode);

        auto = rootView.findViewById(R.id.auto);
        on = rootView.findViewById(R.id.on);
        off = rootView.findViewById(R.id.off);

        switch (appSettings.getTheme()) {
            case 0:
                darkMode.check(R.id.auto);
                ChipDisable(auto,on,off);
                break;
            case 1:
                darkMode.check(R.id.on);
                ChipDisable(on,off,auto);
                break;
            case 2:
                darkMode.check(R.id.off);
                ChipDisable(off,auto,on);
                break;
        }

        darkMode.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                switch (i) {
                    case R.id.auto:
                        appSettings.setTheme(0);
                        ChipDisable(auto,on,off);
                        break;
                    case R.id.on:
                        appSettings.setTheme(1);
                        ChipDisable(on,off,auto);
                        break;
                    case R.id.off:
                        appSettings.setTheme(2);
                        ChipDisable(off,auto,on);
                        break;
                }
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Intent i = new Intent(getActivity(), MainActivity.class);
                        i.putExtra("FROM","SETTINGS");
                        startActivity(i);
                        getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        getActivity().finish();
                    }
                }, 200);
            }
        });

        return rootView;
    }
    private void ChipDisable(Chip one,Chip two, Chip three) {
        one.setClickable(false);
        two.setClickable(true);
        three.setClickable(true);
    }
}