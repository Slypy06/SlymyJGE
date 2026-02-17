package fr.slypy.slymyjge.utils;

public abstract class UpdateLoop {

	private int ups;
	private int upsCap;
	private Thread t;
	private Synchronizer sync;
	
	public UpdateLoop(int cap) {
		
		this.upsCap = cap;
		this.sync = new Synchronizer(cap);
		this.t = new Thread() {
			
			@Override
			public void run() {
				
				RepeatedScheduler tpsUpdateScheduler = new RepeatedScheduler(1);
				long ups = 0;

				while(!Thread.interrupted()) {
						
					ups++;
						
					UpdateLoop.this.update(sync.getDelta());
						
					if (tpsUpdateScheduler.isReady()) {
							
						UpdateLoop.this.setUps((int) ups);
							
						ups = 0;
	
					}
					
					sync.sync();
					
				}
				
			}
			
		};
		
	}
	
	public abstract void update(double alpha);
	
	public void start() {
		
		if(!t.isAlive()) {
			
			sync.start();
			t.start();
			
		}
		
	}
	
	public int getUps() {
		
		return ups;
		
	}
	
	protected void setUps(int ups) {
		
		this.ups = ups;
		
	}
	
	public int getUpsCap() {
		
		return upsCap;
		
	}
	
	public Thread getThread() {
		
		return t;
		
	}
	
}
