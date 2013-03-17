package com.triangleApp.util;

public enum QuickEventType {
	FOOD, BROHOOD, SPORTS, OTHER;
	
	@Override public String toString() {
		//only capitalize the first letter
		String s = super.toString();
		return s.substring(0, 1) + s.substring(1).toLowerCase();
	}
	
}
