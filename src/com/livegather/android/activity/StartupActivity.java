package com.livegather.android.activity;

import com.livegather.android.R;
import com.livegather.android.R.id;
import com.livegather.android.R.layout;

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
	    
	    Button loginButton = (Button) findViewById(R.id.LoginButton);
	    loginButton.setOnClickListener(this);
	    Button createAccountButton = (Button) findViewById(R.id.CreateAccountButton);
	    createAccountButton.setOnClickListener(this);
	}
	
	public void onClick(View view)
	{
		switch(view.getId())
		{
		case R.id.LoginButton:
			Intent loginIntent = new Intent(view.getContext(), LoginActivity.class);
			startActivityForResult(loginIntent, 0);
			break;
		case R.id.CreateAccountButton:
			Intent createAccountIntent = new Intent(view.getContext(), CreateAccountActivity.class);
			startActivityForResult(createAccountIntent, 0);
			break;
		}	
	}

}
