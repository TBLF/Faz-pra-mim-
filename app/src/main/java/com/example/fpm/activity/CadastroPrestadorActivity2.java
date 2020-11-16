package com.example.fpm.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.fpm.R;
import com.example.fpm.activity.CadastroPrestadorServicoActivity;
import com.example.fpm.activity.HomeActivity;
import com.example.fpm.config.Base64Custom;
import com.example.fpm.config.ConfiguracaoFirebase;
import com.example.fpm.config.UsuarioFireBase;
import com.example.fpm.moldes.Prestador;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Random;

import static com.example.fpm.activity.CadastroPrestadorActivity.datanasc2;
import static com.example.fpm.activity.CadastroPrestadorActivity.ender2;
import static com.example.fpm.activity.CadastroPrestadorActivity.imagem2;
import static com.example.fpm.activity.CadastroPrestadorActivity.nome2;
import static com.example.fpm.activity.CadastroPrestadorActivity.telefone2;
import static com.example.fpm.activity.LoginActivity.u;

public class CadastroPrestadorActivity2 extends AppCompatActivity {

    private TextInputEditText senha, senha2, email;
    private DatabaseReference ref = ConfiguracaoFirebase.getFirebaseDatabase().child("Prestador");
    private Prestador prestador = new Prestador();
    private FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAutentication();
    private ImageButton imageButtonVoltar;
    private StorageReference storageReference;
    private LocationManager locationManager;
    private LocationListener locationListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = new Intent(this, CadastroPrestadorServicoActivity.class);
        setContentView(R.layout.activity_cadastro2);
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
                auth.createUserWithEmailAndPassword(prestador.getEmail(), prestador.getSenha()).addOnCompleteListener(CadastroPrestadorActivity2.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            try{
                                String identificadorUsuario = Base64Custom.codificarBase64(prestador.getEmail());
                                //Salvando dados

                                prestador.setUid(identificadorUsuario);
                                ref.child(auth.getUid().toString()).setValue(prestador);

                                //Pegando localização atual

                                recuperarLocalizacaoAtualUsuario(auth.getUid());

                                //Salvando Imagem
                                if(u==true){
                                    FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAutentication();
                                    String id = usuario.getCurrentUser().getUid();

                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    imagem2.compress(Bitmap.CompressFormat.JPEG,70,baos);
                                    byte[] dadosimagem = baos.toByteArray();
                                    StorageReference imageRef =storageReference
                                            .child("Imagens")
                                            .child("perfil")
                                            .child(id+".jpeg");
                                    UploadTask uploadTask = imageRef.putBytes(dadosimagem);
                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(CadastroPrestadorActivity2.this, "Erro ao fazer upload da imagem", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            Toast.makeText(CadastroPrestadorActivity2.this, "Sucesso ao fazer upload da imagem", Toast.LENGTH_SHORT).show();
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
        prestador.setEmail(email.getText().toString());
        prestador.setSenha(senha.getText().toString());
        prestador.setNome(nome2.getText().toString());
        prestador.setTelefone(telefone2.getUnMasked().toString());
        prestador.setIdade(Integer.parseInt(datanasc2.getText().toString()));
        prestador.setEndereco(ender2.getText().toString());
    }

    private void recuperarLocalizacaoAtualUsuario(String uid) {
        DatabaseReference refLatLong = ConfiguracaoFirebase.getFirebaseDatabase().child("Prestador").child(uid);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();


            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    10000,
                    10,
                    locationListener

            );
        }
    }
}