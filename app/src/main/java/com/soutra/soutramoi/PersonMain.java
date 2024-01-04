package com.soutra.soutramoi;

        import android.annotation.SuppressLint;
        import android.content.Intent;
        import android.support.annotation.NonNull;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.ProgressBar;
        import android.widget.TextView;

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

public class PersonMain extends AppCompatActivity {
    RecyclerView recyclerview;
    List<PersonEgaree> etudiantsList = new ArrayList<>();
    AdapterPersonPerdue adapter;
    TextView message,empty;
    String   idpays;
    ProgressBar loading;
    androidx.appcompat.widget.SearchView recherche;


    int incr;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        recyclerview = findViewById(R.id.recyclerview);
        message = findViewById(R.id.message);
        empty = findViewById(R.id.empty);
        loading = findViewById(R.id.load);
        recherche = findViewById(R.id.search);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager( new LinearLayoutManager(PersonMain.this));
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


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
        idpays = MySharePreference.readString(PersonMain.this, "pays", "");

        // Référence à la base de données
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("person perdus").child(idpays);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PersonEgaree etudiant = snapshot.getValue(PersonEgaree.class);
                    etudiantsList.add(etudiant);
                }
                if (!etudiantsList.isEmpty()){
                    adapter = new AdapterPersonPerdue(PersonMain.this, etudiantsList);
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
// LA FONCTION DE RECHERCHE
    private void filterList(String newText) {
        List<PersonEgaree> filterUser = new ArrayList<>();
        for(PersonEgaree user:etudiantsList){
            if(user.getLieuDisparition().toLowerCase().contains(newText.toLowerCase()) ||
                    user.getNomComplet().toString().contains(newText.toLowerCase()) ){
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
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(PersonMain.this, PublierPersonPerdue.class));
            finish();
            return true;
        } else if (id == R.id.action_profile) {
            startActivity(new Intent(PersonMain.this, MainPersonTrouvee.class));
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