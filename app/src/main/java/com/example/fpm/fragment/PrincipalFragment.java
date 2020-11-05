package com.example.fpm.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fpm.R;
import com.example.fpm.activity.ServicosActivity;
import com.example.fpm.adapter.AdapterPesquisa;
import com.example.fpm.config.ConfiguracaoFirebase;
import com.example.fpm.config.UsuarioFireBase;
import com.example.fpm.moldes.Prestador;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PrincipalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrincipalFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static ConstraintLayout constraintLayout;
    public static  Button button;
    public static  TextView textNome;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<Prestador> lista;
    private DatabaseReference reference,refImg;
    private List<Integer> numeros;
    private ImageButton imageButton,imageButton2,imageButton3,btn_irTelaServic;
    private AdapterPesquisa adapterPesquisa;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PrincipalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PrincipalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PrincipalFragment newInstance(String param1, String param2) {
        PrincipalFragment fragment = new PrincipalFragment();
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

        View v = inflater.inflate(R.layout.fragment_principal, container, false);
        SearchView searchView = v.findViewById(R.id.searchViewPesquisa);
        RecyclerView recyclerView = v.findViewById(R.id.recyclerPesquisa);
        lista = new ArrayList<Prestador>();
        adapterPesquisa = new AdapterPesquisa(lista,getActivity());
        numeros = new ArrayList<Integer>();
        reference = ConfiguracaoFirebase.getFirebaseDatabase().child("Prestador");
        refImg = ConfiguracaoFirebase.getFirebaseDatabase().child("Contratante").child(UsuarioFireBase.getUsuarioAtual().getUid().toString()).child("Interface Servico");

        btn_irTelaServic = v.findViewById(R.id.btn_irTelaServicos);
        constraintLayout= v.findViewById(R.id.bloco_de_dados);
        constraintLayout.setVisibility(View.INVISIBLE);
        button = v.findViewById(R.id.btn_negociar);
        textNome = v.findViewById(R.id.textNome);
        ImageButton cancel = v.findViewById(R.id.cancel);
        imageButton = v.findViewById(R.id.image_button_cao);
        imageButton2 = v.findViewById(R.id.image_button_vassoura);
        imageButton3= v.findViewById(R.id.image_button_tijolos);


        refImg.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
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
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        searchView.setQueryHint("Buscar prestadores ");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String textoDigitado = newText.toUpperCase();
                pesquisar(textoDigitado);
                return true;

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constraintLayout.setVisibility(View.INVISIBLE);
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { constraintLayout.setVisibility(View.INVISIBLE); }
        });

        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constraintLayout.setVisibility(View.INVISIBLE);
            }
        });

        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constraintLayout.setVisibility(View.INVISIBLE);
            }
        });
        btn_irTelaServic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),ServicosActivity.class);
                startActivity(i);

            }
        });

        return v;
    }

    private void pesquisar(String texto){
        lista.clear();
        if(texto.length() >=2){
            Query query = reference.orderByChild("nomePesquisa")
                    .startAt(texto)
                    .endAt(texto + "\uf8ff");

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    lista.clear();
                    for(DataSnapshot ds : snapshot.getChildren()){
                        lista.add(ds.getValue(Prestador.class));
                    }
                    adapterPesquisa.notifyDataSetChanged();
                    int total = lista.size();
                    Toast.makeText(getContext(), String.valueOf(total) , Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private  void carregarImagens(){
        if(numeros.size()<4){
            switch (numeros.get(0)){
                case 1:
                    imageButton.setImageResource(R.drawable.icone_vassoura);
                    break;
                case 2:
                    imageButton.setImageResource(R.drawable.icone_cachorro);
                    break;
                case 3:
                    imageButton.setImageResource(R.drawable.icone_parede_de_tijolos);
                    break;
                case 4:
                    imageButton.setImageResource(R.drawable.icone_encanamento);
                    break;
                case 5:
                    imageButton.setImageResource(R.drawable.icone_engenharia);
                    break;
                case 6:
                    imageButton.setImageResource(R.drawable.icone_relampago);
                    break;
            }
            switch (numeros.get(1)){
                case 1:
                    imageButton2.setImageResource(R.drawable.icone_vassoura);
                    break;
                case 2:
                    imageButton2.setImageResource(R.drawable.icone_cachorro);
                    break;
                case 3:
                    imageButton2.setImageResource(R.drawable.icone_parede_de_tijolos);
                    break;
                case 4:
                    imageButton2.setImageResource(R.drawable.icone_encanamento);
                    break;
                case 5:
                    imageButton2.setImageResource(R.drawable.icone_engenharia);
                    break;
                case 6:
                    imageButton2.setImageResource(R.drawable.icone_relampago);
                    break;
            }
            switch (numeros.get(2)){
                case 1:
                    imageButton3.setImageResource(R.drawable.icone_vassoura);
                    break;
                case 2:
                    imageButton3.setImageResource(R.drawable.icone_cachorro);
                    break;
                case 3:
                    imageButton3.setImageResource(R.drawable.icone_parede_de_tijolos);
                    break;
                case 4:
                    imageButton3.setImageResource(R.drawable.icone_encanamento);
                    break;
                case 5:
                    imageButton3.setImageResource(R.drawable.icone_engenharia);
                    break;
                case 6:
                    imageButton3.setImageResource(R.drawable.icone_relampago);
                    break;
            }
        }
        else{
            Toast.makeText(getContext(), "Erro ao carregar", Toast.LENGTH_SHORT).show();
        }
    }


}