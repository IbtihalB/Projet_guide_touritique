package com.example.guide_touristique;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Paint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;


import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class PharmacieAdapter extends RecyclerView.Adapter<PharmacieAdapter.ViewHolder> {
    private ArrayList<Pharmacie> Pharmacies;
    private static final int REQUEST_CALL = 1;
    private Context context;


    public PharmacieAdapter(@NonNull ArrayList<Pharmacie> Pharmacies, Context context) {
        this.Pharmacies=Pharmacies;
        this.context=context;
    }
    public int getCount() {
        return Pharmacies.size();
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.pharmacie_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        viewHolder.adress_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(), Maps_Appel_Activity.class);
                intent.putExtra("service", viewHolder.name_textview.getText());
                intent.putExtra("Latitude", Double.parseDouble((String) viewHolder.LATITUDE_textview.getText()));
                intent.putExtra("Longitude", Double.parseDouble((String)viewHolder.LONGITUDE_textview.getText()));
                intent.putExtra("Tel",viewHolder.tel_textview.getText().toString());
                parent.getContext().startActivity(intent);
            }
        });
        viewHolder.tel_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall(parent.getContext(), (String) viewHolder.tel_textview.getText());
            }
        });
        return viewHolder;

    }

    private void makePhoneCall(Context context, String tel) {
        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.CALL_PHONE) ==
                PackageManager.PERMISSION_GRANTED) {

            String Tel =tel;
            Intent call;
            call = new Intent(Intent.ACTION_CALL);
            call.setData(Uri.parse("tel:" + Tel));
            context.startActivity(call);
        } else {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected. In this UI,
            // include a "cancel" or "no thanks" button that allows the user to
            // continue using your app without granting the permission.
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, Context context, String Tel) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall(context, Tel);
            } else {
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.name_textview.setText(Pharmacies.get(position).getPharmaciename());
        holder.adress_textview.setText(Pharmacies.get(position).getPharmacieadress());
        holder.adress_textview.setPaintFlags(holder.adress_textview.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.tel_textview.setText(Pharmacies.get(position).getPharmacietel());
        holder.tel_textview.setPaintFlags(holder.tel_textview.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        Glide.with(context).load(Pharmacies.get(position).getImageurl()).into(holder.imageView);;
        holder.LONGITUDE_textview.setText(Double.toString(Pharmacies.get(position).getPharmacielongitude()));
        holder.LATITUDE_textview.setText(Double.toString(Pharmacies.get(position).getPharmacielatitude()));
        holder.distance_textview.setText(Pharmacies.get(position).getDuree());
        holder.localisation.setImageResource(R.drawable.marker_red);
        holder.appel.setImageResource( R.drawable.phone_green);


    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
       return Pharmacies.size();}





class ViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public CardView linearLayout;
    public TextView name_textview;
    public TextView adress_textview;
    public TextView tel_textview;
    public ImageView localisation;
    public ImageView appel;
    public TextView LATITUDE_textview;
    public TextView LONGITUDE_textview;
    public TextView distance_textview;
    public ImageView duree;

    public ViewHolder(View itemView) {
        super(itemView);
        this.name_textview=(TextView)itemView.findViewById(R.id.pharmacie_name);
        this.adress_textview=(TextView)itemView.findViewById(R.id.pharmacie_adress);
        this.tel_textview=(TextView)itemView.findViewById(R.id.pharmacie_tel);
        this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
        this.localisation=(ImageView) itemView.findViewById(R.id.imageView3);
        this.duree=(ImageView) itemView.findViewById(R.id.imageView4);
        this.appel=(ImageView) itemView.findViewById(R.id.imageView2);
        linearLayout = (CardView) itemView.findViewById(R.id.pharmacie_layout);
        this.LATITUDE_textview = (TextView) itemView.findViewById(R.id.latitude);
        this.LONGITUDE_textview = (TextView) itemView.findViewById(R.id.longitude);
        this.distance_textview = (TextView) itemView.findViewById(R.id.distance);

    }}}

