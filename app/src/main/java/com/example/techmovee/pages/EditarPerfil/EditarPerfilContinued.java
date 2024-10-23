package com.example.techmovee.pages.EditarPerfil;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.techmovee.R;

public class EditarPerfilContinued extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil_continued);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //transição contraria
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

//        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}