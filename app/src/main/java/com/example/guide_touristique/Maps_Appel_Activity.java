package com.example.guide_touristique;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;



public class Maps_Appel_Activity extends FragmentActivity
        implements OnMapReadyCallback
{
    private static final int REQUEST_CALL=1;
    private GoogleMap mMap;
    @BindView(R.id.appel)
    ImageButton appeler;

    @BindView(R.id.imageButton)
    ImageButton Back;
    @BindView(R.id.service_name)
    TextView service_name;

    static double Longitude;
    static String Tel;
    static double Latitude;
    String service_selectionned;
    LatLng a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appel_maps);
        ButterKnife.bind(this);
        Intent intent = getIntent();
       service_selectionned = intent.getStringExtra("service");
        boolean state = intent.getBooleanExtra("state",true);
        Latitude = intent.getDoubleExtra("Latitude",0);
        Longitude = intent.getDoubleExtra("Longitude",0);
        a= getIntent().getExtras().getParcelable("loca");
        Tel = intent.getStringExtra("tel");
        if(state==false)
            appeler.setVisibility(View.INVISIBLE);
        service_name.setText(service_selectionned);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // You can use the API that requires the permission.
        appeler.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                makePhoneCall();}});



        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Maps_Appel_Activity.this.finish();

            }
        });

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Add a marker in Sydney and move the camera
        LatLng service = a;
        mMap.addMarker(new MarkerOptions().position(service).title(" Marker in "+service_selectionned ));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(service,50));
    }


    private void makePhoneCall()
    { if (ActivityCompat.checkSelfPermission(
            Maps_Appel_Activity.this, Manifest.permission.CALL_PHONE) ==
            PackageManager.PERMISSION_GRANTED) {
        Intent intent = getIntent();
        Tel = intent.getStringExtra("tel");
        Intent call;
        call = new Intent(Intent.ACTION_CALL);
        call.setData(Uri.parse("tel:"+Tel));
        startActivity(call);
    } else  {
        // In an educational UI, explain to the user why your app requires this
        // permission for a specific feature to behave as expected. In this UI,
        // include a "cancel" or "no thanks" button that allows the user to
        // continue using your app without granting the permission.
        ActivityCompat.requestPermissions(Maps_Appel_Activity.this,new String []{Manifest.permission.CALL_PHONE},REQUEST_CALL); }}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQUEST_CALL){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {makePhoneCall();}
            else{
                Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();}
        }
    }



}







