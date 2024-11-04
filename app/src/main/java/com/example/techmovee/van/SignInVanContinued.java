package com.example.techmovee.van;

import android.content.Intent;
import android.graphics.Outline;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.techmovee.R;
import com.yalantis.ucrop.UCrop;

import java.io.File;

public class SignInVanContinued extends AppCompatActivity {
    ImageView btnGoBack;
    ImageView photoVan;
    ImageView insertVanPhotoBtn;
    Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in_van_continued);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        insertVanPhotoBtn = findViewById(R.id.insertVanPhotoBtn);
        photoVan = findViewById(R.id.photoVan);
        insertVanPhotoBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            resultLauncherGaleria.launch(intent);
        });

        btnGoBack = findViewById(R.id.btnGoBack);
        btnGoBack.setOnClickListener(v -> {
            Intent intent = new Intent(SignInVanContinued.this, SignInVan.class);
            startActivity(intent);
        });
    }

    // Launcher para abrir a galeria
    private ActivityResultLauncher<Intent> resultLauncherGaleria = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    if (imageUri != null) {
                        // Agora que temos a imagem, iniciamos o processo de recorte retangular
                        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "cropped_profile_image.jpg"));
                        UCrop.Options options = new UCrop.Options();
                        // Removendo o recorte circular
                        options.setCircleDimmedLayer(false); // Configuração para recorte retangular
                        UCrop.of(imageUri, destinationUri)
                                .withAspectRatio(20, 15)  // Ajuste a proporção para o retângulo desejado, por exemplo, 4:3
                                .withMaxResultSize(1000, 700)  // Tamanho máximo da imagem resultante
                                .withOptions(options)
                                .start(com.example.techmovee.van.SignInVanContinued.this);
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

                // Exibe a imagem recortada no formato retangular
                photoVan.setVisibility(View.VISIBLE);
                insertVanPhotoBtn.setVisibility(View.INVISIBLE);
                photoVan.setImageURI(croppedUri); // Exibe a imagem redimensionada e cortada

                // Adiciona o OutlineProvider para bordas arredondadas
                photoVan.setOutlineProvider(new ViewOutlineProvider() {
                    @Override
                    public void getOutline(View view, Outline outline) {
                        int cornerRadius = (int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP, 10, view.getResources().getDisplayMetrics());
                        outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), cornerRadius);
                    }
                });
                photoVan.setClipToOutline(true);  // Garante que o contorno do provider seja aplicado
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

}