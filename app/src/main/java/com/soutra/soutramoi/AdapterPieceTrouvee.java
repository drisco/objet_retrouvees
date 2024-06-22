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
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.soutra.soutramoi.models.Trouvee;


import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class AdapterPieceTrouvee extends RecyclerView.Adapter<AdapterPieceTrouvee.ViewHolder>{
    private List<Trouvee> usersList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    Context context;

    // data is passed into the constructor
    AdapterPieceTrouvee(Context context, List<Trouvee> data) {
        this.mInflater = LayoutInflater.from(context);
        this.usersList = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @NotNull
    @Override
    public AdapterPieceTrouvee.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapterpiecetrouvee, parent, false);
        return new AdapterPieceTrouvee.ViewHolder(view);
    }


    // binds the data to the TextView in each row
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(AdapterPieceTrouvee.ViewHolder holder, int position) {

        Trouvee lists = usersList.get(position);
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
        holder.textViewTitle.setText(lists.getTitre());
        holder.textViewName.setText(lists.getNom());
        holder.textViewNumber.setText(lists.getNumero());
        holder.textViewCity.setText(lists.getVille());
        holder.textViewDate.setText(lists.getDate());
        holder.descr.setText(lists.getDescr());
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

    public void userNotFound(List<Trouvee> filterUser) {
        this.usersList=filterUser;
        notifyDataSetChanged();
    }

    public void setFilterUser(List<Trouvee> filterUser) {
        this.usersList=filterUser;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout imageContainer;
        TextView textViewTitle;
        ProgressBar progressBar;
        TextView textViewName;
        TextView textViewNumber,descr;
        TextView textViewCity;
        TextView textViewDate,call;


        ViewHolder(View itemView) {
            super(itemView);
            imageContainer = itemView.findViewById(R.id.imageContainer);
            textViewTitle = itemView.findViewById(R.id.textViewTitle1);
            textViewName = itemView.findViewById(R.id.textViewName1);
            textViewNumber = itemView.findViewById(R.id.textViewNumber1);
            call = itemView.findViewById(R.id.call1);
            textViewCity = itemView.findViewById(R.id.textViewCity1);
            textViewDate = itemView.findViewById(R.id.textViewDate1);
            progressBar.setVisibility(View.VISIBLE);
            descr = itemView.findViewById(R.id.descr);
            call.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

    }

    // convenience method for getting data at click position
    Trouvee getItem(int id) {
        return usersList.get(id);
    }

    void setClickListener(AdapterPieceTrouvee.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;

    }


    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);

    }
}

