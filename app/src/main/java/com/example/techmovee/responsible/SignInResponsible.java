package com.example.techmovee.responsible;

import android.Manifest;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.techmovee.R;
import com.example.techmovee.SignInActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.UUID;

public class SignInResponsible extends AppCompatActivity {

    ImageView btnGoBack;

    private ImageView photo;
    private ImageView btnAddProfilePicture;
    private Uri imageUri;

    private EditText edtEmail;
    private EditText edtPassword;
    private EditText edtPasswordConfirm;
    private Button btnSignIn;
    private EditText edtNome;

    private TextInputLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_responsible);

        edtNome = findViewById(R.id.nome);
        edtEmail = findViewById(R.id.email);
        edtPassword = findViewById(R.id.senha);
        edtPasswordConfirm = findViewById(R.id.confSenha);
        photo = findViewById(R.id.photo);

        edtPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        edtPasswordConfirm.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);

        btnSignIn = findViewById(R.id.btnNext);

        // Adicionando listeners para os campos
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

        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validateEmail();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                validatePassword();
            }
        });

        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validatePassword();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                validatePassword();
            }
        });

        edtPasswordConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validatePassword();
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
        btnSignIn.setOnClickListener(v -> {
            uploadImageAndNavigate();
        });

        //voltar
        btnGoBack = findViewById(R.id.btnGoBack);
        btnGoBack.setOnClickListener(v -> {
            Intent intent = new Intent(com.example.techmovee.responsible.SignInResponsible.this, SignInActivity.class);
            startActivity(intent);
        });


//      Botão para abrir a galeria
        btnAddProfilePicture = findViewById(R.id.btnAddProfilePicture);
        btnAddProfilePicture.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            resultLauncherGaleria.launch(intent);
        });


        // Restaurar o estado se disponível
        if (savedInstanceState != null) {
            edtNome.setText(savedInstanceState.getString("nome", ""));
            edtEmail.setText(savedInstanceState.getString("email", ""));
            edtPassword.setText(savedInstanceState.getString("senha", ""));
            edtPasswordConfirm.setText(savedInstanceState.getString("senha", ""));
        }


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
                                .withAspectRatio(2, 2)  // Proporção 1:1 para manter o círculo
                                .withMaxResultSize(500, 500)  // Tamanho máximo da imagem resultante
                                .withOptions(options)
                                .start(com.example.techmovee.responsible.SignInResponsible.this);
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


    // Método para validar o nome
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

    // Método para validar o email
    private void validateEmail() {
        String emailInput = edtEmail.getText().toString().trim();

        // Verificar se o email contém espaços
        if (emailInput.contains(" ")) {
            edtEmail.setError("Espaços não são permitidos.");
            btnSignIn.setEnabled(false);
            return;
        }

        // Verificar se o email contém "@" e termina com um domínio válido
        if (!emailInput.matches("^[\\w-.]+@[\\w-]+\\.[a-z]{2,3}(\\.[a-z]{2,3})?$")) {
            edtEmail.setError("Email inválido. Deve conter '@' e um domínio válido.");
            btnSignIn.setEnabled(false);
            return;
        }

        // Se o email for válido, habilitar o botão
        edtEmail.setError(null); // Limpa o erro
        btnSignIn.setEnabled(true);
    }

    // Método para validar a senha
    private void validatePassword() {
        String passwordInput = edtPassword.getText().toString().trim();
        String confirmPasswordInput = edtPasswordConfirm.getText().toString().trim();

        // Verificar se a senha contém espaços
        if (passwordInput.contains(" ")) {
            edtPassword.setError("Espaços não são permitidos.", null);
            btnSignIn.setEnabled(false);
            return;
        }

        // Verificar se a senha tem pelo menos 8 caracteres, uma letra maiúscula, uma letra minúscula, um número e um caractere especial
        if (!passwordInput.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!.]).{8,}$")) {
            edtPassword.setError("A senha deve ter pelo menos 8 caracteres, uma letra maiúscula, uma letra minúscula, um número e um caractere especial.",null);
            btnSignIn.setEnabled(false);
            return;
        }

        // Verificar se a senha e a confirmação de senha são iguais
        if (!passwordInput.equals(confirmPasswordInput)) {
            edtPasswordConfirm.setError("As senhas não coincidem.", null);
            btnSignIn.setEnabled(false);
            return;
        }

        // Se a senha for válida, limpar os erros e habilitar o botão
        edtPassword.setError(null); // Limpa o erro
        edtPasswordConfirm.setError(null); // Limpa o erro
        btnSignIn.setEnabled(true);
    }

    private void uploadImageAndNavigate() {
        String nome = edtNome.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String senha = edtPassword.getText().toString().trim();
        String confirmSenha = edtPasswordConfirm.getText().toString().trim();

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || confirmSenha.isEmpty()) {
            Toast.makeText(com.example.techmovee.responsible.SignInResponsible.this, "Por favor, preencha todos os campos obrigatórios.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!senha.equals(confirmSenha)) {
            Toast.makeText(com.example.techmovee.responsible.SignInResponsible.this, "As senhas não coincidem.", Toast.LENGTH_SHORT).show();
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
                    bundle.putString("email", email);
                    bundle.putString("senha", senha);
                    bundle.putString("imageUrl", imageUrl);

                    Intent intent = new Intent(com.example.techmovee.responsible.SignInResponsible.this, SignInResponsibleContinued.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                });
            }).addOnFailureListener(e -> {
                Toast.makeText(com.example.techmovee.responsible.SignInResponsible.this, "Erro ao fazer upload da imagem.", Toast.LENGTH_SHORT).show();

            });
        }else {
            // Caso a imagem não seja obrigatória, apenas passe os dados
            Bundle bundle = new Bundle();
            bundle.putString("nome", nome);
            bundle.putString("email", email);
            bundle.putString("senha", senha);

            Intent intent = new Intent(com.example.techmovee.responsible.SignInResponsible.this, SignInResponsibleContinued.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }


    //Restaurar Pagina
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Salvar os valores dos campos no Bundle
        outState.putString("nome", edtNome.getText().toString());
        outState.putString("email", edtEmail.getText().toString());
        outState.putString("senha", edtPassword.getText().toString());
        outState.putString("senhaConfirmada", edtPasswordConfirm.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Restaurar os dados do nome, email, senha e confirmação de senha
        if (savedInstanceState != null) {
            edtNome.setText(savedInstanceState.getString("nome"));
            edtEmail.setText(savedInstanceState.getString("email"));
            edtPassword.setText(savedInstanceState.getString("senha"));
            edtPasswordConfirm.setText(savedInstanceState.getString("senhaConfirmada"));
        }
    }


}