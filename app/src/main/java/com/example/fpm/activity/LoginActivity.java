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

import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends AppCompatActivity {

    public static boolean u =false;
    public static boolean bifurcacao = false;
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
