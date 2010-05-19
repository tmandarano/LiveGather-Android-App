package com.livegather.android.activity;

import com.livegather.android.LiveGatherApplication;
import com.livegather.android.R;
import com.livegather.android.model.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class LandingActivity extends Activity implements OnClickListener  {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.landing);

	    TextView output = (TextView) findViewById(R.id.status);

	    StringBuffer sb = new StringBuffer();
	    
		User user = ((LiveGatherApplication) this.getApplication()).getUser();
	    sb.append("Username: ").append(user.getUsername()).append("\n");
	    sb.append("Password: ").append(user.getPassword()).append("\n");
	    sb.append("Email: ").append(user.getEmail()).append("\n");
	    sb.append("Location: ").append(user.getLocation());

	    output.setText(sb);

	    setTitle("LiveGather Android - " + user.getUsername());
	    
	    Button uploadPicture = (Button) findViewById(R.id.uploadPicture);
	    uploadPicture.setOnClickListener(this);
	}

	public void onClick(View view)
	{
		Intent uploadPhotoIntent = new Intent(view.getContext(), UploadPhotoActivity.class);
		startActivityForResult(uploadPhotoIntent, 0);
	}
}
