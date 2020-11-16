package com.example.fpm.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.fpm.R;
import com.google.android.material.textfield.TextInputEditText;
import com.santalu.maskara.widget.MaskEditText;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.fpm.activity.LoginActivity.u;

public class CadastroPrestadorActivity extends AppCompatActivity {
    public static TextInputEditText nome2,datanasc2,ender2,desc;
    public static MaskEditText telefone2;
    private ImageButton buttonVoltar,btnCamera,btnGaleria;
    private static final int SELECA0_CAMERA =100;
    private static final int SELECA0_GALERIA =200;
    private CircleImageView circleImageViewPerfil;
    public static Bitmap imagem2 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_prestador);

        Intent i = new Intent(this,EscolhaActivity.class);

        imagem2 = null;
        u=false;
        nome2= findViewById(R.id.editNome);
        telefone2=findViewById(R.id.editFone);
        datanasc2=findViewById(R.id.editData);
        ender2=findViewById(R.id.editEnd);
        desc =findViewById(R.id.editDes);
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

            final Intent tela2Activity = new Intent(this, CadastroPrestadorServicoActivity.class);
            startActivity(tela2Activity);
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
                        imagem2 = (Bitmap)data.getExtras().get("data");
                        break;
                    case SELECA0_GALERIA:
                        Uri localImagem = data.getData();
                        imagem2 = MediaStore.Images.Media.getBitmap(getContentResolver(),localImagem);
                        break;
                }

                if(imagem2!=null){
                    u=true;
                    circleImageViewPerfil.setImageBitmap(imagem2);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public boolean verificarCampos(boolean v){
        v=true;
        if(nome2.getText().toString().isEmpty()){
            v=false;
        }
        if(telefone2.getText().toString().isEmpty()){
            v = false;
        }
        else if(telefone2.getUnMasked().toString().length()>11){
            Toast.makeText(this, "Número de telefone inválido.", Toast.LENGTH_SHORT).show();
            v=false;
        }
        else if(telefone2.getUnMasked().toString().length()<11){
            Toast.makeText(this, "Número de telefone inválido.", Toast.LENGTH_SHORT).show();
            v=false;
        }
        if(datanasc2.getText().toString().isEmpty()){
            v=false;
        } else if(Integer.parseInt(datanasc2.getText().toString())<0 || Integer.parseInt(datanasc2.getText().toString())>120){
            Toast.makeText(this, "Insira uma idade adequada.", Toast.LENGTH_SHORT).show();
            v=false;
        } else if(Integer.parseInt(datanasc2.getText().toString())<16){
            Toast.makeText(this, "Você deve ter no mínimo 16 anos.", Toast.LENGTH_SHORT).show();
            v=false;
        }
        if(ender2.getText().toString().isEmpty()){
            v=false;
        }
        if(desc.getText().toString().isEmpty()){
            v =false;
        }

        return v;
    }
}