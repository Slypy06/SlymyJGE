package fr.slypy.slymyjge.animations;

import java.awt.Color;

public abstract class AnimationFrame {

	public long speed = 200000000;
	
	public abstract void render(float x, float y, int w, int h, Color c);
	
	public float getSpeed() {
		
		return (long) (1000000000D / speed);
		
	}
	
	public void setSpeed(float speed) {
		
		this.speed = (long) (1000000000D / speed);
		
	}
	
}
