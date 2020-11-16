package com.example.fpm.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.fpm.R;
import com.example.fpm.activity.CadastroActivity;

public class EscolhaActivity extends AppCompatActivity {
    private Intent i;
    private ImageButton btnVoltar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolha);
        btnVoltar = findViewById(R.id.sair_toolbar);
        i=new Intent(this, LoginActivity.class);

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);
            }
        });
    }
    public void IrContrante(View view){
        i=new Intent(this, CadastroActivity.class);
        startActivity(i);
    }
    public void startcadastro_prestadorActivity (View view) {
            final Intent cadastro_prestadorActivity = new Intent(this, CadastroPrestadorActivity.class);
            startActivity(cadastro_prestadorActivity);
    }


}