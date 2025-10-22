package fr.slypy.slymyjge.utils;

public class Synchronizer {

	private int rate;         // ticks per second (e.g. 60, 100, etc.)
    private long delta;   // time per tick in ns
    private long lastTickTime;       // last tick timestamp (ns)
    private long elapsed;     // time since last tick (ns)
    public static final long NANOS_PER_MS = 1_000_000L;
    public static final long NANOS_PER_S = 1_000_000_000L;
    private static final long OVERSHOOT_CEILING = 10*NANOS_PER_MS; //if lastTickTime drift by more than [10ms] then reupdate it correctly with nano time
 
    public Synchronizer(int rate) {
    	
        setRate(rate);
        this.elapsed = 0;
        
    }
    
    public void start() {
    	
    	lastTickTime = System.nanoTime() - (long) delta;
    	
    }

    public void sync() {
        
        elapsed = System.nanoTime() - lastTickTime;

        if (elapsed < delta) {
        	
            long remaining = delta - elapsed;
            long waitMs = (long) (remaining / NANOS_PER_MS);

            if (waitMs > 5) {

                try {

					Thread.sleep(waitMs - 3); //we busy wait the last 3ms to be accurate

				} catch (InterruptedException e) {
					
					Thread.currentThread().interrupt();
					return;
					
				}
                
            }
            
            while (System.nanoTime() - lastTickTime < delta) {
            	
                Thread.onSpinWait();
                
            }
            
            elapsed = System.nanoTime() - lastTickTime;
            
        }

        lastTickTime += delta;
        
        if(lastTickTime + OVERSHOOT_CEILING < System.nanoTime()) {
        	
        	lastTickTime = System.nanoTime();
        	
        }
        
    }


    public double getDelta() {
    	
        return elapsed / (double) NANOS_PER_S;
        
    }

    public void setRate(int rate) {
    	
        this.rate = rate;
        this.delta = (long) ((1.0D / rate) * NANOS_PER_S);
        
    }

    public int getRate() {
    	
        return rate;
        
    }
	
}
