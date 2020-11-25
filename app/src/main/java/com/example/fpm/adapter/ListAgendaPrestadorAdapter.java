package com.example.fpm.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.fpm.R;
import com.example.fpm.moldes.Prestador;
import com.firebase.ui.storage.images.FirebaseImageLoader;

import java.util.List;

public class ListAgendaPrestadorAdapter extends BaseAdapter {

    List<Prestador> prestadorList;
    Context context;

    public ListAgendaPrestadorAdapter(List<Prestador> prestadorList, Context context) {
        this.prestadorList = prestadorList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return prestadorList.size();
    }

    @Override
    public Prestador getItem(int position) {
        return prestadorList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.layout_line4,parent,false);

        TextView textNome = view.findViewById(R.id.textNome_agenda);
        TextView texttempo = view.findViewById(R.id.textTempo_agenda);
        ImageView Image = view.findViewById(R.id.ImagePrestador_agenda);
        ImageView imageTipo = view.findViewById(R.id.imageTipo_agenda);
        Button btn_cancelar = view.findViewById(R.id.btn_cancelar_agenda);
        Prestador prestador = prestadorList.get(position);

        textNome.setText(prestador.getNome());
        texttempo.setText(prestador.getTempo()+"dias");
        Image.setImageResource(R.drawable.imagem_fotouser);
        btn_cancelar.setBackgroundResource(R.color.colorPrimary);
        btn_cancelar.setTextColor(Color.BLACK);

        switch (prestador.getTipo()){
            case 1:
                imageTipo.setImageResource(R.drawable.icone_vassoura3);
                break;
            case 2:
                imageTipo.setImageResource(R.drawable.icone_cachorro3);
                break;
            case 3:
                imageTipo.setImageResource(R.drawable.icone_parede_de_tijolos3);
                break;
            case 4:
                imageTipo.setImageResource(R.drawable.icone_engenharia3);
                break;
            case 5:
                imageTipo.setImageResource(R.drawable.icone_encanamento3);
                break;
            case 6:
                imageTipo.setImageResource(R.drawable.icone_relampago3);
                break;
        }


        return view;
    }
}