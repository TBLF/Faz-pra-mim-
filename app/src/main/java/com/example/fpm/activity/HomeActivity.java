package com.example.fpm.activity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.fpm.config.ConfiguracaoFirebase;
import com.example.fpm.adapter.ListPrestadorAdapter;
import com.example.fpm.R;
import com.example.fpm.fragment.AnteriorFragment;
import com.example.fpm.fragment.PerfilFragment;
import com.example.fpm.fragment.PrincipalFragment;
import com.example.fpm.moldes.Prestador;
import com.google.android.gms.common.util.Strings;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import java.util.ArrayList;
import java.util.List;

import static com.example.fpm.fragment.PrincipalFragment.constraintLayout;
import static com.example.fpm.fragment.PrincipalFragment.textNome;
import static com.example.fpm.fragment.PrincipalFragment.button;
import static com.example.fpm.activity.LoginActivity.u;


public class HomeActivity extends FragmentActivity implements
        GoogleMap.OnMarkerClickListener,
        OnMapReadyCallback {
    public static int f;
    private GoogleMap mMap;
    private ScrollView scrollView;
    private List<Prestador> prestadorItem;
    private List<Prestador> prestadorLatLngId;
    private ListView listPrestador;
    private ListPrestadorAdapter adapter;
    public static String UidPrestador;
    public static String NomePrestador;
    private FloatingActionButton fabLocalizaoAtual, floatingActionButton;;
    private  LatLng meuLocal = null;
    private Switch aSwitch;
    private String[] appPermissions = null;
    private static final int CODIGO_PERMISSOES_REQUERIDAS = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        IniciarComponents();
    }

    private void IniciarComponents() {
        scrollView = findViewById(R.id.scroll_view);
        listPrestador = findViewById(R.id.list_prestador);
        floatingActionButton = findViewById(R.id.floatingActionButton2);
        prestadorItem = new ArrayList<Prestador>();
        prestadorLatLngId = new ArrayList<Prestador>();
        appPermissions = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        u=true;

        prestadorItem.add(new Prestador("Tiago Brasil Lima", "23/12/2020", R.drawable.imagem_fotouser));
        prestadorItem.add(new Prestador("Lúcia Pires", "25/03/2020", R.drawable.imagem_fotouser));
        prestadorItem.add(new Prestador("Adriano", "30/109/2020", R.drawable.imagem_fotouser));
        adapter = new ListPrestadorAdapter(prestadorItem, this);
        listPrestador.setAdapter(adapter);

        ConfiguraBottomNavigation();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bnve);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem;


        if (f == 2) {
            fragmentTransaction.replace(R.id.view_pager, new PerfilFragment()).commit();
            menuItem = menu.getItem(2);
            menuItem.setChecked(true);
            f = 0;

        } else {
            fragmentTransaction.replace(R.id.view_pager, new PrincipalFragment()).commit();
        }

        scrollView.setVisibility(View.INVISIBLE);
        floatingActionButton.setVisibility(View.INVISIBLE);
    }


    private void ConfiguraBottomNavigation() {
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bnve);
        bottomNavigationViewEx.enableAnimation(true);
        bottomNavigationViewEx.enableItemShiftingMode(true);
        bottomNavigationViewEx.enableShiftingMode(true);
        bottomNavigationViewEx.setTextVisibility(true);

        HabilitarNavegacao(bottomNavigationViewEx);

        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

    }

    private void HabilitarNavegacao(BottomNavigationViewEx viewEx) {
        viewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                switch (menuItem.getItemId()) {
                    case R.id.ic_home:
                        aSwitch.setVisibility(View.VISIBLE);
                        scrollView.setVisibility(View.INVISIBLE);
                        floatingActionButton.setVisibility(View.INVISIBLE);
                        fragmentTransaction.replace(R.id.view_pager, new PrincipalFragment()).commit();
                        return true;
                    case R.id.ic_contacts:
                        aSwitch.setVisibility(View.INVISIBLE);
                        scrollView.setVisibility(View.VISIBLE);
                        floatingActionButton.setVisibility(View.VISIBLE);
                        fragmentTransaction.replace(R.id.view_pager, new AnteriorFragment()).commit();
                        return true;
                    case R.id.ic_perfil:
                        aSwitch.setVisibility(View.INVISIBLE);
                        scrollView.setVisibility(View.INVISIBLE);
                        floatingActionButton.setVisibility(View.INVISIBLE);
                        fragmentTransaction.replace(R.id.view_pager, new PerfilFragment()).commit();
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        aSwitch = findViewById(R.id.switch1);
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        LatLng manaus = new LatLng(-3.0895571, -59.9644187);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(manaus,12));
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(HomeActivity.this, R.raw.style_json3));
        if(verficarPermissoes()) {
            recuperarLocalizacaoUsuario();
            recuperarLocalizacoes();
        }else{
            Toast.makeText(this, "Nem todas as permissões estão ativas", Toast.LENGTH_SHORT).show();
            finish();
        }
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(HomeActivity.this, R.raw.style_json4));
                    aSwitch.setTextColor(getResources().getColor(R.color.black));
                }else{
                    googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(HomeActivity.this, R.raw.style_json3));
                    aSwitch.setTextColor(getResources().getColor(R.color.white));

                }
            }
        });

    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();
        String uid = "";
        String nome = "";
        Intent i = new Intent(this, NegociarActivity.class);
        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
            Toast.makeText(this,
                    marker.getTitle() +
                            " foi clicado " + clickCount + " vezes.",
                    Toast.LENGTH_SHORT).show();

        }

        for (int ij = 0; ij < prestadorLatLngId.size(); ij++) {
            if (prestadorLatLngId.get(ij).getLatLngPrestador().equals(marker.getPosition())) {
                uid = prestadorLatLngId.get(ij).getUid();
                nome = prestadorLatLngId.get(ij).getNome();
            }
        }
        if (!uid.isEmpty()) {
            UidPrestador = uid;
            NomePrestador = nome;
            constraintLayout.setVisibility(View.VISIBLE);
            aSwitch.setVisibility(View.INVISIBLE);
            textNome.setText(nome);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(i);
                }
            });

        }

        return false;
    }

    private void recuperarLocalizacaoUsuario() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.setPadding(0,1300,30,0);
        }
    }

    private void recuperarLocalizacoes(){
        DatabaseReference prestadorplaceDatabse = ConfiguracaoFirebase.getFirebaseDatabase().child("Prestador");

        prestadorplaceDatabse.addValueEventListener (new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    LatLng newPosition = null;
                    for (DataSnapshot d : snapshot.getChildren()) {
                        String nome = d.child("Nome").getValue().toString();
                        String uId =d.child("uid").getValue().toString();
                        double lat = (double) d.child("LatAtual").getValue();
                        double longitude = (double) d.child("LongAtual").getValue();
                        newPosition = new LatLng(lat, longitude);

                        mMap.addMarker(
                                new MarkerOptions()
                                        .position(newPosition)
                                        .title(nome)
                                        .icon(BitmapDescriptorFactory.defaultMarker())
                        );
                        prestadorLatLngId.add(new Prestador(newPosition,uId, nome));

                    }
                }
            }
            @Override
            public void onCancelled (@NonNull DatabaseError error){

            }
        });
    }

    public boolean verficarPermissoes(){
        List<String> permissoesRequeridas = new ArrayList<>();
        for(String permission : appPermissions){
            if(ContextCompat.checkSelfPermission(this,permission)!=PackageManager.PERMISSION_GRANTED){
                permissoesRequeridas.add(permission);
            }
        }
        if(!permissoesRequeridas.isEmpty()){
            ActivityCompat.requestPermissions(this,permissoesRequeridas.toArray(new String[permissoesRequeridas.size()]),CODIGO_PERMISSOES_REQUERIDAS);
            return false;
        }

        return true;
    }
}