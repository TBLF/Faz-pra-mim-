package com.example.fpm.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    private ImageButton cancel;
    private AdapterPesquisa adapterPesquisa;
    private  RecyclerView recyclerView;
    private SearchView searchView;


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
        //Configurando mecanismo de pesquisa
        searchView = v.findViewById(R.id.searchViewPesquisa);
        recyclerView = v.findViewById(R.id.recyclerPesquisa);
        lista = new ArrayList<Prestador>();
        adapterPesquisa = new AdapterPesquisa(lista,getActivity());;

        //Referenciando banco de dados
        reference = ConfiguracaoFirebase.getFirebaseDatabase().child("Prestador");


        //Referenciando objetos
        constraintLayout= v.findViewById(R.id.bloco_de_dados);
        constraintLayout.setVisibility(View.INVISIBLE);
        button = v.findViewById(R.id.btn_negociar);
        cancel = v.findViewById(R.id.cancel);
        textNome = v.findViewById(R.id.textNome);

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
                        String nome, end;
                        nome = ds.child("Nome").getValue().toString();
                        end = ds.child("Endereco").getValue().toString();

                        lista.add(new Prestador(end,nome));
                    }
                    Toast.makeText(getActivity(), "aqui", Toast.LENGTH_SHORT).show();
                    adapterPesquisa.notifyDataSetChanged();
                    int total = lista.size();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }




}