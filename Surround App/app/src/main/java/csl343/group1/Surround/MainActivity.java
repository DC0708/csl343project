/**
 * Author: Aditya Abhas
 * Song Fetch done by Abhishek Raj (broadcast and domusic functions)
 *
 * */

package csl343.group1.Surround;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {

    private TextView txtName;
    private TextView txtEmail;
    private Button btnLogout;

    private TextView songName;

    private SQLiteManager db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        songName = (TextView) findViewById(R.id.songName);

        // SqLite database handler
        db = new SQLiteManager(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");

        // Displaying the user details on the screen
        txtName.setText(name);
        //	txtEmail.setText(email);

        doMusicStuff();

        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLoginStatus(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);
        finish();
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String cmd = intent.getStringExtra("command");
            Log.v("tag ", action + " / " + cmd);
            String artist = intent.getStringExtra("artist");
            String album = intent.getStringExtra("album");
            String track = intent.getStringExtra("track");
          //  artist = "Demo Artist";
          //  album = "Demo album";
          //  track = "Demo track";
            Log.v("tag", artist + ":" + album + ":" + track);
            songName.setText( artist + ":" + album + ":" + track);
            																																						updateSongOnServer(artist,album,track);

        }
    };


    public void doMusicStuff() {
        IntentFilter iF = new IntentFilter();
        iF.addAction("com.android.music.metachanged");
        iF.addAction("com.android.music.playstatechanged");
        iF.addAction("com.android.music.playbackcomplete");
        iF.addAction("com.android.music.queuechanged");
        iF.addAction("com.android.music.metachanged");

        iF.addAction("com.htc.music.metachanged");

        iF.addAction("fm.last.android.metachanged");
        iF.addAction("com.sec.android.app.music.metachanged");
        iF.addAction("com.nullsoft.winamp.metachanged");
        iF.addAction("com.amazon.mp3.metachanged");
        iF.addAction("com.miui.player.metachanged");
        iF.addAction("com.real.IMP.metachanged");
        iF.addAction("com.sonyericsson.music.metachanged");
        iF.addAction("com.rdio.android.metachanged");
        iF.addAction("com.samsung.sec.android.MusicPlayer.metachanged");
        iF.addAction("com.andrew.apollo.metachanged");

        registerReceiver(mReceiver, iF);
    }

    public void updateSongOnServer(final String artist,final String album,final String track){

        HashMap<String, String> user = db.getUserDetails();

        final String uid = user.get("uid");


        String tag_string_req = "req_register";


        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                ServerConfig.REGISTER_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String serverResponse) {

                try {
                    JSONObject jObj = new JSONObject(serverResponse);
                    boolean error = jObj.getBoolean("error");
                    if (error) {
                        String errorMessage = jObj.getString("error_msg");
         //               Toast.makeText(getApplicationContext(),
           //                     errorMessage, Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError songUpdateError) {
             //   Toast.makeText(getApplicationContext(),
             //           songUpdateError.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {

            @Override																																																																																																																																																																	
            protected Map<String, String> getParams() {
                // POST parameters to registration url
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("tag", "getsongs");
                parameters.put("uid",uid);
                parameters.put("artist", artist);
                parameters.put("album", album);
                parameters.put("track", track);

                return parameters;
            }

        };

        AppManager.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }





}

