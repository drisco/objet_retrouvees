package com.soutra.soutramoi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.soutra.soutramoi.models.Egaree;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


import org.jetbrains.annotations.NotNull;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class AdapterPieceEgaree extends RecyclerView.Adapter<AdapterPieceEgaree.ViewHolder>{
    private List<Egaree> usersList;
    private LayoutInflater mInflater;
    private AdapterPieceEgaree.ItemClickListener mClickListener;
    Context context;

    // data is passed into the constructor
    AdapterPieceEgaree(Context context, List<Egaree> data) {
        this.mInflater = LayoutInflater.from(context);
        this.usersList = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @NotNull
    @Override
    public AdapterPieceEgaree.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapterpieceegaree, parent, false);
        return new AdapterPieceEgaree.ViewHolder(view);
    }


    // binds the data to the TextView in each row
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(AdapterPieceEgaree.ViewHolder holder, int position) {

        Egaree lists = usersList.get(position);
        System.out.println("NBBVFDBVFDJBVDBV.BQVHDBLQB HBBBBVBDQBJQBBLBNBNBNFBJN "+lists.getImageUris());

        for (String imageUrl : lists.getImageUris()) {
            ImageView imageView = new ImageView(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(500, 500);
            layoutParams.setMargins(5, 0, 5, 0); // Set margin if needed
            layoutParams.gravity = Gravity.CENTER;
            imageView.setLayoutParams(layoutParams);
            if (imageUrl == null || imageUrl.isEmpty()) {
                Glide.with(context)
                        .load(R.drawable.fonti) // Charge l'image par défaut
                        .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(30, 0)))
                        .into(imageView);
            } else {
                Glide.with(context)
                        .load(imageUrl)
                        .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(30, 0)))
                        .into(imageView);
            }

            imageView.setOnClickListener(v -> showImageDialog(imageUrl != null && !imageUrl.isEmpty() ? imageUrl : "default_image_url"));
            holder.imageContainer.addView(imageView);
        }
        holder.text_title.setText(lists.getTitre());

        holder.text_description.setText(lists.getDescr());

        holder.text_date.setText(lists.getDate());
        holder.text_location.setText(lists.getVille());
        holder.text_victim_name.setText(lists.getNom());
        holder.text_number.setText(lists.getNumero());


    }

    private void showImageDialog(String imageUrl) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.dialog_image_view);

        ImageView fullImageView = bottomSheetDialog.findViewById(R.id.fullImageView);
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.fonti) // Image par défaut en cas d'erreur ou de chargement
                .into(fullImageView);

        bottomSheetDialog.show();
    }


    // total number of rows
    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public void userNotFound(List<Egaree> filterUser) {
        this.usersList=filterUser;
        notifyDataSetChanged();
    }

    public void setFilterUser(List<Egaree> filterUser) {
        this.usersList=filterUser;
        notifyDataSetChanged();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout imageContainer;
        ProgressBar progressBar;
        TextView text_title,text_description,text_victim_name,text_number,text_location,text_date,text;
        RelativeLayout relativeLayout;



        ViewHolder(View itemView) {
            super(itemView);
            //imageUser = itemView.findViewById(R.id.imageUser);
            text_title = itemView.findViewById(R.id.text_title);
            imageContainer = itemView.findViewById(R.id.imageContainer);
            text_description = itemView.findViewById(R.id.text_description);
            text_victim_name = itemView.findViewById(R.id.text_victim_name);
            text_number= itemView.findViewById(R.id.text_number);
            text_location= itemView.findViewById(R.id.text_location);

            text_date= itemView.findViewById(R.id.text_date);
            //relativeLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

    }

    // convenience method for getting data at click position
    Egaree getItem(int id) {
        return usersList.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(AdapterPieceEgaree.ItemClickListener itemClickListener) {
        this.mClickListener = (AdapterPieceEgaree.ItemClickListener) itemClickListener;

    }


    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);

    }
}

