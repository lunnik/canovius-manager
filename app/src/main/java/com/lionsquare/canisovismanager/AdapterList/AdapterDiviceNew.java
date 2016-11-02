package com.lionsquare.canisovismanager.AdapterList;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lionsquare.canisovismanager.Beans.Lista_Dispositivo;
import com.lionsquare.canisovismanager.LocalizarDispositivo;
import com.lionsquare.canisovismanager.R;
import com.lionsquare.canisovismanager.Thread.DeleteDivice;
import com.lionsquare.canisovismanager.network.Network;

import java.util.ArrayList;

/**
 * Created by archivaldo on 11/08/16.
 */
public class AdapterDiviceNew extends RecyclerView.Adapter<AdapterDiviceNew.ViewHolder> {


    private ArrayList<Lista_Dispositivo> divice;
    private Context context;
    private Bundle bundle;
    private MapView mapView;
    private GoogleMap map;
    boolean deleteAcept;
    DeleteDivice deleteDivice;
    int itemPosition;


    public AdapterDiviceNew(ArrayList<Lista_Dispositivo> countries, Context context, Bundle bundle) {
        this.divice = countries;
        this.context = context;
        this.bundle = bundle;
    }

    @Override
    public AdapterDiviceNew.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ovis_entrada, viewGroup, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        Lista_Dispositivo item = divice.get(i);

        viewHolder.num.setText(item.getNumList());
        viewHolder.id_divice.setText(item.getDivice());
        viewHolder.divice.setText(item.getDivice());
        viewHolder.direct.setText(item.getDirect());
        viewHolder.fecha.setText(item.getFecha());

        if (item.getLat().isEmpty() && item.getLon().isEmpty()) {

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

            LatLng PERTH = new LatLng(Double.valueOf(item.getLat()), Double.valueOf(item.getLon()));
            Marker perth = map.addMarker(new MarkerOptions()
                    .position(PERTH)
                    .draggable(true));


        }

        viewHolder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(viewHolder.overflow);
                itemPosition = i;

            }
        });

    }

    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_iten_divece, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.query_divice:
                    queryDivice(itemPosition);
                    Toast.makeText(context, "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.delete_divice:
                    deleteDivice(itemPosition);
                    Toast.makeText(context, "Play next", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
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
        ImageView overflow;

        public ViewHolder(View view) {
            super(view);
            overflow = (ImageView) view.findViewById(R.id.overflow);
            num = (TextView) view.findViewById(R.id.num);
            id_divice = (TextView) view.findViewById(R.id.id_divice);
            divice = (TextView) view.findViewById(R.id.divice);
            direct = (TextView) view.findViewById(R.id.direct);
            fecha = (TextView) view.findViewById(R.id.fecha);
            mapView = (MapView) view.findViewById(R.id.mapview);
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

    private void queryDivice(int position) {

        Intent i = new Intent(context, LocalizarDispositivo.class);
        Lista_Dispositivo item = divice.get(position);
        i.putExtra("lat", item.getLat());
        i.putExtra("lon", item.getLon());
        i.putExtra("emei", item.getEmei());
        i.putExtra("dic", item.getDirect());
        i.putExtra("model", item.getDivice());
        i.putExtra("fecha", item.getFecha());

        if (item.getLon().equals("") || item.getLat().equals("")) {
            Toast.makeText(context, "La informacion de la ubicacion esta incompleta", Toast.LENGTH_SHORT).show();

        } else {
            context.startActivity(i);
        }
        notifyItemRangeChanged(position, divice.size());
    }

    private void deleteDivice(final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.are_your_sure))
                .setTitle(context.getString(R.string.delete_divice))
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setNegativeButton(context.getString(R.string.msg_no),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                .setPositiveButton(context.getString(R.string.msg_si),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                if (Network.networkAvailable(context)) {

                                    Lista_Dispositivo itemLista = divice.get(position);
                                    String emei = itemLista.getEmei();
                                    deleteDivice = new DeleteDivice(emei, context);
                                    deleteDivice.execute();
                                    //comprobacion
                                    if (deleteAcept = true)
                                        divice.remove(position);
                                    notifyDataSetChanged();
                                    deleteAcept = false;
                                } else {

                                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                                    alertDialog.setTitle(R.string.msg_progress_alert);
                                    alertDialog.setMessage(context.getString(R.string.msg_sin_intenet));
                                    alertDialog.setButton(context.getString(R.string.msg_si), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // aquí puedes añadir funciones
                                        }
                                    });
                                    alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                                    alertDialog.show();

                                    notifyItemRangeChanged(position, divice.size());

                                }

                            }
                        }).show();
        notifyItemRangeChanged(position, divice.size());
    }

}
