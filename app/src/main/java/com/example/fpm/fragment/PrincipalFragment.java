package com.example.fpm.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fpm.R;
import com.example.fpm.activity.HomeActivity;
import com.example.fpm.config.ConfiguracaoFirebase;
import com.example.fpm.moldes.Prestador;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PrincipalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrincipalFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static ConstraintLayout constraintLayout;
    public static  Button button;
    public static  TextView textNome;
    public static CircleImageView imagemPrestador;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<Prestador> lista;
    private DatabaseReference reference;
    private ImageButton cancel;
    private SearchView searchView;
    private HomeActivity homeActivity;
    private  GoogleMap   mMap;
    private  TextView textCont;
    private ImageView btn_ir;


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
        lista = new ArrayList<Prestador>();
        textNome = v.findViewById(R.id.textNome);
        //Referenciando banco de dados
        reference = ConfiguracaoFirebase.getFirebaseDatabase().child("Prestador");


        //Referenciando objetos
        imagemPrestador = v.findViewById(R.id.imagePrestador);
        textCont = v.findViewById(R.id.textQuant);
        btn_ir = v.findViewById(R.id.imageProx);
        constraintLayout= v.findViewById(R.id.bloco_de_dados);
        constraintLayout.setVisibility(View.INVISIBLE);
        btn_ir.setVisibility(View.INVISIBLE);
        textCont.setVisibility(View.INVISIBLE);
        button = v.findViewById(R.id.btn_negociar);
        cancel = v.findViewById(R.id.cancel);
        textNome = v.findViewById(R.id.textNome);




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
                    mMap = homeActivity.getmMap();
                    lista.clear();
                    LatLng newPosition = null;
                    String nome = null, end=null;
                    double lat,longitude;
                    float cor;
                    int tipo;



                    for(DataSnapshot ds : snapshot.getChildren()){

                        nome = ds.child("Nome").getValue().toString();
                        end = ds.child("Endereco").getValue().toString();
                        lat = (double) ds.child("LatAtual").getValue();
                        longitude = (double) ds.child("LongAtual").getValue();
                        newPosition = new LatLng(lat, longitude);
                        tipo = Integer.parseInt(ds.child("Tipo").getValue().toString());
                        lista.add(new Prestador(end,nome,newPosition));

                        switch (tipo){
                            case 1:
                                cor = 30.0f;
                                break;
                            case 2:
                                cor = 180.0f;
                                break;
                            case 3:
                                cor = 300.0f;
                                break;
                            case 4:
                                cor = 210.0f;
                                break;
                            case 5:
                                cor = 240.0f;
                                break;
                            case 6:
                                cor = 60.0f;
                                break;

                            default:
                                throw new IllegalStateException("Unexpected value: " + tipo);
                        }
                        mMap.addMarker(
                                new MarkerOptions()
                                        .position(newPosition)
                                        .title(nome)
                                        .icon(BitmapDescriptorFactory.defaultMarker(cor))
                        );


                    }
                    int total = lista.size();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPosition,12));
                    btn_ir.setVisibility(View.VISIBLE);
                    textCont.setVisibility(View.VISIBLE);
                    andarLista(total);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void andarLista (int total){
        final int[] cont = {1};
        textCont.setText("0/"+String.valueOf(total));
        btn_ir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cont[0]!=total){
                    textCont.setText(String.valueOf(cont[0])+"/"+String.valueOf(total));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lista.get(cont[0]).getLatLngPrestador(),12));
                    cont[0]++;
                }

            }
        });


    }


}