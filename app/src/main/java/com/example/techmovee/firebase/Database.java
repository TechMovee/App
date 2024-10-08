package com.example.techmovee.firebase;

import android.util.Log;

import com.example.techmovee.driver.Motorista;
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
        motoristaData.put("cep", motorista.getCep());
        motoristaData.put("telefone", motorista.getTelefone());
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
}
