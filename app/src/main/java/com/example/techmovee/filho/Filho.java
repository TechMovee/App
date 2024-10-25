package com.example.techmovee.filho;

public class Filho {

    private String nome;
    private String cpf;
    private String idade;
    private String serie;
    private String deficiente;
    private String imageUrl;
    private String periodoInicial;
    private String periodoFinal;
    private String escola;

    public Filho(String nome, String cpf, String idade, String serie, String deficiente, String imageUrl, String periodoInicial, String periodoFinal, String escola) {
        this.nome = nome;
        this.cpf = cpf;
        this.idade = idade;
        this.serie = serie;
        this.deficiente = deficiente;
        this.imageUrl = imageUrl;
        this.periodoInicial = periodoInicial;
        this.periodoFinal = periodoFinal;
        this.escola = escola;
    }

    public Filho() {
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

    public String getIdade() {
        return idade;
    }

    public void setIdade(String idade) {
        this.idade = idade;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getDeficiente() {
        return deficiente;
    }

    public void setDeficiente(String deficiente) {
        this.deficiente = deficiente;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPeriodoInicial() {
        return periodoInicial;
    }

    public void setPeriodoInicial(String periodoicial) {
        this.periodoInicial = periodoicial;
    }

    public String getPeriodoFinal() {
        return periodoFinal;
    }

    public void setPeriodoFinal(String periodoFinal) {
        this.periodoFinal = periodoFinal;
    }

    public String getEscola() {
        return escola;
    }

    public void setEscola(String escola) {
        this.escola = escola;
    }
}
