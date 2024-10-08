package com.example.techmovee.driver;

public class Motorista {

    private long id;
    private String nome;
    private String email;
    private String senha;
//    private String cnh;
    private String cpf;
    private String cep;
    private String telefone;
    private String dataNascimento;
    private String imageUrl;


    public Motorista(String nome, String email, String senha/*, String cnh*/, String cpf, String cep, String telefone ,String dataNascimento, String imageUrl){
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
//        this.cnh = cnh;
        this.cep = cep;
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
        this.imageUrl = imageUrl;
    }
    public Motorista() {
    }



    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
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

//    public String getCnh() {
//        return cnh;
//    }
//
//    public void setCnh(String cnh) {
//        this.cnh = cnh;
//    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getTelefone(){
        return telefone;
    }

    public void setTelefone(String telefone){
        this.telefone = telefone;
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
