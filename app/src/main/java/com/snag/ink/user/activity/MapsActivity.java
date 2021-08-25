package com.snag.ink.user.activity;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.snag.ink.user.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    LatLng userLatLang = new LatLng(10.830552, 78.693445);
    String locname = "Home";
    TextView btnlocation;
    EditText locationname;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        btnlocation = findViewById(R.id.btnlocation);
        locationname = findViewById(R.id.locname);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //locname = locationname.getText().toString().trim();
               /* if (locname.isEmpty()) {
                    Toast.makeText(MapsActivity.this, "Enter name for selected location", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                Intent returnIntent = new Intent();
                returnIntent.putExtra("coordinates", String.valueOf(userLatLang));
                returnIntent.putExtra("locname", locname);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLang, 15));
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                Toast.makeText(MapsActivity.this, String.valueOf(userLatLang), Toast.LENGTH_SHORT).show();
                mMap.addMarker(new MarkerOptions().position(latLng).title("Delivery Location"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                userLatLang = latLng;
            }
        });
    }
}