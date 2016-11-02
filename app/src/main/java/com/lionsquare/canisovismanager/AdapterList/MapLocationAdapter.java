/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lionsquare.canisovismanager.AdapterList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.MapView;
import com.lionsquare.canisovismanager.Beans.MapLocation;
import com.lionsquare.canisovismanager.LocalizarDispositivo;
import com.lionsquare.canisovismanager.R;
import com.lionsquare.canisovismanager.Thread.DeleteDivice;
import com.lionsquare.canisovismanager.network.Network;

import java.util.ArrayList;
import java.util.HashSet;

public class MapLocationAdapter extends RecyclerView.Adapter<MapLocationViewHolder> {
    protected HashSet<MapView> mMapViews = new HashSet<>();
    protected ArrayList<MapLocation> mMapLocations;
    protected int itemPosition;
    Context context;
    boolean deleteAcept;
    DeleteDivice deleteDivice;

    public void setMapLocations(ArrayList<MapLocation> mapLocations,Context context) {
        mMapLocations = mapLocations;
        context=context;
    }

    @Override
    public MapLocationViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ovis_entrada, viewGroup, false);
        MapLocationViewHolder viewHolder = new MapLocationViewHolder(viewGroup.getContext(), view);


        mMapViews.add(viewHolder.mapView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MapLocationViewHolder viewHolder, final int position) {
        MapLocation mapLocation = mMapLocations.get(position);

        viewHolder.itemView.setTag(mapLocation);

        viewHolder.id_divice.setText(mapLocation.getDivice());
        viewHolder.divice.setText(mapLocation.getEmei());
        viewHolder.direct.setText(mapLocation.getDirect());
        viewHolder.fecha.setText(mapLocation.getFecha());

        viewHolder.setMapLocation(mapLocation);

        viewHolder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(viewHolder.overflow);
                itemPosition = position;

            }
        });
    }

    @Override
    public int getItemCount() {
        return mMapLocations == null ? 0 : mMapLocations.size();
    }

    public HashSet<MapView> getMapViews() {
        return mMapViews;
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

    private void queryDivice(int position) {

        Intent i = new Intent(context, LocalizarDispositivo.class);
        MapLocation item = mMapLocations.get(position);
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
        notifyItemRangeChanged(position, mMapLocations.size());
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

                                    MapLocation itemLista = mMapLocations.get(position);
                                    String emei = itemLista.getEmei();
                                    deleteDivice = new DeleteDivice(emei, context);
                                    deleteDivice.execute();
                                    //comprobacion
                                    if (deleteAcept = true)
                                        mMapLocations.remove(position);
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

                                    notifyItemRangeChanged(position, mMapLocations.size());

                                }

                            }
                        }).show();
        notifyItemRangeChanged(position, mMapLocations.size());
    }
}
