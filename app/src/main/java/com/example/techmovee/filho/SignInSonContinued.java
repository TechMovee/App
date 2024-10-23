package com.example.techmovee.filho;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techmovee.MainActivity2;
import com.example.techmovee.R;
import com.example.techmovee.firebase.Database;



public class SignInSonContinued extends AppCompatActivity {



    private EditText serie;
    private EditText per1;
    private EditText per2;
    private CheckBox deficiente;
    private EditText escola;
    private Button finalizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_son_continued);



        per1 = findViewById(R.id.das);
        per2 = findViewById(R.id.as);
        serie = findViewById(R.id.serie);
        deficiente = findViewById(R.id.deficiente);
        finalizar = findViewById(R.id.finalizar);
        escola = findViewById(R.id.escola);

        finalizar.setOnClickListener(v ->{
            // Recebe o Bundle da tela anterior
            Bundle bundle = getIntent().getExtras();
            String nome = bundle.getString("nome");
            String idade = bundle.getString("idade");
            String cpf = bundle.getString("cpf");
            String imageUrl = bundle.getString("imageUrl");

            String per1Value = per1.getText().toString();
            String per2Value = per2.getText().toString();
            String serieValue = serie.getText().toString();
            String deficienteValue = deficiente.isChecked() ? "sim" : "nao";
            String escolaValue = escola.getText().toString();

            if (per1Value.isEmpty() || per2Value.isEmpty() || serieValue.isEmpty() || deficienteValue.isEmpty()) {
                Toast.makeText(SignInSonContinued.this, "Por favor, preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return; // Interrompe a execução se algum campo estiver vazio
            }

            bundle.putString("nome", nome);
            bundle.putString("cpf", cpf);
            bundle.putString("idade", idade);
            bundle.putString("periodoInicial", per1Value);
            bundle.putString("periodoFinal", per2Value);
            bundle.putString("serie", serieValue);
            bundle.putString("deficiencia", deficienteValue);
            bundle.putString("imageUrl", imageUrl);
            bundle.putString("escola", escolaValue);

            Filho filho = new Filho(nome, cpf, idade, serieValue, deficienteValue, imageUrl, per1Value, per2Value, escolaValue);

            Database db = new Database();
            db.inserirFilho(filho);
            Toast.makeText(this, "serie: " + serieValue, Toast.LENGTH_SHORT).show();
            Toast.makeText(SignInSonContinued.this, "Filho cadastrado com sucesso!", Toast.LENGTH_SHORT).show();


            FilhoUtil.nome = nome;
            FilhoUtil.cpf = cpf;
            FilhoUtil.idade = idade;
            FilhoUtil.serie = serieValue;
            FilhoUtil.deficiente = deficienteValue;
            FilhoUtil.imageUrl = imageUrl;
            FilhoUtil.periodoInicial = per1Value;
            FilhoUtil.periodoFinal = per2Value;
            FilhoUtil.escola = escolaValue;

            Intent intent = new Intent(SignInSonContinued.this, MainActivity2.class);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        serie.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
        serie.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateSerieTurma();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Adicionando TextWatcher para a validação e formatação do horário de início
        per1.addTextChangedListener(createTimeTextWatcher(per1));
        // Adicionando TextWatcher para a validação e formatação do horário de término
        per2.addTextChangedListener(createTimeTextWatcher(per2));

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // Esconde os botões de navegação
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // Esconde a barra de status
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY); // Mantém o modo imersivo
    }

    private void validateSerieTurma() {
        String serieTurmaInput = serie.getText().toString().trim().toUpperCase();

        if (!serieTurmaInput.matches("\\d+[A-Z]")) {
            serie.setError("Formato inválido! Ex: 5A");
            return;
        }

        Toast.makeText(this, "Série e Turma válidos!", Toast.LENGTH_SHORT).show();
    }

    private void validatePeriodos() {
        String periodo1 = per1.getText().toString().trim();
        String periodo2 = per2.getText().toString().trim();

        if (!isValidHora(periodo1)) {
            per1.setError("Formato inválido!");
        } else if (!isValidHora(periodo2)) {
            per2.setError("Formato inválido!");
        } else {
            Toast.makeText(this, "Períodos válidos!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidHora(String hora) {
        // Verifica se a string está no formato "HH:mm"
        return hora.matches("^([01]?\\d|2[0-3]):[0-5]\\d$");
    }

    private TextWatcher createTimeTextWatcher(EditText editText) {
        return new TextWatcher() {
            private boolean isUpdating = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) return;

                isUpdating = true;

                String input = s.toString().replace(":", ""); // Remove ":" para formatar corretamente
                StringBuilder formatted = new StringBuilder();

                for (int i = 0; i < input.length(); i++) {
                    formatted.append(input.charAt(i));
                    if (i == 1 && input.length() > 2) {
                        formatted.append(':'); // Adiciona ":" após os dois primeiros dígitos
                    }
                }

                editText.setText(formatted.toString());
                editText.setSelection(formatted.length()); // Move o cursor para o final do texto
                validatePeriodos();

                isUpdating = false; // Permite futuras atualizações
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };
    }
}
