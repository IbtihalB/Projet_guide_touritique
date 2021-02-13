
package com.example.guide_touristique;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HopitauxFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressWarnings("unchecked")
public class HopitauxFragment extends Fragment {
    private static final int REQUEST_CALL = 1;
    FusedLocationProviderClient fusedLocationProviderClient;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "ville";
    Bundle bundle=this.getArguments();
    View root_view;


    // TODO: Rename and change types of parameters
    private static String Ville;
    public HopitauxFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     //* @param param1 Parameter 1.
     * @return A new instance of fragment HopitauxFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HopitauxFragment newInstance(String param1) {
        HopitauxFragment fragment = new HopitauxFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
       Ville= param1;

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @SuppressLint("WrongThread")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
         root_view = inflater.inflate(R.layout.fragment_hopitaux2, container, false);

        // Alimentation of the listview
         new HttpReqTask4(this.Ville).execute();

        /*ImageButton home = (ImageButton ) root_view.findViewById(R.id.HOME);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent = new Intent(HopitauxFragment.this.getActivity(), );
               // HopitauxFragment.this.getActivity().startActivity(intent);
            }
        });*/

        //Get the selected chip group
        Chip all = (Chip) root_view.findViewById(R.id.CHIPALL);
        Chip distance = (Chip) root_view.findViewById(R.id.ditance);
        Chip rating = (Chip) root_view.findViewById(R.id.rating);
        Chip prive = (Chip) root_view.findViewById(R.id.prive);
        Chip publicc = (Chip) root_view.findViewById(R.id.publicc);
        //All hospitals
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publicc.setChecked(false);
                prive.setChecked(false);
                rating.setChecked(false);
                distance.setChecked(false);
                new HttpReqTask4(Ville).execute();


            }});
        //type
        prive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publicc.setChecked(false);
                rating.setChecked(false);
                distance.setChecked(false);
                new HttpReqTask2(Ville,"privé").execute();

            }});
        publicc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prive.setChecked(false);
                rating.setChecked(false);
                distance.setChecked(false);
                new HttpReqTask2(Ville,"public").execute();
            }
        });

        //rating

        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publicc.setChecked(false);
                prive.setChecked(false);
                distance.setChecked(false);
                new HttpReqTask5(Ville).execute();

            }
        });
        //closer to current localisation
        //get the current location
        distance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publicc.setChecked(false);
                prive.setChecked(false);
                rating.setChecked(false);
                new HttpReqTask(Ville).execute();

                }

             });

        //Implementing the search view
        SearchView search = root_view.findViewById(R.id.hopital_search);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override

            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
              new HttpReqTask1(Ville,newText).execute();
                return true;
            }
        });
        return root_view;
    }


    private void makePhoneCall() {
        if (ActivityCompat.checkSelfPermission(
                HopitauxFragment.this.getContext(), Manifest.permission.CALL_PHONE) ==
                PackageManager.PERMISSION_GRANTED) {

            String Tel = getArguments().getString("tel");
            Intent call;
            call = new Intent(Intent.ACTION_CALL);
            call.setData(Uri.parse("tel:" + Tel));
            startActivity(call);
        } else {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected. In this UI,
            // include a "cancel" or "no thanks" button that allows the user to
            // continue using your app without granting the permission.
            ActivityCompat.requestPermissions(HopitauxFragment.this.getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(HopitauxFragment.this.getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private class HttpReqTask extends AsyncTask<Void,Void, ArrayList <Hopital >>
    {
        public String ville;
        public HttpReqTask(String ville) {
            super();
            this.ville = ville;
        }


        @Override
        protected ArrayList <Hopital >doInBackground(Void... voids) {

            ArrayList <Hopital > hopitals = new ArrayList <Hopital >();
            try {
                String apiUri="http://192.168.1.5:8080//villes/"+ville+"/hopitaux";
                RestTemplate restTemplate=new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                hopitals= new ArrayList(Arrays.asList(restTemplate.getForObject(apiUri,Hopital[].class)));

            }
            catch (Exception ex) {
                Log.e("",ex.getMessage());
            }

            return hopitals ;
        }

        @Override
        protected void onPostExecute(  ArrayList <Hopital >hopitals) {
            super.onPostExecute(hopitals);

                fetchAllHopitalssortedBydistance(hopitals);}

        }
        private class HttpReqTask5 extends AsyncTask<Void,Void, ArrayList <Hopital >>
        {
            public String ville;
            public HttpReqTask5(String ville) {
                super();
                this.ville = ville;
            }

            @Override
            protected ArrayList <Hopital >doInBackground(Void... voids) {

                ArrayList <Hopital > hopitals = new ArrayList <Hopital >();
                try {
                    String apiUri="http://192.168.1.5:8080//villes/"+ville+"/hopitalrating";
                    RestTemplate restTemplate=new RestTemplate();
                    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                    hopitals= new ArrayList(Arrays.asList(restTemplate.getForObject(apiUri,Hopital[].class)));

                }
                catch (Exception ex) {
                    Log.e("",ex.getMessage());
                }

                return hopitals ;
            }

            @Override
            protected void onPostExecute(  ArrayList <Hopital >hopitals) {
                super.onPostExecute(hopitals);

                    fetchAllHopitals(hopitals);}


        }
private class HttpReqTask4 extends AsyncTask<Void,Void, ArrayList <Hopital >>
{
    public String ville;
    public HttpReqTask4(String ville) {
        super();
        this.ville = ville;
    }


    @Override
    protected ArrayList <Hopital >doInBackground(Void... voids) {

        ArrayList <Hopital > hopitals = new ArrayList <Hopital >();
        try {
            String apiUri="http://192.168.1.5:8080//villes/"+ville+"/hopitaux";
            RestTemplate restTemplate=new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            hopitals= new ArrayList(Arrays.asList(restTemplate.getForObject(apiUri,Hopital[].class)));

        }
        catch (Exception ex) {
        Log.e("",ex.getMessage());
    }

        return hopitals ;
}

    @Override
    protected void onPostExecute(  ArrayList <Hopital >hopitals) {
        super.onPostExecute(hopitals);


            fetchAllHopitals(hopitals);

    }
}

    private void fetchAllHopitals(ArrayList<Hopital> hopitals) {


        RecyclerView recyclerView = (RecyclerView) root_view.findViewById(R.id.hopita_list);
        HopitalAdapter MyListAdapter = new HopitalAdapter(hopitals,getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(HopitauxFragment.this.getActivity()));
        ProgressBar progressBar= (ProgressBar) root_view.findViewById (R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        recyclerView.setAdapter(MyListAdapter);
    }

    private class HttpReqTask1 extends AsyncTask<Void,Void, ArrayList <Hopital >>
    {
        public String ville;
        public String name;
        public HttpReqTask1(String ville,String name) {
            super();
            this.ville = ville;
            this.name = name;
        }

        @Override
        protected ArrayList <Hopital >doInBackground(Void... voids) {

            ArrayList <Hopital > hopitals = new ArrayList <Hopital >();
            try {
                String apiUri="http://192.168.1.5:8080//villes/"+ville+"/hopitalname/"+name;
                RestTemplate restTemplate=new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                hopitals= new ArrayList(Arrays.asList(restTemplate.getForObject(apiUri,Hopital[].class)));

            }
            catch (Exception ex) {
                Log.e("",ex.getMessage());
            }

            return hopitals ;
        }

        @Override
        protected void onPostExecute(  ArrayList <Hopital> hopitals) {
            super.onPostExecute(hopitals);
                fetchAllHopitals(hopitals);
        }
    }
    private class HttpReqTask2 extends AsyncTask<Void,Void, ArrayList <Hopital >>
    {
        public String ville;
        public String type;
        public HttpReqTask2(String ville,String type) {
            super();
            this.ville = ville;
            this.type = type;
        }

        @Override
        protected ArrayList <Hopital >doInBackground(Void... voids) {

            ArrayList <Hopital > hopitals = new ArrayList <Hopital >();
            try {
                String apiUri="http://192.168.1.5:8080//villes/"+ville+"/hopitaltype/"+type;
                RestTemplate restTemplate=new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                hopitals= new ArrayList(Arrays.asList(restTemplate.getForObject(apiUri,Hopital[].class)));

            }
            catch (Exception ex) {
                Log.e("",ex.getMessage());
            }

            return hopitals ;
        }

        @Override
        protected void onPostExecute(  ArrayList <Hopital> hopitals) {
            super.onPostExecute(hopitals);


                fetchAllHopitals(hopitals);}

        }


    private void fetchAllHopitalssortedBydistance(ArrayList<Hopital> hopitaux) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(HopitauxFragment.this.getActivity());
        if (ActivityCompat.checkSelfPermission(HopitauxFragment.this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    Geocoder geocoder =null;
                    if (location != null) {
                        geocoder = new Geocoder(HopitauxFragment.this.getContext(), Locale.getDefault());

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
                        for (int i = 0; i < hopitaux.size(); i++) {
                            endPoint.setLatitude(hopitaux.get(i).getHopitallatitude());
                            endPoint.setLongitude(hopitaux.get(i).getHopitallongitude());
                            distance = endPoint.distanceTo(startPoint);
                            distance/=1600;
                            int heure= (int) (distance/60);
                            int mn = (int) (distance% 60);

                            hopitaux.get(i).setDistance(distance);
                            hopitaux.get(i).setDuree(heure+"h"+mn+"min");

                        }
                        RecyclerView recyclerView = (RecyclerView) root_view.findViewById(R.id.hopita_list);
                        hopitaux.sort(Hopital.distanceComparator);
                        HopitalAdapter MyListAdapter = new HopitalAdapter(hopitaux, getContext());
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(HopitauxFragment.this.getContext()));
                        ProgressBar progressBar= (ProgressBar) root_view.findViewById (R.id.progress_bar);
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setAdapter(MyListAdapter);
                    }}});
        }






        else {
            ActivityCompat.requestPermissions(HopitauxFragment.this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }
    }

