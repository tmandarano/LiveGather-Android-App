package com.livegather.android.model;

public class User {
	private String username;
	private String email;
	private String password;
	private String dateOfBirth;
	private String location;

	public void setUsername(String username) {
		this.username = username;
	}
	public String getUsername() {
		return username;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmail() {
		return email;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword() {
		return password;
	}
	
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	public String getLocation() {
		return location;
	}
}
