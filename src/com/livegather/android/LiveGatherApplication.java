package com.livegather.android;

import java.net.URI;
import java.net.URISyntaxException;

import com.livegather.android.model.User;

import android.app.Application;

public class LiveGatherApplication extends Application {
	private User user;
	private URI uri;
	private String systemUsername;
	private String systemPassword;

	/**
	 * An application in Android is a singleton and is fired up when the app is. 
	 * As such we can initialize global variables to use once or 
	 * objects to be used throughout the application. We should do this 
	 * later with the RestClient.		 
	 * */
	public LiveGatherApplication() {
		// For now create a URI so we only need to do this in one location
		try {
// 			this.uri = new URI("http://192.168.1.111:80");
			this.uri = new URI("http://dev.livegather.com");
			this.systemUsername = "projc";
			this.systemPassword = "pr0j(";
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public URI getUri() {
		return this.uri;
	}

	public void setSystemUsername(String systemUsername) {
		this.systemUsername = systemUsername;
	}

	public String getSystemUsername() {
		return systemUsername;
	}

	public void setSystemPassword(String systemPassword) {
		this.systemPassword = systemPassword;
	}

	public String getSystemPassword() {
		return systemPassword;
	}

}
