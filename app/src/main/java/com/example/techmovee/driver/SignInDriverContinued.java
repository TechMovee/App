package com.example.techmovee.driver;

import android.app.DatePickerDialog;
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

import com.example.techmovee.firebase.Database;
import com.example.techmovee.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SignInDriverContinued extends AppCompatActivity {

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
        setContentView(R.layout.activity_sing_in_driver_continued);

        cep = findViewById(R.id.cep);
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
            String imageUrl = bundle.getString("imageUrl");

            String cepValue = cep.getText().toString();
            String telefoneValue = telefone.getText().toString();
            String cpfValue = cpf.getText().toString();
            String dataNascimentoValue = dataNascimento.getText().toString();

            // Validação para garantir que todos os campos estejam preenchidos
            if (cepValue.isEmpty() || telefoneValue.isEmpty() || cpfValue.isEmpty() || dataNascimentoValue.isEmpty()) {
                Toast.makeText(SignInDriverContinued.this, "Por favor, preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return; // Interrompe a execução se algum campo estiver vazio
            }

            bundle.putString("nome", nome);
            bundle.putString("cpf", cpfValue);
            bundle.putString("email", email);
            bundle.putString("senha", senha);
            bundle.putString("dataNascimento", dataNascimentoValue);
            bundle.putString("cep", cepValue);
            bundle.putString("telefone", telefoneValue);
            bundle.putString("imageUrl", imageUrl);

            Motorista motorista = new Motorista(nome, email, senha, cpfValue, cepValue, telefoneValue, dataNascimentoValue, imageUrl);

            // Criando um objeto da classe Database e chamando o método de inserção
            Database db = new Database();
            db.inserirMotorista(motorista);

            Toast.makeText(SignInDriverContinued.this, "Motorista cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
        });


        dataNascimento.setOnClickListener(v -> {
            new DatePickerDialog(SignInDriverContinued.this, (view, year, monthOfYear, dayOfMonth) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
                validateAge(dayOfMonth, monthOfYear, year);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        btnGoBack = findViewById(R.id.btnGoBack3);
        btnGoBack.setOnClickListener(v -> {
            finish();
        });

        cpf.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

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
            public void afterTextChanged(Editable s) { }
        });

        // Validação do CEP
        cep.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;

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
