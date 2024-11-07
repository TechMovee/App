package com.example.techmovee.driver;

import com.google.gson.annotations.SerializedName;

public class Motorista {

    private String nome;
    private String email;
    private String senha;
    private String cpf;
    private String cnh;
    private Integer telefone_id;

    @SerializedName("dt_nascimento") // Mapeia o nome do campo do JSON para o campo dataNascimento
    private String dataNascimento;
    private Integer imageUrl;


    public Motorista(String nome, String email, String senha, String cpf, String cnh, Integer telefone_id , String dataNascimento, Integer imageUrl){
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
        this.cnh = cnh;
        this.telefone_id = telefone_id;
        this.dataNascimento = dataNascimento;
        this.imageUrl = imageUrl;
    }





    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCnh() {
        return cnh;
    }

    public void setCnh(String cnh) {
        this.cnh = cnh;
    }

    public Integer getTelefone_id(){
        return telefone_id;
    }

    public void setTelefone_id(Integer telefone_id){
        this.telefone_id = telefone_id;
    }

    public String getDataNascimento(){
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento){
        this.dataNascimento = dataNascimento;
    }
    public Integer getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(Integer imageUrl) {
        this.imageUrl = imageUrl;
    }


}
