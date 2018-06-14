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

public class ZidActivity extends AppCompatActivity {

    private TextInputEditText EdT_Lamda_vz, EdT_Azid, EdT_Gt, EdT_Qpotr, EdT_tu, EdT_tv, EdT_h, EdT_fpr, EdT_ce_s, EdT_A;
    private Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zid);

        EdT_Lamda_vz = (TextInputEditText) findViewById(R.id.lambda_vz2);
        EdT_Azid = (TextInputEditText) findViewById(R.id.Azid);
        EdT_Gt = (TextInputEditText) findViewById(R.id.Gt);
        EdT_Qpotr = (TextInputEditText) findViewById(R.id.Qpotr);
        EdT_tu = (TextInputEditText) findViewById(R.id.tu);
        EdT_tv = (TextInputEditText) findViewById(R.id.tv);
        EdT_h = (TextInputEditText) findViewById(R.id.h);
        EdT_fpr = (TextInputEditText) findViewById(R.id.fpr);
        EdT_ce_s = (TextInputEditText) findViewById(R.id.ce_s);
        EdT_A = (TextInputEditText) findViewById(R.id.A);

        final SharedPreferences sharedPref = getSharedPreferences(MainActivity.globalPreferenceName, MODE_PRIVATE);

        EdT_Lamda_vz.setText(sharedPref.getString("EdT_Lamda_vz", ""));
        EdT_Azid.setText(sharedPref.getString("EdT_Azid", ""));
        EdT_Gt.setText(sharedPref.getString("EdT_Gt", ""));
        EdT_Qpotr.setText(sharedPref.getString("EdT_Qpotr", ""));
        EdT_tu.setText(sharedPref.getString("EdT_tu", ""));
        EdT_tv.setText(sharedPref.getString("EdT_tv", ""));
        EdT_h.setText(sharedPref.getString("EdT_h", ""));
        EdT_fpr.setText(sharedPref.getString("EdT_fpr", ""));
        EdT_ce_s.setText(sharedPref.getString("EdT_ce_s", ""));
        EdT_A.setText(sharedPref.getString("EdT_A", ""));

        confirm = (Button) findViewById(R.id.zidButton);

        confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences(MainActivity.globalPreferenceName, MODE_PRIVATE).edit();

                if(is_full_fill()){
                    // Izracunati rezultat te ga spremiti u polje
                    float rezultat[] = vanjski_zid_izracun();

                    // Postaviti shared preference
                    editor.putFloat("Z_Qu", rezultat[0]);
                    editor.putFloat("Z_Qpu", rezultat[1]);
                    editor.putFloat("Z_U", rezultat[2]);
                    editor.putFloat("Z_Inv", rezultat[3]);
                    editor.putBoolean("Zid_popunjeno", true);
                    Toast.makeText(getApplicationContext(),"Forma je točno ispunjena!",Toast.LENGTH_SHORT).show();
                }else{
                    editor.putBoolean("Zid_popunjeno", false);
                    Toast.makeText(getApplicationContext(),"Sva polja moraju biti ispunjena kako bi mogli nastaviti s izračunom!",Toast.LENGTH_LONG).show();
                }

                editor.putString("EdT_Lamda_vz", EdT_Lamda_vz.getText().toString());
                editor.putString("EdT_Azid", EdT_Azid.getText().toString());
                editor.putString("EdT_Gt", EdT_Gt.getText().toString());
                editor.putString("EdT_Qpotr", EdT_Qpotr.getText().toString());
                editor.putString("EdT_tu", EdT_tu.getText().toString());
                editor.putString("EdT_tv", EdT_tv.getText().toString());
                editor.putString("EdT_h", EdT_h.getText().toString());
                editor.putString("EdT_fpr", EdT_fpr.getText().toString());
                editor.putString("EdT_ce_s", EdT_ce_s.getText().toString());
                editor.putString("EdT_A", EdT_A.getText().toString());
                editor.commit();

                Intent i = new Intent(ZidActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    public boolean is_full_fill(){
        if(!MainActivity.isEmpty(EdT_Lamda_vz) && !MainActivity.isEmpty(EdT_Azid) && !MainActivity.isEmpty(EdT_Gt) && !MainActivity.isEmpty(EdT_Qpotr) && !MainActivity.isEmpty(EdT_tu) && !MainActivity.isEmpty(EdT_tv) && !MainActivity.isEmpty(EdT_h) && !MainActivity.isEmpty(EdT_fpr) && !MainActivity.isEmpty(EdT_ce_s) && !MainActivity.isEmpty(EdT_A)) return true;
        else return false;
    }



    public float[] vanjski_zid_izracun(){
        // Formule za proracun isplativosti ulaganja u zid

        /*
        *  lzid = Debljina toplinske izolacije (cm) (formula)
        *  Gdje su:
            lzid – debljina toplinske izolacije (cm) (Rezutat)
            λvz1 – koeficijent prolaska topline vanjskog zida prije izolacije (W/m2K) (Konstanta, 0.15) +
            λvz2 - koeficijent prolaska topline vanjskog zida nakon izolacije (W/m2K) -
            Rplu – Unutarnji plošni otpor prijelaza topline (m2K/W) (Konstanta, 0.13)
            Rplv – Vanjski plošni otpor prijelaza topline (m2K/W) (Konstanta, 0.04)
            Ui – Koeficijent toplinske izolacije zida (W/mK) (Konstanta, 0.035)
        * */


        float lambda_vz2 = Float.valueOf(EdT_Lamda_vz.getText().toString());
        // Glavna formula
        float lzid = (float) (((1/0.15)-(1/lambda_vz2)-0.09)*0.035);


        /*
        * Gtr = Transmisijski gubitak zida (W/K) (Formula)
        * Gdje su:
            Azid – površina vanjskog zida (m2) -
            Gtr - transmisijski gubitak zida (W/K) (Rezultat)
        * */
        float Azid = Float.valueOf(EdT_Azid.getText().toString());
        float Gtr = (float) (0.25*Azid);

        /*
        * Qvz = Gubitak topline kroz zid (kWh) (Formula)
        * Gdje su:
            Qvz - Gubitak topline kroz zid (kWh) (Rezultat)
            Gtr
            Gt – Zbrojeni toplinski gubici u objektu (W/K) -
            Qpotr - Teoretska godišnja potrebna energija (kWh) -
        */
        float Gt = Float.valueOf(EdT_Gt.getText().toString());
        float Qpotr = Float.valueOf(EdT_Qpotr.getText().toString());
        float Qvz = (float) (Gtr/Gt)*Qpotr;

        /*
        *  Qvzt = Teoretski gubitak topline kroz zid (kWh)
            Gdje su:
            Qvzt – Teoretski gubitak topline kroz zid (kWh) (Rezultat)
            tu – predviđena unutrašnja temperatura (℃) -
            tv - Prosječna vanjska temperatura u mjesecima u kojima se grije (℃) -
            h – broj radnih sati godišnje (h) -
        * */
        float tu = Float.valueOf(EdT_tu.getText().toString());
        float tv = Float.valueOf(EdT_tv.getText().toString());
        float h = Float.valueOf(EdT_h.getText().toString());
        float Qvzt = (float) Gt*(tu-tv)*h;

        /*
        * Qu - Ušteda energije (kWh)(Formula)
        * Gdje je:
  (Koristiti iznad)Qu – ušteda energije (kWh)(Rezultat)
        *
        */
        float Qu = (float) ((1-(0.25/(lambda_vz2+0.1)))*Qvzt); // Izlazna varijabla

        /*
            Qpu = Ušteda primarne energije (kWh)
             Gdje su:
            - fpr – faktor primarne energije -
            - Qpu – Ušteda primarne energije (kWh) (Rezultat)
        */
        float fpr = Float.valueOf(EdT_fpr.getText().toString());
        float Qpu = (float) fpr * Qu; // Izlazna varijabla

        /*
        *  U = Ušteda (kn) (Formula)
        *  Gdje su:
            - U – ušteda (kn) (Rezultat)
            - ce,s – Cijena energenta za grijanje (kn/kWh) -
        * */
        float ce_s = Float.valueOf(EdT_ce_s.getText().toString());
        float U = (float) ce_s*Qu; // Izlazna varijabla

        /*
        * Inv = Investicija (kn) (Formula)
        *   - Inv – investicija (kn)(Rezultat)
            - A – površina izolacije () -
            - ci  - prosječna cijena izolacije (350 kn/m2) -/+ (Prosjek je u zagradi)
            tu – predviđena unutrašnja temperatura (℃) -
            tv - Prosječna vanjska temperatura u mjesecima u kojima se grije (℃) -
            h – broj radnih sati godišnje (h) -
        */
        float A = Float.valueOf(EdT_A.getText().toString());
        float Inv = (float) A*350; // Izlazna varijabla

        /* ?
         * JPP = Jedinstveni period povrata (god) (Formula)
         * */

        /* ?
        * eco2 = Smanjenje emisije CO2 (kg/god) (Formula)
        * Gdje je:
            - eco2 – godišnje smanjenje emisije CO2 (kg/god) (Rezultat)
            - ee – koeficijent emisije CO2 energenta za grijanje (kg/kWh) -
        * */
        return new float[] {Qu, Qpu, U, Inv};
    }
}
