package com.example.techmovee.van;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.techmovee.R;
import com.example.techmovee.driver.SignInDriverContinued;

public class SignInVan extends AppCompatActivity {

    ImageView btnGoBack;
    Button btnNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in_van);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnGoBack = findViewById(R.id.btnGoBack);
        btnGoBack.setOnClickListener(v -> {
            Intent intent = new Intent(SignInVan.this, SignInDriverContinued.class);
            startActivity(intent);
        });

        btnNext = findViewById(R.id.btnNext2);
        btnNext.setOnClickListener(v -> {
            Intent intent = new Intent(SignInVan.this, SignInVanContinued.class);
            startActivity(intent);
        });
    }
}