package com.example.guide_touristique;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class MosqueeAdapter extends RecyclerView.Adapter<MosqueeAdapter.ViewHolder> {



    private ArrayList<Mosquee> Mosquees;
    private static final int REQUEST_CALL = 1;
    private Context context;


    public MosqueeAdapter(@NonNull ArrayList<Mosquee> mosquees, Context context) {

        this.Mosquees=mosquees;
        this.context=context;}
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.mosquee_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        viewHolder.adress_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(), Maps_Appel_Activity.class);
                intent.putExtra("service", viewHolder.name_textview.getText());
                intent.putExtra("state", false);
                intent.putExtra("Latitude",Double.parseDouble((String) viewHolder.LATITUDE_textview.getText()));
                intent.putExtra("Longitude", Double.parseDouble((String)viewHolder.LONGITUDE_textview.getText()));
                parent.getContext().startActivity(intent);
            }
        });

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Mosquee myListData =Mosquees.get(position);
        holder.name_textview.setText(Mosquees.get(position).getMosqueename());
        holder.adress_textview.setText(Mosquees.get(position).getMosqueeadress().trim());
        holder.adress_textview.setPaintFlags(holder.adress_textview.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.localisation.setImageResource(R.drawable.marker_red);
        Glide.with(context).load(Mosquees.get(position).getImageurl()).into(holder.imageView);;
        holder.LONGITUDE_textview.setText(Double.toString( Mosquees.get(position).getMosqueelongitude()));
        holder.LATITUDE_textview.setText(Double.toString(Mosquees.get(position).getMosqueelatitude()));
        holder.distance_textview.setText(Mosquees.get(position).getDuree());}





    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return Mosquees.size();

    }



    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView duree;public ImageView imageView;
    public ImageView localisation;
    public CardView linearLayout;
    public TextView name_textview;
    public TextView adress_textview;
    public TextView LATITUDE_textview;
    public TextView LONGITUDE_textview;
    public TextView distance_textview;

        public ViewHolder(View itemView) {
        super(itemView);
        this.name_textview=(TextView)itemView.findViewById(R.id.mosquee_name);
        this.adress_textview=(TextView)itemView.findViewById(R.id.mosquee_adress);
        this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
        this.localisation = (ImageView) itemView.findViewById(R.id.imageView4);

            this.duree = (ImageView) itemView.findViewById(R.id.imageView3);
        linearLayout = (CardView)itemView.findViewById(R.id.mosquee_layout);
        this.LATITUDE_textview = (TextView) itemView.findViewById(R.id.latitude);
        this.LONGITUDE_textview = (TextView) itemView.findViewById(R.id.longitude);
        this.distance_textview = (TextView) itemView.findViewById(R.id.distance1);

        }}








}

