package com.example.fpm.activity;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fpm.R;
import com.example.fpm.config.Base64Custom;
import com.example.fpm.config.ConfiguracaoFirebase;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

import static  com.example.fpm.activity.HomeActivity.f;
import static com.example.fpm.activity.HomeActivity.UidPrestador;
import static com.example.fpm.activity.ConversasPrestadorActivity.UidContratante;
import static com.example.fpm.activity.ConversasPrestadorActivity.nomeContratante;
import static com.example.fpm.activity.HomeActivity.NomePrestador;
import static com.example.fpm.activity.LoginActivity.bifurcacaoLogin;

public class ConfirmarPedidoActivity extends AppCompatActivity {


    private ImageButton voltar;
    private CircleImageView imgPerfil;
    private String nome,descricacao;
    private TextView textNome,textDescricao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_pedido);
        voltar = findViewById(R.id.sair_toolbar);
        imgPerfil = findViewById(R.id.imgPrefil_confirmar);
        textNome = findViewById(R.id.nome_confirmar);
        textDescricao = findViewById(R.id.descricao_confirmar);

        StorageReference storageReference = ConfiguracaoFirebase.getFirebaseStorage();
        StorageReference strg;
        if(bifurcacaoLogin == false){
            strg = storageReference.child("Imagens").child("perfilPrestador").child(UidPrestador+".jpeg");
            recuperarDados("Contratante", UidPrestador);
        }else {
            strg = storageReference.child("Imagens").child("perfilContratante").child(UidContratante + ".jpeg");
            recuperarDados("Prestador",UidContratante);
        }

        String foto = strg.toString();
        if(foto!= null){
            Glide.with(this)
                    .using(new FirebaseImageLoader())
                    .load(strg)
                    .into(imgPerfil);
        }
        else{
            imgPerfil.setImageResource(R.drawable.imagem_fotouser);
        }

        textNome.setText(nome);
        textDescricao.setText(descricacao);

        f=3;
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ConfirmarPedidoActivity.this,NegociarActivity.class);
                startActivity(i);
            }
        });
    }

    private void recuperarDados(String bif,String uid){
        DatabaseReference ref = ConfiguracaoFirebase.getFirebaseDatabase().child(bif);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot d : snapshot.getChildren()){
                    if(d.child("uid").getValue().toString() == uid ){
                        nome = d.child("nome").getValue().toString();
                        descricacao = d.child("descricao").getValue().toString();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void Confirmar(View view){
        Intent i = new Intent(ConfirmarPedidoActivity.this,HomeActivity.class);
        startActivity(i);
    }
}