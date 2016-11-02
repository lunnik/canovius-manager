package com.lionsquare.canisovismanager;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.MapView;
import com.lionsquare.canisovismanager.AdapterList.MapLocationAdapter;
import com.lionsquare.canisovismanager.Beans.MapLocation;
import com.lionsquare.canisovismanager.Thread.ListDivice;
import com.lionsquare.canisovismanager.helper.SQLiteHandler;
import com.lionsquare.canisovismanager.network.Network;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class ActivityManager extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    public String userId;
    private String emei;
    android.support.v7.app.AlertDialog.Builder builder;
    DialogoDirection dialogoDirection;
    ArrayList<MapLocation> lista_divice = new ArrayList<MapLocation>();
    HashMap<String, String> cursor;
    public boolean deleteAcept;
    private SQLiteHandler db;
    int cont = 0;
    private AdView adView;
    String itemDialogDirc = "No disponible";
    private static final String DIRC_URL = "http://canovius.16mb.com/canovius/login/locationDivece.php";
    private SwipeRefreshLayout swipeContainer;
    ListDivice listDivice;
    DialogoLista dialogoLista;

    Bundle bundle;
    RecyclerView recyclerView;
    private Paint p = new Paint();
    protected MapLocationAdapter mListAdapter;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private  final int REQUEST_PERMISSION = 0;


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_manager);
        bundle = savedInstanceState;

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


        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);

        SharedPreferences preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        String usuario = preferences.getString("user", "");

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.srlContainer);
        swipeContainer.setOnRefreshListener(this);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        if (getIntent() != null) {
            userId = getIntent().getExtras().getString("id");
        }


        if (Network.networkAvailable(ActivityManager.this)) {
            //new cargarLita().execute();
            listDivice = new ListDivice(userId, lista_divice, ActivityManager.this);
            listDivice.execute();
            FDialog();

        } else {
            Toast.makeText(ActivityManager.this, "sin conexion", Toast.LENGTH_LONG).show();
        }

        db = new SQLiteHandler(getApplicationContext());
        cursor = db.getUserDetails();
        if (!cursor.isEmpty()) {

            userId = cursor.get("idUser");

        }
        adView = new AdView(this);
        adView.setAdUnitId("ca-app-pub-5060471640712079/9973671147");
        adView.setAdSize(AdSize.BANNER);
        LinearLayout layout = (LinearLayout) findViewById(R.id.manager);
        layout.addView(adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Dispositivos");

        //toolbar.setLogo(R.mipmap.ic_launcher);


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


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        if (id == R.id.canovius) {

            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=com.lionsquare.canoviusclient")));

        }

        if (id == R.id.actualizar) {
            if (Network.networkAvailable(ActivityManager.this)) {
                lista_divice.clear();
                listDivice = new ListDivice(userId, lista_divice, ActivityManager.this);
                listDivice.execute();
            } else {
                Toast.makeText(this, "No hay conexion a internet", Toast.LENGTH_LONG).show();
            }


            return true;
        }


        if (id == R.id.salir) {
            db.deleteUsers();
            finish();
            Intent i = new Intent(ActivityManager.this, MainActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.zoom_entrada, R.anim.zoom_salida);
        }

        return super.onOptionsItemSelected(item);
    }

    private PopupMenu.OnMenuItemClickListener event = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {

            int id = menuItem.getItemId();
            if (id == R.id.borrar) {

                //AlertDialog alert = builder.create();
                //alert.show();


                return true;
            }

            return false;
        }
    };

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Update data in ListView
                if (Network.networkAvailable(ActivityManager.this)) {
                    lista_divice.clear();
                    listDivice = new ListDivice(userId, lista_divice, ActivityManager.this);
                    listDivice.execute();
                } else {
                    Toast.makeText(ActivityManager.this, "sin conexion", Toast.LENGTH_LONG).show();
                }
                // Remove widget from screen.
                swipeContainer.setRefreshing(false);
            }
        }, 1000);


    }

    private void FDialog() {
        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            public void run() {
                if (listDivice.datos.equals("=(")) {

                    dialogoLista = new DialogoLista();
                    dialogoLista.show(getFragmentManager(), "miDialog");
                    dialogoLista.setCancelable(false);

                }
            }
        }, 1000);
    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(ActivityManager.this, StoreDivice.class);
        startActivity(i);
    }


    public void Lista() {


        AnimationSet set = new AnimationSet(true);

        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(50);
        set.addAnimation(animation);

        animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f
        );
        animation.setDuration(300);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        //recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(1), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(mLayoutManager);
        mListAdapter = createMapListAdapter();
        recyclerView.setAdapter(mListAdapter);


    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ActivityManager Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.lionsquare.canisovismanager/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mListAdapter != null) {
            for (MapView m : mListAdapter.getMapViews()) {
                m.onPause();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ActivityManager Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.lionsquare.canisovismanager/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (resultCode == ConnectionResult.SUCCESS) {
            recyclerView.setAdapter(mListAdapter);
        } else {
            GooglePlayServicesUtil.getErrorDialog(resultCode, this, 1).show();
        }

        if (mListAdapter != null) {
            for (MapView m : mListAdapter.getMapViews()) {
                m.onResume();
            }
        }
    }

    @Override
    protected void onDestroy() {

        if (mListAdapter != null) {
            for (MapView m : mListAdapter.getMapViews()) {
                m.onDestroy();
            }
        }
        super.onDestroy();
    }

    @SuppressLint("ValidFragment")
    public class DialogoDirection extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            super.onCreateDialog(savedInstanceState);
            builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_direccion, null);
            builder.setView(view);
            TextView dirc_dialog = (TextView) view.findViewById(R.id.dirc_log);
            dirc_dialog.setText(itemDialogDirc);


            return builder.create();


        }
    }


    @SuppressLint("ValidFragment")
    public class DialogoLista extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            super.onCreateDialog(savedInstanceState);
            builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_lista, null);
            builder.setView(view);

            FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
            fab.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent i = new Intent(ActivityManager.this, StoreDivice.class);
                            startActivity(i);
                            dismiss();
                        }
                    }
            );


            return builder.create();


        }
    }


    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();

        if (mListAdapter != null) {
            for (MapView m : mListAdapter.getMapViews()) {
                m.onLowMemory();
            }
        }
    }


    protected abstract MapLocationAdapter createMapListAdapter();

    public abstract void showMapDetails(View view);


}
