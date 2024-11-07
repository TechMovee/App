package com.example.techmovee;

import com.example.techmovee.comments.Comments;
import com.example.techmovee.driver.Motorista;
import com.example.techmovee.models.Foto;
import com.example.techmovee.models.Telefone;
import com.example.techmovee.responsible.Responsavel;
import com.example.techmovee.van.Van;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Body;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("transportador/selecionar")
    Call<List<Motorista>> getMotoristas();

    @GET("van/selecionar")
    Call<List<Van>> getVan();

    @GET("telefones/selecionar")
    Call<List<Telefone>> getTelefones();

    @GET("telefones/buscarPorId/{id}")
    Call<Telefone> getTelefoneById(@Path("id") Integer idPath, @Query("id") Integer idQuery);

    @POST("responsavel/cadastrar")
    Call<Responsavel> cadastrarResponsavel(@Body Responsavel responsavel);

    //buscar responsavel
    @GET("responsavel/buscarPorEmail/{email}")
    Call<Responsavel> getResponsavel(@Path("email") String emailPath, @Query("email") String emailQuery);

    //buscar comentários de avaliação
    @GET("/api/list")
    Call<List<Comments>> getAllComments();

    //adicionar comentários
    @POST("api/add")
    Call<Void> addComment(@Body Comments comment);

    @GET("van/{endpoint}")
    Call<List<Van>> filtrar(@Path("endpoint") String endpoint);

    // Endpoint para cadastrar telefone
    @POST("telefones/inserirTelefone")
    Call<Telefone> cadastrarTelefone(@Body Telefone telefone);

    // Endpoint para cadastrar foto
    @POST("fotos/inserirFotos")
    Call<Foto> cadastrarFoto(@Body Foto foto);

    // Endpoint para cadastrar motorista
    @POST("transportador/inserirTransportador")
    Call<Motorista> cadastrarMotorista(@Body Motorista motorista);


}