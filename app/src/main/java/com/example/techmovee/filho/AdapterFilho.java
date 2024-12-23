package com.example.techmovee.filho;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.techmovee.R;
import com.example.techmovee.pages.Perfil.EditarFilho;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterFilho extends RecyclerView.Adapter<AdapterFilho.MeuViewHolder> {

    private List<Filho> listaFilho;

    public AdapterFilho(List<Filho> listaFilho) {
        this.listaFilho = listaFilho;
    }

    @NonNull
    @Override
    public AdapterFilho.MeuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // carregar o Template de visualização
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.son_info_card,parent,false);

        // chamar o MeuViewHolder para carregar os dados
        return new MeuViewHolder(viewItem);
    }
    @Override
    public void onBindViewHolder(@NonNull AdapterFilho.MeuViewHolder holder, int position) {
        Picasso.get().load(listaFilho.get(position).getImageUrl()).into(holder.foto);

        holder.nome.setText(listaFilho.get(position).getNome());
        holder.escola.setText(listaFilho.get(position).getEscola());
        holder.idade.setText(listaFilho.get(position).getIdade());
        holder.periodo.setText(listaFilho.get(position).getPeriodoInicial() + " às " + listaFilho.get(position).getPeriodoFinal());
        holder.deficiencia.setText(listaFilho.get(position).getDeficiente());
        holder.serieturma.setText(listaFilho.get(position).getSerie());

        holder.editarFilho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EditarFilho.class);
                v.getContext().startActivity(intent);
            }
        });

    }
    @Override
    public int getItemCount() {
        return listaFilho.size();
    }

    public class MeuViewHolder extends RecyclerView.ViewHolder {

        // criar os elementos de visualização
        ImageView foto;
        TextView nome, escola, idade, periodo, deficiencia, serieturma;
        ImageView editarFilho;

        public MeuViewHolder(@NonNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.foto);
            nome = itemView.findViewById(R.id.nome);
            escola = itemView.findViewById(R.id.escola);
            idade = itemView.findViewById(R.id.idade);
            periodo = itemView.findViewById(R.id.periodo);
            deficiencia = itemView.findViewById(R.id.deficiencia);
            serieturma = itemView.findViewById(R.id.serieturma);
            editarFilho = itemView.findViewById(R.id.editarFilho);
        }
    }
}
