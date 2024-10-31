package com.example.techmovee.pages.EditarPerfil;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techmovee.R;

public class EditarPerfilContinued extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_pt2);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //transição contraria
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

//        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}