package com.example.fpm.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.fpm.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CadastroPrestadorServicoActivity extends AppCompatActivity {
    public  static  int num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_prestador_servico);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, SERVICOS);
        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.editServ);
        textView.setAdapter(adapter);
        ImageButton i_button1 = findViewById(R.id.image_button_servicos);
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);

        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        i_button1.setImageResource(R.drawable.icone_vassoura2);
                        num =1;
                        break;
                    case 1:
                        i_button1.setImageResource(R.drawable.icone_cachorro2);
                        num =2;
                        break;
                    case 2:
                        i_button1.setImageResource(R.drawable.icone_parede_de_tijolos2);
                        num =3;
                        break;
                    case 3:
                        i_button1.setImageResource(R.drawable.icone_engenharia2);
                        num =4;
                        break;
                    case 4:
                        i_button1.setImageResource(R.drawable.icone_encanamento2);
                        num =5;
                        break;
                    case 5:
                        i_button1.setImageResource(R.drawable.icone_relampago2);
                        num =6;
                        break;
                }
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(num!=0){
                    Intent i = new Intent(CadastroPrestadorServicoActivity.this, CadastroPrestadorActivity.class) ;
                    startActivity(i);
                }else{
                    Toast.makeText(CadastroPrestadorServicoActivity.this, "Selecione ao menos um seviço para prosseguir", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private static final String[] SERVICOS= new String[] {
            "Fáxina", "Cuidado de animais", "Trabalho na obra", "Engenharia civil", "Conserto de encanmento" , "Conserto de fiação elétrica"
    };


}