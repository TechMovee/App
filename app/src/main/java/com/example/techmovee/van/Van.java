package com.example.techmovee.van;

public class Van {
    private String placa;
    private String modelo;
    private boolean acessibilidade;
    private double mensalidade;
    private String imageUrl;
    private int capacidade;
    private String transportadorCNH;

    public Van(String placa, String modelo, boolean acessibilidade, double mensalidade, String imageUrl, int capacidade, String transportadorCNH) {
        this.placa = placa;
        this.modelo = modelo;
        this.acessibilidade = acessibilidade;
        this.mensalidade = mensalidade;
        this.imageUrl = imageUrl;
        this.capacidade = capacidade;
        this.transportadorCNH = transportadorCNH;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public boolean isAcessibilidade() {
        return acessibilidade;
    }

    public void setAcessibilidade(boolean acessibilidade) {
        this.acessibilidade = acessibilidade;
    }

    public double getMensalidade() {
        return mensalidade;
    }

    public void setMensalidade(double mensalidade) {
        this.mensalidade = mensalidade;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public String getTransportadorCNH() {
        return transportadorCNH;
    }

    public void setTransportadorCNH(String transportadorCNH) {
        this.transportadorCNH = transportadorCNH;
    }
}
