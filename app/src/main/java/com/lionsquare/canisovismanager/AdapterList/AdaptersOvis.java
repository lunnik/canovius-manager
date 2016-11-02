package com.lionsquare.canisovismanager.AdapterList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lionsquare.canisovismanager.SwipesList.BaseSwipListAdapter;

import java.util.ArrayList;

/**
 * Created by archivaldo on 29/11/15.
 */
public abstract class AdaptersOvis extends BaseSwipListAdapter {

    //Este código lo que hace es crear un adaptador para ListView. En el constructor
    // hay que pasarle el contexto de la aplicación, el id del layout de la entrada
    // (en este ejemplo “R.layout.entrada”), y el ArrayList que construimos anteriormente
    // con los handler con los datos. Como es obvio, ya que pide los datos del ArrayList,
    // este código irá justo después de la creación del ArrayList.
    private ArrayList<?> entradas;
    private int R_layout_IdView;
    private Context contexto;

    public AdaptersOvis(Context contexto, int R_layout_IdView, ArrayList<?> entradas) {
        super();
        this.contexto = contexto;
        this.entradas = entradas;
        this.R_layout_IdView = R_layout_IdView;
    }

    @Override
    public View getView(int posicion, View view, ViewGroup pariente) {
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R_layout_IdView, null);
        }
        onEntrada(entradas.get(posicion), view);
        return view;
    }

    @Override
    public int getCount() {
        return entradas.size();
    }

    @Override
    public Object getItem(int posicion) {
        return entradas.get(posicion);
    }

    @Override
    public long getItemId(int posicion) {
        return posicion;
    }

    /**
     * Devuelve cada una de las entradas con cada una de las vistas a la que debe de ser asociada
     *
     * @param entrada La entrada que será la asociada a la view. La entrada es del tipo del paquete/handler
     * @param view    View particular que contendrá los datos del paquete/handler
     */
    public abstract void onEntrada(Object entrada, View view);

}
