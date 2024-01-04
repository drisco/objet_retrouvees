package com.soutra.soutramoi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.soutra.soutramoi.models.PersonEgaree;
import com.soutra.soutramoi.models.Trouvee;



import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class PieceTrouveeMain extends AppCompatActivity implements AdapterPieceTrouvee.ItemClickListener{
    int incr;
    TextView message,empty;
    ProgressBar loading;
    List<Trouvee> etudiantsList = new ArrayList<>();
    AdapterPieceTrouvee adapter;
    RecyclerView recyclerview;
    androidx.appcompat.widget.SearchView recherche;
    String tel;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piece_trouvee_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        recyclerview = findViewById(R.id.recyclerview1);
        message = findViewById(R.id.message);
        recherche = findViewById(R.id.search);
        empty = findViewById(R.id.empty);
        loading = findViewById(R.id.load);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
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

        recyclerview.setLayoutManager( new LinearLayoutManager(PieceTrouveeMain.this));

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("objets retrouves").child(MySharePreference.readString(PieceTrouveeMain.this, "pays", ""));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Trouvee etudiant = snapshot.getValue(Trouvee.class);
                    etudiantsList.add(etudiant);
                }
                System.out.println("vdbfhdezhfjrebfjhrfgjherjrehlrg "+etudiantsList.size());
                if (!etudiantsList.isEmpty()){
                    adapter = new AdapterPieceTrouvee(PieceTrouveeMain.this, etudiantsList);
                    recyclerview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    adapter.setClickListener(PieceTrouveeMain.this);
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
        List<Trouvee> filterUser = new ArrayList<>();
        for(Trouvee user:etudiantsList){
            if(user.getTitre().toLowerCase().contains(newText.toLowerCase()) ||
                    user.getVille().toString().contains(newText.toLowerCase()) ){
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
        getMenuInflater().inflate(R.menu.menu_pretrouve, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.settings) {
            startActivity(new Intent(PieceTrouveeMain.this,PublierPiecesRetrouvee.class));
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
            Intent intent = new Intent(this, PiecesMainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Trouvee show = (Trouvee) adapter.getItem(position);
        Toast.makeText(this, "Aucun", Toast.LENGTH_SHORT).show();
        tel=show.getNumero();
        System.out.println("OHLAHLAH CEST BON CEST BIEN="+tel);
        makeCAll(show.getNumero());

    }
    private void makeCAll(String numero) {
        int permissionCheck = ContextCompat.checkSelfPermission(PieceTrouveeMain.this,
                android.Manifest.permission.CALL_PHONE);
        if (permissionCheck== PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{android.Manifest.permission.CALL_PHONE}, 12);
        }else{
            String dial ="tel:"+numero;
            Uri uri =Uri.parse(dial);
            Intent intent = new Intent(Intent.ACTION_CALL, uri);
            startActivity(intent);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 12) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makeCAll(tel);
                System.out.println("OHLAHLAH CEST BON CEST 858585858BIEN="+tel);
            }else{
                Toast.makeText(this,"permition denied",Toast.LENGTH_SHORT).show();
            }
        }
    }
}