package com.example.techmovee.responsible;

import com.google.gson.annotations.SerializedName;


public class Responsavel {


    private String nome;
    private String email;
    private String senha;
    private String cpf;
    private String cep;
    private Integer telefone_id;

    @SerializedName("dt_nascimento")
    private String dataNascimento;
    private String imageUrl;


    public Responsavel(String nome, String email, String senha, String cpf, String cep, Integer telefone_id ,String dataNascimento, String imageUrl){
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
        this.cep = cep;
        this.telefone_id = telefone_id;
        this.dataNascimento = dataNascimento;
        this.imageUrl = imageUrl;
    }
    public Responsavel() {
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

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Integer getTelefone_id(){
        return telefone_id;
    }

    public void setTelefone_id(Integer telefone){
        this.telefone_id = telefone;
    }

    public String getDataNascimento(){
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento){
        this.dataNascimento = dataNascimento;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


}
