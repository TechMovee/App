package com.example.techmovee;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.yalantis.ucrop.UCrop;

import java.io.File;

public class SignInDriver extends AppCompatActivity {

    ImageView btnGoBack;

    //foto
    private static final String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private ImageView photo;
    private ImageView btnAddProfilePicture;
    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_in_driver);

        btnGoBack = findViewById(R.id.btnGoBack);
        photo = findViewById(R.id.photo);



//      Botão para abrir a galeria
        btnAddProfilePicture = findViewById(R.id.btnAddProfilePicture);
        btnAddProfilePicture.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            resultLauncherGaleria.launch(intent);
        });
        btnGoBack.setOnClickListener(v -> {
            Intent intent = new Intent(SignInDriver.this, SignInActivity.class);
            startActivity(intent);
            finish();
        });

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
                                .start(SignInDriver.this);
                    }
                }
            }
    );

    // Método para lidar com o resultado do corte de imagem
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri croppedUri = UCrop.getOutput(data);
            if (croppedUri != null) {
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
}
