package com.livegather.android.activity;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.livegather.android.LiveGatherApplication;
import com.livegather.android.R;
import com.livegather.android.exceptions.FileUtilities;
import com.livegather.android.model.RestClient;
import com.livegather.android.model.User;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AbstractRestActivity implements OnClickListener 
{
	private TextView status;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.login);
	
		status = (TextView) findViewById(R.id.status);
	    Button button = (Button) findViewById(R.id.submitButton);
	    button.setOnClickListener(this);
	}

	public void onClick(View view)
	{
		Map<String, String> params = new HashMap<String, String>();
		
		EditText usernameEdit = (EditText) findViewById(R.id.username);
		String username = usernameEdit.getText().toString();
		params.put("username", username);

		EditText passwordEdit = (EditText) findViewById(R.id.password);
		String password = passwordEdit.getText().toString();
		params.put("password", FileUtilities.getMd5Hash(password));

		contactWebService(view, params);
	}
	
	protected void contactWebService(View view, Map<String, String> params) {
		// Attempt to create a REST client and connect to the URI with the 
		// params specified.
		RestClient restclient = new RestClient();
		URI uri = ((LiveGatherApplication) this.getApplication()).getUri();

		String ret = restclient.sendJSONPost(uri, "/sessions/", params, false);

		if(ret != null && restclient.getResponse().getStatusLine().getStatusCode() != 401) {
			User user = new User();
			try {
				JSONObject json = restclient.getJson();
				user.setUsername((String) json.get("username"));
				user.setEmail((String) json.get("email"));
				user.setPassword((String) json.get("password"));
				user.setLocation((String) json.get("location"));
				
				((LiveGatherApplication) this.getApplication()).setUser(user);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Editor e = this.getPreferences(Context.MODE_PRIVATE).edit();
			e.putString("username", user.getUsername());
			e.putString("password", user.getPassword());
			e.commit();			

			Intent landingIntent = new Intent(view.getContext(), LandingActivity.class);
			startActivityForResult(landingIntent, 0);
		}
		else if(restclient.getResponse().getStatusLine().getStatusCode() == 401) {
			status.setText("User not found, please try again");
		}
	}	
}
