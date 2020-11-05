package com.example.fpm.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.fpm.R;
import com.example.fpm.activity.AvaliarPedidoActivity;
import com.example.fpm.activity.MainActivity;

public class ConfirmarPedidoActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_pedido);
    }

    public void Voltar(View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void Confirmar(View view){
        Intent i = new Intent(this, AvaliarPedidoActivity.class);
        startActivity(i);
    }
}