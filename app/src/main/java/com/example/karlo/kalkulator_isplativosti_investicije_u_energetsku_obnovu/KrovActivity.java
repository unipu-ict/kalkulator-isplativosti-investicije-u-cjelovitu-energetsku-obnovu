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

public class KrovActivity extends AppCompatActivity {

    private Button confirm;

    private TextInputEditText EdT_Lambda_st, EdT_Akr, EdT_Guk, EdT_Epotr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_krov);

        EdT_Lambda_st = (TextInputEditText) findViewById(R.id.Lambda_st);
        EdT_Akr = (TextInputEditText) findViewById(R.id.Akr);
        EdT_Guk = (TextInputEditText) findViewById(R.id.Guk);
        EdT_Epotr = (TextInputEditText) findViewById(R.id.Epotr);

        final SharedPreferences sharedPref = getSharedPreferences(MainActivity.globalPreferenceName, MODE_PRIVATE);

        EdT_Lambda_st.setText(sharedPref.getString("EdT_Lambda_st", ""));
        EdT_Akr.setText(sharedPref.getString("EdT_Akr", ""));
        EdT_Guk.setText(sharedPref.getString("EdT_Guk", ""));
        EdT_Epotr.setText(sharedPref.getString("EdT_Epotr", ""));

        confirm = (Button) findViewById(R.id.krovButton);

        confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences(MainActivity.globalPreferenceName, MODE_PRIVATE).edit();
                if(is_full_fill()){
                    // Izracunati rezultat te ga spremiti u polje
                    float rezultat[] = krov_strop_izracun();

                    // Postaviti shared preference
                    editor.putFloat("K_lkrov", rezultat[0]);
                    editor.putFloat("K_Gtranz", rezultat[1]);
                    editor.putFloat("K_Qtranz", rezultat[2]);
                    editor.putBoolean("Krov_popunjeno", true);
                    Toast.makeText(getApplicationContext(),"Forma je točno ispunjena!",Toast.LENGTH_SHORT).show();
                }else{
                    editor.putBoolean("Krov_popunjeno", false);
                    Toast.makeText(getApplicationContext(), "Sva polja moraju biti ispunjena kako bi mogli nastaviti s izračunom!", Toast.LENGTH_LONG).show();
                }

                editor.putString("EdT_Lambda_st", EdT_Lambda_st.getText().toString());
                editor.putString("EdT_Akr", EdT_Akr.getText().toString());
                editor.putString("EdT_Guk", EdT_Guk.getText().toString());
                editor.putString("EdT_Epotr", EdT_Epotr.getText().toString());
                editor.commit();

                Intent i = new Intent(KrovActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    public boolean is_full_fill(){
        if(!MainActivity.isEmpty(EdT_Lambda_st )&& !MainActivity.isEmpty(EdT_Akr )&& !MainActivity.isEmpty(EdT_Guk )&& !MainActivity.isEmpty(EdT_Epotr)) return true;
        else return false;
    }

    public float[] krov_strop_izracun(){
        // Formule za izracun isplativosti ulaganja u strop

        /*
        * lkrov = Debljina izolacije (cm) (Formula)
        * Gdje su:
            lkrov – debljina izolacije krova (cm) (rezultat)
            λnov – novi koeficijent prolaska topline (W/m2K) (Konstanta, 0.01)
            λst – stari koeficijent prolaska topline (W/m2K) -
            Rplu – unutarnji plošni otpor prijelaza topline (m2K/W) (Konstanta, 0.13)
            Rplv – vanjski plošni otpor prijelaza topline (m2K/W) (Konstanta, 0.04)
            Kiz – koeficijent toplinske izolacije stropa/krova (W/mK) (Konstanta, 0.035)
        * */
        float Lambda_st = Float.valueOf(EdT_Lambda_st.getText().toString());
        float lkrov = (float) (((1/0.01)-(1/Lambda_st)-0.09)*0.035); // Izlazna varijabla

        /*
        * Gtranz = Transmisijski gubici (W/K)
        * Gdje je:
            Akr – površina krova/stropa (m2) -
        * */
        float Akr = Float.valueOf(EdT_Akr.getText().toString());
        float Gtranz = (float) ((Lambda_st+0.1)*Akr); // Izlazna varijabla

        /*
        * Qtranz = Transmisijski gubici topline (kWh)
        * Gdje je:
            Guk – ukupni gubici zgrade -
            Epotr  – godišnja potrebna električne energija (kWh) -
        * */
        float Guk = Float.valueOf(EdT_Guk.getText().toString());
        float Epotr = Float.valueOf(EdT_Epotr.getText().toString());
        float Qtranz = (float) (Gtranz/Guk)*Epotr; // Izlazna varijabla

        /* POTREBNO NAPRAVITI SLJEDECE
            λst – stari koeficijent prolaska topline (W/m2K) +
            Akr – površina krova/stropa (m2) -
            Guk – ukupni gubici zgrade -
            Epotr  – godišnja potrebna električne energija (kWh) -
        */
        return new float[] {lkrov, Gtranz, Qtranz};
    }
}
