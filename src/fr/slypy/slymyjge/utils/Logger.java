package fr.slypy.slymyjge.utils;

public class Logger {

	public static void log(String text) {
		
		System.out.println("[" + Date.getTime() + "] " + text);
		
	}
	
	public static void warn(String text) {
		
		System.out.println("\u001B[33m" + "[" + Date.getTime() + "] (WARN)" + text + "\u001B[0m");
		
		
	}
	
}
