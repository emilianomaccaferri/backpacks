package com.emilianomaccaferri.backpacks;

public class Utils {

	public static String colored(String s) {
		
		s = "&f[Backpacks]&r " + s;
		return new String(s.replaceAll("&", "§"));
		
	}
	
	public static String coloredNoPrefix(String s) {
		
		return new String(s.replaceAll("&", "§"));
		
	}
	
}
