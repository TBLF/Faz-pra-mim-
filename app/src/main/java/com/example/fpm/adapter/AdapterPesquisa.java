package com.example.fpm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fpm.R;
import com.example.fpm.moldes.Prestador;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterPesquisa extends RecyclerView.Adapter<AdapterPesquisa.MyViewHolder>{
    private List<Prestador> listaPrestador;
    private Context context;

    public AdapterPesquisa(List<Prestador> l, Context c) {
        this.listaPrestador = l;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pesquisa_prestador,parent,false);
        return  new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Prestador prestador = listaPrestador.get(position);
        holder.nome.setText(prestador.getNome());
        holder.local.setText(prestador.getEndereco());
        holder.foto.setImageResource(R.drawable.imagem_user);
    }

    @Override
    public int getItemCount() {
        return listaPrestador.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        CircleImageView foto;
        TextView nome;
        TextView local;

        public MyViewHolder(View itemView){
            super(itemView);
            foto = itemView.findViewById(R.id.ImagePrestador);
            nome = itemView.findViewById(R.id.textNomePrestador);
            local = itemView.findViewById(R.id.textLocalPrestador);
        }
    }
}
