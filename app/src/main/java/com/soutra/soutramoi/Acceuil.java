package com.soutra.soutramoi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.text.Normalizer;

public class Acceuil extends AppCompatActivity {
    private LinearLayout ll1,ll2,ll3,ll4;
    int incr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceuil);
        ll2=findViewById(R.id.ll2);
        ll3=findViewById(R.id.ll3);

        ll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String   idpays = MySharePreference.readString(Acceuil.this, "pays", "");
                if (!idpays.isEmpty()){
                    startActivity(new Intent(Acceuil.this,PiecesMainActivity.class));
                    finish();
                }else {
                    showEditTextPopup();
                }
            }
        });
        ll3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String   idpays = MySharePreference.readString(Acceuil.this, "pays", "");
                if (!idpays.isEmpty()){
                    startActivity(new Intent(Acceuil.this,PersonMain.class));
                    finish();
                }else {
                    showEditTextPopup();
                }
            }
        });
    }
    public void showEditTextPopup() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.payspopup, null);
        dialogBuilder.setView(dialogView);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final EditText editTextPopup = dialogView.findViewById(R.id.editTextPopup);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final EditText postal = dialogView.findViewById(R.id.editTextPopup2);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button btnOK = dialogView.findViewById(R.id.btnOK);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        // Configurez le bouton OK pour récupérer le texte de l'EditText
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextPopup.getText().toString().isEmpty()) {
                    editTextPopup.setError("Ce champ est obligatoire");
                    return;
                }
                if (postal.getText().toString().isEmpty()|| !(postal.getText().toString().contains("+"))||postal.getText().length()<2) {
                    postal.setError("Veuillez le saisir correctement");
                    return;
                }
                String pays = editTextPopup.getText().toString();
                String code = postal.getText().toString();
                MySharePreference.writeString(Acceuil.this,"pays",normalizeCityName(pays));
                MySharePreference.writeString(Acceuil.this,"postal",code);
                startActivity(new Intent(Acceuil.this,PiecesMainActivity.class));
                finish();
                alertDialog.dismiss();
                //  addStudentRequest();
            }
        });

        alertDialog.show();
    }
    private String normalizeCityName(String cityName) {
        // Normaliser la chaîne en Unicode NFD et supprimer les caractères diacritiques (accents)
        String normalizedCityName = Normalizer.normalize(cityName, Normalizer.Form.NFD);
        normalizedCityName = normalizedCityName.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        // Convertir en minuscules
        normalizedCityName = normalizedCityName.toLowerCase();

        return normalizedCityName;
    }
}