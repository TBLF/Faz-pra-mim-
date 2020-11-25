package com.example.fpm.activity;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fpm.R;
import com.example.fpm.config.Base64Custom;
import com.example.fpm.config.ConfiguracaoFirebase;
import com.example.fpm.config.UsuarioFireBase;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import de.hdodenhof.circleimageview.CircleImageView;

import static  com.example.fpm.activity.HomeActivity.f;
import static com.example.fpm.activity.HomeActivity.UidPrestador;
import static com.example.fpm.activity.ConversasPrestadorActivity.UidContratante;
import static com.example.fpm.activity.ConversasPrestadorActivity.nomeContratante;
import static com.example.fpm.activity.HomeActivity.NomePrestador;
import static com.example.fpm.activity.LoginActivity.bifurcacaoLogin;
import  static  com.example.fpm.activity.NegociarActivity.idUsuarioDestinatario;
import  static  com.example.fpm.activity.NegociarActivity.idUsuarioRemetente;
import  static  com.example.fpm.activity.NegociarActivity.tempoServico;
import static  com.example.fpm.activity.HomeActivity.tipoPrestador;

public class  ConfirmarPedidoActivity extends AppCompatActivity {


    private ImageButton voltar;
    private CircleImageView imgPerfil;
    private String nome,descricacao;
    private TextView textNome,textDescricao;
    private StorageReference storageReference = ConfiguracaoFirebase.getFirebaseStorage();
    private  StorageReference strg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_pedido);
        voltar = findViewById(R.id.sair_toolbar);
        imgPerfil = findViewById(R.id.imgPrefil_confirmar);
        textNome = findViewById(R.id.nome_confirmar);
        textDescricao = findViewById(R.id.descricao_confirmar);

        setarBifurcacao();
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

    @Override
    protected void onStart() {
        super.onStart();
        setarBifurcacao();
    }

    private void recuperarDados(String bif, String uid){
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
    private void setarBifurcacao(){
        if(bifurcacaoLogin == false){
            strg = storageReference.child("Imagens").child("perfilPrestador").child(UidPrestador+".jpeg");
            recuperarDados("Prestador", UidPrestador);
        }else {
            strg = storageReference.child("Imagens").child("perfilContratante").child(UidContratante + ".jpeg");
            recuperarDados("Contratante",UidContratante);
        }
    }

    public void Confirmar(View view){
        DatabaseReference ref = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference agendaRef,historicoRef ;
        String nome1,nome2;
        if(bifurcacaoLogin==false){
           nome2 =NomePrestador;
           recuperarDados("Contratante",idUsuarioRemetente);
           nome1= nome;
        }else{
            nome2 =nomeContratante;
            recuperarDados("Prestador", idUsuarioRemetente);
            nome1 =nome;
        }



        //primeiro nó agenda
        agendaRef =  ref.child("Agenda") .child(idUsuarioRemetente)
                .child(idUsuarioDestinatario);
        agendaRef .child("nome").setValue(nome1);
        agendaRef.child("tipo").setValue(tipoPrestador);
        agendaRef.child("tempo").setValue(tempoServico);
        agendaRef.child("uidPrestador").setValue(idUsuarioDestinatario);
        agendaRef.child("uidContratante").setValue(idUsuarioRemetente);
       //segundo nó agenda
        agendaRef =  ref.child("Agenda") .child(idUsuarioDestinatario)
                .child(idUsuarioRemetente);
        recuperarDados("Contratante",UidContratante);
        agendaRef .child("nome").setValue(nome2);
        agendaRef.child("tipo").setValue(tipoPrestador);
        agendaRef.child("tempo").setValue(tempoServico);
        agendaRef.child("uidPrestador").setValue(idUsuarioDestinatario);
        agendaRef.child("uidContratante").setValue(idUsuarioRemetente);
        //primeiro nó historico
        historicoRef = ref.child("Historico") .child(idUsuarioRemetente)
                .child(idUsuarioDestinatario);
        historicoRef.child("nome").setValue(nome1);
        historicoRef .child("data").setValue(getDateTime());
        historicoRef.child("uidPrestador").setValue(idUsuarioDestinatario);
        historicoRef.child("uidContratante").setValue(idUsuarioRemetente);

        //segundo nó historico
        historicoRef = ref.child("Historico") .child(idUsuarioDestinatario)
                .child(idUsuarioRemetente);

        historicoRef.child("nome").setValue(nome2);
        historicoRef .child("data").setValue(getDateTime());
        historicoRef.child("uidPrestador").setValue(idUsuarioDestinatario);
        historicoRef.child("uidContratante").setValue(idUsuarioRemetente);


        setarBifurcacao();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                 Intent i = new Intent(ConfirmarPedidoActivity.this,HomeActivity.class);
                startActivity(i);
            }
        },5000);



    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }
}