package com.example.fpm.activity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.example.fpm.config.ConfiguracaoFirebase;
import com.example.fpm.R;
import com.example.fpm.config.UsuarioFireBase;
import com.example.fpm.fragment.AnteriorFragment;
import com.example.fpm.fragment.PerfilFragment;
import com.example.fpm.fragment.PrincipalFragment;
import com.example.fpm.moldes.Prestador;
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
    public static GoogleMap mMap;
    private List<Prestador> prestadorLatLngId;
    public static String UidPrestador;
    public static String NomePrestador;
    public static int numeros[] = new int[3];
    private Switch aSwitch;
    private String[] appPermissions = null;
    private static final int CODIGO_PERMISSOES_REQUERIDAS = 1;
    private ImageButton imageButton, imageButton2, imageButton3, btn_irTelaServic, cancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        IniciarComponents();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constraintLayout.setVisibility(View.INVISIBLE);
                mMap.clear();
                recuperarLocalizacoes(numeros[0]);
            }
        });

        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constraintLayout.setVisibility(View.INVISIBLE);
                mMap.clear();
                recuperarLocalizacoes(numeros[1]);
            }
        });

        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constraintLayout.setVisibility(View.INVISIBLE);
                mMap.clear();
                recuperarLocalizacoes(numeros[2]);
            }
        });
        btn_irTelaServic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, ServicosActivity.class);
                startActivity(i);

            }
        });


    }

    private void IniciarComponents() {
        //referenciando e inicializando componentes
        DatabaseReference refImg = ConfiguracaoFirebase.getFirebaseDatabase().child("Contratante").child(UsuarioFireBase.getUsuarioAtual().getUid().toString()).child("Interface Servico");
        prestadorLatLngId = new ArrayList<Prestador>();
        imageButton3 = findViewById(R.id.image_button_cao);
        imageButton2 = findViewById(R.id.image_button_vassoura);
        imageButton = findViewById(R.id.image_button_tijolos);
        btn_irTelaServic = findViewById(R.id.btn_irTelaServicos);

        //atribuindo permissões a um array
        appPermissions = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        //reniciando variavel de verificação de imagem
        u = true;

        //carregando posição dos botões
        refImg.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int n, cont = 0;
                    for (DataSnapshot d : snapshot.getChildren()) {
                        n = Integer.parseInt(d.child("numero").getValue().toString());
                        numeros[cont] = n;
                        cont++;
                    }
                    carregarImagens();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //configurando botton navgation
        ConfiguraBottomNavigation();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // configuração para inicialização na tela de perfil vindo da tela de editarPerfil
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bnve);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem;


        if (f == 2) {
            fragmentTransaction.replace(R.id.view_pager, new PerfilFragment()).commit();
            menuItem = menu.getItem(2);
            menuItem.setChecked(true);
            f = 0;

        } else if (f == 3) {
            fragmentTransaction.replace(R.id.view_pager, new AnteriorFragment()).commit();
            menuItem = menu.getItem(2);
            menuItem.setChecked(true);
            f = 0;

        } else {
            fragmentTransaction.replace(R.id.view_pager, new PrincipalFragment()).commit();
        }
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
                        fragmentTransaction.replace(R.id.view_pager, new PrincipalFragment()).commit();
                        return true;
                    case R.id.ic_contacts:
                        aSwitch.setVisibility(View.INVISIBLE);
                        fragmentTransaction.replace(R.id.view_pager, new AnteriorFragment()).commit();
                        return true;
                    case R.id.ic_perfil:
                        aSwitch.setVisibility(View.INVISIBLE);
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
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(manaus, 12));
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(HomeActivity.this, R.raw.style_json3));
        if (verficarPermissoes()) {
            recuperarLocalizacaoUsuario();
            recuperarLocalizacoes(0);
        } else {
            Toast.makeText(this, "Nem todas as permissões estão ativas", Toast.LENGTH_SHORT).show();
            finish();
        }
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(HomeActivity.this, R.raw.style_json4));
                    aSwitch.setTextColor(getResources().getColor(R.color.black));
                } else {
                    googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(HomeActivity.this, R.raw.style_json3));
                    aSwitch.setTextColor(getResources().getColor(R.color.white));

                }
            }
        });

    }

    public static GoogleMap getmMap() {
        mMap.clear();
        return mMap;
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
        }
    }

    private boolean recuperarLocalizacoes(int num) {
        prestadorLatLngId.clear();

        DatabaseReference prestadorplaceDatabse = ConfiguracaoFirebase.getFirebaseDatabase().child("Prestador");
        final boolean[] cond = {false};
        prestadorplaceDatabse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    LatLng newPosition = null;
                    float cor;
                    if (num == 0) {
                        for (DataSnapshot d : snapshot.getChildren()) {
                            String nome = d.child("Nome").getValue().toString();
                            String uId = d.child("uid").getValue().toString();
                            double lat = (double) d.child("LatAtual").getValue();
                            double longitude = (double) d.child("LongAtual").getValue();
                            newPosition = new LatLng(lat, longitude);

                            mMap.addMarker(
                                    new MarkerOptions()
                                            .position(newPosition)
                                            .title(nome)
                                            .icon(BitmapDescriptorFactory.defaultMarker())
                            );
                            prestadorLatLngId.add(new Prestador(newPosition, uId, nome));

                        }
                    } else {
                        for (DataSnapshot d : snapshot.getChildren()) {
                            if (Integer.parseInt(d.child("Tipo").getValue().toString()) == num) {

                                String nome = d.child("Nome").getValue().toString();
                                String uId = d.child("uid").getValue().toString();
                                double lat = (double) d.child("LatAtual").getValue();
                                double longitude = (double) d.child("LongAtual").getValue();
                                newPosition = new LatLng(lat, longitude);

                                switch (num) {
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
                                        throw new IllegalStateException("Unexpected value: " + num);
                                }
                                mMap.addMarker(
                                        new MarkerOptions()
                                                .position(newPosition)
                                                .title(nome)
                                                .icon(BitmapDescriptorFactory.defaultMarker(cor))
                                );
                                prestadorLatLngId.add(new Prestador(newPosition, uId, nome));

                            }
                        }
                        cond[0] = true;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return cond[0];
    }

    private void carregarImagens() {
        if (numeros.length < 4) {
            switch (numeros[0]) {
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
                    imageButton.setImageResource(R.drawable.icone_engenharia);
                    break;
                case 5:
                    imageButton.setImageResource(R.drawable.icone_encanamento);
                    break;
                case 6:
                    imageButton.setImageResource(R.drawable.icone_relampago);
                    break;
            }
            switch (numeros[1]) {
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
                    imageButton.setImageResource(R.drawable.icone_engenharia);
                    break;
                case 5:
                    imageButton.setImageResource(R.drawable.icone_encanamento);
                    break;
                case 6:
                    imageButton2.setImageResource(R.drawable.icone_relampago);
                    break;
            }
            switch (numeros[2]) {
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
                    imageButton.setImageResource(R.drawable.icone_engenharia);
                    break;
                case 5:
                    imageButton.setImageResource(R.drawable.icone_encanamento);
                    break;
                case 6:
                    imageButton3.setImageResource(R.drawable.icone_relampago);
                    break;
            }
        } else {
            Toast.makeText(this, "Erro ao carregar filtros de serviços", Toast.LENGTH_SHORT).show();
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
}