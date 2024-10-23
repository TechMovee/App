package com.example.techmovee.filho;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;


import android.Manifest;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.techmovee.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.UUID;

public class SignInSon extends AppCompatActivity {

    ImageView btnGoBack;

    private static final String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };


    private Button btnSignIn;
    private ImageView photo;

    private EditText edtNome;
    private EditText edtCPF;
    private EditText edtIdade;
    private Uri imageUri;
    private ImageView btnAddProfilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_son);


        btnGoBack = findViewById(R.id.btnGoBack);
        edtNome = findViewById(R.id.nome);
        edtCPF = findViewById(R.id.cpf);
        edtIdade = findViewById(R.id.idade);
        photo = findViewById(R.id.photo);
        btnAddProfilePicture = findViewById(R.id.btnAddProfilePicture);
        btnSignIn = findViewById(R.id.btnNext2);

        btnGoBack.setOnClickListener(view -> {
            // Adicione a lógica para voltar para a tela anterior aqui
            // Por exemplo, você pode chamar finish() para encerrar a atividade atual
            // ou voltar para a tela anterior usando o método onBackPressed()
            onBackPressed();
//            finish();
        });
        //Botão para abrir a galeria

        btnAddProfilePicture.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            resultLauncherGaleria.launch(intent);
        });
        btnSignIn.setOnClickListener(v -> {
            uploadImageAndNavigate();
        });

        edtNome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validateName();
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        edtCPF.addTextChangedListener(new TextWatcher() {
            boolean isUpdating = false;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) return;

                String edtCPFInput = edtCPF.getText().toString().replace(".", "").replace("-", "");

                if (!edtCPFInput.matches("\\d*")) {
                    edtCPF.setError("CPF deve conter apenas números!");
                    return;
                }
                StringBuilder formattedCpf = new StringBuilder();

                if (edtCPFInput.length() > 0) {
                    formattedCpf.append(edtCPFInput.substring(0, Math.min(edtCPFInput.length(), 3)));
                }
                if (edtCPFInput.length() > 3) {
                    formattedCpf.append(".").append(edtCPFInput.substring(3, Math.min(edtCPFInput.length(), 6)));
                }
                if (edtCPFInput.length() > 6) {
                    formattedCpf.append(".").append(edtCPFInput.substring(6, Math.min(edtCPFInput.length(), 9)));
                }
                if (edtCPFInput.length() > 9) {
                    formattedCpf.append("-").append(edtCPFInput.substring(9, Math.min(edtCPFInput.length(), 11)));
                }

                isUpdating = true;
                edtCPF.setText(formattedCpf.toString());
                edtCPF.setSelection(formattedCpf.length());
                isUpdating = false;

                if (edtCPFInput.length() < 11) {
                    edtCPF.setError("CPF deve conter 11 dígitos!");
                } else if (edtCPFInput.length() != 11 ) {
                    edtCPF.setError("CPF inválido!");
                } else {
                    edtCPF.setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        edtIdade.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 2) {
                    edtIdade.setText(charSequence.subSequence(0, 2));
                    edtIdade.setSelection(2); // Move o cursor para o final
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String idadeStr = edtIdade.getText().toString().trim();

                // Verifica se o campo de idade está vazio
                if (idadeStr.isEmpty()) {
                    edtIdade.setError("Idade é obrigatória!");
                    btnSignIn.setEnabled(false);
                    return;
                }

                // Verifica se a idade é um número válido
                if (!idadeStr.matches("\\d+")) {
                    edtIdade.setError("Idade deve ser um número válido!");
                    btnSignIn.setEnabled(false);
                    return;
                }

                int idade = Integer.parseInt(idadeStr);

                // Verifica se a idade é menor ou igual a 18 anos
                if (idade > 18) {
                    edtIdade.setError("Idade não pode ser maior que 18 anos!");
                    btnSignIn.setEnabled(false);
                } else {
                    edtIdade.setError(null); // Limpa o erro
                    btnSignIn.setEnabled(true);
                }
            }
        });



        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // Esconde os botões de navegação
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // Esconde a barra de status
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY); // Mantém o modo imersivo


    }

    // Launcher para abrir a galeria
    private ActivityResultLauncher<Intent> resultLauncherGaleria = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    if (imageUri != null) {
                        // Agora que temos a imagem, iniciamos o processo de recorte circular
                        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "cropped_profile_image.jpg"));
                        UCrop.Options options = new UCrop.Options();
                        options.setCircleDimmedLayer(true); // Configura o recorte circular
                        UCrop.of(imageUri, destinationUri)
                                .withAspectRatio(1, 1)  // Proporção 1:1 para manter o círculo
                                .withMaxResultSize(500, 500)  // Tamanho máximo da imagem resultante
                                .withOptions(options)
                                .start(com.example.techmovee.filho.SignInSon.this);
                    }
                }
            }
    );
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri croppedUri = UCrop.getOutput(data);
            if (croppedUri != null) {
                // Atualize a imageUri para o croppedUri
                imageUri = croppedUri;

                // Exibe a imagem recortada no formato circular
                photo.setVisibility(View.VISIBLE);
                btnAddProfilePicture.setVisibility(View.INVISIBLE);
                photo.setImageURI(croppedUri); // Exibe a imagem redimensionada e cortada
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            // Lidar com erro, se necessário
        }
    }



    private void validateName() {
        String nameInput = edtNome.getText().toString();
        // Verificar se o nome tem espaços no início ou no fim
        if (nameInput.startsWith(" ") || nameInput.endsWith(" ")) {
            edtNome.setError("Espaços não são permitidos no início ou no fim.");
            btnSignIn.setEnabled(false);
            return;
        }

        // Se o nome for válido, habilitar o botão
        edtNome.setError(null); // Limpa o erro
        btnSignIn.setEnabled(true);
    }



    private void uploadImageAndNavigate() {
        String nome = edtNome.getText().toString().trim();
        String idade = edtIdade.getText().toString().trim();
        String cpf = edtCPF.getText().toString().trim();

        if (nome.isEmpty() || cpf.isEmpty() || idade.isEmpty() ) {
            Toast.makeText(com.example.techmovee.filho.SignInSon.this, "Por favor, preencha todos os campos obrigatórios.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageUri != null) {
            String fileName = UUID.randomUUID().toString();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("gs//techmovee-4a854.appspot.com/" + fileName);

            storageReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    Bundle bundle = new Bundle();
                    bundle.putString("nome", nome);
                    bundle.putString("cpf", cpf);
                    bundle.putString("idade", idade);
                    bundle.putString("imageUrl", imageUrl);

                    Intent intent = new Intent(SignInSon.this, SignInSonContinued.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                });
            }).addOnFailureListener(e -> {
                Toast.makeText(com.example.techmovee.filho.SignInSon.this, "Erro ao fazer upload da imagem.", Toast.LENGTH_SHORT).show();

            });
        }else {
            // Caso a imagem não seja obrigatória, apenas passe os dados
            Bundle bundle = new Bundle();
            bundle.putString("nome", nome);
            bundle.putString("cpf", cpf);
            bundle.putString("idade", idade);

            Intent intent = new Intent(com.example.techmovee.filho.SignInSon.this, SignInSonContinued.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
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
}