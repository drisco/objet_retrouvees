package com.soutra.soutramoi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.soutra.soutramoi.models.PersonEgaree;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

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

        if (!lists.getPhotoUrl().isEmpty()){
            Picasso.get()
                    .load(lists.getPhotoUrl()).into(holder.imageViewPhoto1, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.progressBar.setVisibility(View.GONE); // Rend la barre de progression invisible après le chargement réussi
                        }

                        @Override
                        public void onError(Exception e) {
                            holder.progressBar.setVisibility(View.GONE); // Rend la barre de progression invisible en cas d'erreur de chargement
                        }
                    });
        }else{
            Picasso.get().load(R.drawable._person).into(holder.imageViewPhoto1);
        }

        holder.text_title.setText(lists.getNomComplet());

        holder.text_description.setText(lists.getDescriptionPhysique());

        holder.text_date.setText(lists.getDateDisparition());
        holder.text_location.setText(lists.getLieuDisparition());
        holder.text_victim_name.setText(lists.getPnom_du_parent());
        holder.text_number.setText(lists.getInformationsContact());
        holder.imageViewPhoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ((listView)context).other();
                Intent intent = new Intent(context,PhotoView.class);
                intent.putExtra("id","personne perdue");
                intent.putExtra("photo",lists.getPhotoUrl());
                context.startActivity(intent);
                ((Activity)context).finish();
            }


        });

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
        TextView text_title,text_description,text_victim_name,text_number,text_location,text_date;
        RelativeLayout relativeLayout;



        ViewHolder(View itemView) {
            super(itemView);
            imageViewPhoto1 = itemView.findViewById(R.id.imageViewPhoto1);
            text_title = itemView.findViewById(R.id.text_title);
            text_description = itemView.findViewById(R.id.text_description);
            text_victim_name = itemView.findViewById(R.id.text_victim_name);
            text_number= itemView.findViewById(R.id.text_number);
            text_location= itemView.findViewById(R.id.text_location);
            text_date= itemView.findViewById(R.id.text_date);
            progressBar= itemView.findViewById(R.id.bar);
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


