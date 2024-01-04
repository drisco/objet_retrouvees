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
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.soutra.soutramoi.models.Trouvee;


import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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
        if (!lists.getPhoto().isEmpty()){
            Picasso.get()
                    .load(lists.getPhoto()).into(holder.imageViewPhoto, new Callback() {
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
            Picasso.get().load(R.drawable._search).into(holder.imageViewPhoto);
        }
        holder.textViewTitle.setText(lists.getTitre());
        holder.textViewName.setText(lists.getNom());
        holder.textViewNumber.setText(lists.getNumero());
        holder.textViewCity.setText(lists.getVille());
        holder.textViewDate.setText(lists.getDate());
        holder.descr.setText(lists.getDescr());
        holder.imageViewPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ((listView)context).other();
                Intent intent = new Intent(context,PhotoView.class);
                intent.putExtra("id","piece trouvee");
                intent.putExtra("photo",lists.getPhoto());
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

    public void userNotFound(List<Trouvee> filterUser) {
        this.usersList=filterUser;
        notifyDataSetChanged();
    }

    public void setFilterUser(List<Trouvee> filterUser) {
        this.usersList=filterUser;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageViewPhoto;
        TextView textViewTitle;
        ProgressBar progressBar;
        TextView textViewName;
        TextView textViewNumber,descr;
        TextView textViewCity;
        TextView textViewDate,call;


        ViewHolder(View itemView) {
            super(itemView);
            imageViewPhoto = itemView.findViewById(R.id.imageViewPhoto1);
            textViewTitle = itemView.findViewById(R.id.textViewTitle1);
            textViewName = itemView.findViewById(R.id.textViewName1);
            textViewNumber = itemView.findViewById(R.id.textViewNumber1);
            call = itemView.findViewById(R.id.call1);
            textViewCity = itemView.findViewById(R.id.textViewCity1);
            textViewDate = itemView.findViewById(R.id.textViewDate1);
            progressBar = itemView.findViewById(R.id.bar);
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

