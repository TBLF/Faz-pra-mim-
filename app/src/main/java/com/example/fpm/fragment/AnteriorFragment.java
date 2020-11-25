package com.example.fpm.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.fpm.R;
import com.example.fpm.adapter.ListPrestadorAdapter;
import com.example.fpm.config.Base64Custom;
import com.example.fpm.config.ConfiguracaoFirebase;
import com.example.fpm.config.UsuarioFireBase;
import com.example.fpm.moldes.Prestador;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;


import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnteriorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnteriorFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<Prestador> prestadorItem;
    private ListView listPrestadorHistorico, listPrestadorAgenda;
    private ListPrestadorAdapter adapter;
    public static ScrollView scrollView;
    private  StorageReference storageReference ;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AnteriorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AnteriorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AnteriorFragment newInstance(String param1, String param2) {
        AnteriorFragment fragment = new AnteriorFragment();
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
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_anterior, container, false);

        //referenciando objetos
        listPrestadorHistorico = v.findViewById(R.id.list_prestador);
        listPrestadorAgenda = v.findViewById(R.id.list_agenda);
        scrollView = v.findViewById(R.id.scroll_view);

        //inacializando variáveis de uso
        prestadorItem = new ArrayList<Prestador>();

        int i =0;


        //Referenciando banco de dados
        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
        FirebaseUser user = UsuarioFireBase.getUsuarioAtual();
        DatabaseReference refAgenda = database.child("Agenda").child(Base64Custom.codificarBase64(user.getEmail()));
        DatabaseReference refHist = database.child("Historico").child(Base64Custom.codificarBase64(user.getEmail()));

        //recuperando mudanças na agenda
        refAgenda.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot d : snapshot.getChildren()){
                        String nomeAgenda,tempoAgenda,uid;
                        int tipoPrestador;

                         nomeAgenda = d.child("nome").getValue().toString();
                         tempoAgenda = d.child("tempo").getValue().toString();
                         uid = d.child("uidPrestador").getValue().toString();
                         tipoPrestador = Integer.parseInt(d.child("tipo").getValue().toString());

                        StorageReference  strg = storageReference.child("Imagens").child("perfilPrestador").child(uid+".jpeg");

                        prestadorItem.add(new Prestador(nomeAgenda, tipoPrestador, strg,tempoAgenda));
                        //configurando listadapter de histórico do usuário
                        adapter = new ListPrestadorAdapter(prestadorItem, getActivity());
                        listPrestadorAgenda.setAdapter(adapter);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //recuperando adições na agenda

        refHist.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot d : snapshot.getChildren()){
                        String   nomeHistorico,dataHistorico ,uid;
                        nomeHistorico = d.child("nome").getValue().toString();
                        dataHistorico = d.child("data").getValue().toString();
                        uid = d.child("uidPrestador").getValue().toString();


                        StorageReference  strg = storageReference.child("Imagens").child("perfilPrestador").child(uid+".jpeg");

                        prestadorItem.add(new Prestador(nomeHistorico, dataHistorico, strg));
                        //configurando listadapter de histórico do usuário
                        adapter = new ListPrestadorAdapter(prestadorItem, getActivity());
                        listPrestadorHistorico.setAdapter(adapter);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return v;
    }

}