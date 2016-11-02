package com.lionsquare.canisovismanager.Thread;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.widget.Toast;

import com.lionsquare.canisovismanager.ActivityManager;
import com.lionsquare.canisovismanager.Beans.MapLocation;
import com.lionsquare.canisovismanager.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by archivaldo on 06/02/16.
 */
public class ListDivice extends AsyncTask<String, String, String> {
    Context mContext;
    ActivityManager main;
    public String datos = "";
    private ProgressDialog pDialog;
    private static final String DIRC_URL = "http://canovius.16mb.com/canovius/login/locationDivece.php";
    String id;
    public String lat = "";
    public String lon = "";
    private String direccionText = "";
    public ArrayList<MapLocation> lista_divice = new ArrayList<MapLocation>();

    public ListDivice(String id, ArrayList<MapLocation> lista_divice, Context context) {
        this.id = id;
        this.lista_divice = lista_divice;
        this.mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Revisando dispositivos registrados... ");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    @Override
    protected String doInBackground(String... args) {
        main = (ActivityManager) mContext;
        try {

            URL url = new URL(DIRC_URL);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("POST");
            conexion.setDoOutput(true);
            OutputStreamWriter datSal = new OutputStreamWriter(conexion.getOutputStream());
            datSal.write("id=");
            datSal.write(URLEncoder.encode(String.valueOf(id), "UTF-8"));
            datSal.flush();
            datSal.close();
            if (conexion.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                String linea = reader.readLine();
                while (linea != null) {
                    datos += linea;
                    linea = reader.readLine();
                }
                reader.close();
                if (datos.equals("null")) {
                    datos = "=(";
                } else {

                    try {
                        JSONArray jArray = new JSONArray(datos);
                        datos = "";
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject json_data = jArray.getJSONObject(i);
                            lat = json_data.getString("lat");
                            lon = json_data.getString("lon");

                            Geocoder geocoder = new Geocoder(mContext);
                            List<Address> direcciones = null;
                            try {
                                direcciones = geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lon), 2);
                            } catch (Exception e) {
                                //error de ubicacion
                            }
                            if (direcciones != null && direcciones.size() > 0) {

                                // Creamos el objeto address
                                Address direccion = direcciones.get(0);

                                // Creamos el string a partir del elemento direccion
                                direccionText = String.format("%s, %s, %s",
                                        direccion.getMaxAddressLineIndex() > 0 ? direccion.getAddressLine(0) : "",
                                        direccion.getLocality(),
                                        direccion.getCountryName());
                            }

                            int contador = 1;
                            contador = contador + i;
                            String item = String.valueOf(contador + ".");
                            if (json_data.getString("lat").equals("") || json_data.getString("lon").equals("")) {
                                lista_divice.add
                                        (new MapLocation(000000000,
                                                -000000000,
                                                R.drawable.divice,
                                                json_data.getString("model"),
                                                json_data.getString("lat"),
                                                json_data.getString("lon"),
                                                direccionText,
                                                item,
                                                json_data.getString("id_emei")
                                                , json_data.getString("fecha")));
                            } else {
                                lista_divice.add
                                        (new MapLocation(Double.valueOf(json_data.getString("lat")),
                                                Double.valueOf(json_data.getString("lon")),
                                                R.drawable.divice,
                                                json_data.getString("model"),
                                                json_data.getString("lat"),
                                                json_data.getString("lon"),
                                                direccionText,
                                                item,
                                                json_data.getString("id_emei")
                                                , json_data.getString("fecha")));

                            }


                        }

                    } catch (JSONException e) {   //Se asocia el error a la salida en pantalla
                        //   salida.append("Error parseando datos: " + e+"\n");
                    }
                }
            } else {   //Se asocia el error a la salida en pantalla
                // salida.append("ERROR: " + conexion.getResponseMessage() + "\n");
            }
            conexion.disconnect();

        } catch (Exception e) {
            return mContext.getString(R.string.no_se_puede_acceder_al_servidor);
        }

        return null;

    }

    protected void onPostExecute(String file_url) {
        pDialog.dismiss();
        if (file_url != null) {
            Toast.makeText(mContext, file_url, Toast.LENGTH_LONG).show();

        }
        main.Lista();

    }


}
