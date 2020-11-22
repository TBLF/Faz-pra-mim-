package com.example.fpm.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fpm.R;
import com.example.fpm.config.ConfiguracaoFirebase;
import com.example.fpm.moldes.Usuario;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.santalu.maskara.widget.MaskEditText;

import java.io.ByteArrayOutputStream;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.fpm.activity.LoginActivity.bifurcacaoCadastro;
import static com.example.fpm.activity.LoginActivity.u;
import static com.example.fpm.activity.HomeActivity.f;
import static com.example.fpm.activity.PesquisarEnderecoActivity.lat;
import static com.example.fpm.activity.PesquisarEnderecoActivity.lng;


public class EditarPerfilActivity extends AppCompatActivity {

    private EditText nomeView;
    AutoCompleteTextView  enderecoView;
    private MaskEditText telefoneView;
    private  TextInputEditText latlngedit;
    private CircleImageView imagemCircle;
    private Usuario usuarios = new Usuario();
    private DatabaseReference ref = ConfiguracaoFirebase.getFirebaseDatabase().child("Contratante");
    private StorageReference storageReference;
    public ImageButton btnCamera,btnGaleria;
    public static int cidade2 =0;
    public static LatLng localcidade2 = null;
    public  static  boolean entrou2 = false;
    private static final int SELECA0_CAMERA =100;
    private static final int SELECA0_GALERIA =200;
    private Bitmap imagem ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        nomeView = findViewById(R.id.editNome);
        telefoneView = findViewById(R.id.editFone);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, CIDADES);
        enderecoView = (AutoCompleteTextView)
                findViewById(R.id.editEnd);
        enderecoView.setAdapter(adapter);
        imagemCircle = findViewById(R.id.circle_perfil);
        btnCamera = findViewById(R.id.btn_camera);
        btnGaleria = findViewById(R.id.btn_galeria);
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();
        latlngedit= findViewById(R.id.editLatLng);
        bifurcacaoCadastro = false;

        if(cidade2!=0) {
            switch (cidade2) {
                case 1:
                    enderecoView.setText("Manaus");
                    break;
                case 2:
                    enderecoView.setText("São paulo");
                    break;
                case 3:
                    enderecoView.setText("Rio de Janeiro");
                    break;
            }
        }
        if(entrou2==true){
            latlngedit.setHint("");
            latlngedit.setText(String.valueOf(lat)+"/"+String.valueOf(lng));
        }

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


        enderecoView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        localcidade2= new LatLng(-3.0895571, -59.9644187);
                        cidade2 =1;
                        break;
                    case 1:
                        localcidade2 = new LatLng(-3.0895571, -59.9644187);
                        cidade2 =2;
                        break;
                    case 2:
                        localcidade2 = new LatLng(-3.0895571, -59.9644187);
                        cidade2 =3;
                        break;
                }
            }
        });

    }

    public void Salvar(View view) {
        Intent tela2Activity = new Intent(this, HomeActivity.class);
        FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAutentication();
        FirebaseUser user = usuario.getCurrentUser();
        String id = user.getUid();
        if (u == true && imagem!=null) {
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
        Log.i("aqui","dentro do verificar campos");
        cadastrarClasse();
        if(!nomeView.getText().toString().isEmpty()){
            ref.child(id).child("nome").setValue(usuarios.getNome());
        }

        if(!telefoneView.getUnMasked().toString().isEmpty()){
            if(telefoneView.getUnMasked().toString().length()>11){
                Toast.makeText(this, "Número de telefone iválido.", Toast.LENGTH_SHORT).show();
            }else if(telefoneView.getUnMasked().toString().length()<11){
                Toast.makeText(this, "Número de telefone iválido.", Toast.LENGTH_SHORT).show();
            } else {
                ref.child(id).child("telefone").setValue(usuarios.getTelefone());
            }
        }
        if(!latlngedit.getText().toString().isEmpty()){
            ref.child(id).child("latitude").setValue(usuarios.getLatitude());
            ref.child(id).child("longitude").setValue(usuarios.getLongitude());
        }

    }
    public void cadastrarClasse () {
        if(!nomeView.getText().toString().isEmpty()) usuarios.setNome(nomeView.getText().toString());
        if(!telefoneView.getText().toString().isEmpty())usuarios.setTelefone(telefoneView.getText().toString());
        if(!latlngedit.getText().toString().isEmpty())usuarios.setLatitude(lat);usuarios.setLongitude(lng);
    }

    private static final String[] CIDADES= new String[] {
            "Manaus","São Paulo","Rio de Janeiro"
    };

    public void abrirMapa(View view){
        Intent i = new Intent(this, PesquisarEnderecoActivity.class);
        if(cidade2 != 0){
            startActivity(i);
        }else{
            Toast.makeText(this, "Selecione uma cidade", Toast.LENGTH_SHORT).show();
        }

    }

}