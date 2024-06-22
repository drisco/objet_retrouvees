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
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class PublierPiecesRetrouvee extends AppCompatActivity {
    int incr;
    String idpays,postal;
    Bitmap captureImage;
    byte[] imageData;
    private List<Bitmap> selectedImages = new ArrayList<>();
    private LinearLayout imageContainer;
    Uri uri;
    ImageView imageViewSelectedImage;
    ProgressDialog progressDialog;
    TextView editTextTitle,editTextLocation,editTextVictimName,editTextContactNumber,descr;
    Button buttonPublish;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publier_pieces_retrouvee);
        imageViewSelectedImage = findViewById(R.id.imageViewSelectedImage);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextLocation = findViewById(R.id.editTextLocation);
        descr = findViewById(R.id.descr);
        imageContainer = findViewById(R.id.imageContainer);
        editTextVictimName = findViewById(R.id.editTextVictimName);
        editTextContactNumber = findViewById(R.id.editTextContactNumber);
        progressDialog = new ProgressDialog(PublierPiecesRetrouvee.this);
        buttonPublish = findViewById(R.id.buttonPublish);
        int permissionCheck = ContextCompat.checkSelfPermission(PublierPiecesRetrouvee.this,
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
                if (editTextTitle.getText().toString().isEmpty()) {
                    editTextTitle.setError("Ce champ est obligatoire.");
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
                if (editTextVictimName.getText().toString().isEmpty()) {
                    editTextVictimName.setError("Ce champ est obligatoire.");
                    progressDialog.dismiss();
                    return;
                }
                if (editTextContactNumber.getText().toString().isEmpty()|| editTextContactNumber.getText().toString().length()<5) {
                    editTextContactNumber.setError("Ce champ est obligatoire.");
                    progressDialog.dismiss();
                    return;
                }

                String titre =editTextTitle.getText().toString();
                String ville =editTextLocation.getText().toString();
                String nom =editTextVictimName.getText().toString();
                String description =descr.getText().toString();
                idpays = MySharePreference.readString(PublierPiecesRetrouvee.this, "pays", "");
                postal = MySharePreference.readString(PublierPiecesRetrouvee.this, "postal", "");
                String numero1 =editTextContactNumber.getText().toString();
                String numero ="";
                if (numero1.contains("+")){
                     numero =removeSpace(numero1);
                }else{
                    numero=postal+removeSpace(numero1);
                }
                if (selectedImages.isEmpty()) {
                    progressDialog.dismiss();
                    Toast.makeText(PublierPiecesRetrouvee.this, "image de l'objet est obligatoire", Toast.LENGTH_SHORT).show();
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
        builder.setTitle("Souhaitez vous vraiment ouvrir la caméra?");

        builder.setPositiveButton("OUI", new DialogInterface.OnClickListener() {
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
                addImageToContainer(captureImage);
            }
        }
        if (captureImage != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            captureImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            imageData = baos.toByteArray();
            imageViewSelectedImage.setImageBitmap(captureImage);

        }
    }

    private void addImageToContainer(Bitmap Image) {
        selectedImages.add(Image);
        refreshImageContainer();
    }

    private void refreshImageContainer() {
        imageContainer.removeAllViews();
        for (Bitmap bitmap : selectedImages) {
            ImageView imageView = new ImageView(this);
            imageView.setImageBitmap(bitmap);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(500, 500);
            layoutParams.setMargins(5, 0, 5, 0);
            layoutParams.gravity = Gravity.CENTER;
            imageView.setLayoutParams(layoutParams);
            Glide.with(this).load(bitmap)
                    .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(30, 0)))
                    .into(imageView);
            imageContainer.addView(imageView);
        }
    }

    private void MethodPublier(String titre, String ville, String nom, String numero, String idpays, byte[] imageData, String description) {
        // Obtenez une référence à Firebase Storage
        List<String> imageUris = new ArrayList<>();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        String timestamp = String.valueOf(System.currentTimeMillis());
        StorageReference imageRef = storageRef.child("photo_de_piece/"+timestamp + ".jpg");


        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference("objets retrouves").child(idpays).push();
        Date currentDate = new Date();
        String id = RootRef.getKey();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(currentDate);

        for (Bitmap bitmap : selectedImages) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = imageRef.putBytes(data);
            uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return imageRef.getDownloadUrl();
            }).addOnCompleteListener( task -> {
                if (task.isSuccessful()){
                    imageUris.add(task.getResult().toString());
                    if (imageUris.size() == selectedImages.size()) {
                        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                String utf8Descrs = new String(description.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
                                Map<String, Object> etudiant = new HashMap<>();
                                etudiant.put("id", id);
                                etudiant.put("titre", titre);
                                etudiant.put("imageUris", imageUris);
                                etudiant.put("numero", numero);
                                etudiant.put("ville", ville);
                                etudiant.put("descr", utf8Descrs);
                                etudiant.put("nom", nom);
                                etudiant.put("date", formattedDate);
                                RootRef.setValue(etudiant);
                                startActivity(new Intent(PublierPiecesRetrouvee.this, PieceTrouveeMain.class));
                                finish();
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                progressDialog.dismiss();
                            }
                        });

                    }
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        incr++;
        if (incr == 1) {
            Intent intent = new Intent(this, PieceTrouveeMain.class);
            startActivity(intent);
            finish();
        }
    }
}