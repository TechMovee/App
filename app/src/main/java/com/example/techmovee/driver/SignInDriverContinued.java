package com.example.techmovee.driver;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.techmovee.ApiService;
import com.example.techmovee.RetrofitClient;
import com.example.techmovee.SignInDriver;
import com.example.techmovee.firebase.Database;
import com.example.techmovee.R;
import com.example.techmovee.models.Foto;
import com.example.techmovee.models.Telefone;
import com.example.techmovee.van.SignInVan;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInDriverContinued extends AppCompatActivity {

    private ImageView btnGoBack;
    private EditText cnh;
    private EditText telefone;
    private EditText cpf;
    private EditText dataNascimento;
    private Calendar calendar;
    private TextView ageValidationMessage;
    private ApiService apiService;


    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_in_driver_continued);

        cnh = findViewById(R.id.cnh);
        telefone = findViewById(R.id.telefone);
        cpf = findViewById(R.id.cpf);
        dataNascimento = findViewById(R.id.date);
        calendar = Calendar.getInstance();
        ageValidationMessage = findViewById(R.id.ageValidationMessage);
        btnNext = findViewById(R.id.btnNext2);

        dataNascimento.setFocusable(false); // Impede que o usuário digite

        // Se existir um estado salvo, restaura os valores
        if (savedInstanceState != null) {
            cnh.setText(savedInstanceState.getString("cnh"));
            telefone.setText(savedInstanceState.getString("telefone"));
            cpf.setText(savedInstanceState.getString("cpf"));
            dataNascimento.setText(savedInstanceState.getString("dataNascimento"));
        }

        // Recupera os dados da tela anterior
        Bundle bundle = getIntent().getExtras();
        String nome = bundle.getString("nome");
        String email = bundle.getString("email");
        String senha = bundle.getString("senha");
        String imageUri = bundle.getString("imageUrl");


        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        btnNext.setOnClickListener(v -> {
            String telefoneValue = telefone.getText().toString();
            String cnhValue = cnh.getText().toString();
            String cpfValue = cpf.getText().toString();
            String dataNascimentoValue = dataNascimento.getText().toString();

            // Valida os campos
            if (telefoneValue.isEmpty() || cnhValue.isEmpty() || cpfValue.isEmpty() || dataNascimentoValue.isEmpty()) {
                Toast.makeText(SignInDriverContinued.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Passo 1: Criar o objeto Telefone sem o ID, já que ele será gerado pelo banco
            Telefone telefoneObj = new Telefone(null, telefoneValue, "Transportador");  // "mobile" ou o tipo correto esperado pela API

// Passo 2: Chamar o endpoint da API para cadastrar o telefone
            apiService.cadastrarTelefone(telefoneObj).enqueue(new Callback<Telefone>() {
                @Override
                public void onResponse(Call<Telefone> call, Response<Telefone> response) {
                    Log.d("Resposta API", "Corpo da resposta: " + response.body().toString());

                    if (response.isSuccessful()) {
                        Log.d("Resposta API", "Corpo da resposta: " + response.body().toString());

                        // Se o telefone for cadastrado com sucesso, obtemos o ID gerado automaticamente
                        Telefone telefoneCadastrado = response.body();
                        int telefoneId = telefoneCadastrado.getId();  // O ID será atribuído automaticamente pelo backend
                        Log.d("ID do Telefone", "ID do Telefone: " + telefoneId);
                        // Passo 3: Cadastrar a foto
                        if(imageUri == null){
                            // Passo 4: Cadastrar o motorista com os IDs de telefone e foto
                            Motorista motorista = new Motorista(
                                    nome, email, senha, cpfValue, cnhValue, telefoneId,
                                    dataNascimentoValue, null
                            );
                            Log.d("Motorista", new Gson().toJson(motorista.getImageUrl()));
                            apiService.cadastrarMotorista(motorista).enqueue(new Callback<Motorista>() {
                                @Override
                                public void onResponse(Call<Motorista> call, Response<Motorista> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(SignInDriverContinued.this, "Motorista cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(SignInDriverContinued.this, "Erro ao cadastrar motorista", Toast.LENGTH_SHORT).show();
                                        Log.d("Erro ao cadastrar motorista", response.toString());
                                    }
                                }

                                @Override
                                public void onFailure(Call<Motorista> call, Throwable t) {
                                    Toast.makeText(SignInDriverContinued.this, "Falha na comunicação com a API", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else{
                            Foto fotoObj = new Foto(0, imageUri);  // ID será gerado automaticamente pelo backend
                            apiService.cadastrarFoto(fotoObj).enqueue(new Callback<Foto>() {
                                @Override
                                public void onResponse(Call<Foto> call, Response<Foto> response) {

                                    if (response.isSuccessful()) {
                                        Foto fotoCadastrada = response.body();
                                        int fotoId = fotoCadastrada.getId();  // O ID da foto

                                        // Passo 4: Cadastrar o motorista com os IDs de telefone e foto
                                        Motorista motorista = new Motorista(
                                                nome, email, senha, cpfValue, cnhValue, telefoneId,
                                                dataNascimentoValue, fotoId
                                        );
                                        apiService.cadastrarMotorista(motorista).enqueue(new Callback<Motorista>() {
                                            @Override
                                            public void onResponse(Call<Motorista> call, Response<Motorista> response) {
                                                if (response.isSuccessful()) {
                                                    Toast.makeText(SignInDriverContinued.this, "Motorista cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(SignInDriverContinued.this, "Erro ao cadastrar motorista", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Motorista> call, Throwable t) {
                                                Toast.makeText(SignInDriverContinued.this, "Falha na comunicação com a API", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        Toast.makeText(SignInDriverContinued.this, "Erro ao cadastrar foto", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Foto> call, Throwable t) {
                                    Toast.makeText(SignInDriverContinued.this, "Falha na comunicação com a API de fotos", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }


                    } else {
                        Toast.makeText(SignInDriverContinued.this, "Erro ao cadastrar telefone", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Telefone> call, Throwable t) {
                    Toast.makeText(SignInDriverContinued.this, "Falha na comunicação com a API de telefones"+t.toString(), Toast.LENGTH_SHORT).show();
                    Log.d("Falha na comunicação com a API de telefones", t.toString());

                }
            });

        });


//        btnNext.setOnClickListener(v -> {
//            // Recebe o Bundle da tela anterior
//            Bundle bundle2 = getIntent().getExtras();
//            String nome2 = bundle2.getString("nome");
//            String email2 = bundle2.getString("email");
//            String senha2 = bundle2.getString("senha");
//            String imageUri2 = bundle2.getString("imageUrl");
//
//            String cnhValue = cnh.getText().toString();
//            String telefoneValue = telefone.getText().toString();
//            String cpfValue = cpf.getText().toString();
//            String dataNascimentoValue = dataNascimento.getText().toString();
//
//            // Validação para garantir que todos os campos estejam preenchidos
//            if (cnhValue.isEmpty() || telefoneValue.isEmpty() || cpfValue.isEmpty() || dataNascimentoValue.isEmpty()) {
//                Toast.makeText(SignInDriverContinued.this, "Por favor, preencha todos os campos!", Toast.LENGTH_SHORT).show();
//                return; // Interrompe a execução se algum campo estiver vazio
//            }
//
//            FirebaseAuth auth = FirebaseAuth.getInstance();
//            auth.createUserWithEmailAndPassword(email2, senha2)
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            if (imageUri2 != null) {
//                                String fileName = UUID.randomUUID().toString();
//                                StorageReference storageReference = FirebaseStorage.getInstance().getReference("gs//techmovee-4a854.appspot.com/" + fileName);
//
//                                storageReference.putFile(Uri.parse(imageUri2)).addOnSuccessListener(taskSnapshot -> {
//                                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
//                                        String imageUrl = uri.toString();
//
//                                        bundle.putString("nome", nome);
//                                        bundle.putString("cpf", cpfValue);
//                                        bundle.putString("email", email);
//                                        bundle.putString("senha", senha);
//                                        bundle.putString("dataNascimento", dataNascimentoValue);
//                                        bundle.putString("cnh", cnhValue);
//                                        bundle.putString("telefone", telefoneValue);
//                                        bundle.putString("imageUrl", imageUri);
//
//                                        Motorista motorista = new Motorista(nome, email, senha, cpfValue, cnhValue, Integer.parseInt(telefoneValue), dataNascimentoValue, Integer.parseInt(imageUri));
//
//                                        // Criando um objeto da classe Database e chamando o método de inserção
//                                        Database db = new Database();
//                                        db.inserirMotorista(motorista);
//
//                                        Toast.makeText(SignInDriverContinued.this, "Motorista cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
//                                    });
//                                }).addOnFailureListener(e -> {
//                                    Toast.makeText(SignInDriverContinued.this, "Erro ao fazer upload da imagem.", Toast.LENGTH_SHORT).show();
//                                });
//                            } else {
//                                // Caso a imagem não seja obrigatória, apenas passe os dados
//
//                                bundle.putString("nome", nome);
//                                bundle.putString("cpf", cpfValue);
//                                bundle.putString("email", email);
//                                bundle.putString("senha", senha);
//                                bundle.putString("dataNascimento", dataNascimentoValue);
//                                bundle.putString("cnh", cnhValue);
//                                bundle.putString("telefone", telefoneValue);
//                                bundle.putString("imageUrl", imageUri);
//
//                                Motorista motorista = new Motorista(nome, email, senha, cpfValue, cnhValue, Integer.parseInt(telefoneValue), dataNascimentoValue, Integer.parseInt(imageUri));
//
//                                // Criando um objeto da classe Database e chamando o método de inserção
//                                Database db = new Database();
//                                db.inserirMotorista(motorista);
//
//                                Toast.makeText(SignInDriverContinued.this, "Motorista cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
//                                Toast.makeText(SignInDriverContinued.this, "Este email já está cadastrado.", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(SignInDriverContinued.this, "Erro ao cadastrar: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//            Intent intent = new Intent(SignInDriverContinued.this, SignInVan.class);
//            intent.putExtras(bundle);
//            startActivity(intent);
//            Toast.makeText(SignInDriverContinued.this, "Motorista cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
//        });


        dataNascimento.setOnClickListener(v -> {
            new DatePickerDialog(SignInDriverContinued.this, (view, year, monthOfYear, dayOfMonth) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
                validateAge(dayOfMonth, monthOfYear, year);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        btnGoBack = findViewById(R.id.btnGoBack);
        btnGoBack.setOnClickListener(v -> {
            Intent intent = new Intent(SignInDriverContinued.this, SignInDriver.class);
            startActivity(intent);
        });


        telefone.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (isUpdating) {
                    return;
                }

                isUpdating = true;
                String unformatted = s.toString().replaceAll("[^\\d]", ""); // Remove tudo que não for número

                if (unformatted.length() > 11) {
                    unformatted = unformatted.substring(0, 11); // Limita a 11 dígitos
                }

                StringBuilder formatted = new StringBuilder();
                int length = unformatted.length();

                if (length > 0) {
                    formatted.append("(");
                    formatted.append(unformatted.substring(0, Math.min(length, 2))); // DDD
                    if (length >= 3) {
                        formatted.append(") ");
                        formatted.append(unformatted.substring(2, Math.min(length, 7))); // Primeira parte do número
                        if (length >= 8) {
                            formatted.append("-");
                            formatted.append(unformatted.substring(7)); // Segunda parte do número
                        }
                    }
                }
                telefone.setText(formatted.toString());
                // Verificação para garantir que a posição não exceda o comprimento do texto
                int selectionPosition = formatted.length();

                if (selectionPosition > telefone.getText().length()) {
                    selectionPosition = telefone.getText().length();
                }
                telefone.setSelection(selectionPosition); // Define a posição da seleção corretamente
                isUpdating = false;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        cpf.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) return;

                String cpfInput = s.toString().replace(".", "").replace("-", "");

                if (!cpfInput.matches("\\d*")) {
                    cpf.setError("CPF deve conter apenas números!");
                    return;
                }
                StringBuilder formattedCpf = new StringBuilder();

                if (cpfInput.length() > 0) {
                    formattedCpf.append(cpfInput.substring(0, Math.min(cpfInput.length(), 3)));
                }
                if (cpfInput.length() > 3) {
                    formattedCpf.append(".").append(cpfInput.substring(3, Math.min(cpfInput.length(), 6)));
                }
                if (cpfInput.length() > 6) {
                    formattedCpf.append(".").append(cpfInput.substring(6, Math.min(cpfInput.length(), 9)));
                }
                if (cpfInput.length() > 9) {
                    formattedCpf.append("-").append(cpfInput.substring(9, Math.min(cpfInput.length(), 11)));
                }

                isUpdating = true;
                cpf.setText(formattedCpf.toString());
                cpf.setSelection(formattedCpf.length());
                isUpdating = false;

                if (cpfInput.length() < 11) {
                    cpf.setError("CPF deve conter 11 dígitos!");
                } else if (cpfInput.length() == 11 && !isCpfValid(cpfInput)) {
                    cpf.setError("CPF inválido!");
                } else {
                    cpf.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Validação do CEP
        cnh.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) return;

                String cnhInput = s.toString().replaceAll("[^0-9]", ""); // Remove qualquer caractere que não seja número


                isUpdating = true;
                cnh.setText(cnhInput);
                cnh.setSelection(cnhInput.length());
                isUpdating = false;

                // Verifica a validade do CEP apenas quando tiver 8 dígitos (sem contar o hífen)
                if (cnhInput.length() > 11) {
                    cnh.setError("CHN deve conter apenas 11 dígitos!");
                } else if (cnhInput.length() == 11) {
                    // Aqui você pode adicionar uma chamada para o método de validação se desejar
                    cnh.setError(null);
                } else {
                    cnh.setError("CNH deve conter 11 dígitos!");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("cnh", cnh.getText().toString());
        outState.putString("telefone", telefone.getText().toString());
        outState.putString("cpf", cpf.getText().toString());
        outState.putString("dataNascimento", dataNascimento.getText().toString());
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        cnh.setText(savedInstanceState.getString("cnh"));
        telefone.setText(savedInstanceState.getString("telefone"));
        cpf.setText(savedInstanceState.getString("cpf"));
        dataNascimento.setText(savedInstanceState.getString("dataNascimento"));
    }

    private void validateAge(int day, int month, int year) {
        Calendar today = Calendar.getInstance();
        Calendar birthDate = Calendar.getInstance();
        birthDate.set(year, month, day);

        int age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        if (age < 18) {
            ageValidationMessage.setVisibility(View.VISIBLE);
            btnNext.setEnabled(false);
        } else {
            ageValidationMessage.setVisibility(View.GONE);
            btnNext.setEnabled(true);
        }
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.getDefault());
        dataNascimento.setText(dateFormat.format(calendar.getTime()));
    }

    private boolean isCpfValid(String cpf) {
        if (cpf.length() != 11) return false;

        // Implementar a lógica de validação do CPF
        // Para simplificar, você pode utilizar um método existente ou implementar a lógica correta.

        return true; // Mude isso para a lógica real de validação do CPF.
    }
}
