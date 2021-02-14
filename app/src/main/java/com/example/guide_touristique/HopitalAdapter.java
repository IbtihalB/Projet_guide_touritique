package com.example.guide_touristique;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;

public class HopitalAdapter extends RecyclerView.Adapter<ViewHolder> {

    ArrayList<Hopital> Hopitaux;
    ArrayList<Hopital> Restaurants_Listed_Inlaunch;
    private static final int REQUEST_CALL = 1;
    private Context context;


    public HopitalAdapter(ArrayList<Hopital> hopitaux,  Context context) {
        this.Hopitaux = hopitaux;
        this.context = context;

    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.hopital_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);

        viewHolder.adress_textview.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ArrayList<Hopital>hopitals=new ArrayList<Hopital>();
                Intent intent = new Intent(parent.getContext(), Maps_Appel_Activity.class);
                intent.putExtra("service", viewHolder.name_textview.getText());
                intent.putExtra("loca", getLatLangFromAddress(viewHolder.adress_textview.getText().toString(),parent.getContext()));
                intent.putExtra("tel", viewHolder.tel_textview.getText());
                parent.getContext().startActivity(intent);
            }
        });
        viewHolder.tel_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall(parent.getContext(), viewHolder.tel_textview.getText().toString());
            }
        });
        return viewHolder;
    }

    public LatLng getLatLangFromAddress(String strAddress,Context context){
        Geocoder coder = new Geocoder(context, Locale.getDefault());
        List<Address> address;
        try {
            address = coder.getFromLocationName(strAddress,5);
            if (address == null) {
                return new LatLng(-10000, -10000);
            }
            Address location = address.get(0);
            return new LatLng(location.getLatitude(), location.getLongitude());
        } catch (IOException e) {
            return new LatLng(-10000, -10000);
        }
    }

    private void makePhoneCall(Context context, String tel) {
        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.CALL_PHONE) ==
                PackageManager.PERMISSION_GRANTED) {

            String Tel = tel;
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

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, Context context, String tel) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall(context, tel);
            } else {
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }



    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.hopital_rating.setRating(Hopitaux.get(position).getHopitalrating());
        Glide.with(context).load(Hopitaux.get(position).getImageurl()).into(holder.imageView);;
        holder.name_textview.setText(Hopitaux.get(position).getHopitalname());
        holder.adress_textview.setText(Hopitaux.get(position).getHopitaladress());
        holder.adress_textview.setPaintFlags(holder.adress_textview.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.tel_textview.setText(Hopitaux.get(position).getHopitaltel());
        holder.tel_textview.setPaintFlags(holder.tel_textview.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.type_textview.setText(Hopitaux.get(position).getHopitaltype());
        holder.LONGITUDE_textview.setText(Double.toString( Hopitaux.get(position).getHopitallongitude()));
        holder.LATITUDE_textview.setText(Double.toString(Hopitaux.get(position).getHopitallatitude()));
        holder.localisation_pic.setImageResource(R.drawable.marker_red);
        holder.distance_textview.setText(Hopitaux.get(position).getDuree());
        holder.tel_pic.setImageResource(R.drawable.phone_green);

        Log.d("jnkljmlkj", "nkmljùh");
    }


    public int getItemCount() {
        return Hopitaux.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}


    class ViewHolder extends RecyclerView.ViewHolder {
        public RatingBar hopital_rating;
        public ImageView imageView;
        public CardView linearLayout;
        public TextView name_textview;
        public TextView adress_textview;
        public TextView tel_textview;
        public TextView type_textview;
        public TextView LATITUDE_textview;
        public TextView LONGITUDE_textview;
        public TextView distance_textview;
        public ImageView localisation_pic;
        public ImageView tel_pic;

        public ViewHolder(View itemView) {
            super(itemView);
            this.name_textview = (TextView) itemView.findViewById(R.id.hopital_name);
            this.adress_textview = (TextView) itemView.findViewById(R.id.hopital_adress);
            this.tel_textview = (TextView) itemView.findViewById(R.id.hopital_tel);
            this.type_textview = (TextView) itemView.findViewById(R.id.hopital_type);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.hopital_rating=(RatingBar)itemView.findViewById(R.id.ratingBar);
            linearLayout = (CardView) itemView.findViewById(R.id.hopital_layout);
            this.localisation_pic = (ImageView) itemView.findViewById(R.id.imageView3);
            this.LATITUDE_textview = (TextView) itemView.findViewById(R.id.latitude);
            this.LONGITUDE_textview = (TextView) itemView.findViewById(R.id.longitude);
            this.distance_textview = (TextView) itemView.findViewById(R.id.distance1);

            this.tel_pic = (ImageView) itemView.findViewById(R.id.imageView2);


        }
    }
