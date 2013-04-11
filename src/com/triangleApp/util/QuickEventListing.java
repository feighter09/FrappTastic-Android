package com.triangleApp.util;

public class QuickEventListing {

	private String title, creator, date, time, when;
	
	public QuickEventListing() {
		title = creator = when = "";
	}
	
	public QuickEventListing(String TITLE, String CREATOR, String WHEN){
		title = TITLE;
		creator = CREATOR;
		when = WHEN;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = "Title: " + title;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = "Event creator: " + creator;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	public String getWhen() {
		return when;
	}

	public void setWhen(String when) {
		this.when = "When: " + when;
	}
}
