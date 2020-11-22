package com.example.fpm.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.bumptech.glide.Glide;
import com.example.fpm.adapter.MensagensAdapter;
import com.example.fpm.config.Base64Custom;
import com.example.fpm.config.ConfiguracaoFirebase;
import com.example.fpm.config.UsuarioFireBase;
import com.example.fpm.moldes.Conversa;
import com.example.fpm.moldes.Mensagem;
import com.example.fpm.R;
import com.example.fpm.moldes.Usuario;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.santalu.maskara.widget.MaskEditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.fpm.activity.HomeActivity.UidPrestador;
import static com.example.fpm.activity.ConversasPrestadorActivity.UidContratante;
import static com.example.fpm.activity.ConversasPrestadorActivity.nomeContratante;
import static com.example.fpm.activity.HomeActivity.NomePrestador;
import static com.example.fpm.activity.LoginActivity.bifurcacaoLogin;

public class NegociarActivity extends AppCompatActivity {
    private EditText editText;
    private CurrencyEditText editPreco;
    private MaskEditText editTempo;
    private TextView textNomeConversa,textTitulo,textPreco,textTempo;
    private ImageView imageCamera;
    private FloatingActionButton fab2,fab;
    private ConstraintLayout constraintLayout;
    private ImageButton imageButtonCancel,imageButtonSair,imageButtonCancelPopup;
    private CircleImageView imgPrestador;
    private Button btnEnviar,btnConfirmar;
    private String idUsuarioRemetente;
    private String idUsuarioDestinatario;
    private CurrencyEditText currencyEditText;
    private  StorageReference storageReference ;
    private Usuario usuarioDestinatario;
    private ConstraintLayout popupConstraint;
    private  Intent i,j;
    private LinearLayout barraMensagem;

    private RecyclerView recyclerView;
    private MensagensAdapter adapter;
    private List<Mensagem> mensagens = new ArrayList<>();

    private DatabaseReference database;
    private  DatabaseReference mensagensRef,negociacaoRef;
    private ChildEventListener childEventListenerMensagens;

