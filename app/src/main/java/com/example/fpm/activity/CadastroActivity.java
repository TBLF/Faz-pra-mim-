package com.example.fpm.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Size;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fpm.R;
import com.example.fpm.config.ConfiguracaoFirebase;
import com.example.fpm.moldes.Usuario;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;
import com.santalu.maskara.widget.MaskEditText;
import static com.example.fpm.activity.LoginActivity.u;

import de.hdodenhof.circleimageview.CircleImageView;

public class CadastroActivity extends AppCompatActivity {
    public static TextInputEditText nome,datanasc,ender;
    public static MaskEditText telefone;
    public ImageButton buttonVoltar,btnCamera,btnGaleria;
    private static final int SELECA0_CAMERA =100;
    private static final int SELECA0_GALERIA =200;
    private CircleImageView circleImageViewPerfil;
    public static  Bitmap imagem ;
    public static Usuario usuario = new Usuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        Intent i = new Intent(this,LoginActivity.class);

        imagem = null;
        u=false;
        nome= findViewById(R.id.editNome);
        telefone=findViewById(R.id.editFone);
        datanasc=findViewById(R.id.editData);
        ender=findViewById(R.id.editEnd);
        btnCamera = findViewById(R.id.btnCamera);
        btnGaleria = findViewById(R.id.btnGaleria);
        buttonVoltar = findViewById(R.id.sair_toolbar);
        circleImageViewPerfil = findViewById(R.id.imagePerfil);

        buttonVoltar.setOnClickListener(new View.OnClickListener() {
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
    public void starttela2Activity (View view) {
        boolean v = true;
        if (verificarCampos(v) == true) {

            final Intent tela2Activity = new Intent(this, CadastroActivity2.class);
            startActivity(tela2Activity);
        }
        else{
            Toast.makeText(getApplicationContext(),"Preencha todos os campos", Toast.LENGTH_LONG ).show();
        }
    }
    public void startcadstro_prestadorActivity (View view) {
        boolean v = true;
        if (verificarCampos(v) == true) {

            final Intent cadstro_prestadorActivity = new Intent(this, CadastroActivity2.class);
            startActivity(cadstro_prestadorActivity);
        }
        else{
            Toast.makeText(getApplicationContext(),"Preencha todos os campos", Toast.LENGTH_LONG ).show();
        }
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
                    circleImageViewPerfil.setImageBitmap(imagem);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public boolean verificarCampos(boolean v){
        v=true;
        if(nome.getText().toString().isEmpty()){
            v=false;
        }
        if(telefone.getText().toString().isEmpty()){
            v = false;
        }
        else if(telefone.getUnMasked().toString().length()>11){
            Toast.makeText(this, "Número de telefone iválido.", Toast.LENGTH_SHORT).show();
            v=false;
        }
        else if(telefone.getUnMasked().toString().length()<11){
            Toast.makeText(this, "Número de telefone iválido.", Toast.LENGTH_SHORT).show();
            v=false;
        }
        if(datanasc.getText().toString().isEmpty()){
            v=false;
        } else if(Integer.parseInt(datanasc.getText().toString())<0 || Integer.parseInt(datanasc.getText().toString())>120){
            Toast.makeText(this, "Insira uma idade adequada.", Toast.LENGTH_SHORT).show();
            v=false;
        } else if(Integer.parseInt(datanasc.getText().toString())<16){
            Toast.makeText(this, "Você deve ter no mínimo 16 anos.", Toast.LENGTH_SHORT).show();
            v=false;
        }

        if(ender.getText().toString().isEmpty()){
            v=false;
        }

        return v;
    }
}