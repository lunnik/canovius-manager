package com.lionsquare.canisovismanager;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocalizarDispositivo extends AppCompatActivity {

    private GoogleMap googleMap;
    private String lat = "000000";
    private String lon = "000000";
    private ImageView im1, im2;
    private AdView adView;

    public LocalizarDispositivo() {
    }

    LocationManager locationManager;
    String provider;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle parametros) {
        super.onCreate(parametros);
        setContentView(R.layout.activity_localizar_dispositivo);

        if (getIntent() != null) {
            lat = getIntent().getExtras().getString("lat");
            lon = getIntent().getExtras().getString("lon");


        }

        adView = new AdView(this);
        adView.setAdUnitId("ca-app-pub-5060471640712079/5403870747");
        adView.setAdSize(AdSize.BANNER);


        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //  toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.divice));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

            // toolbar.setBackground(getResources().getDrawable(R.mipmap.ic_launcher));
        } else {

        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        int codigoGooglePlay = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);//numero unico de google play
        if (codigoGooglePlay != ConnectionResult.SUCCESS) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(codigoGooglePlay, this, 6);
            if (dialog != null) {
                dialog.show();
            } else {
                Toast.makeText(LocalizarDispositivo.this,
                        "Error al verificar Google Play Servivces",
                        Toast.LENGTH_LONG).show();
                finish();
            }
        }

        FragmentManager myFragmentManager = getSupportFragmentManager();
        SupportMapFragment mySupportMapFragment = (SupportMapFragment) myFragmentManager.findFragmentById(R.id.map);
        googleMap = mySupportMapFragment.getMap();

        if (googleMap != null) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);// tipo de mapa que se av mostrar
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            googleMap.setMyLocationEnabled(true);  // muestra el boton de localizacion
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(13f));

            FloatingActionButton imgbtn = (FloatingActionButton) findViewById(R.id.fab); //your button
            imgbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new
                            LatLng(Double.parseDouble(lat), Double.parseDouble(lon)), 15));
                }
            });


        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);//obtnemos lat y log
        Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (loc != null) {
            showLocation(Double.parseDouble(lat), Double.parseDouble(lon));


        }


        locationListener = new LocationListener() {
            //puede mover la camara del mapa cuando cabie de ubicacion
            //y se actuliza la ubicacion si se meueve
            public void onLocationChanged(Location location) {
                CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));

                //mueve el foco en la pantalla cuando se muesta el mapa
                googleMap.moveCamera(center);//mueve la panatala al la marca
                //showLocation(location.getLatitude(), location.getLongitude());
                showLocation(Double.parseDouble(lat), Double.parseDouble(lon));
            }

            public void onProviderDisabled(String provider) {
            }

            public void onProviderEnabled(String provider) {


            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
        };
        Criteria criteria;
        criteria = new Criteria();//permiete definir criterios de ubicacion para encontra de manera
        // mas precisa el dispositovo
        criteria.setAccuracy(Criteria.ACCURACY_FINE);// que tal alto es el criterio de ubicacion
        criteria.setAltitudeRequired(true);//la ubicaion es mas precisa aun
        criteria.setCostAllowed(true);//le dice a la app que no debe usar el 3g pro el consumo de datos
        criteria.setSpeedRequired(true);//velocidad de en la que se debe encontrar


        provider = locationManager.getBestProvider(criteria, true);//obteer ek mejro provedor disponible
        if (provider != null) {
            locationManager.requestLocationUpdates(provider, 1000, 70, locationListener);//se muetsra una  nueva hubicacion
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);

    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(locationListener);
        super.onPause();
    }

    private void showLocation(double lat, double lng) {
        googleMap.addMarker(new MarkerOptions()//SIMBOLO DE GOTA INVERTIDA (MARCADOR )
                .position(new LatLng(lat, lng)));
        //.icon(BitmapDescriptorFactory.fromResource(R.drawable.divice))
    }
}
