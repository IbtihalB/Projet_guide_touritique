                    package com.example.guide_touristique;

                    import android.Manifest;
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
                    import android.widget.TextView;
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
                     * Use the {@link pharmacieesFragment#newInstance} factory method to
                     * create an instance of this fragment.
                     */
                    public class pharmacieesFragment extends Fragment {
                        FusedLocationProviderClient fusedLocationProviderClient;
                        View root_view;

                        private static final int REQUEST_CALL=1;
                        // TODO: Rename parameter arguments, choose names that match
                        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
                        private static final String ARG_PARAM1 = "ville";
                        private static final String ARG_PARAM2 = "param2";

                        // TODO: Rename and change types of parameters
                        private static String Ville;
                        private  String mParam2;

                        public pharmacieesFragment() {
                            // Required empty public constructor
                        }

                        /**
                         * Use this factory method to create a new instance of
                         * this fragment using the provided parameters.
                         *
                         * @param param1 Parameter 1.
                         * @return A new instance of fragment pharmacieesFragment.
                         */
                        // TODO: Rename and change types and number of parameters
                        public static pharmacieesFragment newInstance(String param1) {
                            pharmacieesFragment fragment = new pharmacieesFragment();
                            Bundle args = new Bundle();
                            args.putString(ARG_PARAM1, param1);
                            Ville=param1;
                            fragment.setArguments(args);
                            return fragment;
                        }

                        @Override
                        public void onCreate(Bundle savedInstanceState) {
                            super.onCreate(savedInstanceState);
                            if (getArguments() != null) {
                                Ville = getArguments().getString(ARG_PARAM1);
                            }
                        }

                        @Override
                        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                                 Bundle savedInstanceState) {
                            // Inflate the layout for this fragment
                             root_view=inflater.inflate(R.layout.fragment_pharmaciees, container, false);
                             new HttpReqTask3(Ville).execute();
                             Chip all = (Chip) root_view.findViewById(R.id.allP);

                           /* ImageButton home = (ImageButton ) root_view.findViewById(R.id.HOME);
                            home.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Intent intent = new Intent(HopitauxFragment.this.getActivity(), );
                                     //pharmacieesFragment.this.getActivity().startActivity(intent);
                                }
                            });*/

                             all.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    new HttpReqTask3(Ville).execute();
                                }});
                            //implement searchview
                             SearchView search=root_view.findViewById(R.id.pharmacie_search);
                             search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                @Override

                                public boolean onQueryTextSubmit(String query) {

                                    return false;
                                }


                                @Override
                                public boolean onQueryTextChange(String newText) {
                                    new HttpReqTask4(Ville,newText).execute();



                                    return true;
                                }
                            });


                    //closer to current localisation
                            //get the current location
                            Chip distance = (Chip) root_view.findViewById(R.id.ditance);

                            distance.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    new HttpReqTask5(Ville).execute();
                                }});

                            return root_view;
                        }
                        private void makePhoneCall()
                        { if (ActivityCompat.checkSelfPermission(
                                pharmacieesFragment.this.getContext(), Manifest.permission.CALL_PHONE) ==
                                PackageManager.PERMISSION_GRANTED) {

                            String Tel =getArguments().getString("tel");
                            Intent call;
                            call = new Intent(Intent.ACTION_CALL);
                            call.setData(Uri.parse("tel:"+Tel));
                            startActivity(call);
                        } else  {
                            // In an educational UI, explain to the user why your app requires this
                            // permission for a specific feature to behave as expected. In this UI,
                            // include a "cancel" or "no thanks" button that allows the user to
                            // continue using your app without granting the permission.
                            ActivityCompat.requestPermissions(pharmacieesFragment.this.getActivity(),new String []{Manifest.permission.CALL_PHONE},REQUEST_CALL); }}

                        @Override
                        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                            if(requestCode==REQUEST_CALL){
                                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                                {makePhoneCall();}
                                else{
                                    Toast.makeText(pharmacieesFragment.this.getActivity(),"Permission Denied",Toast.LENGTH_SHORT).show();}
                            }
                        }
                        private class HttpReqTask3 extends AsyncTask<Void,Void, ArrayList<Pharmacie>>
                        {
                            public String ville;
                            public HttpReqTask3(String ville) {
                                super();
                                this.ville = ville;}
                            @Override
                            protected ArrayList<Pharmacie> doInBackground(Void... voids) {
                                ArrayList <Pharmacie> pharmacies = new ArrayList <Pharmacie>() ;
                                try {
                                    String apiUri="http://192.168.1.5:8080/villes/"+ville+"/pharmacies";
                                    RestTemplate restTemplate=new RestTemplate();
                                    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                                    pharmacies= new ArrayList(Arrays.asList(restTemplate.getForObject(apiUri,Pharmacie[].class)));

                                }
                                catch (Exception ex) {
                                    Log.e("",ex.getMessage());
                                }
                                return pharmacies ;
                            }
                            @Override
                            protected void onPostExecute(  ArrayList <Pharmacie>  pharmacies) {
                                super.onPostExecute(pharmacies );

                                fetchAllPharmacies(pharmacies);}
                        }

                            private void fetchAllPharmacies(ArrayList<Pharmacie> pharmacies) {

                                ProgressBar progressBar= (ProgressBar) root_view.findViewById (R.id.progress_bar);
                                RecyclerView recyclerView = (RecyclerView) root_view.findViewById (R.id.pharmacie_list);
                                PharmacieAdapter MyListAdapter =  new PharmacieAdapter (pharmacies, getContext());
                                recyclerView.setHasFixedSize ( true );
                                recyclerView.setLayoutManager ( new LinearLayoutManager(this.getActivity() ));
                                progressBar.setVisibility(View.GONE);
                                recyclerView.setAdapter (MyListAdapter);
                            }

                        private class HttpReqTask4 extends AsyncTask<Void,Void, ArrayList<Pharmacie>>
                        {
                            public String ville;
                            public String name;
                            public HttpReqTask4(String ville,String name) {
                                super();
                                this.ville = ville;
                                this.name = name;
                            }
                            @Override
                            protected ArrayList<Pharmacie> doInBackground(Void... voids) {
                                ArrayList <Pharmacie> pharmacies =new ArrayList <Pharmacie>() ;
                                try {
                                    String apiUri="http://192.168.1.5:8080/villes/"+ville+"/pharmaciename/"+name;
                                    RestTemplate restTemplate=new RestTemplate();
                                    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                                    pharmacies= new ArrayList(Arrays.asList(restTemplate.getForObject(apiUri,Pharmacie[].class)));

                                }
                                catch (Exception ex) {
                                    Log.e("",ex.getMessage());
                                }
                                return pharmacies ;
                            }
                            @Override
                            protected void onPostExecute(  ArrayList <Pharmacie>  pharmacies) {
                                super.onPostExecute(pharmacies );
                                fetchAllPharmaciesbyname(pharmacies);
                            }}

                            private void fetchAllPharmaciesbyname(ArrayList<Pharmacie> pharmacies) {
                                RecyclerView recyclerView = (RecyclerView) root_view.findViewById (R.id.pharmacie_list);
                                PharmacieAdapter MyListAdapter =  new PharmacieAdapter (pharmacies, getContext());
                                recyclerView.setHasFixedSize ( true );
                                recyclerView.setLayoutManager ( new LinearLayoutManager(this.getActivity()));
                                    ProgressBar progressBar= (ProgressBar) root_view.findViewById (R.id.progress_bar);
                                    progressBar.setVisibility(View.GONE);
                                recyclerView.setAdapter (MyListAdapter);
                            }

                        private class HttpReqTask5 extends AsyncTask<Void,Void, ArrayList<Pharmacie>>
                        {
                            public String ville;
                            public HttpReqTask5(String ville) {
                                super();
                                this.ville = ville;}
                            @Override
                            protected ArrayList<Pharmacie> doInBackground(Void... voids) {
                                ArrayList <Pharmacie> pharmacies = new ArrayList <Pharmacie>() ;
                                try {
                                    String apiUri="http://192.168.1.5:8080/villes/"+ville+"/pharmacies";
                                    RestTemplate restTemplate=new RestTemplate();
                                    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                                    pharmacies= new ArrayList(Arrays.asList(restTemplate.getForObject(apiUri,Pharmacie[].class)));

                                }
                                catch (Exception ex) {
                                    Log.e("hhhhhhhhhhhhh",ex.getMessage());
                                }
                                return pharmacies ;
                            }
                            @Override
                            protected void onPostExecute(  ArrayList <Pharmacie>  pharmacies) {
                                super.onPostExecute(pharmacies );

                                fetchAllphamaciessortedbydistace(pharmacies);
                                }
                            }

                            private void fetchAllphamaciessortedbydistace(ArrayList<Pharmacie> pharmacies) {

                                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(pharmacieesFragment.this.getActivity());
                                if (ActivityCompat.checkSelfPermission(pharmacieesFragment.this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                    fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                                        @RequiresApi(api = Build.VERSION_CODES.N)
                                        @Override
                                        public void onComplete(@NonNull Task<Location> task) {
                                            Location location = task.getResult();
                                            Geocoder geocoder = null;
                                            if (location != null) {
                                                geocoder = new Geocoder(pharmacieesFragment.this.getContext(), Locale.getDefault());

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
                                                for (int i = 0; i < pharmacies.size(); i++) {
                                                    endPoint.setLatitude(pharmacies.get(i).getPharmacielatitude());
                                                    endPoint.setLongitude(pharmacies.get(i).getPharmacielongitude());
                                                    distance = startPoint.distanceTo(endPoint);
                                                    distance/=1333.3333333333;
                                                    pharmacies.get(i).setDistance(distance);
                                                    int heure= (int) (distance/60);
                                                    int mn = (int) (distance% 60);
                                                   pharmacies.get(i).setDuree(heure+"h"+mn+"min");

                                                }
                                                pharmacies.sort(Pharmacie.distanceComparator);
                                                RecyclerView recyclerView = (RecyclerView) root_view.findViewById (R.id.pharmacie_list);
                                                PharmacieAdapter MyListAdapter = new PharmacieAdapter(pharmacies, getContext());
                                                recyclerView.setHasFixedSize(true);
                                                recyclerView.setLayoutManager(new LinearLayoutManager(pharmacieesFragment.this.getContext()));
                                                ProgressBar progressBar= (ProgressBar) root_view.findViewById (R.id.progress_bar);
                                                progressBar.setVisibility(View.GONE);
                                                recyclerView.setAdapter(MyListAdapter);
                                            } }});}
                            }



                    }