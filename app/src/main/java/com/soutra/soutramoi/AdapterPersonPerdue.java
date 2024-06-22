package com.soutra.soutramoi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
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
import com.soutra.soutramoi.models.PersonEgaree;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class AdapterPersonPerdue extends RecyclerView.Adapter<AdapterPersonPerdue.ViewHolder>{
    private List<PersonEgaree> usersList;
    private LayoutInflater mInflater;
    private AdapterPieceEgaree.ItemClickListener mClickListener;
    Context context;


    // data is passed into the constructor
    AdapterPersonPerdue(Context context, List<PersonEgaree> data) {
        this.mInflater = LayoutInflater.from(context);
        this.usersList = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @NotNull
    @Override
    public AdapterPersonPerdue.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapterpersonegaree, parent, false);
        return new AdapterPersonPerdue.ViewHolder(view);
    }


    // binds the data to the TextView in each row
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(AdapterPersonPerdue.ViewHolder holder, int position) {

        PersonEgaree lists = usersList.get(position);

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

        holder.text_title.setText(lists.getNomComplet());

        holder.text_description.setText(lists.getDescriptionPhysique());

        holder.text_date.setText(lists.getDateDisparition());
        holder.text_location.setText(lists.getLieuDisparition());
        holder.text_victim_name.setText(lists.getPnom_du_parent());
        holder.text_number.setText(lists.getInformationsContact());

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

    public void userNotFound(List<PersonEgaree> filterUser) {
        this.usersList=filterUser;
//        empty.setVisibility(View.VISIBLE);
        notifyDataSetChanged();
    }

    public void setFilterUser(List<PersonEgaree> filterUser) {
        this.usersList=filterUser;
        notifyDataSetChanged();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageViewPhoto1;
        ProgressBar progressBar;
        LinearLayout imageContainer;
        TextView text_title,text_description,text_victim_name,text_number,text_location,text_date;
        RelativeLayout relativeLayout;



        ViewHolder(View itemView) {
            super(itemView);
            imageContainer = itemView.findViewById(R.id.imageContainer);
            text_title = itemView.findViewById(R.id.text_title);
            text_description = itemView.findViewById(R.id.text_description);
            text_victim_name = itemView.findViewById(R.id.text_victim_name);
            text_number= itemView.findViewById(R.id.text_number);
            text_location= itemView.findViewById(R.id.text_location);
            text_date= itemView.findViewById(R.id.text_date);
            progressBar.setVisibility(View.VISIBLE);
            //relativeLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

    }

    // convenience method for getting data at click position
    PersonEgaree getItem(int id) {
        return usersList.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(AdapterPersonPerdue.ItemClickListener itemClickListener) {
        this.mClickListener = (AdapterPieceEgaree.ItemClickListener) itemClickListener;

    }


    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);

    }

}


