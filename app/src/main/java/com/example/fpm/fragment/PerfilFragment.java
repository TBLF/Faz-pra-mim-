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
        FloatingActionButton floatingActionButton;
        TextView textNome,textIdade,textEndereco,textTelefone;
        FirebaseUser user = UsuarioFireBase.getUsuarioAtual();
        String id = user.getUid();
        CircleImageView img;


        DatabaseReference ref = ConfiguracaoFirebase.getFirebaseDatabase().child("Contratante").child(id);
        StorageReference storageReference = ConfiguracaoFirebase.getFirebaseStorage();
        StorageReference  strg = storageReference.child("Imagens").child("perfil").child(id+".jpeg");

        floatingActionButton = v.findViewById(R.id.btn_editarperfil);
        textNome =  v.findViewById(R.id.text_nome);
        textIdade =  v.findViewById(R.id.text_idade);
        textTelefone =  v.findViewById(R.id.text_telefone);
        textEndereco =  v.findViewById(R.id.text_endereco);
        img = v.findViewById(R.id.profile_image);

        if(u==true&&strg!=null){
            Glide.with(getContext())
                    .using(new FirebaseImageLoader())
                    .load(strg)
                    .into(img);

        }

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String nome,idade,endereco,telefone;
                     nome =  snapshot.child("nome").getValue().toString();
                     idade = snapshot.child("idade").getValue().toString();
                     endereco = snapshot.child("endereco").getValue().toString();
                     telefone = snapshot.child("telefone").getValue().toString();

                     textNome.setText(nome);
                     textIdade.setText(idade+" anos");
                     textTelefone.setText(telefone);
                     textEndereco.setText(endereco);
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