package com.example.parcial_v2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class add_music extends AppCompatActivity {

    private MusicDbHelper musicDbHelper;
    private Music music;

    private Button btnBuscar;
    private EditText txtBuscar;
    private RequestQueue queue;

    //txt
    private TextView tvCancion;
    private TextView tvArtista;
    private TextView tvAlbum;
    private TextView tvDireccion;
    private TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_music);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        musicDbHelper  = new MusicDbHelper(this);

        queue = Volley.newRequestQueue(this);

        txtBuscar = (EditText) findViewById(R.id.edtBuscar);
        tvCancion = (TextView) findViewById(R.id.tvCancion);
        tvArtista = (TextView) findViewById(R.id.tvArtista);
        tvAlbum = (TextView) findViewById(R.id.tvAlbum);
        tvDireccion = (TextView) findViewById(R.id.tvDuracion);
        info = (TextView) findViewById(R.id.info);
        btnBuscar = (Button)findViewById(R.id.btnBuscar);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetMusic();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.insert_music:
                InsertDbMusic();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void GetMusic(){
        String URL = "https://api.deezer.com/search?q="+txtBuscar.getText();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray jsonArrayData = response.getJSONArray("data");
                            JSONObject jsonObjectMusic = jsonArrayData.optJSONObject(0);
                            String title = jsonObjectMusic.getString("title");
                            int duration = jsonObjectMusic.getInt("duration");
                            JSONObject jsonObjectArtist = jsonObjectMusic.getJSONObject("artist");
                            String autor = jsonObjectArtist.getString("name");

                            info.setText("Informacion");
                            tvCancion.setText(title);
                            tvArtista.setText(autor);
                            tvAlbum.setText(title);
                            tvDireccion.setText(duration+"");

                            music = new Music(title, autor, duration+"");

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

    private void InsertDbMusic(){
        if(music != null) {
            SQLiteDatabase db = musicDbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(MusicContract.MusicEntry.TITLE, music.title);
            values.put(MusicContract.MusicEntry.AUTOR, music.autor);
            values.put(MusicContract.MusicEntry.DURATION, music.duration);

            long result = db.insert(MusicContract.MusicEntry.TABLE_NAME, null, values);

            if (result == -1) {
                Toast.makeText(getApplicationContext(), "Error a la hora de insertar el dato", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Cancion a sido agregada a la playlist", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
            db.close();
        }else
            Toast.makeText(getApplicationContext(), "PAra agregar una cancion primero debe registrarla", Toast.LENGTH_LONG).show();
    }
}
