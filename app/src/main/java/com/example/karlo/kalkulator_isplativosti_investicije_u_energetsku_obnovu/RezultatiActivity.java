package com.example.karlo.kalkulator_isplativosti_investicije_u_energetsku_obnovu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RezultatiActivity extends AppCompatActivity {

    private Button natrag;

    private EditText Z_Qu, Z_Qpu, Z_U, Z_Inv, S_Quinf, S_Qutrans, S_Quk, K_lkrov, K_Gtranz, K_Qtranz, K_Qp_u, K_Qku, R_Lm, R_Pled, R_Inv_ras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rezultati);

        natrag = (Button) findViewById(R.id.natragButton);

        Z_Qu = (EditText) findViewById(R.id.Z_Qu);
        Z_Qpu = (EditText) findViewById(R.id.Z_Qpu);
        Z_U = (EditText) findViewById(R.id.Z_U);
        Z_Inv = (EditText) findViewById(R.id.Z_Inv);
        S_Quinf = (EditText) findViewById(R.id.S_Quinf);
        S_Qutrans = (EditText) findViewById(R.id.S_Qutrans);
        S_Quk = (EditText) findViewById(R.id.S_Quk);
        K_lkrov = (EditText) findViewById(R.id.K_lkrov);
        K_Gtranz = (EditText) findViewById(R.id.K_Gtranz);
        K_Qtranz = (EditText) findViewById(R.id.K_Qtranz);
        K_Qp_u = (EditText) findViewById(R.id.K_Qp_u);
        K_Qku = (EditText) findViewById(R.id.K_Qku);
        R_Lm = (EditText) findViewById(R.id.R_Lm);
        R_Pled = (EditText) findViewById(R.id.R_Pled);
        R_Inv_ras = (EditText) findViewById(R.id.R_Inv_ras);

        SharedPreferences sharedPref = getSharedPreferences(MainActivity.globalPreferenceName, MODE_PRIVATE);
        Z_Qu.setText(Float.toString(sharedPref.getFloat("Z_Qu", -1)));
        Z_Qpu.setText(Float.toString(sharedPref.getFloat("Z_Qpu", -1)));
        Z_U.setText(Float.toString(sharedPref.getFloat("Z_U", -1)));
        Z_Inv.setText(Float.toString(sharedPref.getFloat("Z_Inv", -1)));
        S_Quinf.setText(Float.toString(sharedPref.getFloat("S_Qinf", -1)));
        S_Qutrans.setText(Float.toString(sharedPref.getFloat("S_Qutrans", -1)));
        S_Quk.setText(Float.toString(sharedPref.getFloat("S_Quk", -1)));
        K_lkrov.setText(Float.toString(sharedPref.getFloat("K_lkrov", -1)));
        K_Gtranz.setText(Float.toString(sharedPref.getFloat("K_Gtranz", -1)));
        K_Qtranz.setText(Float.toString(sharedPref.getFloat("K_Qtranz", -1)));
        K_Qp_u.setText(Float.toString(sharedPref.getFloat("Ko_Qp_u", -1)));
        K_Qku.setText(Float.toString(sharedPref.getFloat("Ko_Qku", -1)));
        R_Lm.setText(Float.toString(sharedPref.getFloat("R_Lm", -1)));
        R_Pled.setText(Float.toString(sharedPref.getFloat("R_Pled", -1)));
        R_Inv_ras.setText(Float.toString(sharedPref.getFloat("R_Inv", -1)));

        natrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RezultatiActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}
