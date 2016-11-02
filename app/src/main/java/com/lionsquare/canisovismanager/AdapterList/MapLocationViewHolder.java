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

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lionsquare.canisovismanager.Beans.MapLocation;
import com.lionsquare.canisovismanager.R;

public class MapLocationViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {
    TextView num;
    TextView id_divice;
    TextView divice;
    TextView direct;
    TextView fecha;
    ImageView overflow;


    protected GoogleMap mGoogleMap;
    protected MapLocation mMapLocation;

    public MapView mapView;
    private Context mContext;

    public MapLocationViewHolder(Context context, View view) {
        super(view);

        mContext = context;

        overflow = (ImageView) view.findViewById(R.id.overflow);
        num = (TextView) view.findViewById(R.id.num);
        id_divice = (TextView) view.findViewById(R.id.id_divice);
        divice = (TextView) view.findViewById(R.id.divice);
        direct = (TextView) view.findViewById(R.id.direct);
        fecha = (TextView) view.findViewById(R.id.fecha);
        mapView = (MapView) view.findViewById(R.id.mapview);

        mapView.onCreate(null);
        mapView.getMapAsync(this);
    }

    public void setMapLocation(MapLocation mapLocation) {
        mMapLocation = mapLocation;

        // If the map is ready, update its content.
        if (mGoogleMap != null) {
            updateMapContents();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        MapsInitializer.initialize(mContext);
        googleMap.getUiSettings().setMapToolbarEnabled(false);

        // If we have map data, update the map content.
        if (mMapLocation != null) {
            updateMapContents();
        }
    }

    protected void updateMapContents() {
        // Since the mapView is re-used, need to remove pre-existing mapView features.
        mGoogleMap.clear();

        // Update the mapView feature data and camera position.
        mGoogleMap.addMarker(new MarkerOptions().position(mMapLocation.center));
/*
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mMapLocation.center, 10f);
        mGoogleMap.moveCamera(cameraUpdate);*/

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mMapLocation.center, 15);
        mGoogleMap.animateCamera(cameraUpdate);
    }
}
