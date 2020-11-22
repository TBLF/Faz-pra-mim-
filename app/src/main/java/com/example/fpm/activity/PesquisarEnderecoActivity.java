package com.example.fpm.activity;

import androidx.core.app.ActivityCompat;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.example.fpm.activity.CadastroEnderecoActivity.localcidade;
import static com.example.fpm.activity.EditarPerfilActivity.localcidade2;
import static com.example.fpm.activity.CadastroActivity.entrou;
import static com.example.fpm.activity.EditarPerfilActivity.entrou2;
import static com.example.fpm.activity.LoginActivity.bifurcacaoCadastro;

public class PesquisarEnderecoActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static double  lat =0.0, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisar_endereco);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        entrou =  true;
        entrou2 = true;
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
        if(bifurcacaoCadastro ==true){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localcidade, 12));
        }else{
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localcidade2, 12));
        }
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                mMap.addMarker(
                        new MarkerOptions()
                                .position(latLng)
                                .title("Sua residência")
                                .icon(BitmapDescriptorFactory.defaultMarker())
                );

                lat = latLng.latitude;
                lng = latLng.longitude;
            }
        });
    }
    public void sairMapa(View view){

        if(lat!= 0.0){
            Intent i;
            if(bifurcacaoCadastro ==true){
                i = new Intent(this, CadastroEnderecoActivity.class);
            }else{
                i = new Intent(this, EditarPerfilActivity.class);
            }
            startActivity(i);
        }else{
            Toast.makeText(this, "Precisamos que você selecione a localização de sua residência no mapa.", Toast.LENGTH_SHORT).show();
        }

    }
}