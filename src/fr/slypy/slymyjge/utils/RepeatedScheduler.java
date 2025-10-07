package fr.slypy.slymyjge.utils;


public class RepeatedScheduler {
	
	private long lastUpdate;
	private long timeout;
	
	public RepeatedScheduler(double seconds) {
		
		timeout = (long) (seconds * 1000000000L);
		lastUpdate = System.nanoTime();
		
	}
	
	public boolean isReady() {
		
		if(System.nanoTime() - lastUpdate >= timeout) {
			
			lastUpdate = System.nanoTime();
			return true;
			
		}
		
		return false;
		
	}

}
