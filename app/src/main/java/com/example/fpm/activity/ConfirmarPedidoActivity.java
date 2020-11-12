package com.example.fpm.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.fpm.R;
import static  com.example.fpm.activity.HomeActivity.f;

public class ConfirmarPedidoActivity extends AppCompatActivity {


    private ImageButton voltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_pedido);
        voltar = findViewById(R.id.sair_toolbar);
        f=3;
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ConfirmarPedidoActivity.this,NegociarActivity.class);
                startActivity(i);
            }
        });
    }


    public void Confirmar(View view){
        Intent i = new Intent(ConfirmarPedidoActivity.this,HomeActivity.class);
        startActivity(i);
    }
}