package com.example.karlo.kalkulator_isplativosti_investicije_u_energetsku_obnovu;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import gr.net.maroulis.library.EasySplashScreen;

public class splashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EasySplashScreen config = new EasySplashScreen(splashScreen.this)
                .withFullScreen()
                .withTargetActivity(MainActivity.class)
                .withSplashTimeOut(2500)
                .withBackgroundColor(Color.parseColor("#F3F3F3"))
                .withLogo(R.drawable.logo)
                .withHeaderText("Dobrodosli")
                .withFooterText("Copyright 2018")
                .withAfterLogoText("Kalkulator isplativosti energetske obnove");

        config.getHeaderTextView().setTextColor(Color.parseColor("#696969"));
        config.getHeaderTextView().setPadding(0,50,0,0);
        config.getHeaderTextView().setTextSize(34);

        config.getFooterTextView().setTextColor(Color.parseColor("#696969"));
        config.getFooterTextView().setPadding(0,0,0,20);
        config.getAfterLogoTextView().setTextColor(Color.parseColor("#696969"));
        config.getAfterLogoTextView().setTextSize(20);
        config.getAfterLogoTextView().setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        config.getAfterLogoTextView().setPadding(0,50,0,0);

        View view = config.create();

        setContentView(view);

    }
}
