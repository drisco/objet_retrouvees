package com.soutra.soutramoi;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.soutra.soutramoi.models.Egaree;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


import org.jetbrains.annotations.NotNull;

import java.util.List;

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

        if (!lists.getPhoto().isEmpty()){
            Picasso.get()
                    .load(lists.getPhoto()).into(holder.imageViewPhoto, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.text.setVisibility(View.GONE);
                            holder.progressBar.setVisibility(View.GONE); // Rend la barre de progression invisible après le chargement réussi
                        }

                        @Override
                        public void onError(Exception e) {
                            holder.text.setVisibility(View.GONE);
                            holder.progressBar.setVisibility(View.GONE); // Rend la barre de progression invisible en cas d'erreur de chargement
                        }
                    });
        }else{
            Picasso.get()
                    .load(R.drawable._search).into(holder.imageViewPhoto, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.progressBar.setVisibility(View.GONE); // Rend la barre de progression invisible après le chargement réussi
                        }

                        @Override
                        public void onError(Exception e) {
                            holder.progressBar.setVisibility(View.GONE); // Rend la barre de progression invisible en cas d'erreur de chargement
                        }
                    });
        }
        holder.text_title.setText(lists.getTitre());

        holder.text_description.setText(lists.getDescr());

        holder.text_date.setText(lists.getDate());
        holder.text_location.setText(lists.getVille());
        holder.text_victim_name.setText(lists.getnom());
        holder.text_number.setText(lists.getNumero());

        if (!lists.getPhoto().isEmpty()){
            holder.imageViewPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // ((listView)context).other();
                    Intent intent = new Intent(context,PhotoView.class);
                    intent.putExtra("id","piece egaree");
                    intent.putExtra("photo",lists.getPhoto());
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }


            });
        }

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
        ImageView imageViewPhoto;
        ProgressBar progressBar;
        TextView text_title,text_description,text_victim_name,text_number,text_location,text_date,text;
        RelativeLayout relativeLayout;



        ViewHolder(View itemView) {
            super(itemView);
            //imageUser = itemView.findViewById(R.id.imageUser);
            text_title = itemView.findViewById(R.id.text_title);
            imageViewPhoto = itemView.findViewById(R.id.imageViewPhoto1);
            text_description = itemView.findViewById(R.id.text_description);
            text_victim_name = itemView.findViewById(R.id.text_victim_name);
            text_number= itemView.findViewById(R.id.text_number);
            text_location= itemView.findViewById(R.id.text_location);
            progressBar= itemView.findViewById(R.id.bar);
            text= itemView.findViewById(R.id.text);
            progressBar.setVisibility(View.VISIBLE);
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

