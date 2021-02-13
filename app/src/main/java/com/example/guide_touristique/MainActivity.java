package com.example.guide_touristique;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.Hopitaux)
    Button Hopitaux;
    @BindView(R.id.Pharmacies)
    Button Pharmacies;
    @BindView(R.id.Mosquees)
    Button Mosquees;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        String ville = "Agadir";
        Bundle bundle=new Bundle();
        bundle.putString("ville",ville);
        HopitauxFragment hopitaux_fragement= HopitauxFragment.newInstance(ville);
        pharmacieesFragment Pharmacies_fragement=pharmacieesFragment.newInstance(ville);
        MosquesFragment mosquees_fragement= MosquesFragment.newInstance(ville);

       Mosquees.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().addToBackStack("mosque3").replace(R.id.fragment,mosquees_fragement).commit();

            }
            });

        Pharmacies.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().addToBackStack("mosqueee1").replace(R.id.fragment,Pharmacies_fragement).commit();

            }
        });
        Hopitaux.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().addToBackStack("mosquee2").replace(R.id.fragment,hopitaux_fragement).commit();

            }
        });

    }
}