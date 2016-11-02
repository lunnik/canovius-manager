package com.lionsquare.canisovismanager.Thread;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.lionsquare.canisovismanager.JsonParser.JSONParser;
import com.lionsquare.canisovismanager.MainActivity;
import com.lionsquare.canisovismanager.R;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by archivaldo on 06/02/16.
 */
public class CreateUser extends AsyncTask<String, String, String> {
    Context mContext;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static final String REGISTER_URL = "http://canovius.16mb.com/canovius/login/register.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    MainActivity.MiDialgoPersonalizado miDialgoPerzonalizado;

    String user,pass,verf;
    public CreateUser(String user,String pass,String verf,MainActivity.MiDialgoPersonalizado miDialgoPerzonalizado,Context context){
        this.user=user;
        this.pass=pass;
        this.verf=verf;
        this.miDialgoPerzonalizado= miDialgoPerzonalizado;
        this.mContext=context;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Creando cuenta...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected String doInBackground(String... args) {
        // TODO Auto-generated method stub
        // Check for success tag
        int success;

        try {

            if(user.equals("")||pass.equals("")||verf.equals("")){

                return  mContext.getString(R.string.ingersar_todo_los_datos);


            }else{

                if(user.length()<=7){
                    return  mContext.getString(R.string.el_usuario_debe_tener_ocho_caracteres);
                }
                if(pass.length()<=7){
                    return  mContext.getString(R.string.la_contrasena_debe_tener_ocho_caracteres);
                }

            }


            // Building Parameters
            List params = new ArrayList();
            params.add(new BasicNameValuePair("username", user));
            params.add(new BasicNameValuePair("password", pass));
            params.add(new BasicNameValuePair("passwordver", verf));


            JSONObject json = jsonParser.makeHttpRequest(
                    REGISTER_URL, "POST", params);


            // Log.d("Registering attempt", json.toString());

            // json success element
            success = json.getInt(TAG_SUCCESS);
            if (success == 1) {

                //miDialgoPerzonalizado.dismiss();
                return json.getString(TAG_MESSAGE);
            }else{
                return json.getString(TAG_MESSAGE);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e) {

            String err = (e.getMessage()==null) ? "" : e.getMessage();

            return  mContext.getString(R.string.no_se_puede_acceder_al_servidor);
        }

        return null;

    }

    protected void onPostExecute(String file_url) {
        pDialog.dismiss();
        if (file_url != null){
            Toast.makeText(mContext, file_url, Toast.LENGTH_LONG).show();
        }
    }


}
