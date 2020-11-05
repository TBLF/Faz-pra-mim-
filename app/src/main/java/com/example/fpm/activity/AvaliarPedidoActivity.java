package com.example.fpm.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.fpm.R;

public class AvaliarPedidoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avaliar_pedido);
    }

    public void Avaliar (View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
    public void Voltar(View view){
        Intent i = new Intent(this, ConfirmarPedidoActivity.class);
        startActivity(i);
    }
}