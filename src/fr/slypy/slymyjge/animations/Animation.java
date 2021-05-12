package fr.slypy.slymyjge.animations;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Animation {
	
	private List<AnimationFrame> frames = new ArrayList<AnimationFrame>();
	
	private long lastUpdate = 0;
	private long speed = 200000000;
	
	private int frame = 0;

	public Animation(AnimationFrame[] frames) {
		
		this.frames.addAll(Arrays.asList(frames));
		
	}
	
	public void render(float x, float y, int w, int h, Color color) {
			
		if(frames.size() < 1) {
			
			return;
			
		} else if(frame >= frames.size()) {
			
			frame = 0;
			
		}
		
		if(lastUpdate == 0) {
				
			lastUpdate = System.nanoTime();
			frame = 0;
				
		} else {
				
			long alpha = System.nanoTime() - lastUpdate;
				
			if(alpha > speed) {
				
				long changes = Math.floorDiv(alpha, speed);
					
				long newFrame = frame + changes;
					
				long rounds = Math.floorDiv(newFrame, frames.size());
					
				newFrame -= rounds * (frames.size());
					
				frame = (int) newFrame;
					
				lastUpdate = System.nanoTime();
				
			}
				
		}
		
		frames.get(frame).render(x, y, w, h, color);
		
	}
	
	public void render(float x, float y, int w, int h) {
		
		render(x, y, w, h, Color.white);
		
	}
	
	public void setSpeed(float speed) {
		
		this.speed = (long) (speed * 1000000000L);
		
	}
	
	public float getSpeed() {
		
		return (float) speed / 1000000000F;
		
	}
	
	public List<AnimationFrame> getFrames() {
		
		return frames;
		
	}
	
	public void setFrames(AnimationFrame[] frames) {
		
		this.frames = Arrays.asList(frames);
		
	}
	
	public void addFrame(AnimationFrame frame) {
		
		frames.add(frame);
		
	}
	
	public void addFrames(AnimationFrame[] frames) {
		
		this.frames.addAll(Arrays.asList(frames));
		
	}
	
	public void setFrame(int frame) {
		
		long rounds = Math.floorDiv(frame, frames.size());
		
		frame -= rounds * (frames.size());
			
		this.frame = (int) frame;
		
	}
	
}
