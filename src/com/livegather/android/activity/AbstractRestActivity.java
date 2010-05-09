package com.livegather.android.activity;

import java.util.Map;

import android.app.Activity;
import android.view.View;


public abstract class AbstractRestActivity extends Activity {
	protected abstract void contactWebService(View view, Map<String, String> params);
}
