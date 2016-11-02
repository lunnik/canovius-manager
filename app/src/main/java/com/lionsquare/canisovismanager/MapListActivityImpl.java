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

package com.lionsquare.canisovismanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.lionsquare.canisovismanager.AdapterList.MapLocationAdapter;
import com.lionsquare.canisovismanager.Beans.MapLocation;
import com.lionsquare.canisovismanager.Thread.DeleteDivice;
import com.lionsquare.canisovismanager.network.Network;

public class MapListActivityImpl extends ActivityManager {

    boolean deleteAcept;
    DeleteDivice deleteDivice;
    int itemPosition;
    MapLocation mapLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected MapLocationAdapter createMapListAdapter() {

        MapLocationAdapter adapter = new MapLocationAdapter();
        adapter.setMapLocations(lista_divice, MapListActivityImpl.this);

        return adapter;
    }

    @Override
    public void showMapDetails(View view) {
        itemPosition = recyclerView.getChildAdapterPosition(view);

        Log.e("int", String.valueOf(itemPosition));
        //itemPosition = num - 1;
        showPopupMenu(view);

/*
        Intent intent = new Intent(this, LocalizarDispositivo.class);
        intent.putExtra(LocalizarDispositivo.EXTRA_LATITUDE, mapLocation.center.latitude);
        intent.putExtra(LocalizarDispositivo.EXTRA_LONGITUDE, mapLocation.center.longitude);

        startActivity(intent);*/
    }

    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(this, view);
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

                    return true;
                case R.id.delete_divice:
                    deleteDivice(itemPosition);

                    return true;
                default:
            }
            return false;
        }
    }

    private void queryDivice(int position) {

        Intent i = new Intent(MapListActivityImpl.this, LocalizarDispositivo.class);
        MapLocation item = lista_divice.get(position);
        i.putExtra("lat", item.getLat());
        i.putExtra("lon", item.getLon());
        i.putExtra("emei", item.getEmei());
        i.putExtra("dic", item.getDirect());
        i.putExtra("model", item.getDivice());
        i.putExtra("fecha", item.getFecha());

        if (item.getLon().equals("") || item.getLat().equals("")) {
            Toast.makeText(this, "La informacion de la ubicacion esta incompleta", Toast.LENGTH_SHORT).show();

        } else {
            startActivity(i);
        }
        mListAdapter.notifyItemRangeChanged(position, lista_divice.size());
    }

    private void deleteDivice(final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MapListActivityImpl.this);
        builder.setMessage(getString(R.string.are_your_sure))
                .setTitle(getString(R.string.delete_divice))
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setNegativeButton(getString(R.string.msg_no),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                .setPositiveButton(MapListActivityImpl.this.getString(R.string.msg_si),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                if (Network.networkAvailable(MapListActivityImpl.this)) {

                                    MapLocation itemLista = lista_divice.get(position);
                                    String emei = itemLista.getEmei();
                                    deleteDivice = new DeleteDivice(emei, MapListActivityImpl.this);
                                    deleteDivice.execute();
                                    //comprobacion
                                    if (deleteAcept = true)
                                        lista_divice.remove(position);
                                    mListAdapter.notifyDataSetChanged();
                                    deleteAcept = false;
                                } else {

                                    AlertDialog alertDialog = new AlertDialog.Builder(MapListActivityImpl.this).create();
                                    alertDialog.setTitle(R.string.msg_progress_alert);
                                    alertDialog.setMessage(getString(R.string.msg_sin_intenet));
                                    alertDialog.setButton(getString(R.string.msg_si), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // aquí puedes añadir funciones
                                        }
                                    });
                                    alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                                    alertDialog.show();

                                    mListAdapter.notifyItemRangeChanged(position, lista_divice.size());

                                }

                            }
                        }).show();
        mListAdapter.notifyItemRangeChanged(position, lista_divice.size());
    }


}
