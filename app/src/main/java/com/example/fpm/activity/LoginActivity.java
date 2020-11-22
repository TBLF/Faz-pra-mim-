package com.example.fpm.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fpm.R;
import com.example.fpm.config.ConfiguracaoFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends AppCompatActivity {

    public static boolean u =false;
    public static boolean bifurcacaoCadastro = false;
    public static boolean bifurcacaoLogin = false;
    private List<String> emailPrestador, emailContratante;
    private EditText textEmail;
    private EditText textSenha;
    private FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAutentication();
    private String[] appPermissions = null;
    private static final int CODIGO_PERMISSOES_REQUERIDAS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        textEmail = findViewById(R.id.editEmail);
        textSenha = findViewById(R.id.editSenha);
        emailContratante = new ArrayList<String>();
        emailPrestador = new ArrayList<String>();
        DatabaseReference ref =  ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference refC = ref.child("Contratante");
        refC.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot d : snapshot.getChildren()){
                        emailContratante.add(d.child("email").getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference refP = ref.child("Prestador");
        refP.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot d : snapshot.getChildren()){
                    emailPrestador.add(d.child("email").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //atribuindo permissões a um array
        appPermissions = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        if (!verficarPermissoes()){
            Toast.makeText(this, "Nem todas as permissões estão ativas", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void startHomeActivity (View view){
        boolean v =false;
        Intent homeActivity = new Intent(this, com.example.fpm.activity.HomeActivity.class);
        Intent homePrestadorActivity = new Intent(this, com.example.fpm.activity.HomePrestadorActivity.class);
        if (verificarLogin(v) == true) {
            auth.signInWithEmailAndPassword(textEmail.getText().toString(),textSenha.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    boolean user = false;
                    if(task.isSuccessful()){
                       for (String email : emailContratante){
                           if(email.equals(textEmail.getText().toString())){
                               user = true;
                           }
                       }
                        for (String email : emailPrestador){
                            if(email.equals(textEmail.getText().toString())){
                               user = false;
                            }
                        }

                        if(user==true){
                            bifurcacaoLogin = false;
                            startActivity(homeActivity);
                        }else {
                            bifurcacaoLogin = true;
                            startActivity(homePrestadorActivity);
                        }
                    }else{

                        String excecao ="";

                        try{
                            throw task.getException();
                        }catch (FirebaseAuthInvalidUserException e){
                            excecao = "Usuário não está cadastrado.";
                        }catch (FirebaseAuthInvalidCredentialsException e){
                            excecao = "Email ou senha não correspondem ao usuário cadastrado.";
                        } catch (Exception e) {
                            excecao = "Erro ao autenticar usuário.";
                            e.printStackTrace();
                        }
                        Toast.makeText(LoginActivity.this, excecao, Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }
    }
    public void startCadastroActivity (View view){
        Intent Activity = new Intent(this, EscolhaActivity.class);
        startActivity(Activity);
        finish();
    }

    public boolean verificarLogin(boolean v){
        v=true;
        if(textEmail.getText().toString().isEmpty()){
            if(textSenha.getText().toString().isEmpty()){
                Toast.makeText(getApplicationContext(),"Preencha os campos", Toast.LENGTH_LONG ).show();
            }
            else{
                Toast.makeText(getApplicationContext(),"Preencha o email", Toast.LENGTH_LONG ).show();
            }
            v=false;
        }
        else{
            if(textSenha.getText().toString().isEmpty()){
                Toast.makeText(getApplicationContext(),"Preencha a senha", Toast.LENGTH_LONG ).show();
                v=false;
            }

        }
        return v;
    }

    public boolean verficarPermissoes() {
        List<String> permissoesRequeridas = new ArrayList<>();
        for (String permission : appPermissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissoesRequeridas.add(permission);
            }
        }
        if (!permissoesRequeridas.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissoesRequeridas.toArray(new String[permissoesRequeridas.size()]), CODIGO_PERMISSOES_REQUERIDAS);
            return false;
        }

        return true;
    }

}
