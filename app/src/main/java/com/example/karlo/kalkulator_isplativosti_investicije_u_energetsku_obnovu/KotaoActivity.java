package com.example.karlo.kalkulator_isplativosti_investicije_u_energetsku_obnovu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class KotaoActivity extends AppCompatActivity {

    private Button confirm;

    private TextInputEditText EdT_Qgr_pot, EdT_hk, EdT_Fpr_st, EdT_Fpr_nov, EdT_Ce_s, EdT_Ce_n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kotao);

        EdT_Qgr_pot = (TextInputEditText) findViewById(R.id.Qgr_pot);
        EdT_hk = (TextInputEditText) findViewById(R.id.hk);
        EdT_Fpr_st = (TextInputEditText) findViewById(R.id.Fpr_st);
        EdT_Fpr_nov = (TextInputEditText) findViewById(R.id.Fpr_nov);
        EdT_Ce_s = (TextInputEditText) findViewById(R.id.ce_s);
        EdT_Ce_n = (TextInputEditText) findViewById(R.id.ce_n);


        final SharedPreferences sharedPref = getSharedPreferences(MainActivity.globalPreferenceName, MODE_PRIVATE);

        EdT_Qgr_pot.setText(sharedPref.getString("EdT_Qgr_pot", ""));
        EdT_hk.setText(sharedPref.getString("EdT_hk", ""));
        EdT_Fpr_st.setText(sharedPref.getString("EdT_Fpr_st", ""));
        EdT_Fpr_nov.setText(sharedPref.getString("EdT_Fpr_nov", ""));
        EdT_Ce_s.setText(sharedPref.getString("EdT_Ce_s", ""));
        EdT_Ce_n.setText(sharedPref.getString("EdT_Ce_n", ""));


        confirm = (Button) findViewById(R.id.kotaoButton);

        confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                SharedPreferences.Editor editor = getSharedPreferences(MainActivity.globalPreferenceName, MODE_PRIVATE).edit();

                if(is_full_fill()){
                    // Izracunati rezultat te ga spremiti u polje
                    float rezultat[] = zamjena_kotla_za_grijanje_izracun();

                    // Postaviti shared preference
                    editor.putFloat("Ko_Qp_u", rezultat[0]);
                    editor.putFloat("Ko_Qku", rezultat[1]);
                    editor.putBoolean("Kotao_popunjeno", true);
                    Toast.makeText(getApplicationContext(),"Forma je točno ispunjena!",Toast.LENGTH_SHORT).show();
                }else{
                    editor.putBoolean("Kotao_popunjeno", false);
                    Toast.makeText(getApplicationContext(), "Sva polja moraju biti ispunjena kako bi mogli nastaviti s izračunom!", Toast.LENGTH_LONG).show();
                }
                editor.putString("EdT_Qgr_pot", EdT_Qgr_pot.getText().toString());
                editor.putString("EdT_hk", EdT_hk.getText().toString());
                editor.putString("EdT_Fpr_st", EdT_Fpr_st.getText().toString());
                editor.putString("EdT_Fpr_nov", EdT_Fpr_nov.getText().toString());
                editor.putString("EdT_Ce_s", EdT_Ce_s.getText().toString());
                editor.putString("EdT_Ce_n", EdT_Ce_n.getText().toString());
                editor.commit();

                Intent i = new Intent(KotaoActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }


    public boolean is_full_fill(){
        if(!MainActivity.isEmpty(EdT_Qgr_pot) && !MainActivity.isEmpty(EdT_hk) && !MainActivity.isEmpty(EdT_Fpr_st) && !MainActivity.isEmpty(EdT_Fpr_nov)
                &&  !MainActivity.isEmpty(EdT_Ce_s) && !MainActivity.isEmpty(EdT_Ce_n)) return true;
        else return false;
    }

    public float[] zamjena_kotla_za_grijanje_izracun () {
        // Formule za izracun isplativosti ulaganja u kotao

        /*
        * Pkot - Potrebna snaga kotla (kW)
        * Gdje su:
            Pkot – potrebna snaga kotla (kW) (Rezultat)
            Qgr,pot – potrebna toplinska energija nakon sanacije (kWh) -
            hk – broj radnih sati kotla godišnje (h) -
        * */
        float Qgr_pot = Float.valueOf(EdT_Qgr_pot.getText().toString());
        float hk = Float.valueOf(EdT_hk.getText().toString());
        float Pkot = (float) Qgr_pot/hk;

        /*
        * Qp,u = Ušteda primarne energije (kWh)
        * Gdje su:
            Fpr,st – faktor primarne energije starog energenta -
            Fpr,nov – faktor primarne energije novog energenta -
        * */
        float Fpr_st = Float.valueOf(EdT_Fpr_st.getText().toString());
        float Fpr_nov = Float.valueOf(EdT_Fpr_nov.getText().toString());
        float Qp_u = (float) Qgr_pot*(Fpr_st-Fpr_nov); // Izlazna varijabla

        /*
        * Qu = Ušteda (kn)
        * Gdje su:
            ce,s – cijena starog energenta (kn/kWh) -
            ce,n – cijena novog energenta (kn/kWh) -
        * */
        float Ce_s = Float.valueOf(EdT_Ce_s.getText().toString());
        float Ce_n = Float.valueOf(EdT_Ce_n.getText().toString());
        float U = (float) Qgr_pot*(Ce_s - Ce_n); // Izlazna varijabla

        return new float[] {Qp_u, U};
    }
}
