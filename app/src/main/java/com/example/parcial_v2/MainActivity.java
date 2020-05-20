package com.example.parcial_v2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.parcial_v2.Data.Music;
import com.example.parcial_v2.Data.MusicContract;
import com.example.parcial_v2.Data.MusicDbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final String URL = "https://api.deezer.com/playlist/93489551";
    private RequestQueue queue;
    private MusicDbHelper musicDbHelper;

    private RecyclerView RecyclerView;
    private ArrayList<Music> musics;
    private Adapter_music adapterMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        musicDbHelper = new MusicDbHelper(this);
        queue = Volley.newRequestQueue(this);

        ArrayList<Music> music = DisplayMusic();
        if(music.size() < 1){
            GetMusic();
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_music();
            }
        });
    }

    public void add_music(){
        Toast.makeText(getApplicationContext(), "Add music", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, add_music.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.add_inset_playlist:

                Toast.makeText(getApplicationContext(), "Add music", Toast.LENGTH_LONG).show();
                add_music();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void GetMusic(){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        //musics = new ArrayList<Music>();
                        JSONObject jsonObjectTracks = response.getJSONObject("tracks");
                        JSONArray jsonArrayDatas =  jsonObjectTracks.optJSONArray("data");

                        for (int i=0; i < 5; i++){
                            JSONObject jsonObjectMusic = jsonArrayDatas.getJSONObject(i);

                            String title = jsonObjectMusic.getString("title");
                            int duration = jsonObjectMusic.getInt("duration");

                            JSONObject jsonObjectArtist = jsonObjectMusic.getJSONObject("artist");
                            String aristt = jsonObjectArtist.getString("name");


                            Music music = new Music(title, aristt, duration+"");
                            //Toast.makeText(getApplicationContext(), music.title+" "+music.duration+""+music.autor, Toast.LENGTH_LONG).show();
                            //musics.add(music);

                            InsertDbMusic(music);

                        }
                        //Toast.makeText(getApplicationContext(), musics.size(), Toast.LENGTH_LONG).show();
                        DisplayMusic();
                    } catch (JSONException e) {
                        Log.d(MainActivity.class.getSimpleName(), "erroooooooooooooooooooooooooooo");
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(MainActivity.class.getSimpleName(), "erroooooooooooooooooooooooooooo responce");
            }
        });
        this.queue.add(jsonObjectRequest);
    }

    private void InsertDbMusic(Music music){
        SQLiteDatabase db = musicDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MusicContract.MusicEntry.TITLE, music.title);
        values.put(MusicContract.MusicEntry.AUTOR, music.autor);
        values.put(MusicContract.MusicEntry.DURATION, music.duration);

        long result = db.insert(MusicContract.MusicEntry.TABLE_NAME, null, values);

        if(result == -1){
            //Toast.makeText(getApplicationContext(), "Error a la hora de insertar el dato", Toast.LENGTH_LONG).show();
            Log.d(MainActivity.class.getSimpleName(), "Error a la hora de insertar el dato");
        }else{
            Log.d(MainActivity.class.getSimpleName(), "Dato a sido insertado");
            //Toast.makeText(getApplicationContext(), "Dato a sido insertado", Toast.LENGTH_LONG).show();
        }
    }

    private ArrayList<Music> DisplayMusic(){

        RecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        RecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        musics = new ArrayList<Music>();
        SQLiteDatabase db = musicDbHelper.getReadableDatabase();

        String[] projection = {
                MusicContract.MusicEntry.TITLE,
                MusicContract.MusicEntry.AUTOR,
                MusicContract.MusicEntry.DURATION};

        Cursor cursor = db.query(MusicContract.MusicEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

        try {

            int titleColumnIndex = cursor.getColumnIndex(MusicContract.MusicEntry.TITLE);
            int autorColumnIndex = cursor.getColumnIndex(MusicContract.MusicEntry.AUTOR);
            int durationColumnIndex = cursor.getColumnIndex(MusicContract.MusicEntry.DURATION);

            while (cursor.moveToNext()){
                String title = cursor.getString(titleColumnIndex);
                String autor= cursor.getString(autorColumnIndex);
                String duration = cursor.getString(durationColumnIndex);

                Music music = new Music(title, autor, duration);
                musics.add(music);
            }
            adapterMusic = new Adapter_music(musics);
            RecyclerView.setAdapter(adapterMusic);
        }finally {
            cursor.close();
        }
        return musics;
    }

}
