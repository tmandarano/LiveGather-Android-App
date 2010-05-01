package com.livegather.android.activity;

import com.livegather.android.LiveGatherApplication;
import com.livegather.android.R;
import com.livegather.android.model.User;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class LandingActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.createaccount);

	    TextView output = (TextView) findViewById(R.id.Username);

	    StringBuffer sb = new StringBuffer();
	    
		User user = ((LiveGatherApplication) this.getApplication()).getUser();
	    sb.append("Fullname: ").append(user.getFullname());
	    sb.append("Password: ").append(user.getPassword());
	    sb.append("Email: ").append(user.getEmail());
	    sb.append("Location: ").append(user.getLocation());

	    output.setText(sb);
	}
}
