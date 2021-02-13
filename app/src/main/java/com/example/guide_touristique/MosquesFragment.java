package com.example.guide_touristique;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MosquesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MosquesFragment extends Fragment {
    FusedLocationProviderClient fusedLocationProviderClient;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 ="ville";


    // TODO: Rename and change types of parameters
    private static String Ville;
    View root_view;

    public MosquesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     //* @param param2 Parameter 2.
     * @return A new instance of fragment MosquesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MosquesFragment newInstance(String param1) {
        MosquesFragment fragment = new MosquesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        Ville=param1;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Ville= getArguments().getString(ARG_PARAM1);
        }
    }
    public void fetchAllMosquees(ArrayList <Mosquee>mosquues)
    {
        RecyclerView recyclerView = (RecyclerView) root_view.findViewById (R.id.mosquee_list);
        MosqueeAdapter MyListAdapter =  new MosqueeAdapter (mosquues, getContext());
        recyclerView.setHasFixedSize ( true );
        recyclerView.setLayoutManager ( new LinearLayoutManager(this.getActivity() ));
        ProgressBar progressBar= (ProgressBar) root_view.findViewById (R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        recyclerView.setAdapter (MyListAdapter);
    }
    public void fetchAllMosqueesByname(ArrayList <Mosquee>mosques){
        //implement searchview

        RecyclerView recyclerView = (RecyclerView) root_view.findViewById (R.id.mosquee_list);
        MosqueeAdapter MyListAdapter = new MosqueeAdapter(mosques, getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MosquesFragment.this.getContext()));
        ProgressBar progressBar= (ProgressBar) root_view.findViewById (R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        recyclerView.setAdapter(MyListAdapter);}


    public void fetchAllMosqueessortedbydistace(ArrayList <Mosquee>mosques) {

        RecyclerView recyclerView = (RecyclerView) root_view.findViewById (R.id.mosquee_list);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MosquesFragment.this.getActivity());

        if (ActivityCompat.checkSelfPermission(MosquesFragment.this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    Geocoder geocoder = null;
                    if (location != null) {
                        geocoder = new Geocoder(MosquesFragment.this.getContext(), Locale.getDefault());

                        ArrayList<Address> addresses = new ArrayList<Address>();

                        try {
                            addresses = (ArrayList<Address>) geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Location startPoint = new Location("locationA");
                        double distance;
                        startPoint.setLatitude(addresses.get(0).getLatitude());
                        startPoint.setLongitude(addresses.get(0).getLongitude());
                        Location endPoint = new Location("locationA");
                        for (int i = 0; i < mosques.size(); i++) {
                            endPoint.setLatitude(mosques.get(i).getMosqueelatitude());
                            endPoint.setLongitude(mosques.get(i).getMosqueelongitude());
                            distance = startPoint.distanceTo(endPoint);
                            distance/=1600;
                            mosques.get(i).setDistance(distance);
                            int heure= (int) (distance/60);
                            int mn = (int) (distance% 60);
                            mosques.get(i).setDuree(heure+"h"+mn+"min");
                        }
                        mosques.sort(Mosquee.distanceComparator);
                        MosqueeAdapter MyListAdapter = new MosqueeAdapter(mosques, getContext());
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MosquesFragment.this.getContext()));
                        ProgressBar progressBar= (ProgressBar) root_view.findViewById (R.id.progress_bar);
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setAdapter(MyListAdapter);
                    }}});}
    }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
            root_view=inflater.inflate(R.layout.fragment_mosques, container, false);
            new  HttpReqTask(Ville).execute();

            /*ImageButton home = (ImageButton ) root_view.findViewById(R.id.HOME1);
            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Intent intent = new Intent(HopitauxFragment.this.getActivity(), );
                   // MosquesFragment.this.getActivity().startActivity(intent);
                }
            });*/
            Chip all = (Chip) root_view.findViewById(R.id.allM);
            all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new HttpReqTask(Ville).execute();
                }});

        //implement searchview
        SearchView search=root_view.findViewById(R.id.mosquee_search);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override

            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                new  HttpReqTask1(Ville,newText).execute();

                return true;
            }});

                //closer to current localisation
                //get the current location
                Chip distance = (Chip) root_view.findViewById(R.id.ditance);
                distance.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new HttpReqTask2(Ville).execute();
                      }});
        return root_view ;
    }
    private class HttpReqTask extends AsyncTask<Void,Void, ArrayList <Mosquee >>
    {
        public String ville;
        public HttpReqTask(String ville) {
            super();
            this.ville = ville;
        }
        @Override
        protected ArrayList <Mosquee>doInBackground(Void... voids) {
            ArrayList <Mosquee > mosquees =new  ArrayList <Mosquee >() ;
            try {
                String apiUri="http://192.168.1.5:8080//villes/"+ville+"/mosquees";
                RestTemplate restTemplate=new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                mosquees= new ArrayList(Arrays.asList(restTemplate.getForObject(apiUri,Mosquee[].class)));
            }
            catch (Exception ex) {
                Log.e("GHUI",ex.getMessage());
            }
            return mosquees ;
        }
        @Override
        protected void onPostExecute(  ArrayList <Mosquee> mosquees ) {
            super.onPostExecute(mosquees );
            fetchAllMosquees(mosquees);

        }
    }
    private class HttpReqTask2 extends AsyncTask<Void,Void, ArrayList <Mosquee >>
    {
        public String ville;
        public HttpReqTask2(String ville) {
            super();
            this.ville = ville;
        }
        @Override
        protected ArrayList <Mosquee>doInBackground(Void... voids) {
            ArrayList <Mosquee > mosquees =new  ArrayList <Mosquee >() ;
            try {
                String apiUri="http://192.168.1.5:8080//villes/"+ville+"/mosquees";
                RestTemplate restTemplate=new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                mosquees= new ArrayList(Arrays.asList(restTemplate.getForObject(apiUri,Mosquee[].class)));
            }
            catch (Exception ex) {
                Log.e("GHUI",ex.getMessage());
            }
            return mosquees ;
        }
        @Override
        protected void onPostExecute(  ArrayList <Mosquee> mosquees ) {
            super.onPostExecute(mosquees );
           fetchAllMosqueessortedbydistace(mosquees);}}

    private class HttpReqTask1 extends AsyncTask<Void,Void, ArrayList <Mosquee>>
    {
       public String ville;
       public String name;
        public HttpReqTask1(String ville,String name) {
            super();
            this.ville = ville;
            this.name = name;
        }


        @Override
        protected ArrayList <Mosquee> doInBackground(Void... voids) {
            ArrayList <Mosquee > mosquees = new  ArrayList <Mosquee >() ;
            try {
                String apiUri="http://192.168.1.5:8080//villes/"+ville+"/mosqueename/"+name;
                RestTemplate restTemplate=new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                mosquees= new ArrayList(Arrays.asList(restTemplate.getForObject(apiUri,Mosquee[].class)));
            }
            catch (Exception ex) {
                Log.e("",ex.getMessage());
            }
            return mosquees ;
        }
        @Override
        protected void onPostExecute(  ArrayList <Mosquee> mosquees ) {
            super.onPostExecute(mosquees );
            fetchAllMosqueesByname(mosquees);

        }
    }
}