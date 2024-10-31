package com.example.techmovee;

import com.example.techmovee.comments.Comments;
import com.example.techmovee.driver.Motorista;
import com.example.techmovee.responsible.Responsavel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Body;

public interface ApiService {
    @GET("motoristas")
    Call<List<Motorista>> getMotoristas();

    @POST("motoristas")
    Call<Motorista> createMotorista(@Body Motorista motorista);

    //salvar responsavel
    @POST("/responsavel/inserirResponsavel")
    Call<Responsavel> createResponsavel(@Body Responsavel responsavel);

    //buscar responsavel
    @GET("responsaveis")
    Call<List<Responsavel>> getResponsaveis();

    //buscar comentários de avaliação
    @GET("/api/list")
    Call<List<Comments>> getAllComments();

    //adicionar comentários
    @POST("/api/add")
    Call<Void> addComment(@Body Comments comment);

}