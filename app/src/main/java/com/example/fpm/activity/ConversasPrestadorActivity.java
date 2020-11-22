package com.example.fpm.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.fpm.R;
import com.example.fpm.adapter.ConversasPrestadorAdapter;
import com.example.fpm.config.ConfiguracaoFirebase;
import com.example.fpm.config.UsuarioFireBase;
import com.example.fpm.moldes.Conversa;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class ConversasPrestadorActivity extends AppCompatActivity {
    public static String UidContratante,nomeContratante;
    private RecyclerView recyclerViewConversas;
    private List<Conversa> conversaLista = new ArrayList<>();
    private ConversasPrestadorAdapter conversasPrestadorAdapter;
    private DatabaseReference reference;
    private  DatabaseReference conversasRef;
    private  ChildEventListener childEventListenerConversas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversas_prestador);

        recyclerViewConversas = findViewById(R.id.list_contratante);

        //Configurando adapter
        conversasPrestadorAdapter = new ConversasPrestadorAdapter(conversaLista,this);
        //Configurando recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewConversas.setLayoutManager(layoutManager);
        recyclerViewConversas.setHasFixedSize(true);
        recyclerViewConversas.setAdapter(conversasPrestadorAdapter);

        UidContratante = "bWFyaW9AZ21haWwuY29t";
        nomeContratante = "Luigi";

        //Configura conversas referencia do banco de dados
        String identificadorUsuario  = UsuarioFireBase.getIdentificadorusuario();
        reference = ConfiguracaoFirebase.getFirebaseDatabase();
        conversasRef =  reference.child("Conversa").child(identificadorUsuario);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(ConversasPrestadorActivity.this,NegociarActivity.class);
                startActivity(i);
            }
        },5000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarConversas();
    }

    @Override
    protected void onStop() {
        super.onStop();
        conversasRef.removeEventListener(childEventListenerConversas);
    }

    public  void recuperarConversas(){

        childEventListenerConversas = conversasRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //recuperando conversas

                Conversa conversa = snapshot.getValue(Conversa.class);
                conversaLista.add(conversa);
                conversasPrestadorAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}