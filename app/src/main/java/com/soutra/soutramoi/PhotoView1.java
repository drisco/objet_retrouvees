package com.soutra.soutramoi;

        import android.annotation.SuppressLint;
        import android.content.Intent;
        import android.widget.ImageView;
        import androidx.appcompat.app.AppCompatActivity;
        import android.os.Bundle;

        import com.squareup.picasso.Picasso;

public class PhotoView1 extends AppCompatActivity {
    ImageView fullScreenImageView;
    int incr;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        fullScreenImageView =findViewById(R.id.fullScreenImageView);
        String imageResourceId = getIntent().getStringExtra("photo");
        Picasso.get().load(imageResourceId).into(fullScreenImageView);



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
