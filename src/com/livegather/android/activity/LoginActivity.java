package com.livegather.android.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.livegather.android.LiveGatherApplication;
import com.livegather.android.R;
import com.livegather.android.R.id;
import com.livegather.android.R.layout;
import com.livegather.android.exceptions.InvalidRequestException;
import com.livegather.android.model.RestClient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class LoginActivity extends Activity implements OnClickListener 
{
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.login);
	
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

		try {
			RestClient restclient = new RestClient();
			String url = ((LiveGatherApplication) this.getApplication()).getUrl() + "/sessions/create";
			restclient.connect(url, params, RestClient.POST_REQUEST_METHOD, username.getText().toString(), password.getText().toString());
			
			if(restclient.getResponse().getStatusLine().getStatusCode() == 401) {
				restclient.connect(url, params, RestClient.POST_REQUEST_METHOD, username.getText().toString(), password.getText().toString());
			}

			if(restclient.getResponse().getStatusLine().getStatusCode() == 200) {
				Intent landingIntent = new Intent(view.getContext(), LandingActivity.class);
				startActivityForResult(landingIntent, 0);
			}
		} catch (InvalidRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
}
