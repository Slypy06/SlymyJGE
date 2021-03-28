package fr.slypy.slymyjge.animations;

import java.awt.Color;

public class AdvencedAnimation extends Animation {

	public AdvencedAnimation() {
		
		super(new AnimationFrame[] {});

	}
	
	public void draw(float x, float y, int w, int h, Color color) {
		
		render(x, y, w, h, color);
		
	}
	
	public void draw(float x, float y, int w, int h) {
		
		render(x, y, w, h);
		
	}
	
	public void resetFrames() {
		
		setFrames(new AnimationFrame[] {});
		
	}

}
