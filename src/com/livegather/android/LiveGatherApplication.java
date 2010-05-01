package com.livegather.android;

import com.livegather.android.model.User;

import android.app.Application;

public class LiveGatherApplication extends Application {
	private User user;

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public String getUrl() {
		return "http://192.168.1.111";
	}

}
