package com.example.karlo.kalkulator_isplativosti_investicije_u_energetsku_obnovu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class RasvjetaActivity extends AppCompatActivity {

    private TextInputEditText EdT_Pz, EdT_Z, EdT_CLED;
    Spinner vrstaSijalice;

    private Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rasvjeta);

        EdT_Pz = (TextInputEditText) findViewById(R.id.Pz);
        EdT_Z = (TextInputEditText) findViewById(R.id.Z);
        EdT_CLED = (TextInputEditText) findViewById(R.id.CLED);

        vrstaSijalice = (Spinner) findViewById(R.id.spinnerRasvjeta);

        final SharedPreferences sharedPref = getSharedPreferences(MainActivity.globalPreferenceName, MODE_PRIVATE);

        EdT_Pz.setText(sharedPref.getString("EdT_Pz", ""));
        EdT_Z.setText(sharedPref.getString("EdT_Z", ""));

        EdT_CLED.setText(sharedPref.getString("EdT_CLED", ""));

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(RasvjetaActivity.this,
                android.R.layout.simple_expandable_list_item_1, getResources().getStringArray(R.array.Sijalice));

        myAdapter.setDropDownViewResource(R.layout.spin_item);
        vrstaSijalice.setAdapter(myAdapter);
        vrstaSijalice.setSelection(sharedPref.getInt("vrstaSijaliceSpinner",0));


        confirm = (Button) findViewById(R.id.rasvjetaButton);

        confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                SharedPreferences.Editor editor = getSharedPreferences(MainActivity.globalPreferenceName, MODE_PRIVATE).edit();

                if(is_full_fill()){
                    // Izracunati rezultat te ga spremiti u polje
                    float rezultat[] = zamjena_rasvjete_izracun();

                    // Postaviti shared preference
                    editor.putFloat("R_Lm", rezultat[0]);
                    editor.putFloat("R_Pled", rezultat[1]);
                    editor.putFloat("R_Inv", rezultat[2]);
                    editor.putBoolean("Rasvjeta_popunjeno", true);

                    Toast.makeText(getApplicationContext(),"Forma je točno ispunjena!",Toast.LENGTH_SHORT).show();
                }else{
                    editor.putBoolean("Rasvjeta_popunjeno", false);
                    Toast.makeText(getApplicationContext(), "Sva polja moraju biti ispunjena kako bi mogli nastaviti s izračunom!", Toast.LENGTH_LONG).show();
                }

                editor.putString("EdT_Pz", EdT_Pz.getText().toString());
                editor.putString("EdT_Z", EdT_Z.getText().toString());
                editor.putString("EdT_CLED", EdT_CLED.getText().toString());
                editor.putInt("vrstaSijaliceSpinner", vrstaSijalice.getSelectedItemPosition());
                editor.commit();

                Intent i = new Intent(RasvjetaActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    public boolean is_full_fill(){
        if(!MainActivity.isEmpty(EdT_Pz) && !MainActivity.isEmpty(EdT_Z) && !MainActivity.isEmpty(EdT_CLED)) return true;
        return false;
    }

    public float[] zamjena_rasvjete_izracun (){
        // Formule za proracun isplativosti ulaganja u rasvjetu

        /*
        * Eport = Stara potrošnja električne energije (kWh) (F)
        * Gdje su:
            Pz – snaga zastarjele rasvjete (kW) -
            hs – broj radnih sati sijalica (h) (Konstanta, 750)
        * */
        float Pz = Float.valueOf(EdT_Pz.getText().toString());
        float Eport = (float) Pz * 750;



        /*
        * Lm = Potrebni svjetlosni tok za osvjetljenje prostorija (lm)(F)(Iskoristiti samo jednu)
        * Gdje su:
            Lm – potrebni svjetlosni tok (lm) (Rezultat)
            Z – snaga postojećih sijalica sa žarnom niti (kW) -
            SIz – svjetlosna iskoristivost sijalica sa žarnom niti (lm/kW) (konstanta,17000)
            Hg – snaga postojećih živinih sijalica (kW) -
            SIhg – svjetlosna iskoristivost živinih sijalica (lm/kW)(konstanta, 40000)
            F – snaga postojećih fluo T8 sijalica (kW) -
            SIf – svjetlosna iskoristivost fluo T8 sijalica (lm/kW)(konstanta, 90000)
        */
        float Lm=0; // Izlazna varijabla
        float Z;
        if(vrstaSijalice.getSelectedItem().toString().equals("Žarulja sa žarnom niti")){
            Z = Float.valueOf(EdT_Z.getText().toString());
            Lm = (float) Z*17000;
        }else if(vrstaSijalice.getSelectedItem().toString().equals("Sijalica sa živom")){
            Z = Float.valueOf(EdT_Z.getText().toString());
            Lm = (float) Z*40000;
        }else if(vrstaSijalice.getSelectedItem().toString().equals("Fluo T8 sijalica")){
            Z = Float.valueOf(EdT_Z.getText().toString());
            Lm = (float)Z*90000;
        }
        /*
        * Pled = Potrebna snaga nove LED rasvjete (kW)
        * Gdje su:
            Pled – potrebna snaga nove LED rasvjete (kW) (rezultat)
            SIled-svjetlosna isoristivost LED rasvjete (lm/kW)(konstanta,94000)
        */
        float Pled = (Lm/94000); // Izlazna varijabla
        /*
         * Epotr_n = Nova potrošnja električne energije (kW)
         * hs - broj radnih sati (ima prije)
         */
        float Epotr_n = Pled*750;

        /*
         * Inv = Investicija (kn)
         * Gdje je:
         * CLED – Cijena led rasvjete (kn/kW) -
         * */
        float Cled = Float.valueOf(EdT_CLED.getText().toString());
        float Inv = Pled * Cled; // Izlazna varijabla

        return new float[] {Lm, Pled, Inv};
    }
}