package com.triangleApp.util;

public class DirectoryContact {

	private String name, phone, email;

	public DirectoryContact() {
		name = phone = email = "";
	}
	
	public DirectoryContact(String NAME, String PHONE, String EMAIL){
		name = NAME;
		phone = PHONE; 
		email = EMAIL;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
