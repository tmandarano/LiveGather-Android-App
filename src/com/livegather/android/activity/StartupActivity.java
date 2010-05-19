package com.livegather.android.activity;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.livegather.android.LiveGatherApplication;
import com.livegather.android.R;
import com.livegather.android.model.RestClient;
import com.livegather.android.model.User;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StartupActivity extends Activity implements OnClickListener {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);

		User user = ((LiveGatherApplication) this.getApplication()).getUser();

		if(user == null) {
			String username = this.getPreferences(Context.MODE_PRIVATE).getString("username", "");
			String password = this.getPreferences(Context.MODE_PRIVATE).getString("password", "");
	    
			if(username != null && password != null) {
				Map<String, String> params = new HashMap<String, String>();
				params.put("username", username);
				params.put("password", password);
			
				attemptLoginWithExistingCreds(this.getCurrentFocus(), params);
			}
		}

		
		Button loginButton = (Button) findViewById(R.id.loginButton);
	    loginButton.setOnClickListener(this);
	    Button createAccountButton = (Button) findViewById(R.id.createAccountButton);
	    createAccountButton.setOnClickListener(this);
	}
	
	protected void attemptLoginWithExistingCreds(View view, Map<String, String> params) {
		// Attempt to create a REST client and connect to the URI with the 
		// params specified.
		RestClient restclient = new RestClient();
		URI uri = ((LiveGatherApplication) this.getApplication()).getUri();

		String ret = restclient.sendJSONPost(uri, "/sessions/", params, false);

		if(ret != null && restclient.getResponse().getStatusLine().getStatusCode() != 401) {
			User user = new User();
			try {
				JSONObject json = restclient.getJson();
				
				if(json != null) {
					user.setUsername((String) json.get("username"));
					user.setEmail((String) json.get("email"));
					user.setPassword((String) json.get("password"));
					user.setLocation((String) json.get("location"));
				
					((LiveGatherApplication) this.getApplication()).setUser(user);
				} else {
					return;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Intent landingIntent = new Intent(view.getContext(), LandingActivity.class);
			startActivityForResult(landingIntent, 0);
		}
	}	

	public void onClick(View view)
	{
		switch(view.getId())
		{
		case R.id.loginButton:
			Intent loginIntent = new Intent(view.getContext(), LoginActivity.class);
			startActivityForResult(loginIntent, 0);
			break;
		case R.id.createAccountButton:
			Intent createAccountIntent = new Intent(view.getContext(), CreateAccountActivity.class);
			startActivityForResult(createAccountIntent, 0);
			break;
		}	
	}

}
