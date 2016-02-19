package com.example.dongja94.samplemediastore;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    GridView gridView;
    SimpleCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        gridView = (GridView)findViewById(R.id.gridView);
        String[] from = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME};
        int[] to = {R.id.image_thumb, R.id.text_name};
        mAdapter = new SimpleCursorAdapter(this, R.layout.view_item, null, from, to, 0);
        mAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (columnIndex == idColumn) {
                    long id = cursor.getLong(columnIndex);
                    Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(getContentResolver(), id, MediaStore.Images.Thumbnails.MINI_KIND, null);
                    ImageView iv = (ImageView)view;
                    iv.setImageBitmap(bitmap);
                    return true;
                }
                return false;
            }
        });
        gridView.setAdapter(mAdapter);
        getSupportLoaderManager().initLoader(0, null ,this);
    }

    String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA , MediaStore.Images.Media.DISPLAY_NAME};
    String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC";

    private int idColumn = -1;

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        idColumn = data.getColumnIndex(MediaStore.Images.Media._ID);
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
