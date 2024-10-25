package com.example.techmovee.pages;

import static android.content.Intent.getIntent;
import static androidx.constraintlayout.widget.ConstraintSet.WRAP_CONTENT;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techmovee.MainActivity;
import com.example.techmovee.R;
import com.example.techmovee.filho.AdapterFilho;
import com.example.techmovee.filho.Filho;
import com.example.techmovee.filho.FilhoUtil;
import com.example.techmovee.pages.EditarPerfil.EditarPerfil;
import com.example.techmovee.responsible.Responsavel2;


import java.util.ArrayList;
import java.util.List;

public class FragmentPerfil extends Fragment {

    //Lista do adapter de filho
    RecyclerView rvFilho;
    List<Filho> listaFilho;
    private AdapterFilho filhoAdapter;

    Button editarPerfil, sair;
    private TextView nomeTextView;
    private TextView emailTextView;
    private TextView telefoneTextView;
    private TextView nomeGrande;
    private TextView idade;
    private TextView quantFilhos;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_perfil, container, false);

        rvFilho = view.findViewById(R.id.recyclerView);
        rvFilho.setLayoutManager(new LinearLayoutManager(getContext()));

        editarPerfil = view.findViewById(R.id.editarPerfil);
        sair = view.findViewById(R.id.sair);

        // Referenciando os TextViews no layout
        nomeGrande = view.findViewById(R.id.nomeGrande);
        nomeTextView = view.findViewById(R.id.nome);
        emailTextView = view.findViewById(R.id.email);
        telefoneTextView = view.findViewById(R.id.telefone);
        idade = view.findViewById(R.id.idade);
        quantFilhos = view.findViewById(R.id.filhos);

        editarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditarPerfil.class);
                startActivity(intent);
                // Adiciona transição
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.sair_dialog);
                dialog.getWindow().setLayout(WRAP_CONTENT, WRAP_CONTENT);
                dialog.setCancelable(false);

                Button naoSair = dialog.findViewById(R.id.naoSair);
                Button simSair = dialog.findViewById(R.id.simSair);

                naoSair.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                simSair.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
                dialog.show();
            }
        });

        nomeGrande.setText(Responsavel2.nome);
        emailTextView.setText(Responsavel2.email);
        telefoneTextView.setText(Responsavel2.telefone);
        nomeTextView.setText(Responsavel2.nome);
        idade.setText(Responsavel2.dataNascimento);
        quantFilhos.setText("1");

        //Puxar filho
        String nomeFilho = FilhoUtil.nome;
        String cpfFilho = FilhoUtil.cpf;
        String escolaFilho = FilhoUtil.escola;
        String idadeFilho = FilhoUtil.idade;
        String serieFilho = FilhoUtil.serie;
        String deficienteFilho = FilhoUtil.deficiente;
        String imageUrlFilho = FilhoUtil.imageUrl;
        String periodoInicialFilho = FilhoUtil.periodoInicial;
        String periodoFinalFilho = FilhoUtil.periodoFinal;

        listaFilho = new ArrayList<>();
        listaFilho.add(new Filho(nomeFilho,cpfFilho,idadeFilho,serieFilho,deficienteFilho,imageUrlFilho,periodoInicialFilho,periodoFinalFilho,escolaFilho));


        //Configurar o RecycleView
        AdapterFilho adapterFilho = new AdapterFilho(listaFilho);
        rvFilho.setAdapter(adapterFilho);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
