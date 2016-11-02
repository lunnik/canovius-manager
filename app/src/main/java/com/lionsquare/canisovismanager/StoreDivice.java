package com.lionsquare.canisovismanager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lionsquare.canisovismanager.JsonParser.JSONParser;
import com.lionsquare.canisovismanager.JsonParser.JSONParserSync;
import com.lionsquare.canisovismanager.helper.SQLiteHandler;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class StoreDivice extends AppCompatActivity {

    Button login;
    int success;
    private ProgressDialog pDialog;
    private ProgressDialog pDialogDivice;
    TextView user, pass;
    String lat, lon, emei, model, id, distancia;
    JSONParser jsonParser = new JSONParser();
    JSONParserSync jsonParserSync = new JSONParserSync();
    LocationManager locManager;
    HashMap<String, String> cursor;
    private SQLiteHandler db;
    DialgoAddDivice dialodAdd;
    android.support.v7.app.AlertDialog.Builder builder;
    android.location.Location localizacion;
    LocationListener locListener;
    private static final String LOGIN_URL = "http://canovius.16mb.com/canovius/login/sync_seguridad.php";
    private static final String LOGIN_SYNC = "http://canovius.16mb.com/canovius/login/sincro.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_ID = "id";
    String ID_ANDROID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_divice);

        db = new SQLiteHandler(getApplicationContext());
        cursor = db.getUserDetails();
        user = (EditText) findViewById(R.id.user);
        String email = cursor.get("email");
        user.setKeyListener(null);
        user.setText(email);
        pass = (EditText) findViewById(R.id.pass);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Login().execute();
            }
        });

        ID_ANDROID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

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

        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // cambia de GPS_PROVIDER a NETWORK_PROVIDER
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
        localizacion = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);




         locListener = new LocationListener() {

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProviderEnabled(String provider) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onLocationChanged(android.location.Location localizacion) {
                lat = String.valueOf(localizacion.getLatitude());
                lon = String.valueOf(localizacion.getLongitude());
            }
        };

        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locListener);

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

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        locManager.removeUpdates(locListener);
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();

    }


    class Login extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(StoreDivice.this);
            pDialog.setMessage("Comprobando sus datos ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            String messges = null;
            int agree;

            String username = user.getText().toString();
            String password = pass.getText().toString();
            if (localizacion != null) {

                lat = String.valueOf(localizacion.getLatitude());
                lon = String.valueOf(localizacion.getLongitude());
                distancia = String.valueOf(localizacion.getAccuracy());

            } else {
                return "Debes activar el gps";
            }


            try {

                List params = new ArrayList();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));

                JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST",
                        params);

                success = json.getInt(TAG_SUCCESS);
                messges=json.getString(TAG_MESSAGE);
                id=json.getString("id");
                if(success==1) {
                    //sincro

                    return json.getString(TAG_MESSAGE);
                    }

            } catch (JSONException e) {
                e.printStackTrace();

            }catch (Exception e) {
                String err = (e.getMessage()==null) ? "" : e.getMessage();

                return "No se puede acceder al servidor; intentelo nuevamente";
            }

            return  messges;

        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if (file_url != null) {
                if(success==1){
                    new SyncDivice().execute();
                }
                Toast.makeText(StoreDivice.this, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }




    class SyncDivice extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialogDivice = new ProgressDialog(StoreDivice.this);
            pDialogDivice.setMessage("Agregando dispositivo a tu cuenta");
            pDialogDivice.setIndeterminate(false);
            pDialogDivice.setCancelable(true);
            pDialogDivice.show();
        }

        @Override
        protected String doInBackground(String... args) {
            int success;
            String messges = null;
            int agree;
            db = new SQLiteHandler(getApplicationContext());
            cursor = db.getUserDetails();
            String idUser  = cursor.get("idUser");

            try {

                if(idUser.equals(id)) {
                    //sincro

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    String currentDateandTime = sdf.format(new Date());
                    TelephonyManager mngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    emei = mngr.getDeviceId();
                    if(emei==null){
                        emei = ID_ANDROID;
                    }
                    model = Build.MODEL;
                    List params = new ArrayList();
                    params.add(new BasicNameValuePair("emei", emei));
                    params.add(new BasicNameValuePair("id", idUser));
                    params.add(new BasicNameValuePair("lat", lat));
                    params.add(new BasicNameValuePair("lon", lon));
                    params.add(new BasicNameValuePair("model", model));
                    params.add(new BasicNameValuePair("fecha", currentDateandTime));

                    JSONObject jsonId = jsonParserSync.makeHttpRequest(LOGIN_SYNC, "POST", params);

                    // save user data
                    agree = jsonId.getInt(TAG_SUCCESS);
                    if(agree==1){
                        dialodAdd = new DialgoAddDivice();
                        dialodAdd.show(getFragmentManager(), "miDialog");
                        dialodAdd.setCancelable(false);
                    }else{
                        messges=jsonId.getString(TAG_MESSAGE);
                        return jsonId.getString(TAG_MESSAGE);
                    }
                    messges=jsonId.getString(TAG_MESSAGE);
                    return jsonId.getString(TAG_MESSAGE);
                } else {

                   return StoreDivice.this.getString(R.string.este_dispositivo_ya_esta_vinculado);
                }


            } catch (JSONException e) {
                e.printStackTrace();

            }catch (Exception e) {
                String err = (e.getMessage()==null) ? "" : e.getMessage();

                return  StoreDivice.this.getString(R.string.no_se_puede_acceder_al_servidor);
            }

            return messges;

        }

        protected void onPostExecute(String file_url) {
            pDialogDivice.dismiss();
            if (file_url != null) {
                Toast.makeText(StoreDivice.this, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }



    @SuppressLint("ValidFragment")
    public class DialgoAddDivice extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            super.onCreateDialog(savedInstanceState);
            builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_layout_store_divice, null);
            builder.setView(view);
            Button back=(Button)view.findViewById(R.id.back);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialodAdd.dismiss();
                    finish();
                    //Intent i = new Intent(StoreDivice.this,ActivityManager.class);
                    //startActivity(i);
                }
            });



            return builder.create();

        }
    }
}
