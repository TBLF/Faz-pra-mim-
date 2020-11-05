package com.example.fpm.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.example.fpm.adapter.MensagensAdapter;
import com.example.fpm.config.ConfiguracaoFirebase;
import com.example.fpm.config.UsuarioFireBase;
import com.example.fpm.moldes.Mensagem;
import com.example.fpm.R;
import com.example.fpm.moldes.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.example.fpm.activity.HomeActivity.UidPrestador;
import static com.example.fpm.activity.HomeActivity.NomePrestador;

public class NegociarActivity extends AppCompatActivity {
    private EditText editText;
    private TextView textNomeConversa;
    private FloatingActionButton fab2;
    private ConstraintLayout constraintLayout;
    private ImageButton imageButtonCancel,imageButtonSair;
    private Button btnConfirmar;
    private String idUsuarioRemetente;
    private String idUsuarioDestinatario;
    private CurrencyEditText currencyEditText;


    private RecyclerView recyclerView;
    private MensagensAdapter adapter;
    private List<Mensagem> mensagens = new ArrayList<>();

    private DatabaseReference database;
    private  DatabaseReference mensagensRef;
    private ChildEventListener childEventListenerMensagens;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_negociar);
        Locale locale = new Locale("pt","br");


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        fab2=findViewById(R.id.fab2);
        imageButtonCancel = findViewById(R.id.cancel);
        constraintLayout =findViewById(R.id.bloco_de_dados2);
        constraintLayout.setVisibility(View.GONE);
        editText = findViewById(R.id.editMensagem);
        imageButtonSair = findViewById(R.id.sairToolbar);
        btnConfirmar = findViewById(R.id.btn_confirmar);
        currencyEditText = findViewById(R.id.edit_sugerirpreco);


        currencyEditText.setLocale(locale);
        Intent i = new Intent(this,HomeActivity.class);
        Intent j = new Intent(this,ConfirmarPedidoActivity.class);
        FloatingActionButton fab = findViewById(R.id.fab);
        idUsuarioRemetente = UsuarioFireBase.getIdentificadorusuario();
        idUsuarioDestinatario = UidPrestador;
        recyclerView = findViewById(R.id.recycler_conversas);
        editText = findViewById(R.id.editMensagem);
        textNomeConversa = findViewById(R.id.nome_conversa);
        textNomeConversa.setText(NomePrestador);


        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constraintLayout.setVisibility(View.VISIBLE);
            }
        });

        imageButtonSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);
            }
        });
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(j);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String mensagemText = editText.getText().toString();

                if(!mensagemText.isEmpty()){
                    Mensagem mensagem = new Mensagem();
                    mensagem.setIdUsuario(idUsuarioRemetente);
                    mensagem.setMensagem(mensagemText);

                    salvarMensagem(idUsuarioRemetente,idUsuarioDestinatario,mensagem);
                    editText.setText("");
                }else{
                    Toast.makeText(NegociarActivity.this, "Escreva algo para enviar", Toast.LENGTH_SHORT).show();
                }


            }
        });

        imageButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constraintLayout.setVisibility(View.GONE);
            }
        });

        //Configuração do Adpter;
        adapter = new MensagensAdapter(mensagens, getApplicationContext());

        //Configuração do recyclerview

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        database = ConfiguracaoFirebase.getFirebaseDatabase();
        mensagensRef = database.child("Mensagem")
                .child(idUsuarioRemetente)
                .child(idUsuarioDestinatario);
    }

    private void salvarMensagem(String idRementente, String idDestinatario, Mensagem mensagem){
        DatabaseReference ref = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference msg = ref.child("Mensagem");

        msg.child(idRementente)
                .child(idDestinatario)
                .push()
                .setValue(mensagem);
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarMensagem();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mensagensRef.removeEventListener(childEventListenerMensagens);
    }

    private void recuperarMensagem(){
        childEventListenerMensagens = mensagensRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Mensagem mensagem = snapshot.getValue(Mensagem.class);
                mensagens.add(mensagem);
                adapter.notifyDataSetChanged();
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