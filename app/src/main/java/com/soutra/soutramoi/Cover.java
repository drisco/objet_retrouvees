package com.soutra.soutramoi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Cover extends AppCompatActivity {
    Button btnNext;
    int incr;
    String   idpays;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cover);
        idpays = MySharePreference.readString(Cover.this, "pays", "");
        btnNext=findViewById(R.id.btnNext);

        if (!idpays.isEmpty()){
            startActivity(new Intent(Cover.this, Acceuil.class));
            finish();
        }
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Cover.this, Acceuil.class));
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {

        incr++;
        if (incr == 2) {
            super.onBackPressed();
        }
    }
}