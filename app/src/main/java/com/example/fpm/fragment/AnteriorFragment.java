package com.example.fpm.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;

import androidx.fragment.app.Fragment;

import com.example.fpm.R;
import com.example.fpm.adapter.ListPrestadorAdapter;
import com.example.fpm.moldes.Prestador;

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
    private ListView listPrestador;
    private ListPrestadorAdapter adapter;
    public static ScrollView scrollView;


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
        listPrestador = v.findViewById(R.id.list_prestador);
        scrollView = v.findViewById(R.id.scroll_view);

        prestadorItem = new ArrayList<Prestador>();

        //configurando listadapter de histórico do usuário
        prestadorItem.add(new Prestador("Tiago Brasil Lima", "23/12/2020", R.drawable.imagem_fotouser));
        prestadorItem.add(new Prestador("Lúcia Pires", "25/03/2020", R.drawable.imagem_fotouser));
        prestadorItem.add(new Prestador("Adriano", "30/109/2020", R.drawable.imagem_fotouser));
        adapter = new ListPrestadorAdapter(prestadorItem, getActivity());
        listPrestador.setAdapter(adapter);
        return v;
    }


}