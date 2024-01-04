package com.soutra.soutramoi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
        import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
        import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
        import java.util.HashMap;
        import java.util.Locale;
        import java.util.Map;

public class PublierPersonPerdue extends AppCompatActivity {
    int incr;
    EditText titre,descr,nom,numero,ville,date;
    Bitmap captureImage;
    Uri uri;
    ImageView imageViewSelectedImage,pickerdate;
    byte[] imageData;
    ProgressDialog progressDialog;
    Button confirmer;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publier_person_perdue);
        titre = findViewById(R.id.nomcomplet);
        imageViewSelectedImage =findViewById(R.id.image);
        progressDialog = new ProgressDialog(PublierPersonPerdue.this);

/*
        imageViewSelectedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder =new AlertDialog.Builder(PublierPersonPerdue.this);
                String cond1="admin";
                String pass="pass012";

                if (cond1.equals("admin") && pass.equals("pass012")){
                    builder.setIcon(R.drawable._person);
                    builder.setTitle("Valider");
                    builder.setMessage("votre condition est correct !!!!");
                }else{
                    builder.setIcon(R.drawable._person);
                    builder.setTitle("Erreur");
                    builder.setMessage("votre condition est incorrecte !!!!");
                }
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog dialog =builder.create();
                dialog.show();
            }
        });
*/



        descr = findViewById(R.id.descr);
        pickerdate = findViewById(R.id.pickerdate);
        nom = findViewById(R.id.nomdubien);
        numero = findViewById(R.id.numdubien);
        ville = findViewById(R.id.local);
        date = findViewById(R.id.date);
        confirmer = findViewById(R.id.buttonPublish);
        int permissionCheck = ContextCompat.checkSelfPermission(PublierPersonPerdue.this,
                android.Manifest.permission.CAMERA);
        if (permissionCheck== PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 12);
        }
        pickerdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Afficher le DatePicker lors du clic sur le bouton calendrier
                showDatePicker(date);
            }
        });
        confirmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Traitement en cours..."); // Message à afficher
                progressDialog.setCancelable(false); // Empêche la fermeture de la boîte de dialogue en dehors du code
                progressDialog.show();
                if (titre.getText().toString().isEmpty()) {
                    progressDialog.dismiss();
                    titre.setError("Ce champ est obligatoire.");
                    return;
                }
                if (descr.getText().toString().isEmpty()) {
                    progressDialog.dismiss();
                    descr.setError("Ce champ est obligatoire.");
                    return;
                }
                if (nom.getText().toString().isEmpty()) {
                    progressDialog.dismiss();
                    nom.setError("Ce champ est obligatoire.");
                    return;
                }
                if (ville.getText().toString().isEmpty()) {
                    progressDialog.dismiss();
                    ville.setError("Ce champ est obligatoire.");
                    return;
                }
                if (numero.getText().toString().isEmpty()||numero.getText().toString().length()<5) {
                    numero.setError("Ce champ est obligatoire.");
                    progressDialog.dismiss();
                    return;
                }
                if (captureImage == null) {
                    progressDialog.dismiss();
                    Toast.makeText(PublierPersonPerdue.this, "image de l'objet est obligatoire", Toast.LENGTH_SHORT).show();
                }else{
                    AddInfos();
                }

            }
        });

    }

    private void showDatePicker(EditText date) {
        // Utilisez la date actuelle comme date par défaut
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                /* context */ PublierPersonPerdue.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        // Mettez à jour l'EditText avec la date sélectionnée
                        String selectedDate = day + "/" + (month + 1) + "/" + year;
                        date.setText(selectedDate);
                    }
                },
                year, month, day
        );

        // Afficher le DatePickerDialog
        datePickerDialog.show();
    }

    public void showOptionsPopup(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choisir une option");

        builder.setPositiveButton("Caméra", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,100);
            }
        });

        builder.setNegativeButton("Galerie", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Ouvrir la galerie d'images
                Intent intent =new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,120);
            }
        });

        builder.show();

    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK){
                captureImage = (Bitmap) data.getExtras().get("data");
            }
        }else if(requestCode == 120){
            if (resultCode==Activity.RESULT_OK){
                uri = data.getData();
                try {
                    captureImage =MediaStore.Images.Media.getBitmap(getContentResolver(),uri);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        if (captureImage != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            captureImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            imageData = baos.toByteArray();
            imageViewSelectedImage.setImageBitmap(captureImage);

        }
    }

    private void AddInfos() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        String timestamp = String.valueOf(System.currentTimeMillis());
        StorageReference imageRef = storageRef.child("photo_de_personne_perdue/"+timestamp + ".jpg");

        // Téléchargez l'image dans Firebase Storage
        UploadTask uploadTask = imageRef.putBytes(imageData);
        // Obtenez l'URL de téléchargement de l'image depuis Firebase Storage
        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return imageRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
                String   idpays = MySharePreference.readString(PublierPersonPerdue.this, "pays", "");
                String   postal = MySharePreference.readString(PublierPersonPerdue.this, "postal", "");
                final DatabaseReference RootRef;
                RootRef = FirebaseDatabase.getInstance().getReference("person perdus").child(idpays).push();
                String id = RootRef.getKey();
                String titres=titre.getText().toString();
                String descrs=descr.getText().toString();
                String noms=nom.getText().toString();
                String numeros1=numero.getText().toString();
                String numeros="";
                if (numeros1.contains("+")){
                    numeros =removeSpace(numeros1);
                }else{
                    numeros=postal+removeSpace(numeros1);
                }
                String villes=ville.getText().toString();
                Date currentDate = new Date();

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());

                String formattedDate = dateFormat.format(currentDate);

                String finalNumeros = numeros;
                RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        String utf8Descrs = new String(descrs.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
                        Map<String, Object> etudiant = new HashMap<>();
                        etudiant.put("id", id);
                        etudiant.put("nomComplet", titres);
                        etudiant.put("descriptionPhysique", utf8Descrs);
                        etudiant.put("informationsContact", finalNumeros);
                        etudiant.put("lieuDisparition", villes);
                        etudiant.put("photoUrl", downloadUri.toString());
                        etudiant.put("pnom_du_parent", noms);
                        etudiant.put("dateDisparition", date.getText().toString());
                        etudiant.put("date", formattedDate);
                        RootRef.setValue(etudiant);

                        startActivity(new Intent(PublierPersonPerdue.this,PersonMain.class));
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }else {
                // Gérer l'erreur lors de la récupération de l'URL de téléchargement

            }

        });

    }

    private String removeSpace(String numeros1) {
        return numeros1.replace(" ","");
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
}