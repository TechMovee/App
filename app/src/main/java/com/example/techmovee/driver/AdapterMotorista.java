package com.example.techmovee.driver;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techmovee.R;
import com.example.techmovee.van.Van;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdapterMotorista extends RecyclerView.Adapter<AdapterMotorista.MeuViewHolder> {

    private List<Motorista> listaMotorista;
    private List<Van> listaVan;

    public AdapterMotorista(List<Motorista> listaMotorista, List<Van> listaVan) {
        this.listaMotorista = listaMotorista;
        this.listaVan = listaVan;
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

//        Picasso.get().load(motorista.getImageUrl()).into(holder.foto);
        holder.nome.setText(motorista.getNome());
        // Calcular idade
        String dataNascimentoString = motorista.getDataNascimento();
        LocalDate dataNascimento = LocalDate.parse(dataNascimentoString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        int idade = Period.between(dataNascimento, LocalDate.now()).getYears();

        // Definir a idade no TextView
        holder.idade.setText(String.valueOf(idade));
        holder.valor.setText("R$" + van.getMensalidade());
        holder.telefone.setText(motorista.getTelefone());
    }

    @Override
    public int getItemCount() {
        // Retorna o número de motoristas, assumindo que listaMotorista e listaVan têm o mesmo tamanho
        return Math.min(listaMotorista.size(), listaVan.size());
    }

    public class MeuViewHolder extends RecyclerView.ViewHolder {
        ImageView foto;
        TextView nome, escola, idade, valor, telefone;


        public MeuViewHolder(@NonNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.foto);
            nome = itemView.findViewById(R.id.nome);
            escola = itemView.findViewById(R.id.escola);
            idade = itemView.findViewById(R.id.idade);
            valor = itemView.findViewById(R.id.valor);
            telefone = itemView.findViewById(R.id.telefone);
        }
    }
}