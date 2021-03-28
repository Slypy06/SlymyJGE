package fr.slypy.slymyjge.animations;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import fr.slypy.slymyjge.graphics.Texture;

public class SimpleAnimation extends Animation {

	protected boolean anime = true;
	protected int maxXo;
	protected Texture texture;
	
	public SimpleAnimation(Texture texture, int maxXo) {
		
		super(new AnimationFrame[] {});
		this.maxXo = maxXo;
		this.texture = texture;

	}
	
	public void draw(float x, float y, int w, int h, int yo, Color color) {
		
		setAnimationFrame(yo);
		render(x, y, w, h, color);
		
	}
	
	public void draw(float x, float y, int w, int h, int yo) {
		
		setAnimationFrame(yo);
		render(x, y, w, h);
		
	}
	
	private void setAnimationFrame(int yo) {
		
		if(anime) {
		
			List<AnimationFrame> frames = new ArrayList<AnimationFrame>();
			
			for(int xo = 1; xo < maxXo; xo++) {
				
				frames.add(new AnimationFrame(xo, yo, maxXo, 4, texture));
				
			}
			
			setFrames(frames.toArray(new AnimationFrame[0]));
		
		} else {
			
			setFrames(new AnimationFrame[] {new AnimationFrame(0, yo, maxXo, 4, texture)});
			
		}
		
		
	}

	public boolean isAnime() {
		
		return anime;
		
	}

	public void setAnime(boolean anime) {
		
		this.anime = anime;
		
	}

	public int getMaxXo() {
		
		return maxXo;
		
	}

	public void setMaxXo(int maxXo) {
		
		this.maxXo = maxXo;
		
	}

	public Texture getTexture() {
		
		return texture;
		
	}

	public void setTexture(Texture texture) {
		
		this.texture = texture;
		
	}

}
