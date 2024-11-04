package com.example.techmovee.pages;

import static android.graphics.Color.alpha;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.techmovee.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FragmentMap2 extends Fragment {

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            // Define as coordenadas para os marcadores
            LatLng ruaPeraDoNorte = new LatLng(-23.433599715050306, -46.75072073936463);
            LatLng sydney = new LatLng(-34, 151);
            LatLng newYork = new LatLng(40.7128, -74.0060);
            LatLng london = new LatLng(51.5074, -0.1278);
            LatLng santos = new LatLng(-23.9689, -46.3372);

            // Adiciona os marcadores ao mapa
            googleMap.addMarker(new MarkerOptions()
                    .position(ruaPeraDoNorte)
                    .title("Rua Pera do Norte, nº 28")

            ); // Você pode mudar a cor aqui

            googleMap.addMarker(new MarkerOptions()
                    .position(santos)
                    .title("Marker in Santos, SP")
            ); // Substitua pelo nome do seu ícone

            googleMap.addMarker(new MarkerOptions()
                    .position(sydney)
                    .title("Marker in Sydney")
            );

            googleMap.addMarker(new MarkerOptions()
                    .position(newYork)
                    .title("Marker in New York")
            );

            googleMap.addMarker(new MarkerOptions()
                    .position(london)
                    .title("Marker in London")
            );

            // Move a câmera para o primeiro marcador
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ruaPeraDoNorte, 15)); // Nível de zoom
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_fragment_map2, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }


}