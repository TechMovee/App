package com.example.techmovee;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.techmovee.pages.FragmentMap2;
import com.example.techmovee.pages.FragmentPerfil;
import com.example.techmovee.pages.FragmentDriver;
import com.example.techmovee.pages.FragmentMap;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity2 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        BottomNavigationView footer = findViewById(R.id.footer);

        footer.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;


            if(item.getItemId() == R.id.menu_driver){
                selectedFragment = new FragmentDriver();
            } else if(item.getItemId() == R.id.menu_map){
                selectedFragment = new FragmentMap2();
            }

            else if(item.getItemId() == R.id.menu_perfil){
                selectedFragment = new FragmentPerfil();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_main, selectedFragment)
                        .commit();
            }

            return true;
        });

        footer.setSelectedItemId(R.id.menu_driver);


    }
}
