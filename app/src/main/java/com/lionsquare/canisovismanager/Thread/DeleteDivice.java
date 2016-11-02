package com.lionsquare.canisovismanager.Thread;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.lionsquare.canisovismanager.ActivityManager;
import com.lionsquare.canisovismanager.JsonParser.JSONParser;
import com.lionsquare.canisovismanager.R;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by archivaldo on 06/02/16.
 */
public class DeleteDivice extends AsyncTask<String, String, String> {
    Context mContext;
    private static final String LOGIN_URL = "http://canovius.16mb.com/canovius/login/deleteDivice.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private ProgressDialog pDialog;
    String emei;
    JSONParser jsonParser = new JSONParser();
    ActivityManager main;

    public DeleteDivice(String emei, Context context){

        this.emei=emei;
        this.mContext=context;

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Eliminando dispositivo ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected String doInBackground(String... args) {
        int success;
        main =(ActivityManager) mContext;
        try {

            List params = new ArrayList();
            params.add(new BasicNameValuePair("id_emei", emei));

            JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST",
                    params);

            success = json.getInt(TAG_SUCCESS);
            if (success == 1) {
                main.deleteAcept = true;
                // save user data
                return json.getString(TAG_MESSAGE);
            } else {
                return json.getString(TAG_MESSAGE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e) {
            return  mContext.getString(R.string.no_se_puede_acceder_al_servidor);
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
