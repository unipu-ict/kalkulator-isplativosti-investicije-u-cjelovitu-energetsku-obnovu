package com.example.karlo.kalkulator_isplativosti_investicije_u_energetsku_obnovu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static String globalPreferenceName = "com.karlo.izracun";

    private CardView zidCard, stolarijaCard, kotaoCard, krovCard, rasvjetaCard, rezultatCard;

    private boolean zid = false, stolarija = false, kotao = false, krov = false, rasvjeta = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // defining Cards
        zidCard = (CardView) findViewById(R.id.czid);
        stolarijaCard = (CardView) findViewById(R.id.cstolarija);
        krovCard = (CardView) findViewById(R.id.ckrov);
        kotaoCard = (CardView) findViewById(R.id.ckotao);
        rasvjetaCard = (CardView) findViewById(R.id.crasvjeta);
        rezultatCard = (CardView) findViewById(R.id.crezultat);

        // Add listener to cards
        zidCard.setOnClickListener(this);
        stolarijaCard.setOnClickListener(this);
        krovCard.setOnClickListener(this);
        kotaoCard.setOnClickListener(this);
        rasvjetaCard.setOnClickListener(this);
        rezultatCard.setOnClickListener(this);

        SharedPreferences sharedPrefs = getSharedPreferences(globalPreferenceName, MODE_PRIVATE);
        zid = sharedPrefs.getBoolean("Zid_popunjeno",false);
        stolarija = sharedPrefs.getBoolean("Stolarija_popunjeno", false);
        krov  = sharedPrefs.getBoolean("Krov_popunjeno", false);
        kotao= sharedPrefs.getBoolean("Kotao_popunjeno", false);
        rasvjeta = sharedPrefs.getBoolean("Rasvjeta_popunjeno", false);

        if (zid) zidCard.setCardBackgroundColor(Color.parseColor("#95a5a6"));
        else zidCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        if (stolarija) stolarijaCard.setCardBackgroundColor(Color.parseColor("#95a5a6"));
        else stolarijaCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        if (krov) krovCard.setCardBackgroundColor(Color.parseColor("#95a5a6"));
        else krovCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        if (kotao) kotaoCard.setCardBackgroundColor(Color.parseColor("#95a5a6"));
        else kotaoCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        if (rasvjeta) rasvjetaCard.setCardBackgroundColor(Color.parseColor("#95a5a6"));
        else rasvjetaCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
    }

    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()){
            case R.id.czid : i = new Intent(this, ZidActivity.class);startActivity(i); break;
            case R.id.cstolarija : i = new Intent(this, StolarijaActivity.class);startActivity(i); break;
            case R.id.ckrov : i = new Intent(this, KrovActivity.class);startActivity(i); break;
            case R.id.ckotao : i = new Intent(this, KotaoActivity.class);startActivity(i); break;
            case R.id.crasvjeta : i = new Intent(this, RasvjetaActivity.class);startActivity(i); break;
            case R.id.crezultat :
                if(zid && stolarija && kotao && krov && rasvjeta){
                    i = new Intent(this, RezultatiActivity.class);
                    startActivity(i);
                }else{
                    Toast.makeText(getApplicationContext(),"Sve aktivnosti moraju biti popunjene da biste mogli vidjeti konacan rezultat!", Toast.LENGTH_LONG).show();
                }

                break;
            default : break;
        }
    }

    public static boolean isEmpty(TextInputEditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
}