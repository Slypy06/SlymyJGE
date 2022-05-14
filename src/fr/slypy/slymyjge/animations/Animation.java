package fr.slypy.slymyjge.animations;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.madgag.gif.fmsware.GifDecoder;

import fr.slypy.slymyjge.graphics.Texture;


public class Animation {
	
	private List<AnimationFrame> frames = new ArrayList<AnimationFrame>();
	
	private long lastUpdate = 0;
	
	private int frame = 0;
	
	private boolean playing;

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
				
			if(playing) {
			
				long alpha = System.nanoTime() - lastUpdate;
				
				if(alpha > frames.get(frame).speed) {
					
					while(alpha > frames.get(frame).speed) {
						
						alpha -= frames.get(frame).speed;
						
						if(frame < frames.size() - 1) {
							
							frame++;
							
						} else {
							
							frame = 0;
							
						}
						
					}
						
					lastUpdate = System.nanoTime();
					
				}
			
			} else {
				
				lastUpdate = System.nanoTime();
				
			}
			
		}
		
		frames.get(frame).render(x, y, w, h, color);
		
	}
	
	public void render(float x, float y, int w, int h) {
		
		render(x, y, w, h, Color.white);
		
	}
	
	public void setSpeed(float speed) {
		
		for(AnimationFrame frame : frames) {
			
			frame.setSpeed(speed);
			
		}
		
	}
	
	public float getSpeed(int frame) {
		
		if(frame >= 0 && frame < frames.size()) {
			
			return frames.get(frame).getSpeed();
			
		}
		
		return -1;
		
	}
	
	public float getSpeed() {
		
		return frames.get(0).getSpeed();
		
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

	public boolean isPlaying() {
		
		return playing;
		
	}

	public void setPlaying(boolean playing) {
		
		this.playing = playing;
		
	}
	
	public void resetAnimation() {
		
		lastUpdate = 0;
		
	}
	
	public static AnimationFrame[] loadAnimationFromGif(String gif) {
		
		GifDecoder decoder = new GifDecoder();
		decoder.read(Animation.class.getResourceAsStream("/" + gif));
		
		int frames = decoder.getFrameCount();
		
		AnimationFrame[] animationFrames = new AnimationFrame[frames];
		
		for (int i = 0; i < frames; i++) {
			
			animationFrames[i] = new TexturedAnimationFrame(0, 0, 1, 1, Texture.loadTexture(decoder.getFrame(i)), (float) (1000000000D / (float) (decoder.getDelay(i) * 1000000)));
			
		}
		
		return animationFrames;
		
	}
	
}
