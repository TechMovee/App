package com.example.techmovee.pages;

import static androidx.constraintlayout.widget.ConstraintSet.WRAP_CONTENT;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.techmovee.ApiService;
import com.example.techmovee.MainActivity;
import com.example.techmovee.R;
import com.example.techmovee.RetrofitClient;
import com.example.techmovee.filho.AdapterFilho;
import com.example.techmovee.filho.Filho;
import com.example.techmovee.filho.FilhoUtil;
import com.example.techmovee.models.Telefone;
import com.example.techmovee.pages.Perfil.AddFilho;
import com.example.techmovee.pages.Perfil.EditarPerfil;
import com.example.techmovee.responsible.Responsavel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentPerfil extends Fragment {

    private RecyclerView rvFilho;
    private List<Filho> listaFilho;
    private AdapterFilho filhoAdapter;

    private Button editarPerfil, sair;
    private TextView nomeTextView, emailTextView, telefoneTextView, nomeGrande, idadeTextView;
    private ImageView fotoPerfil, addFilho;

    private ApiService apiService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_perfil, container, false);

        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        FirebaseAuth autenticacao = FirebaseAuth.getInstance();
        FirebaseUser userLogin = autenticacao.getCurrentUser();

        nomeGrande = view.findViewById(R.id.nomeGrande);
        nomeTextView = view.findViewById(R.id.nome);
        emailTextView = view.findViewById(R.id.email);
        telefoneTextView = view.findViewById(R.id.telefone);
        idadeTextView = view.findViewById(R.id.idade);
        fotoPerfil = view.findViewById(R.id.fotoPerfil);
        editarPerfil = view.findViewById(R.id.editarPerfil);
        sair = view.findViewById(R.id.sair);
        addFilho = view.findViewById(R.id.addFilho);

        if (userLogin != null) {
            buscarResponsavelPorEmail(userLogin.getEmail(), view);


        } else {
            // Redirecionar para a tela de login se o usuário não estiver autenticado
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        }

        // Configuração do RecyclerView
        rvFilho = view.findViewById(R.id.recyclerView);
        rvFilho.setLayoutManager(new LinearLayoutManager(getContext()));
        carregarFilhos();

        editarPerfil.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), EditarPerfil.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        sair.setOnClickListener(view12 -> mostrarDialogoSair());

        addFilho.setOnClickListener(view13 -> {
            Intent intent = new Intent(getContext(), AddFilho.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        return view;
    }

    private void buscarResponsavelPorEmail(String email, View view) {
        String encodedEmail = email.replace("@", "%40");  // Codifique o e-mail para a URL

        Call<Responsavel> call = apiService.getResponsavel(encodedEmail, email);

        call.enqueue(new Callback<Responsavel>() {
            @Override
            public void onResponse(Call<Responsavel> call, Response<Responsavel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Responsavel responsavel = response.body();

                    nomeTextView.setText(responsavel.getNome());
                    nomeGrande.setText(responsavel.getNome());
                    emailTextView.setText(responsavel.getEmail());

                    String dataNascimentoString = responsavel.getDataNascimento();
                    LocalDate dataNascimento = LocalDate.parse(dataNascimentoString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    int idade = Period.between(dataNascimento, LocalDate.now()).getYears();
                    idadeTextView.setText(String.valueOf(idade));

                    // Verifica o ID do telefone antes de chamar a API para buscar o telefone
                    Integer telefoneId = responsavel.getTelefone_id();
                    if (telefoneId != null) {
                        buscarTelefonePorId(telefoneId);
                    } else {
                        Toast.makeText(getContext(), "Telefone não cadastrado", Toast.LENGTH_SHORT).show();
                    }

                    // Carregar imagem do perfil com Glide
                    if (responsavel.getImageUrl() != null && responsavel.getImageUrl() != null) {
                        Glide.with(FragmentPerfil.this)
                                .load(responsavel.getImageUrl())
                                .centerCrop()
                                .into(fotoPerfil);
                    } else {
                        fotoPerfil.setImageResource(R.drawable.perfil); // Imagem padrão
                    }
                } else {
                    Toast.makeText(getContext(), "Responsável não encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Responsavel> call, Throwable t) {
                Toast.makeText(getContext(), "Erro ao buscar dados: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("API Error", t.getMessage(), t);
            }
        });
    }

    private void buscarTelefonePorId(Integer id) {
        if (id == null) {
            // Exibe uma mensagem informando que o ID é nulo
            Toast.makeText(getContext(), "ID do telefone não pode ser nulo", Toast.LENGTH_SHORT).show();
            return; // Interrompe a execução para evitar erro
        }

        // Faz a chamada à API para buscar o telefone com o ID fornecido
        Call<Telefone> call = apiService.getTelefoneById(id,id);

        call.enqueue(new Callback<Telefone>() {
            @Override
            public void onResponse(Call<Telefone> call, Response<Telefone> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Telefone telefone = response.body();
                    telefoneTextView.setText(telefone.getNumero());
                } else {
                    Toast.makeText(getContext(), "Telefone não encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Telefone> call, Throwable t) {
                Toast.makeText(getContext(), "Erro ao buscar telefone: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("API Error", t.getMessage(), t);
            }
        });
    }

    private void carregarFilhos() {
        listaFilho = new ArrayList<>();
        listaFilho.add(new Filho(
                FilhoUtil.nome,
                FilhoUtil.cpf,
                FilhoUtil.idade,
                FilhoUtil.serie,
                FilhoUtil.deficiente,
                FilhoUtil.imageUrl,
                FilhoUtil.periodoInicial,
                FilhoUtil.periodoFinal,
                FilhoUtil.escola
        ));

        filhoAdapter = new AdapterFilho(listaFilho);
        rvFilho.setAdapter(filhoAdapter);
    }

    private void mostrarDialogoSair() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.exit_dialog);
        dialog.getWindow().setLayout(WRAP_CONTENT, WRAP_CONTENT);
        dialog.setCancelable(false);

        Button naoSair = dialog.findViewById(R.id.naoSair);
        Button simSair = dialog.findViewById(R.id.simSair);

        naoSair.setOnClickListener(view -> dialog.dismiss());
        simSair.setOnClickListener(view -> {
            dialog.dismiss();
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        dialog.show();
    }
}
