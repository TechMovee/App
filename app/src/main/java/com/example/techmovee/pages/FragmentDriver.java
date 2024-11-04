package com.example.techmovee.pages;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techmovee.ApiService;
import com.example.techmovee.R;
import com.example.techmovee.SpaceItemDecoration;
import com.example.techmovee.driver.AdapterMotorista;
import com.example.techmovee.driver.Motorista;
import com.example.techmovee.van.Van;

import java.util.ArrayList;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentDriver extends Fragment {

    private EditText editTextSearch;
    private RecyclerView recyclerView;
    private AdapterMotorista adapterMotorista;
    private List<Motorista> motoristas = new ArrayList<>();
    private List<Van> vans = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_driver, container, false);

        editTextSearch = view.findViewById(R.id.pesquisar);
        recyclerView = view.findViewById(R.id.recyclerView);

        // Configuração do RecyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Configurar o adapter
        adapterMotorista = new AdapterMotorista(motoristas, vans);
        recyclerView.setAdapter(adapterMotorista);

        // Carregar dados da API
        // Adicionando a SpaceItemDecoration
        int spacingInPixels = 15; // Espaçamento entre os itens, ajuste conforme necessário
        recyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        carregarDados();

        return view;
    }

    private void carregarDados() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://apispring-s3hy.onrender.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        // Chamada para obter motoristas
        Call<List<Motorista>> callMotoristas = apiService.getMotoristas();
        callMotoristas.enqueue(new Callback<List<Motorista>>() {
            @Override
            public void onResponse(Call<List<Motorista>> call, Response<List<Motorista>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    motoristas.addAll(response.body());
                    adapterMotorista.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Erro ao carregar motoristas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Motorista>> call, Throwable t) {
                Toast.makeText(getContext(), "Falha na conexão", Toast.LENGTH_SHORT).show();
                Log.e("FragmentDriver", "Erro: " + t.getMessage());
            }
        });

        // Chamada para obter vans
        Call<List<Van>> callVans = apiService.getVan();
        callVans.enqueue(new Callback<List<Van>>() {
            @Override
            public void onResponse(Call<List<Van>> call, Response<List<Van>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    vans.addAll(response.body());
                    adapterMotorista.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Erro ao carregar vans", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Van>> call, Throwable t) {
                Toast.makeText(getContext(), "Falha na conexão", Toast.LENGTH_SHORT).show();
                Log.e("FragmentDriver", "Erro: " + t.getMessage());
            }
        });
    }
}