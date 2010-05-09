package com.livegather.android.view;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
	private SurfaceHolder holder;
	private Camera camera = null;

	public CameraSurfaceView(Context context) {
		super(context);
		this.holder = getHolder();
		this.holder.addCallback(this);
		this.holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
	
	public void surfaceCreated(SurfaceHolder holder) {
		this.camera = Camera.open();
		try {
			this.camera.setPreviewDisplay(this.holder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void surfaceChanged(SurfaceHolder holder, 
		int format, int width, int height) {
		Camera.Parameters params = camera.getParameters();
//		params.setPreviewSize(width, height);
		camera.setParameters(params);
		camera.startPreview();
	}
	
	public void surfaceDestroyed(SurfaceHolder holder) {
		camera.stopPreview();
		camera.release();
		camera = null;
	}
	
	public boolean capture(Camera.PictureCallback jpegHandler) {
		if(camera != null) {
			camera.takePicture(null, null, jpegHandler);
			return true;
		} else {
			return false;
		}
	}
}
