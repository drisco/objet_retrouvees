package com.soutra.soutramoi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
        import android.app.AlertDialog;
        import android.app.ProgressDialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.graphics.Bitmap;
        import android.net.Uri;
        import android.provider.MediaStore;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;
        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;
        import android.os.Bundle;
        import androidx.core.content.ContextCompat;

        import com.google.firebase.database.*;
        import com.google.firebase.storage.FirebaseStorage;
        import com.google.firebase.storage.StorageReference;
        import com.google.firebase.storage.UploadTask;
        import com.soutra.soutramoi.PieceTrouveeMain;

        import org.jetbrains.annotations.NotNull;

        import java.io.ByteArrayOutputStream;
        import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
        import java.util.Date;
        import java.util.HashMap;
        import java.util.Locale;
        import java.util.Map;

public class PublierPersonRetrouvee extends AppCompatActivity {
    int incr;
    String idpays,postal;
    Bitmap captureImage;
    byte[] imageData;
    Uri uri;
    ImageView imageViewSelectedImage;
    ProgressDialog progressDialog;
    TextView editTextTitle,editTextLocation,editTextVictimName,editTextContactNumber,descr;
    Button buttonPublish;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publier_person_retrouvee);
        imageViewSelectedImage = findViewById(R.id.imageViewSelectedImage);
        editTextTitle = findViewById(R.id.editTextTitle);
        descr = findViewById(R.id.descr);
        editTextLocation = findViewById(R.id.editTextLocation);
        editTextVictimName = findViewById(R.id.editTextVictimName);
        editTextContactNumber = findViewById(R.id.editTextContactNumber);
        progressDialog = new ProgressDialog(PublierPersonRetrouvee.this);
        buttonPublish = findViewById(R.id.buttonPublish);
        int permissionCheck = ContextCompat.checkSelfPermission(PublierPersonRetrouvee.this,
                android.Manifest.permission.CAMERA);
        if (permissionCheck== PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 12);
        }
        buttonPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Traitement en cours..."); // Message à afficher
                progressDialog.setCancelable(false); // Empêche la fermeture de la boîte de dialogue en dehors du code
                progressDialog.show();
                if (imageViewSelectedImage.getDrawable() != null) {
                    Toast.makeText(PublierPersonRetrouvee.this, "Image de l'individu est obligatoire", Toast.LENGTH_SHORT).show();
                }
                if (editTextLocation.getText().toString().isEmpty()) {
                    editTextLocation.setError("Ce champ est obligatoire.");
                    progressDialog.dismiss();
                    return;
                }
                if (editTextLocation.getText().toString().isEmpty()) {
                    editTextLocation.setError("Ce champ est obligatoire.");
                    progressDialog.dismiss();
                    return;
                }
                if (descr.getText().toString().isEmpty()) {
                    descr.setError("Ce champ est obligatoire.");
                    progressDialog.dismiss();
                    return;
                }
                if (editTextVictimName.getText().toString().isEmpty() || editTextVictimName.getText().toString().length()<3) {
                    editTextVictimName.setError("Ce champ est obligatoire.");
                    progressDialog.dismiss();
                    return;
                }
                if (editTextContactNumber.getText().toString().isEmpty()||editTextContactNumber.getText().toString().length()<10) {
                    editTextContactNumber.setError("Ce champ est obligatoire.");
                    progressDialog.dismiss();
                    return;
                }
                if (!(editTextContactNumber.getText().toString().contains("+"))){
                    editTextContactNumber.setError("Ajouter code postal");
                    progressDialog.dismiss();
                    return;
                }
                String titre =editTextTitle.getText().toString();
                String ville =editTextLocation.getText().toString();
                String nom =editTextVictimName.getText().toString();
                String numero1 =editTextContactNumber.getText().toString();
                String numero ="";
                String description =descr.getText().toString();
                idpays = MySharePreference.readString(PublierPersonRetrouvee.this, "pays", "");
                postal = MySharePreference.readString(PublierPersonRetrouvee.this, "postal", "");
                if (numero1.contains("+")){
                    numero =removeSpace(numero1);
                }else{
                    numero=postal+removeSpace(numero1);
                }
                if (captureImage == null) {
                    progressDialog.dismiss();
                    Toast.makeText(PublierPersonRetrouvee.this, "image de l'objet est obligatoire", Toast.LENGTH_SHORT).show();
                }else{
                    MethodPublier(titre,ville,nom,numero,idpays,imageData,description);
                }

            }
        });

    }

    private String removeSpace(String number) {
        return number.replace(" ","");
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

        builder.show();

    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK){
                captureImage = (Bitmap) data.getExtras().get("data");
            }
        }
        if (captureImage != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            captureImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            imageData = baos.toByteArray();
            imageViewSelectedImage.setImageBitmap(captureImage);

        }
    }

    private void MethodPublier(String titre, String ville, String nom, String numero, String idpays, byte[] imageData , String description) {
        // Obtenez une référence à Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        String timestamp = String.valueOf(System.currentTimeMillis());
        StorageReference imageRef = storageRef.child("dbphotopersonnetrouvee/"+timestamp + ".jpg");

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
                final DatabaseReference RootRef;
                RootRef = FirebaseDatabase.getInstance().getReference("person retrouves").child(idpays).push();
                Date currentDate = new Date();
                String id = RootRef.getKey();

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                String formattedDate = dateFormat.format(currentDate);
                RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        String utf8Descrs = new String(description.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
                        Map<String, Object> etudiant = new HashMap<>();
                        etudiant.put("id", id);
                        etudiant.put("titre", titre);
                        etudiant.put("description", utf8Descrs);
                        etudiant.put("photo", downloadUri.toString());
                        etudiant.put("numero", numero);
                        etudiant.put("ville", ville);
                        etudiant.put("nom", nom);
                        etudiant.put("date", formattedDate);
                        RootRef.setValue(etudiant);
                        startActivity(new Intent(PublierPersonRetrouvee.this, PieceTrouveeMain.class));
                        finish();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

            } else {
                // Gérer l'erreur lors de la récupération de l'URL de téléchargement
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        incr++;
        if (incr == 1) {
            Intent intent = new Intent(this, MainPersonTrouvee.class);
            startActivity(intent);
            finish();
        }
    }
}