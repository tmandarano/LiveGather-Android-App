package com.livegather.android.activity;

import com.livegather.android.R;

import android.app.Activity;
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
	    
	    Button loginButton = (Button) findViewById(R.id.loginButton);
	    loginButton.setOnClickListener(this);
	    Button createAccountButton = (Button) findViewById(R.id.createAccountButton);
	    createAccountButton.setOnClickListener(this);
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
