package fr.slypy.slymyjge.network;

public class Network {

	public static boolean checkClientRegistering = false;
	public static NetworkClientRegistering clientRegistering = null;
	
	public void removeClientRegistering() {
		
		clientRegistering = null;
		checkClientRegistering = false;
		
	}
	
	public void addClientRegistering(NetworkClientRegistering registering) {
		
		clientRegistering = registering;
		checkClientRegistering = true;
		
	}
	
	public static boolean hasClientRegistering() {
		
		return checkClientRegistering;
		
	}
	
	public static NetworkClientRegistering getClientRegistering() {
		
		return clientRegistering;
		
	}
	
}
