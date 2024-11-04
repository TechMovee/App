package com.example.techmovee;

import com.example.techmovee.driver.Motorista;
import com.example.techmovee.responsible.Responsavel;
import com.example.techmovee.van.Van;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Body;

public interface ApiService {
    @GET("transportador/selecionar")
    Call<List<Motorista>> getMotoristas();

    @GET("van/selecionar")
    Call<List<Van>> getVan();

    @POST("motoristas")
    Call<Motorista> createMotorista(@Body Motorista motorista);

    //salvar responsavel
    @POST("/responsavel/inserirResponsavel")
    Call<Responsavel> createResponsavel(@Body Responsavel responsavel);

    //buscar responsavel
    @GET("responsaveis")
    Call<List<Responsavel>> getResponsaveis();

}