package com.example.techmovee.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.techmovee.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FragmentMap extends Fragment {
    boolean conexao = true;
    private Button btnConectar;

    //verificar se tem conexao com motorista

    //se não tiver mostrar fragment de conexao
    //se tiver mostrar rota do motorista

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(conexao==true){
            View view = inflater.inflate(R.layout.activity_fragment_map2, container, false);

            return view;
        }else {
            View view = inflater.inflate(R.layout.activity_fragment_map, container, false);

            Button btnNavigate = view.findViewById(R.id.conectar);
            btnNavigate.setOnClickListener(v -> {
                Fragment newFragment = new FragmentDriver(); // Substitua por seu fragmento de destino
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_main, newFragment); // R.id.fragment_container é onde o fragmento é exibido
                transaction.addToBackStack(null); // Para permitir voltar ao fragmento anterior
                transaction.commit();
            });

            return view;

        }

    }
}