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

public class StolarijaActivity extends AppCompatActivity {

    private Button confirm;

    private TextInputEditText EdT_Vgr, EdT_p, EdT_Cp, EdT_n, EdT_Guk, EdT_Epotr_topl, EdT_Ast, EdT_Lambda_s, EdT_np;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stolarija);

        EdT_Vgr = (TextInputEditText) findViewById(R.id.Vgr);
        EdT_p = (TextInputEditText) findViewById(R.id.ρ);
        EdT_Cp = (TextInputEditText) findViewById(R.id.Cp);
        EdT_n = (TextInputEditText) findViewById(R.id.n);
        EdT_Guk = (TextInputEditText) findViewById(R.id.Guk);
        EdT_Epotr_topl = (TextInputEditText) findViewById(R.id.Epotr_topl);
        EdT_Ast = (TextInputEditText) findViewById(R.id.Ast);
        EdT_Lambda_s = (TextInputEditText) findViewById(R.id.Lambda_s);
        EdT_np = (TextInputEditText) findViewById(R.id.np);

        final SharedPreferences sharedPref = getSharedPreferences(MainActivity.globalPreferenceName, MODE_PRIVATE);

        EdT_Vgr .setText(sharedPref.getString("EdT_Vgr", ""));
        EdT_p .setText(sharedPref.getString("EdT_p", ""));
        EdT_Cp .setText(sharedPref.getString("EdT_Cp", ""));
        EdT_n .setText(sharedPref.getString("EdT_n", ""));
        EdT_Guk .setText(sharedPref.getString("EdT_Guk", ""));
        EdT_Epotr_topl .setText(sharedPref.getString("EdT_Epotr_topl", ""));
        EdT_Ast .setText(sharedPref.getString("EdT_Ast", ""));
        EdT_Lambda_s .setText(sharedPref.getString("EdT_Lambda_s", ""));
        EdT_np .setText(sharedPref.getString("EdT_np", ""));

        confirm = (Button) findViewById(R.id.stolarijaButton);

        confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences(MainActivity.globalPreferenceName, MODE_PRIVATE).edit();

                if(is_full_fill()){
                    // Izracunati rezultat te ga spremiti u polje
                    float rezultat[] = vanjska_stolarija_izracun();

                    // Postaviti shared preference

                    editor.putFloat("S_Qinf", rezultat[0]);
                    editor.putFloat("S_Qutrans", rezultat[1]);
                    editor.putFloat("S_Quk", rezultat[2]);
                    editor.putBoolean("Stolarija_popunjeno", true);
                    Toast.makeText(getApplicationContext(),"Forma je točno ispunjena!",Toast.LENGTH_SHORT).show();
                }else{
                    editor.putBoolean("Stolarija_popunjeno", false);
                    Toast.makeText(getApplicationContext(), "Sva polja moraju biti ispunjena kako bi mogli nastaviti s izračunom!", Toast.LENGTH_LONG).show();
                }

                editor.putString("EdT_Vgr", EdT_Vgr.getText().toString());
                editor.putString("EdT_p", EdT_p.getText().toString());
                editor.putString("EdT_Cp", EdT_Cp.getText().toString());
                editor.putString("EdT_n", EdT_n.getText().toString());
                editor.putString("EdT_Guk", EdT_Guk.getText().toString());
                editor.putString("EdT_Epotr_topl", EdT_Epotr_topl.getText().toString());
                editor.putString("EdT_Ast", EdT_Ast.getText().toString());
                editor.putString("EdT_Lambda_s", EdT_Lambda_s.getText().toString());
                editor.putString("EdT_np", EdT_np.getText().toString());
                editor.commit();

                Intent i = new Intent(StolarijaActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    public boolean is_full_fill(){
        if(!MainActivity.isEmpty(EdT_Vgr)&& !MainActivity.isEmpty(EdT_p) && !MainActivity.isEmpty(EdT_Cp) && !MainActivity.isEmpty(EdT_n) && !MainActivity.isEmpty(EdT_Guk)&&
        !MainActivity.isEmpty(EdT_Epotr_topl)&& !MainActivity.isEmpty(EdT_Ast)&& !MainActivity.isEmpty(EdT_Lambda_s)&& !MainActivity.isEmpty(EdT_np))return true;
        else return false;
    }

    public float[] vanjska_stolarija_izracun(){
        // Formula za izracun isplativosti u vanjsku stolariju

        /*
        *   Ginf = Ventilacijski gubitak  – infiltracija (W/K) (Formula)
        *   Gdje su:
            Ginf – ventilacijski gubitak zbog infiltracije (W/K) (rezultat)
            Vgr – volumen grijanog zraka -
            ρ – gustoća zraka (kg/m3) -
            Cp – Specifični toplinski kapacitet zraka (J/kgK) -
            n – broj izmjena zbog infiltracije u satu  (h-1) -
        */
        float Vgr = Float.valueOf(EdT_Vgr.getText().toString());
        float p = Float.valueOf(EdT_p.getText().toString());
        float Cp = Float.valueOf(EdT_Cp.getText().toString());
        float n = Float.valueOf(EdT_n.getText().toString());
        float Ginf = (float) (Vgr*p*Cp*n)/3600;

        /*
        * Qinf = Ventilacijski gubitak energije – infiltracija (kWh)
        * Gdje su:
            Qinf – ventilacijski gubitak energije zbog infiltracije (kWh) (Rezultat)
            Guk – ukupni toplinski gubici zgrade (W/K) -
            Epotr,topl – ukupno potrebna toplinska energija (kWh) -
            Ast – površina stolarije (m2) +
        */

        float Guk = Float.valueOf(EdT_Guk.getText().toString());
        float Epotr_topl = Float.valueOf(EdT_Epotr_topl.getText().toString());
        float Ast = Float.valueOf(EdT_Ast.getText().toString());
        float Qinf = (float) (Ginf/Guk)*Epotr_topl*Ast; // Izlazna varijabla

        /*
        *  Gtrans = Transmisijski gubici (W/K)
        *  Gdje je:
            Gtrans – transmisijski gubici (W/K) (Rezultat)
            λs – Trenutni koeficijent prolaska topline vanjske stolarije (W/m2K) +
        */
        float Lambda_s = Float.valueOf(EdT_Lambda_s.getText().toString());
        float Gtrans = (float) Lambda_s*Ast;

        /*
         * Transmisijski gubici energije (kWh)
         * Epotr -
         * */

        float Qtrans = (float) (Gtrans/Guk)*Epotr_topl*Ast;

        /*
        * Quinf = Ušteda energije zbog smanjenja infiltracije (kWh)
        * Gdje su:
            Quinf – ušteda energije zbog smanjenja infiltracije (kWh)(Rezultat)
            n – broj izmjena zraka zbog infiltracije prije zamjene stolarije (h-1) -
            np – broj izmjena zbog infiltracije u satu nakon zamjene stolarije (h-1) -
        */
        float np = Float.valueOf(EdT_np.getText().toString());
        float Quinf = (float) (1-(n/np))*Qinf;


        /*
        * Qutrans = Ušteda energije zbog smanjenja transmisije (kWh)
        * Gdje je:
            Qutrans – ušteda energije zbog smanjenja transmisije (kWh)
            λn – Novi koeficijent prolaska topline vanjske stolarije (W/m2K) (Konstanta,1.2)
            λs – Trenutni koeficijent prolaska topline vanjske stolarij -
        * */
        float Qutrans = (float) ((1-(1.5/Lambda_s))*Qtrans); // Izlazna varijabla


        /*
         * Quk = Ukupna ušteda energije (kWh)
         * ---
         * Ušteda primarne energije, financijska ušteda, investicija, JPP i smanjenje emsije CO2 računaju se kao za vanjski zid.
         * */
        float Quk = (float) Quinf + Qutrans; // Izlazna varijabla

        return new float[] {Qinf, Qutrans, Quk};
    }
}
