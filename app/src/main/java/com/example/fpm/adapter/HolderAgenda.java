package com.example.fpm.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fpm.R;

public class HolderAgenda {

    TextView textNome, texttempo ;
    ImageView Image,imageTipo;
    Button buttonCancelar;


    public void HolderAgenda(View v){
        textNome = v.findViewById(R.id.textNome_agenda);
        texttempo = v.findViewById(R.id.textTempo_agenda);
        Image = v.findViewById(R.id.ImagePrestador_agenda);
        imageTipo = v.findViewById(R.id.imageTipo_agenda);
        buttonCancelar = v.findViewById(R.id.btn_cancelar_agenda);
    }
}
