package com.example.techmovee.responsible;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.techmovee.filho.SignInSon;
import com.example.techmovee.firebase.Database;
import com.example.techmovee.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInResponsibleContinued extends AppCompatActivity {

    private ApiService apiService;

    private ImageView btnGoBack;
    private EditText cep;
    private EditText telefone;
    private EditText cpf;
    private EditText dataNascimento;
    private Calendar calendar;
    private TextView ageValidationMessage;

    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_responsible_continued);

        cep = findViewById(R.id.cnh);
        telefone = findViewById(R.id.telefone);
        cpf = findViewById(R.id.cpf);
        dataNascimento = findViewById(R.id.date);
        calendar = Calendar.getInstance();
        ageValidationMessage = findViewById(R.id.ageValidationMessage);
        btnNext = findViewById(R.id.btnNext2);

        dataNascimento.setFocusable(false); // Impede que o usuário digite

        // Se existir um estado salvo, restaura os valores
        if (savedInstanceState != null) {
            cep.setText(savedInstanceState.getString("cep"));
            telefone.setText(savedInstanceState.getString("telefone"));
            cpf.setText(savedInstanceState.getString("cpf"));
            dataNascimento.setText(savedInstanceState.getString("dataNascimento"));
        }

        btnNext.setOnClickListener(v -> {
            // Recebe o Bundle da tela anterior
            Bundle bundle = getIntent().getExtras();
            String nome = bundle.getString("nome");
            String email = bundle.getString("email");
            String senha = bundle.getString("senha");
            String imageUri = bundle.getString("imageUri");

            String cepValue = cep.getText().toString();
            String telefoneValue = telefone.getText().toString();
            String cpfValue = cpf.getText().toString();
            String dataNascimentoValue = dataNascimento.getText().toString();

            // Validação para garantir que todos os campos estejam preenchidos
            if (cepValue.isEmpty() || telefoneValue.isEmpty() || cpfValue.isEmpty() || dataNascimentoValue.isEmpty()) {
                Toast.makeText(SignInResponsibleContinued.this, "Por favor, preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return; // Interrompe a execução se algum campo estiver vazio
            }

            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.createUserWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (imageUri != null) {
                                String fileName = UUID.randomUUID().toString();
                                StorageReference storageReference = FirebaseStorage.getInstance().getReference("gs//techmovee-4a854.appspot.com/" + fileName);

                                storageReference.putFile(Uri.parse(imageUri)).addOnSuccessListener(taskSnapshot -> {
                                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                                        String imageUrl = uri.toString();

                                        bundle.putString("nome", nome);
                                        bundle.putString("cpf", cpfValue);
                                        bundle.putString("email", email);
                                        bundle.putString("senha", senha);
                                        bundle.putString("dataNascimento", dataNascimentoValue);
                                        bundle.putString("cep", cepValue);
                                        bundle.putString("telefone", telefoneValue);
                                        bundle.putString("imageUrl", imageUri);

                                        Responsavel responsavel = new Responsavel(nome, email, senha, cpfValue, cepValue, Integer.parseInt(telefoneValue), dataNascimentoValue, imageUri);
                                        // Criando um objeto da classe Database e chamando o método de inserção
                                        Database db = new Database();
                                        db.inserirResponsavel(responsavel);
                                        Responsavel2.nome = nome;

//
                                    });
                                }).addOnFailureListener(e -> {
                                    Toast.makeText(SignInResponsibleContinued.this, "Erro ao fazer upload da imagem.", Toast.LENGTH_SHORT).show();
                                });
                            } else {
                                // Caso a imagem não seja obrigatória, apenas passe os dados

                                bundle.putString("nome", nome);
                                bundle.putString("cpf", cpfValue);
                                bundle.putString("email", email);
                                bundle.putString("senha", senha);
                                bundle.putString("dataNascimento", dataNascimentoValue);
                                bundle.putString("cep", cepValue);
                                bundle.putString("telefone", telefoneValue);
                                bundle.putString("imageUrl", imageUri);

                                //chamar api

                                Responsavel responsavel = new Responsavel(nome, email, senha, cpfValue, cepValue, Integer.parseInt(telefoneValue), dataNascimentoValue, imageUri);

                                chamarApi(responsavel);
                                // Criando um objeto da classe Database e chamando o método de inserção
                                Database db = new Database();
                                db.inserirResponsavel(responsavel);
                                Responsavel2.nome = nome;
                                Responsavel2.email = email;
                                Responsavel2.telefone = telefoneValue;
                                Responsavel2.dataNascimento = String.valueOf(calcularIdade(dataNascimentoValue));
                            }
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(SignInResponsibleContinued.this, "Este email já está cadastrado.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SignInResponsibleContinued.this, "Erro ao cadastrar: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            Intent intent = new Intent(SignInResponsibleContinued.this, SignInSon.class);
            intent.putExtras(bundle);
            startActivity(intent);
            Toast.makeText(SignInResponsibleContinued.this, "Responsável cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
        });


        dataNascimento.setOnClickListener(v -> {
            new DatePickerDialog(SignInResponsibleContinued.this, (view, year, monthOfYear, dayOfMonth) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
                validateAge(dayOfMonth, monthOfYear, year);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        btnGoBack = findViewById(R.id.btnGoBack);
        btnGoBack.setOnClickListener(v -> {
            finish();
        });

        telefone.addTextChangedListener(new TextWatcher() {
            boolean isUpdating = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (isUpdating) { return; }

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
            public void afterTextChanged(Editable s) {}
        });


        cpf.addTextChangedListener(new TextWatcher() {
            boolean isUpdating = false;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                validadeCPF();

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
                } else if (cpfInput.length() != 11) {
                    cpf.setError("CPF inválido!");
                } else {
                    cpf.setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        // Validação do CEP
        cep.addTextChangedListener(new TextWatcher() {
            boolean isUpdating = false;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (isUpdating) return;

                String cepInput = s.toString().replaceAll("[^0-9]", ""); // Remove qualquer caractere que não seja número

                if (cepInput.length() > 5) {
                    cepInput = cepInput.substring(0, 5) + "-" + cepInput.substring(5);
                }

                isUpdating = true;
                cep.setText(cepInput);
                cep.setSelection(cepInput.length());
                isUpdating = false;

                // Verifica a validade do CEP apenas quando tiver 8 dígitos (sem contar o hífen)
                if (cepInput.replace("-", "").length() > 8) {
                    cep.setError("CEP deve conter apenas 8 dígitos!");
                } else if (cepInput.replace("-", "").length() == 8) {
                    // Aqui você pode adicionar uma chamada para o método de validação se desejar
                    cep.setError(null);
                } else {
                    cep.setError("CEP deve conter 8 dígitos!");
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("cep", cep.getText().toString());
        outState.putString("telefone", telefone.getText().toString());
        outState.putString("cpf", cpf.getText().toString());
        outState.putString("dataNascimento", dataNascimento.getText().toString());
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        cep.setText(savedInstanceState.getString("cep"));
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

    public static int calcularIdade(String dataNascimentoStr) {
        if (dataNascimentoStr == null || dataNascimentoStr.isEmpty()) {
            throw new IllegalArgumentException("Data de nascimento não pode ser nula ou vazia");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataNascimento;

        try {
            dataNascimento = LocalDate.parse(dataNascimentoStr, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato da data de nascimento deve ser dd/MM/yyyy");
        }

        LocalDate dataAtual = LocalDate.now();
        if (dataNascimento.isAfter(dataAtual)) {
            throw new IllegalArgumentException("Data de nascimento não pode ser no futuro");
        }

        Period idade = Period.between(dataNascimento, dataAtual);
        return idade.getYears();
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.getDefault());
        dataNascimento.setText(dateFormat.format(calendar.getTime()));
    }


    private void chamarApi(Responsavel responsavel){
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<Responsavel> call = apiService.cadastrarResponsavel(responsavel);

        call.enqueue(new Callback<Responsavel>() {
            @Override
            public void onResponse(Call<Responsavel> call, Response<Responsavel> response) {
                if (response.isSuccessful()){
                    Toast.makeText(SignInResponsibleContinued.this, "Api deu bom", Toast.LENGTH_SHORT).show();
                    Responsavel responsavelResponse = response.body();


                }else {
                    Toast.makeText(SignInResponsibleContinued.this, "Erro ao cadastrar: " + response.message(), Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<Responsavel> call, Throwable t) {
                Toast.makeText(SignInResponsibleContinued.this, "Erro de conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
