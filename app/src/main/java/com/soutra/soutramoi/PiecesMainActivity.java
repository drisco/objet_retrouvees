package com.soutra.soutramoi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.soutra.soutramoi.models.Egaree;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.*;
import com.soutra.soutramoi.models.PersonEgaree;

import java.util.ArrayList;
import java.util.List;

public class PiecesMainActivity extends AppCompatActivity {
    RecyclerView recyclerview;
    List<Egaree> etudiantsList = new ArrayList<>();
    AdapterPieceEgaree adapter;
    TextView message,empty;
    String   idpays;
    ProgressBar loading;
    androidx.appcompat.widget.SearchView recherche;


    int incr;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pieces_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        recyclerview = findViewById(R.id.recyclerview);
        message = findViewById(R.id.message);
        recherche = findViewById(R.id.search);
        empty = findViewById(R.id.empty);
        loading = findViewById(R.id.load);
        recyclerview.setHasFixedSize(true);
        recherche.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        recyclerview.setLayoutManager( new LinearLayoutManager(PiecesMainActivity.this));
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        idpays = MySharePreference.readString(PiecesMainActivity.this, "pays", "");

        // Référence à la base de données
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("objets perdus").child(idpays);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Egaree etudiant = snapshot.getValue(Egaree.class);
                    etudiantsList.add(etudiant);
                }
                System.out.println("vdbfhdezhfjrebfjhrfgjherjrehlrg "+etudiantsList.size());
                if (!etudiantsList.isEmpty()){
                    adapter = new AdapterPieceEgaree(PiecesMainActivity.this, etudiantsList);
                    recyclerview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    loading.setVisibility(View.GONE);
                }else{
                    loading.setVisibility(View.GONE);
                    message.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Gérer les erreurs ici
            }
        });




    }

    private void filterList(String newText) {
        List<Egaree> filterUser = new ArrayList<>();
        for(Egaree user:etudiantsList){
            if(user.getVille().toLowerCase().contains(newText.toLowerCase()) ||
                    user.getTitre().toString().contains(newText.toLowerCase()) ){
                filterUser.add(user);
            }
        }
        if (!etudiantsList.isEmpty()){
            if(filterUser.isEmpty()){
                empty.setVisibility(View.VISIBLE);
                adapter.userNotFound(filterUser);

            }else{
                adapter.setFilterUser(filterUser);
                empty.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.objet_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(PiecesMainActivity.this,PublierPiecesEgaree.class));
            finish();
            return true;
        } else if (id == R.id.action_profile) {
            startActivity(new Intent(PiecesMainActivity.this, PieceTrouveeMain.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        incr++;
        if (incr == 1) {
            Intent intent = new Intent(this, Acceuil.class);
            startActivity(intent);
            finish();
        }
    }
}