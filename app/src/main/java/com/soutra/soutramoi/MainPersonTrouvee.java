package com.soutra.soutramoi;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

        import com.google.firebase.database.*;
import com.soutra.soutramoi.models.PersonEgaree;
import com.soutra.soutramoi.models.PersonTrouvee;

import java.util.ArrayList;
        import java.util.List;

public class MainPersonTrouvee extends AppCompatActivity implements PersonTrouveAdapter.ItemClickListener{
    int incr;
    TextView message,empty;
    ProgressBar loading;
    List<PersonTrouvee> etudiantsList = new ArrayList<>();
    PersonTrouveAdapter adapter;
    RecyclerView recyclerview;
    androidx.appcompat.widget.SearchView recherche;
    String tel;
    @SuppressLint({"MissingInflatedId", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_person_trouvee);
        Toolbar toolbar = findViewById(R.id.toolbar);
        recyclerview = findViewById(R.id.recyclerview1);
        message = findViewById(R.id.message);
        empty = findViewById(R.id.empty);
        recherche = findViewById(R.id.search);
        loading = findViewById(R.id.load);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager( new LinearLayoutManager(MainPersonTrouvee.this));
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

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("person retrouves").child(MySharePreference.readString(MainPersonTrouvee.this, "pays", ""));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot
                        : dataSnapshot.getChildren()) {
                    PersonTrouvee etudiant = snapshot.getValue(PersonTrouvee.class);
                    etudiantsList.add(etudiant);
                }
                if (!etudiantsList.isEmpty()){
                    adapter = new PersonTrouveAdapter(MainPersonTrouvee.this, etudiantsList);
                    recyclerview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    adapter.setClickListener(MainPersonTrouvee.this);
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
        List<PersonTrouvee> filterUser = new ArrayList<>();
        for(PersonTrouvee user:etudiantsList){
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
        getMenuInflater().inflate(R.menu.menu_pretrouve, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.settings) {
            startActivity(new Intent(MainPersonTrouvee.this,PublierPersonRetrouvee.class));
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
            Intent intent = new Intent(this, PersonMain.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        AlertDialog.Builder builder =new AlertDialog.Builder(MainPersonTrouvee.this);
        PersonTrouvee show = (PersonTrouvee) adapter.getItem(position);
        tel=show.getNumero();
        builder.setIcon(R.drawable.call);
        builder.setTitle("Appel urgent");
        builder.setMessage("Voulez-vous vraiment contacter ce numérro ");

        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                makeCAll(show.getNumero());
            }
        });
        builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog =builder.create();
        dialog.show();

    }
    private void makeCAll(String numero) {
        int permissionCheck = ContextCompat.checkSelfPermission(MainPersonTrouvee.this,
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