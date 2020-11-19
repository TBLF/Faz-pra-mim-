package com.example.fpm.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.fpm.R;
import com.google.android.gms.maps.model.LatLng;

import static com.example.fpm.activity.CadastroActivity.cidade;
import static com.example.fpm.activity.CadastroActivity.entrou;
import static com.example.fpm.activity.EscolhaActivity.escolha;


public class CadastroEnderecoActivity extends AppCompatActivity {
   public static LatLng localcidade;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_endereco);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, CIDADES);
        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.editEnd);
        textView.setAdapter(adapter);


        if(cidade!=0){
            switch (cidade){
                case 1:
                    textView.setText("Manaus");
                    break;
                case 2:
                    textView.setText("São paulo");
                    break;
                case 3:
                    textView.setText("Rio de Janeiro");
                    break;
            }

        }
       textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        localcidade = new LatLng(-3.0895571, -59.9644187);
                        cidade =1;
                        break;
                    case 1:
                        localcidade = new LatLng(-3.0895571, -59.9644187);
                        cidade =2;
                        break;
                    case 2:
                        localcidade = new LatLng(-3.0895571, -59.9644187);
                        cidade =3;
                        break;
                }
            }
        });
    }

    public void abrirMapa(View view){
        Intent i = new Intent(this, PesquisarEnderecoActivity.class);
        if(cidade != 0){
            startActivity(i);
        }

    }

    private static final String[] CIDADES= new String[] {
            "Manaus","São Paulo","Rio de Janeiro"
    };

    public void irProximo(View view){
        Intent i;
        if(entrou == true){

            if(escolha == false){
                i = new Intent(this, CadastroPrestadorServicoActivity.class);
            }else{
                i = new Intent(this, CadastroContratanteActivity.class);
            }
            startActivity(i);
        }else{
            Toast.makeText(this, "Selecione seu endereço de cidade e local no mapa", Toast.LENGTH_SHORT).show();
        }

    }
}