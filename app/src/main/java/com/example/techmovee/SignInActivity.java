package com.example.techmovee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techmovee.driver.SignInDriver;
import com.example.techmovee.responsible.SignInResponsible;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);


        ImageView btnVoltarTelaInicial = findViewById(R.id.btnVoltarTelaInicial);
        Button btnSignInMotorista = findViewById(R.id.btnSignInMotorista);
        Button button2 = findViewById(R.id.button2);

        button2.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, SignInResponsible.class);
            startActivity(intent);
            finish();
        });


        btnVoltarTelaInicial.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        btnSignInMotorista.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, SignInDriver.class);
            startActivity(intent);
            finish();
        });


        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // Esconde os botões de navegação
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // Esconde a barra de status
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY); // Mantém o modo imersivo

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