package com.example.fpm.activity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.fpm.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class HomePrestadorActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String[] appPermissions = null;
    private static final int CODIGO_PERMISSOES_REQUERIDAS = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_prestador);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //atribuindo permissões a um array
        appPermissions = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
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
}