package com.example.techmovee.driver;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techmovee.R;
import com.example.techmovee.models.Telefone;
import com.example.techmovee.pages.FragmentConexao;
import com.example.techmovee.van.Van;
import com.squareup.picasso.Picasso;


import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class AdapterMotorista extends RecyclerView.Adapter<AdapterMotorista.MeuViewHolder> {

    private List<Motorista> listaMotorista;
    private List<Van> listaVan;
    private List<Telefone> listaTelefone;

    public AdapterMotorista(List<Motorista> listaMotorista, List<Van> listaVan, List<Telefone> listaTelefone) {
        this.listaMotorista = listaMotorista;
        this.listaVan = listaVan;
        this.listaTelefone = listaTelefone;
    }

    @NonNull
    @Override
    public AdapterMotorista.MeuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.motorista_card, parent, false);
        return new AdapterMotorista.MeuViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMotorista.MeuViewHolder holder, int position) {
        Motorista motorista = listaMotorista.get(position);
        Van van = listaVan.get(position);

        // Buscar o telefone correto baseado no telefone_id do motorista
        Telefone telefone = getTelefoneById(motorista.getTelefone_id());

        holder.nome.setText(motorista.getNome());

        // Calcular idade
        String dataNascimentoString = motorista.getDataNascimento();
        LocalDate dataNascimento = LocalDate.parse(dataNascimentoString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        int idade = Period.between(dataNascimento, LocalDate.now()).getYears();
        holder.idade.setText(String.valueOf(idade));

        holder.valor.setText("R$" + van.getMensalidade());

        // Verificar se o telefone foi encontrado antes de tentar usá-lo
        if (telefone != null) {
            holder.telefone.setText(telefone.getNumero());
        } else {
            holder.telefone.setText("Telefone não encontrado");
        }

        holder.btnContatar.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), FragmentConexao.class);
            view.getContext().startActivity(intent);
        });
    }

    // Método para buscar o Telefone pelo telefone_id
    private Telefone getTelefoneById(int telefoneId) {
        for (Telefone telefone : listaTelefone) {
            if (telefone.getId() == telefoneId) {
                return telefone;
            }
        }
        return null; // Retorna null se não encontrar o telefone correspondente
    }


    @Override
    public int getItemCount() {
        // Retorna o número de motoristas, assumindo que listaMotorista e listaVan têm o mesmo tamanho
        return Math.min(listaMotorista.size(), listaVan.size());
    }

    public class MeuViewHolder extends RecyclerView.ViewHolder {
        ImageView foto;
        TextView nome, escola, idade, valor, telefone;
        Button btnContatar;


        public MeuViewHolder(@NonNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.foto);
            nome = itemView.findViewById(R.id.nome);
            escola = itemView.findViewById(R.id.escola);
            idade = itemView.findViewById(R.id.idade);
            valor = itemView.findViewById(R.id.valor);
            telefone = itemView.findViewById(R.id.telefone);
            btnContatar = itemView.findViewById(R.id.btnContatar);
        }
    }
}