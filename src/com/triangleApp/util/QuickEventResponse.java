package com.triangleApp.util;

public class QuickEventResponse {

	private String name, going;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGoing() {
		return going;
	}

	public void setGoing(String going) {
		String response;
		if(going.equals("1"))
			response = "yes";
		else 
			response = "no";
		
		this.going = "Going: " + response;
	}
}
