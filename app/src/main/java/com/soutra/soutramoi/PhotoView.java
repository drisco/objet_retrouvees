package com.soutra.soutramoi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.squareup.picasso.Picasso;

public class PhotoView extends AppCompatActivity {
    ImageView fullScreenImageView;
    int incr;
    String id;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        fullScreenImageView =findViewById(R.id.fullScreenImageView);
        id = getIntent().getStringExtra("id");
        String imageResourceId = getIntent().getStringExtra("photo");
        Picasso.get().load(imageResourceId).into(fullScreenImageView);



    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        incr++;
        if (incr == 1) {
            if (id.equals("piece egaree")){
                Intent intent = new Intent(this, PiecesMainActivity.class);
                startActivity(intent);
                finish();
            } else if (id.equals("piece trouvee")) {
                Intent intent = new Intent(this, PieceTrouveeMain.class);
                startActivity(intent);
                finish();
            }else if (id.equals("personne perdue")) {
                Intent intent = new Intent(this, PersonMain.class);
                startActivity(intent);
                finish();
            }else if (id.equals("personne trouvee")) {
                Intent intent = new Intent(this, MainPersonTrouvee.class);
                startActivity(intent);
                finish();
            }
        }
    }
}