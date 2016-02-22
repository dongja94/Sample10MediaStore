package com.example.dongja94.samplemediastore;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.define.Define;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
        gridView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        String[] from = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME};
        int[] to = {R.id.image_thumb, R.id.text_name};
        mAdapter = new SimpleCursorAdapter(this, R.layout.view_check_item, null, from, to, 0);
        mAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (columnIndex == dataColumn) {
//                    long id = cursor.getLong(columnIndex);
                    String path = cursor.getString(columnIndex);
                    ImageView iv = (ImageView) view;
//                    Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(getContentResolver(), id, MediaStore.Images.Thumbnails.MINI_KIND, null);
//                    iv.setImageBitmap(bitmap);

//                    Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
//                    Glide.with(MainActivity.this).load(uri).into(iv);
                    File file = new File(path);
                    Uri fileuri = Uri.fromFile(file);
                    Glide.with(MainActivity.this).load(fileuri).into(iv);
                    return true;
                }
                return false;
            }
        });
        gridView.setAdapter(mAdapter);
        Button btn = (Button)findViewById(R.id.btn_select);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray array = gridView.getCheckedItemPositions();
                List<String> imageList = new ArrayList<String>();
                for (int index = 0; index < array.size(); index++) {
                    int position = array.keyAt(index);
                    if (array.get(position)) {
                        Cursor c = (Cursor)gridView.getItemAtPosition(position);
                        String name = c.getString(c.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                        imageList.add(name);
                    }
                }
                Toast.makeText(MainActivity.this, "list : " + imageList, Toast.LENGTH_SHORT).show();
            }
        });

        btn = (Button)findViewById(R.id.btn_library);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FishBun.with(MainActivity.this)
                        .setAlbumThumnaliSize(150)//you can resize album thumnail size
                        .setPickerCount(5)//you can restrict photo count
                        .startAlbum();
            }
        });

        getSupportLoaderManager().initLoader(0, null ,this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Define.ALBUM_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    List<String> path = data.getStringArrayListExtra(Define.INTENT_PATH);
                    Toast.makeText(this, "path : " + path, Toast.LENGTH_SHORT).show();
                    break;
                }
        }
    }

    String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA , MediaStore.Images.Media.DISPLAY_NAME};
    String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC";

    private int idColumn = -1;
    private int dataColumn = -1;

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, sortOrder);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        idColumn = data.getColumnIndex(MediaStore.Images.Media._ID);
        dataColumn = data.getColumnIndex(MediaStore.Images.Media.DATA);
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