    private static final int SELECA0_CAMERA =100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_negociar);

        inicializarComponentes();

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
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(bifurcacaoLogin==false){
                    //contratante
                    salvarNegociacao(0);
                }else{
                   //prestador
                    salvarNegociacao(1);
                }

                recuperarNegociacao();


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

                    //Salvar imagem para o remetente
                    salvarMensagem(idUsuarioRemetente,idUsuarioDestinatario,mensagem);
                    //Salvar imagem para o destinatario
                    salvarMensagem(idUsuarioDestinatario,idUsuarioRemetente,mensagem);
                    //Salvar conversa
                    salvarConversa(mensagem);

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


        //evento de envio de imagem
        imageCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(i.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(i,SELECA0_CAMERA);
                }
            }
        });

        imageButtonCancelPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupConstraint.setVisibility(View.GONE);
                barraMensagem.setClickable(true);
            }
        });

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bifurcacaoLogin == false){
                    startActivity(j);
                }
            }
        });



    }
    private void inicializarComponentes(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Locale locale = new Locale("pt","br");


        fab2=findViewById(R.id.fab2);
        imageButtonCancel = findViewById(R.id.cancel);
        constraintLayout =findViewById(R.id.bloco_de_dados2);
        constraintLayout.setVisibility(View.GONE);
        editText = findViewById(R.id.editMensagem);
        imageButtonSair = findViewById(R.id.sairToolbar);
        btnEnviar = findViewById(R.id.btn_enviar);
        currencyEditText = findViewById(R.id.edit_sugerirpreco);
        imgPrestador = findViewById(R.id.imagePrestador_negociar);
        imageCamera = findViewById(R.id.btn_tirarfoto);
        usuarioDestinatario = new Usuario();
        editPreco = findViewById(R.id.edit_sugerirpreco);
        recyclerView = findViewById(R.id.recycler_conversas);
        editText = findViewById(R.id.editMensagem);
        editTempo = findViewById(R.id.editTextTempo);
        textPreco = findViewById(R.id.textPreco);
        textTitulo = findViewById(R.id.nomeTitulo);
        textTempo = findViewById(R.id.textTempo);
        popupConstraint = findViewById(R.id.popupConstraint);
        fab = findViewById(R.id.fab);
        textNomeConversa = findViewById(R.id.nome_conversa);
        imageButtonCancelPopup = findViewById(R.id.cancelPopup);
        barraMensagem = findViewById(R.id.barra_mensagem);
        btnConfirmar = findViewById(R.id.btn_confirmar);



        StorageReference storageReference = ConfiguracaoFirebase.getFirebaseStorage();
        StorageReference strg ;
        if(bifurcacaoLogin == false){
            strg = storageReference.child("Imagens").child("perfilContratante").child(UidPrestador+".jpeg");
            textNomeConversa.setText(NomePrestador);
            usuarioDestinatario.setNome(NomePrestador);
            usuarioDestinatario.setEmail(Base64Custom.decodificarBase64(UidPrestador));
            idUsuarioDestinatario = UidPrestador;

        }else{
            strg = storageReference.child("Imagens").child("perfilPrestador").child(UidContratante+".jpeg");
            textNomeConversa.setText(nomeContratante);
            usuarioDestinatario.setNome(nomeContratante);
            usuarioDestinatario.setEmail(Base64Custom.decodificarBase64(UidContratante));
            idUsuarioDestinatario = UidContratante;
        }


        currencyEditText.setLocale(locale);
        i = new Intent(this,HomeActivity.class);
        j = new Intent(this,ConfirmarPedidoActivity.class);
        idUsuarioRemetente = UsuarioFireBase.getIdentificadorusuario();

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

        negociacaoRef = database.child("Negociacao")
                .child(idUsuarioRemetente)
                .child(idUsuarioDestinatario);

        //colocando imagem de perfil do prestador
        String foto = strg.toString();
        if(foto!= null){
            Glide.with(this)
                    .using(new FirebaseImageLoader())
                    .load(strg)
                    .into(imgPrestador);
        }
        else{
            imgPrestador.setImageResource(R.drawable.imagem_fotouser);
        }
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

    private void salvarNegociacao(int num){
        DatabaseReference reference1 = ConfiguracaoFirebase.getFirebaseDatabase().child("Negociacao").child(idUsuarioRemetente).child(idUsuarioDestinatario);
        reference1.child("codigo").setValue(num);
        reference1.child("preco").setValue(editPreco.getText().toString());
        reference1.child("tempo").setValue(editTempo.getUnMasked());
        reference1.child("idRemetente").setValue(idUsuarioRemetente);
        reference1.child("idDestinatario").setValue(idUsuarioDestinatario);

        DatabaseReference reference2 = ConfiguracaoFirebase.getFirebaseDatabase().child("Negociacao").child(idUsuarioDestinatario).child(idUsuarioRemetente);
        reference2.child("codigo").setValue(num);
        reference2.child("preco").setValue(editPreco.getText().toString());
        reference2.child("tempo").setValue(editTempo.getUnMasked());
        reference2.child("idRemetente").setValue(idUsuarioRemetente);
        reference2.child("idDestinatario").setValue(idUsuarioDestinatario);
    }
    private  void recuperarNegociacao(){
        negociacaoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(bifurcacaoLogin == false){
                            if(Integer.parseInt(snapshot.child("codigo").getValue().toString()) ==  1) {
                                popupConstraint.setVisibility(View.VISIBLE);
                                constraintLayout.setVisibility(View.GONE);
                                barraMensagem.setClickable(false);
                                textTitulo.setText(snapshot.child("idRemetente").getValue().toString());
                                textPreco.setText(snapshot.child("preco").getValue().toString());
                                textTempo.setText(snapshot.child("tempo").getValue().toString()+" dias");
                            }
                        }



                    }else{
                            if(Integer.parseInt(snapshot.child("codigo").getValue().toString()) ==  0){
                                popupConstraint.setVisibility(View.VISIBLE);
                                constraintLayout.setVisibility(View.GONE);
                                barraMensagem.setClickable(false);
                                textTitulo.setText(snapshot.child("idRemetente").getValue().toString());
                                textPreco.setText(snapshot.child("preco").getValue().toString());
                                textTempo.setText(snapshot.child("tempo").getValue().toString()+" dias");

                            }
                    }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Bitmap imagem = null;
            try{
                switch (requestCode){
                    case SELECA0_CAMERA:
                        imagem = (Bitmap)data.getExtras().get("data");
                        break;
                }

                if(imagem!=null){

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG,70,baos);
                    byte[] dadosimagem = baos.toByteArray();

                    //Criando nome da imagem

                    String nomeImagem = UUID.randomUUID().toString();
                    final StorageReference imageRef =storageReference
                            .child("Imagens")
                            .child("Fotos")
                            .child(idUsuarioRemetente)
                            .child(nomeImagem+".jpeg");

                    UploadTask uploadTask = imageRef.putBytes(dadosimagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(NegociarActivity.this, "Erro ao fazer o upload da imagem", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                           imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                               @Override
                               public void onComplete(@NonNull Task<Uri> task) {
                                   String url = task.getResult().toString();
                                   Mensagem mensagem = new Mensagem();
                                   mensagem.setIdUsuario(idUsuarioRemetente);
                                   mensagem.setMensagem("imagem.jpeg");
                                   mensagem.setImagem(url);


                                   //mensagem para o remetente
                                   salvarMensagem(idUsuarioRemetente,idUsuarioDestinatario,mensagem);
                                   //salvar para o destinatário
                                   salvarMensagem(idUsuarioDestinatario,idUsuarioRemetente,mensagem);
                               }
                           });
                        }
                    });
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void salvarConversa(Mensagem msg){
        Conversa conversaRemetente = new Conversa();
        conversaRemetente.setIdRemetente(idUsuarioRemetente);
        conversaRemetente.setIdDestinatario(idUsuarioDestinatario);
        conversaRemetente.setUltimaMensagem(msg.getMensagem());
        conversaRemetente.setUsuarioExibicao(usuarioDestinatario);

        conversaRemetente.salvar();
    }
}