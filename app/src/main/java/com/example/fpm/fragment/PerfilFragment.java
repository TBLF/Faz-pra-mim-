package com.example.fpm.fragment;

import android.content.Intent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.fpm.R;
import com.example.fpm.activity.EditarPerfilActivity;
import com.example.fpm.config.ConfiguracaoFirebase;
import com.example.fpm.config.UsuarioFireBase;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import static com.example.fpm.activity.LoginActivity.u;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public PerfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_perfil, container, false);

        //instanciando objetos
        FloatingActionButton floatingActionButton;
        TextView textNome,textDescricao;

        CircleImageView img;

        //referenciando banco de dados
        FirebaseUser user = UsuarioFireBase.getUsuarioAtual();
        String id = user.getUid();
        DatabaseReference ref = ConfiguracaoFirebase.getFirebaseDatabase().child("Contratante").child(id);
        StorageReference storageReference = ConfiguracaoFirebase.getFirebaseStorage();

        //referenciando objetos do fragment
        floatingActionButton = v.findViewById(R.id.btn_editarperfil);
        textNome =  v.findViewById(R.id.text_nome);
        textDescricao = v.findViewById(R.id.text_descricao);
        img = v.findViewById(R.id.profile_image);

        //buscando dados do banco de dados
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                     String nome,descricao,uid;
                     nome =  snapshot.child("nome").getValue().toString();
                     descricao = snapshot.child("descricao").getValue().toString();
                     uid = snapshot.child("uid").getValue().toString();
                     textNome.setText(nome);
                     textDescricao.setText(descricao);
                     //referenciando imagem a partir do uid do usuário

                    StorageReference  strg = storageReference.child("Imagens").child("perfilContratante").child(uid+".jpeg");
                    String foto = strg.toString();
                    if(foto!= null){
                        Glide.with(getActivity())
                                .using(new FirebaseImageLoader())
                                .load(strg)
                                .into(img);
                    }
                    else{
                        img.setImageResource(R.drawable.imagem_fotouser);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),EditarPerfilActivity.class);
                startActivity(i);
            }
        });



        return v;
    }


}