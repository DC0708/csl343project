/**
 * Author: Aditya Abhas
 * Entry No: 2012csb1002
 *
 * In this class, checkLogin() function verifies the login user details on the server using the volley http request.
 * */

package csl343.group1.Surround;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;



public class Login extends Activity {
    private static final String logcatTAG = Register.class.getSimpleName();
    private Button loginButton;
    private Button registerButton;
    private EditText email;
    private EditText password;
    private ProgressDialog progressDialog;
    private SessionManager sessionManager;
    private SQLiteManager sqliteDB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        loginButton = (Button) findViewById(R.id.BTN_LOGIN);
        registerButton = (Button) findViewById(R.id.BTN_LINK_TO_REGISTER_SCREEN);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        sessionManager = new SessionManager(getApplicationContext());

        // Database handler
        sqliteDB = new SQLiteManager(getApplicationContext());

        // Start the main activity, if the user is logged in
        if (sessionManager.isLoggedIn()) {
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        }

        loginButton.setOnClickListener(new OnClickListener() {

            public void onClick(View loginView) {

                String email = Login.this.email.getText().toString();
                String password = Login.this.password.getText().toString();

                if (email.length() > 0 && password.length() > 0)
                    checkLogin(email, password);
                else
                    Toast.makeText(getApplicationContext(),
                            "Enter valid email and password!", Toast.LENGTH_LONG)
                            .show();

            }

        });

        registerButton.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Register.class));
                finish();
            }
        });

    }


     // function for verifying login details on server

    private void checkLogin(final String email, final String password) {

        String tagStringReq = "req_login";

        progressDialog.setMessage("Signing in...");
        showProgressDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                ServerConfig.REGISTER_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String serverResponse) {
                Log.d(logcatTAG, "Sign in Response: " + serverResponse.toString());
                hideProgressDialog();

                try {
                    JSONObject jsonObject = new JSONObject(serverResponse);
                    boolean error = jsonObject.getBoolean("error");

                    if (!error) {

                        // start login sessionManager for the user
                        sessionManager.setLoginStatus(true);

                        // storing user info in sqlite database
                        JSONObject user = jsonObject.getJSONObject("user");
                        sqliteDB.addUser(user.getString("name"), user.getString("email"), jsonObject.getString("uid"), user.getString("created_at"));

                        // start main activity after login
                        startActivity(new Intent(Login.this,
                                MainActivity.class));
                        finish();

                    } else {
                        // error
                        Toast.makeText(getApplicationContext(),
                                jsonObject.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError errorResponse) {
                Log.e(logcatTAG, "Login Error: " + errorResponse.getMessage());
                Toast.makeText(getApplicationContext(),
                        errorResponse.getMessage(), Toast.LENGTH_LONG).show();
                hideProgressDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // POST parameters to the login url
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("tag", "login");
                parameters.put("email", email);
                parameters.put("password", password);

                return parameters;
            }

        };

        // Add request to the request queue
        AppManager.getInstance().addToRequestQueue(stringRequest, tagStringReq);
    }

    private void showProgressDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}