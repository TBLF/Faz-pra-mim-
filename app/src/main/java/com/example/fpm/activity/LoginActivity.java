package com.example.fpm.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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


public class LoginActivity extends AppCompatActivity {

    public static boolean u =false;
    private EditText textEmail;
    private EditText textSenha;
    private FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAutentication();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        textEmail = findViewById(R.id.editEmail);
        textSenha = findViewById(R.id.editSenha);


    }

    public void startHomeActivity (View view){
        boolean v =false;
        Intent HomeActivity = new Intent(this, com.example.fpm.activity.HomeActivity.class);
        if (verificarLogin(v) == true) {
            auth.signInWithEmailAndPassword(textEmail.getText().toString(),textSenha.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        startActivity(HomeActivity);
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

}