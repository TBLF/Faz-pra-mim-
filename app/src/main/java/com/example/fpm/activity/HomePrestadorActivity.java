package com.example.fpm.activity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.fpm.R;
import com.example.fpm.adapter.ListAgendaPrestadorAdapter;
import com.example.fpm.adapter.ListPrestadorAdapter;
import com.example.fpm.config.Base64Custom;
import com.example.fpm.config.ConfiguracaoFirebase;
import com.example.fpm.config.UsuarioFireBase;
import com.example.fpm.moldes.Prestador;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomePrestadorActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String[] appPermissions = null;
    private static final int CODIGO_PERMISSOES_REQUERIDAS = 1;
    private List<String> uids;
    private List<Prestador> prestadorItemAgenda,prestadorItemHistorico;
    private ListView listPrestadorHistorico, listPrestadorAgenda;
    private ListPrestadorAdapter adapter;
    private ListAgendaPrestadorAdapter adapter2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_prestador);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        //referenciando objetos
        listPrestadorHistorico = findViewById(R.id.historico_prestador);
        listPrestadorAgenda = findViewById(R.id.agenda_prestador);

        prestadorItemAgenda = new ArrayList<Prestador>();
        prestadorItemHistorico = new ArrayList<Prestador>();
        adapter = new ListPrestadorAdapter(prestadorItemHistorico, this);
        listPrestadorHistorico.setAdapter(adapter);
        adapter2 = new ListAgendaPrestadorAdapter(prestadorItemAgenda, this);
        listPrestadorAgenda.setAdapter(adapter2);

        //Referenciando banco de dados
        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
        FirebaseUser user = UsuarioFireBase.getUsuarioAtual();
        DatabaseReference refAgenda = database.child("Agenda").child(Base64Custom.codificarBase64(user.getEmail()));
        DatabaseReference refHist = database.child("Historico").child(Base64Custom.codificarBase64(user.getEmail()));

        //atribuindo permissões a um array
        appPermissions = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };

        uids = new ArrayList<>();
        recuperarUids();
        recuperarNegociacoes();

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
                        // uid = d.child("uidPrestador").getValue().toString();
                        tipoPrestador = Integer.parseInt(d.child("tipo").getValue().toString());

                        // StorageReference  strg = storageReference.child("Imagens").child("perfilPrestador").child(uid+".jpeg");

                        prestadorItemAgenda.add(new Prestador(nomeAgenda, tipoPrestador,tempoAgenda));
                        //configurando listadapter de histórico do usuário
                        adapter2.notifyDataSetChanged();

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
                        //uid = d.child("uidPrestador").getValue().toString();


                        //  StorageReference  strg = storageReference.child("Imagens").child("perfilPrestador").child(uid+".jpeg");

                        prestadorItemHistorico.add(new Prestador(nomeHistorico, dataHistorico));
                        //configurando listadapter de histórico do usuário

                        adapter.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng manaus = new LatLng(-3.0895571, -59.9644187);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(manaus, 12));
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(HomePrestadorActivity.this, R.raw.style_json3));
        if (verficarPermissoes()) {
            recuperarLocalizacaoUsuario();
        } else {
            Toast.makeText(this, "Nem todas as permissões estão ativas", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    private void recuperarLocalizacaoUsuario() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
    }
    public boolean verficarPermissoes() {
        List<String> permissoesRequeridas = new ArrayList<>();
        for (String permission : appPermissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissoesRequeridas.add(permission);
            }
        }
        if (!permissoesRequeridas.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissoesRequeridas.toArray(new String[permissoesRequeridas.size()]), CODIGO_PERMISSOES_REQUERIDAS);
            return false;
        }

        return true;
    }

    public  void abrirMensagens(View view){
        Intent i = new Intent(this, ConversasPrestadorActivity.class);
        startActivity(i);
    }

    private void recuperarNegociacoes(){
        DatabaseReference contratanteplaceDatabse = ConfiguracaoFirebase.getFirebaseDatabase().child("Contratante");
        contratanteplaceDatabse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    LatLng newPosition = null;
                    for(int j=0;j<uids.size();j++){
                        for (DataSnapshot d : snapshot.getChildren()) {
                            String uid;
                            uid = d.child("uid").getValue().toString();
                            if(uids.get(j).equals(uid)){

                                String nome = d.child("nome").getValue().toString();
                                String uId = d.child("uid").getValue().toString();
                                double lat = (double) d.child("latitude").getValue();
                                double longitude = (double) d.child("longitude").getValue();
                                newPosition = new LatLng(lat, longitude);

                                mMap.addMarker(
                                        new MarkerOptions()
                                                .position(newPosition)
                                                .title(nome)
                                                .icon(BitmapDescriptorFactory.defaultMarker())
                                );

                            }
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void recuperarUids(){
        DatabaseReference ref = ConfiguracaoFirebase.getFirebaseDatabase().child("Negociacao").child(UsuarioFireBase.getIdentificadorusuario());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot d : snapshot.getChildren()){
                        String uid;
                        uid = d.child("idDestinatario").getValue().toString();
                        uids.add(uid);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}