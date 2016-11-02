package com.lionsquare.canisovismanager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.lionsquare.canisovismanager.Revealator.Revealator;
import com.lionsquare.canisovismanager.Thread.CreateUser;
import com.lionsquare.canisovismanager.Thread.Loggin;
import com.lionsquare.canisovismanager.helper.SQLiteHandler;
import com.lionsquare.canisovismanager.network.Network;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText user, pass;
    EditText userR, passR, passverR;
    String id;
    Button login;
    CheckBox checkBoxLogin;
    private SQLiteHandler db;
    Toolbar toolbar;
    HashMap<String, String> cursor;
    private ProgressDialog progressDialog;
    MiDialgoPersonalizado miDialgoPerzonalizado;
    android.support.v7.app.AlertDialog.Builder builder;
    Loggin loginThrede;
    CreateUser createUser;
    MainActivity main;
    private AdView adView;
    private View theAwesomeView;
    public static final String STATEKEY_THE_AWESOME_VIEW_IS_VISIBLE = "the_awesome_view_is_visible";
    private final int REQUEST_PERMISSION = 0;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {

        } else {
            if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, getString(R.string.app_name) + " " + "necesita permisos", Toast.LENGTH_SHORT).show();
                finish();
            }
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION);

        }

        user = (EditText) findViewById(R.id.cajaUser);
        pass = (EditText) findViewById(R.id.cajaContra);
        login = (Button) findViewById(R.id.buttonEntrar);
        checkBoxLogin = (CheckBox) findViewById(R.id.chkLogin);

        SharedPreferences preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        String correo = preferences.getString("user", "");
        Boolean chkLogin = preferences.getBoolean("chklogin", true);
        user.setText(correo);
        if (chkLogin) {
            checkBoxLogin.setChecked(true);
        } else {
            checkBoxLogin.setChecked(false);
        }
        db = new SQLiteHandler(getApplicationContext());
        cursor = db.getUserDetails();

        if (!cursor.isEmpty() && chkLogin == true) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage(getString(R.string.msg_progress_wait));
            progressDialog.setCancelable(false);
            id = cursor.get("idUser");
            SharedPreferences sp = PreferenceManager
                    .getDefaultSharedPreferences(MainActivity.this);
            SharedPreferences.Editor edit = sp.edit();
            edit.apply();
            Intent intent = new Intent(MainActivity.this, MapListActivityImpl.class);
            intent.putExtra("id", id);
            startActivity(intent);
            overridePendingTransition(R.anim.zoom_entrada, R.anim.zoom_salida);
            finish();
        }


        adView = new AdView(this);
        adView.setAdUnitId("ca-app-pub-5060471640712079/4963218747");
        adView.setAdSize(AdSize.BANNER);
        LinearLayout layout = (LinearLayout) findViewById(R.id.bannerLoggin);
        layout.addView(adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Canovius GPS");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //  toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.divice));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            // toolbar.setBackground(getResources().getDrawable(R.mipmap.ic_launcher));
        } else {

        }


        CollapsingToolbarLayout collapser =
                (CollapsingToolbarLayout) findViewById(R.id.collapser);
        collapser.setTitle("Canovius");
        loadImageParallax(R.drawable.app_background);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_register);
        theAwesomeView = findViewById(R.id.the_awesome_view);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Revealator.reveal(theAwesomeView)
                        .from(fab)
                        .withChildsAnimation()
                        //.withDelayBetweenChildAnimation(...)
                        //.withChildAnimationDuration(...)
                        //.withTranslateDuration(...)
                        //.withRevealDuration(...)
                        //.withEndAction(...)
                        .start();
            }
        });

        final View theWonderfulButton = findViewById(R.id.the_wonderful_button);
        assert theWonderfulButton != null;
        theWonderfulButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Revealator.unreveal(theAwesomeView)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                fab.show();
                            }
                        })
                        //.withDuration(...)
                        .start();

            }
        });

        if (savedInstanceState != null && savedInstanceState.getBoolean(STATEKEY_THE_AWESOME_VIEW_IS_VISIBLE)) {
            theAwesomeView.setVisibility(View.VISIBLE);
            fab.setVisibility(View.INVISIBLE);
        }

        userR = (EditText) findViewById(R.id.emailEditText);
        passR = (EditText) findViewById(R.id.contraEditText);
        passverR = (EditText) findViewById(R.id.contraVerifiEditText);


        FloatingActionButton fabSave = (FloatingActionButton) findViewById(R.id.fab_save);
        assert fabSave != null;
        fabSave.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        createUser = new CreateUser(userR.getText().toString()
                                , passR.getText().toString()
                                , passverR.getText().toString()
                                , miDialgoPerzonalizado
                                , MainActivity.this);
                        createUser.execute();
                    }
                }
        );


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.salir) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (Network.networkAvailable(MainActivity.this)) {
            SharedPreferences preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();//crear un editor qu emodifica el archivo de preferencias
            editor.putString("user", user.getText().toString());
            editor.putBoolean("chklogin", checkBoxLogin.isChecked());
            editor.apply();
            loginThrede = new Loggin(user.getText().toString(), pass.getText().toString(), db, main, MainActivity.this);
            loginThrede.execute();
            //new AttemptLogin().execute();
        } else {

            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle(R.string.msg_progress_alert);
            alertDialog.setMessage(getString(R.string.msg_sin_intenet));
            alertDialog.setButton(getString(R.string.msg_si), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // aquí puedes añadir funciones
                }
            });
            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
            alertDialog.show();

        }

    }

    @Override
    public void onStart() {

        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled) {
            alertGPS();
        }


        super.onStart();


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void loadImageParallax(int id) {
        ImageView image = (ImageView) findViewById(R.id.image_paralax);
        // Usando Glide para la carga asíncrona
        Glide.with(this)
                .load(id)
                .centerCrop()
                .into(image);
    }


    @SuppressLint("ValidFragment")
    public class MiDialgoPersonalizado extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            super.onCreateDialog(savedInstanceState);
            builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.dialogo_layout, null);
            builder.setView(view);
            userR = (EditText) view.findViewById(R.id.emailEditText);
            passR = (EditText) view.findViewById(R.id.contraEditText);
            passverR = (EditText) view.findViewById(R.id.contraVerifiEditText);


            FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_save);
            fab.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            createUser = new CreateUser(userR.getText().toString()
                                    , passR.getText().toString()
                                    , passverR.getText().toString()
                                    , miDialgoPerzonalizado
                                    , getActivity());
                            createUser.execute();
                        }
                    }
            );
            return builder.create();


        }
    }


    private void alertGPS() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder
                .setMessage(R.string.gps_msg_deshabilitar)
                .setCancelable(false)
                .setPositiveButton(R.string.gps_msg_habilitar,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(intent);
                            }
                        });
        alertDialogBuilder.setNegativeButton(R.string.msg_no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
}

