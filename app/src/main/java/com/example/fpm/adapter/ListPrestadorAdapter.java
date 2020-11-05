package com.example.fpm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fpm.R;
import com.example.fpm.moldes.Prestador;

import java.util.List;


public class ListPrestadorAdapter extends BaseAdapter {

    List<Prestador> prestadorList;
    Context context;

    public ListPrestadorAdapter(List<Prestador> prestadorList, Context context) {
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

        View view = inflater.inflate(R.layout.layout_line,parent,false);

        TextView textNome = view.findViewById(R.id.textNome);
        TextView textData = view.findViewById(R.id.textData);
        ImageView Image = view.findViewById(R.id.ImagePrestador);

        Prestador prestador = prestadorList.get(position);

        textNome.setText(prestador.getNome());
        textData.setText(prestador.getData_servico());


        return view;
    }
}
