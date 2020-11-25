package com.example.fpm.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;

import com.example.fpm.R;
import com.example.fpm.adapter.ConversasPrestadorAdapter;
import com.example.fpm.config.Base64Custom;
import com.example.fpm.config.ConfiguracaoFirebase;
import com.example.fpm.config.RecyclerItemClickListener;
import com.example.fpm.config.UsuarioFireBase;
import com.example.fpm.moldes.Conversa;
import com.example.fpm.moldes.Usuario;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

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
    public static  String nomePesquisa;

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

        //Configura conversas referencia do banco de dados
        String identificadorUsuario  = UsuarioFireBase.getIdentificadorusuario();
        reference = ConfiguracaoFirebase.getFirebaseDatabase();
        conversasRef =  reference.child("Conversa").child(identificadorUsuario);

        //configurar evento de click
        recyclerViewConversas.addOnItemTouchListener(

                new RecyclerItemClickListener(
                        this,
                        recyclerViewConversas,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Conversa conversaSelecionada = conversaLista.get(position);
                                UidContratante=  conversaSelecionada.getIdDestinatario();
                                pesquisarNome("Contratante",UidContratante);
                                nomeContratante = nomePesquisa;
                                Intent i = new Intent(ConversasPrestadorActivity.this, NegociarActivity.class);
                                i.putExtra("chatContato",  conversaSelecionada.getUsuarioExibicao().getNome());
                                startActivity(i);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );
        recuperarConversas();

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
    private void pesquisarNome(String child, String uid) {
        DatabaseReference reference = ConfiguracaoFirebase.getFirebaseDatabase().child(child);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot d : snapshot.getChildren()) {
                        if (d.child("uid").getValue().toString().equals(uid)) {
                            nomePesquisa = d.child("nome").getValue().toString();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}