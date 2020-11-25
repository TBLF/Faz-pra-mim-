package com.example.fpm.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fpm.config.Base64Custom;
import com.example.fpm.R;
import com.example.fpm.config.ConfiguracaoFirebase;
import com.example.fpm.config.UsuarioFireBase;
import com.example.fpm.moldes.Mensagem;
import com.example.fpm.moldes.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import static com.example.fpm.activity.CadastroActivity.datanasc;
import static com.example.fpm.activity.CadastroActivity.imagem;
import static com.example.fpm.activity.CadastroActivity.nome;
import static com.example.fpm.activity.CadastroActivity.telefone;
import static com.example.fpm.activity.CadastroActivity.descricao;
import static com.example.fpm.activity.LoginActivity.u;


public class CadastroContratanteActivity extends AppCompatActivity {
    private TextInputEditText senha, senha2, email;
    private DatabaseReference ref = ConfiguracaoFirebase.getFirebaseDatabase().child("Contratante");
    private Usuario usuario = new Usuario();
    private FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAutentication();
    private ImageButton imageButtonVoltar;
    private StorageReference storageReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = new Intent(this,CadastroActivity.class);
        setContentView(R.layout.activity_cadastro_contratante);
        senha = findViewById(R.id.editSenha);
        senha2 = findViewById(R.id.editSenha2);
        email = findViewById(R.id.editEmail);
        imageButtonVoltar = findViewById(R.id.sair_toolbar);
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();

        imageButtonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);
            }
        });
    }

    public void starttela2Activity2(View view) {

        boolean v = false;
        if (verificarCampos(v) == true) {
            if(verificarSenhas(v)==true){


                final Intent tela2Activity = new Intent(this, HomeActivity.class);
                cadastrarClasse();
                auth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(CadastroContratanteActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            try{
                                String identificadorUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                                //Salvando dados

                                usuario.setUid(identificadorUsuario);
                                ref.child(auth.getUid().toString()).setValue(usuario);

                                //Configurando interface de pesquisa de serviços
                                ref.child(auth.getUid()).child("Interface Servico").child("Servico 1").child("numero").setValue(1);
                                ref.child(auth.getUid()).child("Interface Servico").child("Servico 2").child("numero").setValue(2);
                                ref.child(auth.getUid()).child("Interface Servico").child("Servico 3").child("numero").setValue(3);

                                //Salvando Imagem
                                if(u==true){
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    imagem.compress(Bitmap.CompressFormat.JPEG,70,baos);
                                    byte[] dadosimagem = baos.toByteArray();
                                    StorageReference imageRef =storageReference
                                            .child("Imagens")
                                            .child("perfilContratante")
                                            .child(usuario.getUid()+".jpeg");
                                    UploadTask uploadTask = imageRef.putBytes(dadosimagem);
                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(CadastroContratanteActivity.this, "Erro ao fazer upload da imagem", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            Toast.makeText(CadastroContratanteActivity.this, "Sucesso ao fazer upload da imagem", Toast.LENGTH_SHORT).show();

                                            imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Uri> task) {
                                                    Uri url = task.getResult();
                                                    UsuarioFireBase.atualizarFotoUsuario(url);
                                                    ref.child(auth.getUid().toString()).child("foto").setValue(url.toString());
                                                }
                                            });
                                        }
                                    });

                                }
                                startActivity(tela2Activity);
                            }catch (Exception e){

                            }


                            finish();
                        } else {

                            String excecao = "";

                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                excecao = "Digite uma senha mais forte.";
                            }catch (FirebaseAuthInvalidCredentialsException e){
                                excecao = "Por favor, digite um e-mail válido.";
                            }catch (FirebaseAuthUserCollisionException e) {
                                excecao = "Esta conta ja foi cadastrada.";
                            }catch (Exception e) {
                                excecao ="Erro ao cadastrar usuário.";
                                e.printStackTrace();
                            }

                            Toast.makeText(getApplicationContext(), excecao, Toast.LENGTH_LONG).show();

                        }

                    }
                });
            }else{
                Toast.makeText(getApplicationContext(),"Senhas não conferem", Toast.LENGTH_LONG ).show();
            }

        }
        else{
            Toast.makeText(getApplicationContext(),"Preencha todos os campos", Toast.LENGTH_LONG ).show();
        }
    }


    public boolean verificarCampos ( boolean v){
        v = true;
        if (email.getText().toString().isEmpty()) {
            v = false;
        }
        if (senha.getText().toString().isEmpty()) {
            v = false;
        }
        if (senha2.getText().toString().isEmpty()) {
            v = false;
        }
        return v;
    }

    public boolean verificarSenhas(boolean v){
        v=true;
        if(!senha.getText().toString().equals(senha2.getText().toString())){
            v=false;
        }
        return v;
    }
    public void cadastrarClasse () {
        usuario.setEmail(email.getText().toString());
        usuario.setSenha(senha.getText().toString());
        usuario.setNome(nome.getText().toString());
        usuario.setTelefone(telefone.getUnMasked().toString());
        usuario.setIdade(Integer.parseInt(datanasc.getText().toString()));
        usuario.setLatitude(PesquisarEnderecoActivity.lat);
        usuario.setLongitude(PesquisarEnderecoActivity.lng);
        usuario.setDescricao(descricao.getText().toString());
    }
}
