package com.example.fpm.activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.fpm.R;
import com.example.fpm.config.ConfiguracaoFirebase;
import com.example.fpm.config.UsuarioFireBase;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ServicosActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private  List<Integer> numeros;
    private int i;
    private ImageButton sair,button1,button2,button3,button4,button5,button6,i_button1,i_button2,i_button3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        databaseReference =  ConfiguracaoFirebase.getFirebaseDatabase().child("Contratante").child(UsuarioFireBase.getUsuarioAtual().getUid().toString()).child("Interface Servico");
        numeros = new ArrayList<Integer>();
        sair = findViewById(R.id.sair_toolbar);
        button1 = findViewById(R.id.image_button_vassoura);
        button2 = findViewById(R.id.image_button_animais);
        button3 = findViewById(R.id.image_button_obras);
        button4 = findViewById(R.id.image_button_engenharia);
        button5 = findViewById(R.id.image_button_encanamento);
        button6 = findViewById(R.id.image_button_eletrecidade);
        i_button1 = findViewById(R.id.image_button_1);
        i_button2 = findViewById(R.id.image_button_2);
        i_button3 = findViewById(R.id.image_button_3);
        i=1;
        i_button1.setBackgroundResource(R.drawable.forma_botao_customizado2);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    numeros.clear();
                    int n;
                    for (DataSnapshot d : snapshot.getChildren()) {
                        n = Integer.parseInt(d.child("numero").getValue().toString());
                        numeros.add(n);
                    }
                    carregarImagens();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numeros.clear();
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            numeros.clear();
                            int n;
                            for (DataSnapshot d : snapshot.getChildren()) {
                                n = Integer.parseInt(d.child("numero").getValue().toString());
                                numeros.add(n);
                            }
                            carregarImagens();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(getApplicationContext(), HomeActivity.class);
                for(int x =0;x<numeros.size();x++){
                    databaseReference.child("Servico "+String.valueOf(x+1)).child("numero").setValue(numeros.get(x));
                }
                startActivity(j);
            }
        });
        i_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=1;
                i_button1.setBackgroundResource(R.drawable.forma_botao_customizado2);
                i_button2.setBackgroundResource(R.drawable.forma_botao_customizado);
                i_button3.setBackgroundResource(R.drawable.forma_botao_customizado);
            }
        });
        i_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=2;
                i_button1.setBackgroundResource(R.drawable.forma_botao_customizado);
                i_button2.setBackgroundResource(R.drawable.forma_botao_customizado2);
                i_button3.setBackgroundResource(R.drawable.forma_botao_customizado);
            }
        });
        i_button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=3;
                i_button1.setBackgroundResource(R.drawable.forma_botao_customizado);
                i_button2.setBackgroundResource(R.drawable.forma_botao_customizado);
                i_button3.setBackgroundResource(R.drawable.forma_botao_customizado2);
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numeros.size()==3){
                    numeros.clear();
                }
                switch (i){
                    case 1:
                        numeros.add(1);
                        i_button1.setImageResource(R.drawable.icone_vassoura);
                        break;
                    case 2:
                        numeros.add(1);
                        i_button2.setImageResource(R.drawable.icone_vassoura);
                        break;
                    case 3:
                        numeros.add(1);
                        i_button3.setImageResource(R.drawable.icone_vassoura);
                        break;
                }
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numeros.size()==3){
                    numeros.clear();
                }
                switch (i){
                    case 1:
                        numeros.add(2);
                        i_button1.setImageResource(R.drawable.icone_cachorro);
                        break;
                    case 2:
                        numeros.add(2);
                        i_button2.setImageResource(R.drawable.icone_cachorro);
                        break;
                    case 3:
                        numeros.add(2);
                        i_button3.setImageResource(R.drawable.icone_cachorro);
                        break;
                }
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numeros.size()==3){
                    numeros.clear();
                }
                switch (i){
                    case 1:
                        numeros.add(3);
                        i_button1.setImageResource(R.drawable.icone_parede_de_tijolos);
                        break;
                    case 2:
                        numeros.add(3);
                        i_button2.setImageResource(R.drawable.icone_parede_de_tijolos);
                        break;
                    case 3:
                        numeros.add(3);
                        i_button3.setImageResource(R.drawable.icone_parede_de_tijolos);
                        break;
                }
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numeros.size()==3){
                    numeros.clear();
                }
                switch (i){
                    case 1:
                        numeros.add(4);
                        i_button1.setImageResource(R.drawable.icone_engenharia);
                        break;
                    case 2:
                        numeros.add(4);
                        i_button2.setImageResource(R.drawable.icone_engenharia);
                        break;
                    case 3:
                        numeros.add(4);
                        i_button3.setImageResource(R.drawable.icone_engenharia);
                        break;
                }
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numeros.size()==3){
                    numeros.clear();
                }
                switch (i){
                    case 1:
                        numeros.add(5);
                        i_button1.setImageResource(R.drawable.icone_encanamento);
                        break;
                    case 2:
                        numeros.add(5);
                        i_button2.setImageResource(R.drawable.icone_encanamento);
                        break;
                    case 3:
                        numeros.add(5);
                        i_button3.setImageResource(R.drawable.icone_encanamento);
                        break;
                }
            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numeros.size()==3){
                    numeros.clear();
                }
                switch (i){
                    case 1:
                        numeros.add(6);
                        i_button1.setImageResource(R.drawable.icone_relampago);
                        break;
                    case 2:
                        numeros.add(6);
                        i_button2.setImageResource(R.drawable.icone_relampago);
                        break;
                    case 3:
                        numeros.add(6);
                        i_button3.setImageResource(R.drawable.icone_relampago);
                        break;
                }
            }
        });





    }

    private void carregarImagens(){
        if(numeros.size()<4){
            switch (numeros.get(0)){
                case 1:
                    i_button1.setImageResource(R.drawable.icone_vassoura);
                    break;
                case 2:
                    i_button1.setImageResource(R.drawable.icone_cachorro);
                    break;
                case 3:
                    i_button1.setImageResource(R.drawable.icone_parede_de_tijolos);
                    break;
                case 4:
                    i_button1.setImageResource(R.drawable.icone_encanamento);
                    break;
                case 5:
                    i_button1.setImageResource(R.drawable.icone_engenharia);
                    break;
                case 6:
                    i_button1.setImageResource(R.drawable.icone_relampago);
                    break;
            }
            switch (numeros.get(1)){
                case 1:
                    i_button2.setImageResource(R.drawable.icone_vassoura);
                    break;
                case 2:
                    i_button2.setImageResource(R.drawable.icone_cachorro);
                    break;
                case 3:
                    i_button2.setImageResource(R.drawable.icone_parede_de_tijolos);
                    break;
                case 4:
                    i_button2.setImageResource(R.drawable.icone_encanamento);
                    break;
                case 5:
                    i_button2.setImageResource(R.drawable.icone_engenharia);
                    break;
                case 6:
                    i_button2.setImageResource(R.drawable.icone_relampago);
                    break;
            }
            switch (numeros.get(2)){
                case 1:
                    i_button3.setImageResource(R.drawable.icone_vassoura);
                    break;
                case 2:
                    i_button3.setImageResource(R.drawable.icone_cachorro);
                    break;
                case 3:
                    i_button3.setImageResource(R.drawable.icone_parede_de_tijolos);
                    break;
                case 4:
                    i_button3.setImageResource(R.drawable.icone_encanamento);
                    break;
                case 5:
                    i_button3.setImageResource(R.drawable.icone_engenharia);
                    break;
                case 6:
                    i_button3.setImageResource(R.drawable.icone_relampago);
                    break;
            }
        }
        else{
            Toast.makeText(ServicosActivity.this, "Erro ao carregar", Toast.LENGTH_SHORT).show();
        }

    }
}