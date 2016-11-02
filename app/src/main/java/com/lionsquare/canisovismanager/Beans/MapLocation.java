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

package com.lionsquare.canisovismanager.Beans;

import com.google.android.gms.maps.model.LatLng;

public class MapLocation {

    private int idImagen;
    private String divice;
    private String lats;
    private String lons;
    private String direct;
    private String numList;
    private String emei;
    private String fecha;

    public LatLng center;

    @SuppressWarnings("unused")
    public MapLocation() {
    }

    public MapLocation(double lat, double lng, int idImagen, String divice, String lats, String lons, String dirct, String num_list, String emei, String fecha) {
        this.center = new LatLng(lat, lng);
        this.idImagen = idImagen;
        this.divice = divice;
        this.lats = lats;
        this.lons = lons;
        this.direct = dirct;
        this.numList = num_list;
        this.emei = emei;
        this.fecha = fecha;
    }


    public String getNumList() {
        return numList;
    }

    public String getEmei() {
        return emei;
    }

    public String getLat() {
        return lats;
    }


    public String getDivice() {
        return divice;
    }

    public String getDirect() {
        return direct;
    }

    public String getLon() {
        return lons;
    }

    public int get_idImagen() {
        return idImagen;
    }

    public String getFecha() {
        return fecha;
    }
}
