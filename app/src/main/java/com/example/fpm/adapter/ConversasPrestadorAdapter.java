package com.example.fpm.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fpm.R;
import com.example.fpm.moldes.Conversa;
import com.example.fpm.moldes.Usuario;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConversasPrestadorAdapter extends RecyclerView.Adapter<ConversasPrestadorAdapter.MyViewHolder> {


    private  List<Conversa> conversas;
    private Context context;
    public ConversasPrestadorAdapter(List<Conversa> lista, Context c) {
        this.conversas = lista;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_line2, parent, false);
       return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Conversa conversa = conversas.get(position);
        holder.ultimaMensagem.setText(conversa.getUltimaMensagem());

        Usuario usuario = conversa.getUsuarioExibicao();
        holder.nome.setText(usuario.getNome());
        if(usuario.getFoto() != null){
            Uri uri = Uri.parse(usuario.getFoto());
            Glide.with(context).load(uri).into(holder.foto);
        }else{
            holder.foto.setImageResource(R.drawable.imagem_fotouser);
        }
    }

    @Override
    public int getItemCount() {
        return conversas.size();
    }

    public class MyViewHolder  extends RecyclerView.ViewHolder{

        CircleImageView foto;
        TextView nome, ultimaMensagem;
          public MyViewHolder(View itemView){
              super(itemView);
              foto = itemView.findViewById(R.id.ImageContratante);
              nome = itemView.findViewById(R.id.textNomeContratante);
              ultimaMensagem = itemView.findViewById(R.id.textUltimaMensagem);

          }

    }

}
