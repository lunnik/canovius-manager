package com.lionsquare.canisovismanager.AdapterList;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lionsquare.canisovismanager.Beans.Lista_Dispositivo;
import com.lionsquare.canisovismanager.R;

import java.util.ArrayList;

/**
 * Created by archivaldo on 03/07/16.
 */
public class AdapterDivice  extends RecyclerView.Adapter<AdapterDivice.ViewHolder> {


    private ArrayList<Lista_Dispositivo> divice;
    private Context context;
    private Bundle bundle;
    private MapView mapView;
    private GoogleMap map;

    public AdapterDivice(ArrayList<Lista_Dispositivo> countries, Context context, Bundle bundle) {
        this.divice = countries;
        this.context = context;
        this.bundle = bundle;
    }

    @Override
    public AdapterDivice.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ovis_entrada, viewGroup, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        Lista_Dispositivo item = divice.get(i);

        viewHolder.num.setText(item.getNumList());
        viewHolder.id_divice.setText(item.getDivice());
        viewHolder.divice.setText(item.getDivice());
        viewHolder.direct.setText(item.getDirect());
        viewHolder.fecha.setText(item.getFecha());

        if (item.getLat().isEmpty()&&item.getLon().isEmpty()) {

            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(0.00000000, 0.000000));

            //mueve el foco en la pantalla cuando se muesta el mapa
            map.moveCamera(center);//mueve la panatala al la marca
            //showLocation(location.getLatitude(), location.getLongitude());
            //map(Double.parseDouble(lat), Double.parseDouble(lon));


        } else {

            // Updates the location and zoom of the MapView
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(
                    Double.valueOf(item.getLat())
                    , Double.valueOf(item.getLon())), 15);
            map.animateCamera(cameraUpdate);

             LatLng PERTH = new LatLng(Double.valueOf(item.getLat()),Double.valueOf(item.getLon()));
            Marker perth = map.addMarker(new MarkerOptions()
                    .position(PERTH)
                    .draggable(true));


        }

    }

    @Override
    public int getItemCount() {
        return divice.size();
    }

    public void addItem(String country) {
        //divice.add(country);
        notifyItemInserted(divice.size());
    }

    public void removeItem(int position) {
        divice.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, divice.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView num;
        TextView id_divice;
        TextView divice;
        TextView direct;
        TextView fecha;

        public ViewHolder(View view) {
            super(view);

            num = (TextView) view.findViewById(R.id.num);
            id_divice = (TextView) view.findViewById(R.id.id_divice);
            divice = (TextView) view.findViewById(R.id.divice);
            direct = (TextView) view.findViewById(R.id.direct);
            fecha = (TextView) view.findViewById(R.id.fecha);
            mapView = (MapView) view.findViewById(R.id.map);
            mapView.onCreate(bundle);
            map = mapView.getMap();
            map.getUiSettings().setMyLocationButtonEnabled(false);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            map.setMyLocationEnabled(true);
            // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
            MapsInitializer.initialize(context);

        }
    }
}
