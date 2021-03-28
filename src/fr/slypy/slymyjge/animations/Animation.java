package fr.slypy.slymyjge.animations;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.slypy.slymyjge.graphics.Renderer;


public class Animation {
	
	private List<AnimationFrame> frames = new ArrayList<AnimationFrame>();
	
	private long lastUpdate = 0;
	private long speed = 200000000;
	
	private int xo = 0;

	protected Animation(AnimationFrame[] frames) {
		
		this.frames.addAll(Arrays.asList(frames));
		
	}
	
	protected void render(float x, float y, int w, int h, Color color) {
			
		if(frames.size() < 1) {
			
			return;
			
		} else if(xo >= frames.size()) {
			
			xo = 0;
			
		}
		
		if(lastUpdate == 0) {
				
			lastUpdate = System.nanoTime();
			xo = 0;
				
		} else {
				
			long alpha = System.nanoTime() - lastUpdate;
				
			if(alpha > speed) {
				
				long changes = Math.floorDiv(alpha, speed);
					
				long totalXo = xo + changes;
					
				long rounds = Math.floorDiv(totalXo, frames.size());
					
				totalXo -= rounds * (frames.size());
					
				xo = (int) totalXo;
					
				lastUpdate = System.nanoTime();
				
			}
				
		}
		
		Renderer.renderEntity(x, y, w, h, color, frames.get(xo).getXo(), frames.get(xo).getYo(), frames.get(xo).getMaxXo(), frames.get(xo).getMaxYo(), frames.get(xo).getTexture());
		
	}
	
	protected void render(float x, float y, int w, int h) {
		
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
	
}
