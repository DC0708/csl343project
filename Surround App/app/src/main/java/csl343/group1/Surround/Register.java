/**
 * Author: Aditya Abhas
 * Entry No: 2012csb1002
 *
 * The function registerUser() will store the user by passing name, email and password to server.
 *
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


public class Register extends Activity {
	private static final String logTAG = Register.class.getSimpleName();
	private Button registerButton;
	private Button loginButton;
	private EditText name;
	private EditText email;
	private EditText password;
	private ProgressDialog progressDialog;
	private SessionManager sessionManager;
	private SQLiteManager sqliteDB;

	@Override
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.register_activity);

		name = (EditText) findViewById(R.id.name);
		email = (EditText) findViewById(R.id.email);
		password = (EditText) findViewById(R.id.password);
		registerButton = (Button) findViewById(R.id.REGISTER_BUTTON);
		loginButton = (Button) findViewById(R.id.LINK_TO_LOGIN_SCREEN);

		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);

		sessionManager = new SessionManager(getApplicationContext());

		sqliteDB = new SQLiteManager(getApplicationContext());

		// If user is logged in, start main activity
		if (sessionManager.isLoggedIn()) {
            Intent i = new Intent(Register.this,
                    MainActivity.class);
			startActivity(i);
			finish();
		}

		registerButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                String username = Register.this.name.getText().toString();
                String userEmail = Register.this.email.getText().toString();
                String userPassword = Register.this.password.getText().toString();

                if (!username.isEmpty() && !userEmail.isEmpty() && !userPassword.isEmpty())
                    registerUser(username, userEmail, userPassword);
                else
                    Toast.makeText(getApplicationContext(),
                            "Please enter the required information!", Toast.LENGTH_LONG)
                            .show();

            }
        });

		loginButton.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),
                        Login.class));
                finish();
            }
        });

	}


	// This function posts the parameters (post tag, user's full name, email, password) to registration url

	private void registerUser(final String name, final String email,
			final String password) {

		String tag_string_req = "req_register";

		progressDialog.setMessage("Registering new user...");
		showDialog();

		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				ServerConfig.REGISTER_URL, new Response.Listener<String>() {

					@Override
					public void onResponse(String serverResponse) {
						Log.d(logTAG, "Register Response: " + serverResponse.toString());
						hideDialog();

						try {
							JSONObject jObj = new JSONObject(serverResponse);
							boolean error = jObj.getBoolean("error");
							if (!error) {
								startActivity(new Intent(Register.this,MainActivity.class));
								finish();
							} else {
								String errorMessage = jObj.getString("error_msg");
								Toast.makeText(getApplicationContext(),
										errorMessage, Toast.LENGTH_LONG).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError regError) {
						Log.e(logTAG, "Registration Error: " + regError.getMessage());
						Toast.makeText(getApplicationContext(),
								regError.getMessage(), Toast.LENGTH_LONG).show();
						hideDialog();
					}
				}) {

			@Override
			protected Map<String, String> getParams() {
				// POST parameters to registration url
				Map<String, String> parameters = new HashMap<String, String>();
				parameters.put("tag", "register");
				parameters.put("name", name);
				parameters.put("email", email);
				parameters.put("password", password);

				return parameters;
			}

		};

		AppManager.getInstance().addToRequestQueue(stringRequest, tag_string_req);
	}

	private void showDialog() {
		if (!progressDialog.isShowing())
			progressDialog.show();
	}

	private void hideDialog() {
		if (progressDialog.isShowing())
			progressDialog.dismiss();
	}
}
