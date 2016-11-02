package com.lionsquare.canisovismanager.Thread;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.lionsquare.canisovismanager.JsonParser.JSONParser;
import com.lionsquare.canisovismanager.MainActivity;
import com.lionsquare.canisovismanager.MapListActivityImpl;
import com.lionsquare.canisovismanager.R;
import com.lionsquare.canisovismanager.helper.SQLiteHandler;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by archivaldo on 05/02/16.
 */

public class Loggin extends AsyncTask<String, String, String> {
    private Context mContext;
    private String user, pass, id;
    private ProgressDialog pDialog;
    private static final String DELETE_URL = "http://canovius.16mb.com/canovius/login/login.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_ID = "id";
    JSONParser jsonParser = new JSONParser();
    private SQLiteHandler db;
    Boolean sessionActivity;
    MainActivity main;


    public Loggin(String user, String pass, SQLiteHandler db, MainActivity main, Context context) {
        super();
        this.user = user;
        this.pass = pass;
        this.db = db;
        this.mContext = context;
        this.main = main;

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Comprobando sus datos ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected String doInBackground(String... args) {
        int success;


        try {

            main = (MainActivity) mContext;
            db = new SQLiteHandler(mContext);
            List params = new ArrayList();
            params.add(new BasicNameValuePair("username", user));
            params.add(new BasicNameValuePair("password", pass));


            JSONObject json = jsonParser.makeHttpRequest(DELETE_URL, "POST",
                    params);

            Log.e("Login attempt", json.toString());

            success = json.getInt(TAG_SUCCESS);
            if (success == 1) {
                id = json.getString(TAG_ID);
                sessionActivity = true;

                db.addUser(user, id, pass, sessionActivity);
                // save user data
                SharedPreferences sp = PreferenceManager
                        .getDefaultSharedPreferences(mContext);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("username", user);
                edit.apply();

                    Intent intent = new Intent(mContext, MapListActivityImpl.class);
                intent.putExtra(TAG_ID, id);
                mContext.startActivity(intent);
                main.overridePendingTransition(R.anim.zoom_entrada, R.anim.zoom_salida);
                main.finish();
                //falta arrgelar la animacion al cambio de actividad

                return json.getString(TAG_MESSAGE);
            } else {
                return json.getString(TAG_MESSAGE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            String err = (e.getMessage() == null) ? "" : e.getMessage();
            Log.e("error", String.valueOf(e));
            return String.valueOf(e);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return null;

    }


    protected void onPostExecute(String file_url) {
        pDialog.dismiss();
        if (file_url != null) {
            Toast.makeText(mContext, file_url, Toast.LENGTH_LONG).show();
        }
    }
}
