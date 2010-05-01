package com.livegather.android.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.livegather.android.LiveGatherApplication;
import com.livegather.android.R;
import com.livegather.android.exceptions.InvalidRequestException;
import com.livegather.android.model.RestClient;
import com.livegather.android.model.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class CreateAccountActivity extends Activity implements OnClickListener 
{
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.createaccount);
	
	    Button button = (Button) findViewById(R.id.SubmitButton);
	    button.setOnClickListener(this);
	}

	public void onClick(View view)
	{
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		EditText username = (EditText) findViewById(R.id.Username);
		params.add(new BasicNameValuePair("fullname", username.getText().toString()));
		EditText password = (EditText) findViewById(R.id.Password);
		params.add(new BasicNameValuePair("password", password.getText().toString()));
		DatePicker dateOfBirth = (DatePicker) findViewById(R.id.DateOfBirthPicker);
		params.add(new BasicNameValuePair("date_of_birth", dateOfBirth.toString()));
		EditText email = (EditText) findViewById(R.id.Email);
		params.add(new BasicNameValuePair("email", email.getText().toString()));
		EditText location = (EditText) findViewById(R.id.Location);
		params.add(new BasicNameValuePair("location", location.getText().toString()));

		contactWebService(view, params);
	}

	private void contactWebService(View view, List<NameValuePair> params) {
		try {
			String url = ((LiveGatherApplication) this.getApplication()).getUrl() + "/users/create";
			RestClient restclient = new RestClient();
			restclient.connect(url, params, RestClient.POST_REQUEST_METHOD);

			User user = new User();
			
			if(restclient.getResponse().getStatusLine().getStatusCode() == 200) {
				JSONObject json = restclient.getJson();
				
				try {
					user.setFullname((String) json.get("fullname"));
					user.setEmail((String) json.get("email"));
					user.setPassword((String) json.get("password"));
					user.setLocation((String) json.get("location"));
					
					((LiveGatherApplication) this.getApplication()).setUser(user);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			Intent landingIntent = new Intent(view.getContext(), LandingActivity.class);
			startActivityForResult(landingIntent, 0);
		} catch (InvalidRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
}
