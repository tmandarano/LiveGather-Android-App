package com.livegather.android.activity;

import java.net.URI;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.livegather.android.LiveGatherApplication;
import com.livegather.android.R;
import com.livegather.android.model.RestClient;
import com.livegather.android.model.User;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CreateAccountActivity extends AbstractRestActivity implements OnClickListener {
	private TextView dateDisplay;
	private Button pickDate;
	private int year;
	private int month;
	private int day;


	static final int DATE_DIALOG_ID = 0;
	
	private DatePickerDialog.OnDateSetListener dateSetListener = 
		new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(android.widget.DatePicker view, int yearOf,
					int monthOfYear, int dayOfMonth) {
				year = yearOf;
				month = monthOfYear;
				day = dayOfMonth;
				updateDisplay();
			}
	};

	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.createaccount);
	
	    Button submitButton = (Button) findViewById(R.id.submitButton);
	    submitButton.setOnClickListener(this);

		dateDisplay = (TextView) findViewById(R.id.dateDisplay);
		pickDate = (Button) findViewById(R.id.pickDate);
		
		pickDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});

		final Calendar cal = Calendar.getInstance();
		this.year = cal.get(Calendar.YEAR);
		this.month = cal.get(Calendar.MONTH);
		this.day = cal.get(Calendar.DAY_OF_MONTH);
		
		updateDisplay();
	}

	public void onClick(View view)
	{
		Map<String, String> params = new HashMap<String, String>();
		EditText username = (EditText) findViewById(R.id.username);
		params.put("username", username.getText().toString());
		EditText password = (EditText) findViewById(R.id.password);
		params.put("password", password.getText().toString());
		EditText email = (EditText) findViewById(R.id.email);
		params.put("email", email.getText().toString());
		TextView birthday = (TextView) findViewById(R.id.dateDisplay);
		params.put("date_of_birth", birthday.getText().toString());
		EditText location = (EditText) findViewById(R.id.location);
		params.put("location", location.getText().toString());
	
		contactWebService(view, params);
	}

	protected void contactWebService(View view, Map<String, String> params) {
		RestClient restclient = new RestClient();
		URI uri = ((LiveGatherApplication) this.getApplication()).getUri();

		String ret = restclient.sendJSONPost(uri, "/users/create", params);

		if(ret != null && ! ret.equals("")) {
			JSONObject json = restclient.getJson();
			
			User user = new User();
			try {
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
	}	

	private void updateDisplay() {
		StringBuilder datesb = new StringBuilder();
		datesb.append(this.month + 1).append(".");
		datesb.append(this.day).append(".");
		datesb.append(this.year);

		this.dateDisplay.setText(datesb.toString());
	}

	@Override
	protected Dialog onCreateDialog(int id) {
	    switch (id) {
	    case DATE_DIALOG_ID:
	        return new DatePickerDialog(this,
	                    this.dateSetListener,
	                    this.year, this.month, this.day);
	    }
	    return null;
	}
}
