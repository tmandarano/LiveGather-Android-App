package com.livegather.android.activity;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.livegather.android.LiveGatherApplication;
import com.livegather.android.R;
import com.livegather.android.model.RestClient;
import com.livegather.android.model.User;
import com.livegather.android.view.CameraSurfaceView;


public class UploadPhotoActivity extends Activity implements OnClickListener {
    final private static String STILL_IMAGE_FILE = "capture.jpg";

	public void onClick(View view) {
		Map<String, String> params = new HashMap<String, String>();
		EditText caption = (EditText) findViewById(R.id.caption);
		params.put("caption", caption.getText().toString());
		EditText tags = (EditText) findViewById(R.id.tags);
		params.put("tags", tags.getText().toString());
		EditText comments = (EditText) findViewById(R.id.comments);
		params.put("comments", comments.getText().toString());
	
		contactWebService(view, params);
	}

	protected void contactWebService(View view, Map<String, String> params) {
		RestClient restclient = new RestClient();
		URI uri = ((LiveGatherApplication) this.getApplication()).getUri();
		User user = ((LiveGatherApplication) this.getApplication()).getUser();

		params.put("username", user.getUsername());
		params.put("password", user.getPassword());
		
		String ret = restclient.sendJSONPost(uri, "/photos/", params, false);

		
		
//		if(ret != null && ! ret.equals("")) {
//			JSONObject json = restclient.getJson();
//			
//			User user = new User();
//			try {
//				user.setUsername((String) json.get("username"));
//				user.setEmail((String) json.get("email"));
//				user.setPassword((String) json.get("password"));
//				user.setLocation((String) json.get("location"));
//				
//				((LiveGatherApplication) this.getApplication()).setUser(user);
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//			Editor e = this.getPreferences(Context.MODE_PRIVATE).edit();
//			e.putString("username", user.getUsername());
//			e.putString("password", user.getPassword());
//			e.commit();			
//
//			Intent landingIntent = new Intent(view.getContext(), LandingActivity.class);
//			startActivityForResult(landingIntent, 0);
//		}
	}	

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.uploadphoto);
	    
	    final CameraSurfaceView cameraView = new CameraSurfaceView(getApplicationContext());
	    FrameLayout frame = (FrameLayout) findViewById(R.id.frame);
	    frame.addView(cameraView);

	    
	    Button submitButton = (Button) findViewById(R.id.submitButton);
	    submitButton.setOnClickListener(this);

	    Button capture = (Button) findViewById(R.id.capture);
	    capture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cameraView.capture(new Camera.PictureCallback() {

                    public void onPictureTaken(byte[] data, Camera camera) {
                        Log.v("Still", "Image data received from camera");
                        try {
                            Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
                            String fileUrl = MediaStore.Images.Media.insertImage(getContentResolver(), bm, "Camera Still Image", "Camera Pic Sample App Took");

                            if (fileUrl == null) {
                                Log.d("Still", "Image Insert failed");
                                return;
                            } else {
                                // Force the media scanner to go. Not required,
                                // but good for testing.
                                Uri picUri = Uri.parse(fileUrl);
                                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, picUri));
                            }
                        } catch (Exception e) {
                            Log.e("Still", "Error writing file", e);
                        }
                    }
                });
            }
        });             
	}
}