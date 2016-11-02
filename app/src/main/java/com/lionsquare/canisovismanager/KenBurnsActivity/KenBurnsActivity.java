package com.lionsquare.canisovismanager.KenBurnsActivity;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;

public abstract class KenBurnsActivity extends Activity {

    private boolean mPaused;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mPaused) {
          /*  item.setIcon(R.drawable.ic_play);
            item.setTitle(R.string.play);*/
        } else {
           /* item.setIcon(R.drawable.ic_pause);
            item.setTitle(R.string.pause);*/
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    protected abstract void onPlayClick();
    protected abstract void onPauseClick();

}