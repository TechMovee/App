package com.example.techmovee.firebase;

import android.util.Log;

import com.example.techmovee.driver.Motorista;
import com.example.techmovee.filho.Filho;
import com.example.techmovee.responsible.Responsavel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Database {

//    private static final String COLLECTION_NAME = "motoristas";
//    private static int nextId = 1; // Inicializa o contador de IDs

    // Método para inserir motorista no Firebase com ID sequencial
    public void inserirMotorista(Motorista motorista) {
        // Criando um novo ID para o motorista
//        String motoristaId = String.valueOf(nextId);

        // Criando um mapa de dados para o motorista
        Map<String, Object> motoristaData = new HashMap<>();
        motoristaData.put("nome", motorista.getNome());
        motoristaData.put("email", motorista.getEmail());
        motoristaData.put("senha", motorista.getSenha());
        motoristaData.put("cpf", motorista.getCpf());
        motoristaData.put("cep", motorista.getCnh());
        motoristaData.put("telefone", motorista.getTelefone_id());
        motoristaData.put("dataNascimento", motorista.getDataNascimento());
        motoristaData.put("imageUrl", motorista.getImageUrl());

        // Acesso ao Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("motoristas")
                .add(motoristaData)  // Isso gera um ID único automaticamente
                .addOnSuccessListener(documentReference -> Log.d("Firebase", "User added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w("Firebase", "Error adding user", e));
//        CollectionReference motoristasRef = db.collection(COLLECTION_NAME);
//
//        // Adicionando o motorista com o ID sequencial
//        DocumentReference newMotoristaRef = motoristasRef.document(motoristaId);
//        newMotoristaRef.set(motoristaData, SetOptions.merge())
//                .addOnSuccessListener(documentReference -> {
//                    // Sucesso
//                    nextId++; // Incrementa o ID para o próximo motorista
//                })
//                .addOnFailureListener(e -> {
//                    // Falha
//                    Log.w("Database", "Erro ao adicionar motorista", e);
//
//                });
    }

    public void inserirResponsavel(Responsavel responsavel) {

        Map<String, Object> responsavelData = new HashMap<>();
        responsavelData.put("nome", responsavel.getNome());
        responsavelData.put("email", responsavel.getEmail());
        responsavelData.put("senha", responsavel.getSenha());
        responsavelData.put("cpf", responsavel.getCpf());
        responsavelData.put("cep", responsavel.getCep());
        responsavelData.put("telefone", responsavel.getTelefone_id());
        responsavelData.put("dataNascimento", responsavel.getDataNascimento());
        responsavelData.put("imageUrl", responsavel.getImageUrl());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("responsavel")
                .add(responsavelData)
                .addOnSuccessListener(documentReference -> Log.d("Firebase", "User added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w("Firebase", "Error adding user", e));


    }

    public void inserirFilho(Filho filho) {

        Map<String, Object> filhoData = new HashMap<>();
        filhoData.put("nome", filho.getNome());
        filhoData.put("cpf", filho.getCpf());
        filhoData.put("idade", filho.getIdade());
        filhoData.put("serie", filho.getSerie());
        filhoData.put("deficiencia", filho.getDeficiente());
        filhoData.put("imageUrl", filho.getImageUrl());
        Log.d("Database", "Filho: " + filho.getSerie());
        Log.d("Database", "Filho: " + filhoData);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("filhos")
                .add(filhoData)
                .addOnSuccessListener(documentReference -> Log.d("Firebase", "User added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w("Firebase", "Error adding user", e));
    }

}
