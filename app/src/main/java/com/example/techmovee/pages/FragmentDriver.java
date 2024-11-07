package com.example.techmovee.pages;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.example.techmovee.models.Telefone;
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
    private List<Telefone> telefones = new ArrayList<>();
    private Spinner spinnerFiltro;
    private Button btnFiltro;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_driver, container, false);

        editTextSearch = view.findViewById(R.id.pesquisar);
        recyclerView = view.findViewById(R.id.recyclerView);
        btnFiltro = view.findViewById(R.id.btnFiltro);
        spinnerFiltro = view.findViewById(R.id.spinnerFiltro);

        // Configuração do RecyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Configurar o adapter
        adapterMotorista = new AdapterMotorista(motoristas, vans, telefones);
        recyclerView.setAdapter(adapterMotorista);

        // Adicionando a SpaceItemDecoration
        int spacingInPixels = 15; // Espaçamento entre os itens, ajuste conforme necessário
        recyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));

        // Configurando o Spinner com opções de filtro
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(getContext(),
                R.array.opcoes_filtro, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFiltro.setAdapter(adapterSpinner);

        // Configuração do clique do botão de filtro
        btnFiltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Alterna a visibilidade do Spinner
                if (spinnerFiltro.getVisibility() == View.GONE) {
                    spinnerFiltro.setVisibility(View.VISIBLE);
                } else {
                    spinnerFiltro.setVisibility(View.GONE);
                }
            }
        });

        // Listener para o Spinner
        spinnerFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedFilter = parent.getItemAtPosition(position).toString();
                aplicarFiltro(selectedFilter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Não faz nada
            }
        });

        // Carregar dados da API
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
                    motoristas.clear();
                    motoristas.addAll(response.body());
                    carregarTelefones(apiService);  // Carregar telefones depois de obter motoristas
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
                    vans.clear();
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

    private void carregarTelefones(ApiService apiService) {
        Call<List<Telefone>> callTelefones = apiService.getTelefones();
        callTelefones.enqueue(new Callback<List<Telefone>>() {
            @Override
            public void onResponse(Call<List<Telefone>> call, Response<List<Telefone>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    telefones.clear();
                    telefones.addAll(response.body());
                    adapterMotorista.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Erro ao carregar telefones", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Telefone>> call, Throwable t) {
                Toast.makeText(getContext(), "Falha na conexão", Toast.LENGTH_SHORT).show();
                Log.e("FragmentDriver", "Erro: " + t.getMessage());
            }
        });
    }

    private void aplicarFiltro(String filtro) {
        String endpoint;
        switch (filtro) {
            case "Mensalidade":
                endpoint = "selecionarMensalidade";
                break;
            case "Capacidade":
                endpoint = "selecionarCapacidade";
                break;
            case "Suporte PCD":
                endpoint = "selecionarAcessivel";
                break;
            default:
                endpoint = null;
        }

        if (endpoint == null) {
            motoristas.clear();
            vans.clear();
            carregarDados();  // Recarregar todos os dados
        } else {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://apispring-s3hy.onrender.com/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiService apiService = retrofit.create(ApiService.class);
            Call<List<Van>> callFiltrarVans = apiService.filtrar(endpoint);
            callFiltrarVans.enqueue(new Callback<List<Van>>() {
                @Override
                public void onResponse(Call<List<Van>> call, Response<List<Van>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        vans.clear();
                        vans.addAll(response.body());
                        adapterMotorista.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "Erro ao aplicar filtro para vans", Toast.LENGTH_SHORT).show();
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




}
