package com.example.fpm.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fpm.R;
import com.example.fpm.config.Base64Custom;
import com.example.fpm.config.ConfiguracaoFirebase;
import com.example.fpm.moldes.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.santalu.maskara.widget.MaskEditText;

import java.io.ByteArrayOutputStream;
import de.hdodenhof.circleimageview.CircleImageView;
import static com.example.fpm.activity.LoginActivity.u;
import static com.example.fpm.activity.HomeActivity.f;



public class EditarPerfilActivity extends AppCompatActivity {

    private EditText nomeView,idadeView,enderecoView;
    private MaskEditText telefoneView;
    private CircleImageView imagemCircle;
    private Usuario usuarios = new Usuario();
    private DatabaseReference ref = ConfiguracaoFirebase.getFirebaseDatabase().child("Contratante");
    private StorageReference storageReference;
    public ImageButton btnCamera,btnGaleria;
    private static final int SELECA0_CAMERA =100;
    private static final int SELECA0_GALERIA =200;
    private Bitmap imagem ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        nomeView = findViewById(R.id.editNome);
        idadeView = findViewById(R.id.editData);
        telefoneView = findViewById(R.id.editFone);
        enderecoView = findViewById(R.id.editEnd);
        imagemCircle = findViewById(R.id.circle_perfil);
        btnCamera = findViewById(R.id.btn_camera);
        btnGaleria = findViewById(R.id.btn_galeria);
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();


        f=2;
        Intent i = new Intent(this,HomeActivity.class);
        ImageButton imageButton = findViewById(R.id.sair_toolbar);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(i.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(i,SELECA0_CAMERA);
                }

            }
        });
        btnGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if(i.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(i,SELECA0_GALERIA);
                }
            }
        });
    }

    public void Salvar(View view) {
        Intent tela2Activity = new Intent(this, HomeActivity.class);
        FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAutentication();
        FirebaseUser user = usuario.getCurrentUser();
        String id = user.getUid();
        if (u == true) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
            byte[] dadosimagem = baos.toByteArray();
            StorageReference imageRef = storageReference
                    .child("Imagens")
                    .child("perfil")
                    .child(id + ".jpeg");

            UploadTask uploadTask = imageRef.putBytes(dadosimagem);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditarPerfilActivity.this, "Erro ao fazer upload da imagem", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(EditarPerfilActivity.this, "Sucesso ao fazer upload da imagem", Toast.LENGTH_SHORT).show();
                }
            });

        }
        verificarCampos(id);
        startActivity(tela2Activity);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){

            try{
                switch (requestCode){
                    case SELECA0_CAMERA:
                        imagem = (Bitmap)data.getExtras().get("data");
                        break;
                    case SELECA0_GALERIA:
                        Uri localImagem = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(),localImagem);
                        break;
                }

                if(imagem!=null){
                    u=true;
                    imagemCircle.setImageBitmap(imagem);
                }else{
                    u=false;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public void verificarCampos(String id){
        cadastrarClasse();
        if(!nomeView.getText().toString().isEmpty()){
            ref.child(id).setValue(usuarios.getNome());
        }

        if(!telefoneView.getUnMasked().toString().isEmpty()){
            if(telefoneView.getUnMasked().toString().length()>11){
                Toast.makeText(this, "Número de telefone iválido.", Toast.LENGTH_SHORT).show();
            }else if(telefoneView.getUnMasked().toString().length()<11){
                Toast.makeText(this, "Número de telefone iválido.", Toast.LENGTH_SHORT).show();
            } else {
                ref.child(id).setValue(usuarios.getTelefone());
            }
        }

        if(!idadeView.getText().toString().isEmpty()){
            if(Integer.parseInt(idadeView.getText().toString())<0 || Integer.parseInt(idadeView.getText().toString())>120){
                Toast.makeText(this, "Insira uma idade adequada.", Toast.LENGTH_SHORT).show();
            } else if(Integer.parseInt(idadeView.getText().toString())<16){
                Toast.makeText(this, "Você deve ter no mínimo 16 anos.", Toast.LENGTH_SHORT).show();
            }else{
                ref.child(id).setValue(usuarios.getIdade());
            }
        }

        if(!enderecoView.getText().toString().isEmpty()){
            ref.child(id).setValue(usuarios.getEndereco());
        }

    }
    public void cadastrarClasse () {
        if(!nomeView.getText().toString().isEmpty()) usuarios.setNome(nomeView.getText().toString());
        if(!telefoneView.getText().toString().isEmpty())usuarios.setTelefone(telefoneView.getText().toString());
        if(!idadeView.getText().toString().isEmpty())usuarios.setIdade(Integer.parseInt(idadeView.getText().toString()));
        if(!enderecoView.getText().toString().isEmpty())usuarios.setEndereco(enderecoView.getText().toString());
    }

}