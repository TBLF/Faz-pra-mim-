package com.example.fpm.config;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.fpm.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

public class UsuarioFireBase {

    public static String nome;
    public static  String getIdentificadorusuario(){
        FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAutentication();
        String email = usuario.getCurrentUser().getEmail();
        String identificadorUsuario = Base64Custom.codificarBase64(email);

        return  identificadorUsuario;
    }

    public static FirebaseUser getUsuarioAtual(){

        FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAutentication();
        FirebaseUser user = usuario.getCurrentUser();

        return user;
    }

     public static boolean atualizarFotoUsuario(Uri url){
       try {
            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(url)
                    .build();
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {
                        System.out.println("erro ao atualizar foto");
                    }
                }
            });
            return true;
        }catch (Exception e){
            return false;
        }

    }
    public static boolean atualizarDadosUsuario(String nome){
        try {
            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nome)
                    .build();
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {
                        System.out.println("erro ao atualizar nome");
                    }
                }
            });

            return true;
        }catch (Exception e){
            return false;
        }

    }

    public static String getNomeUsuarioAtual(){
        FirebaseUser user = UsuarioFireBase.getUsuarioAtual();
        String id = user.getUid();
        DatabaseReference ref = ConfiguracaoFirebase.getFirebaseDatabase().child("Contratante").child(id);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    nome = snapshot.child("nome").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return nome;
    }

}
