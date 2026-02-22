package fr.slypy.slymyjge.animations.framed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import com.madgag.gif.fmsware.GifDecoder;

import fr.slypy.slymyjge.graphics.Texture;
import fr.slypy.slymyjge.graphics.shape.Shape;
import fr.slypy.slymyjge.graphics.shape.TexturedRectangle;


public class Animation {
	
	private List<AnimationFrame> frames = new ArrayList<>();
	
	private double lastUpdate = 0;
	private double currentTime;
	
	private int frame = 0;
	
	private boolean playing;

	public Animation(AnimationFrame... frames) {
		
		this.frames.addAll(Arrays.asList(frames));
		
	}
	
	public void step(double delta) {

		currentTime += delta;
			
		if(lastUpdate == 0) {
					
			lastUpdate = currentTime;
			frame = 0;
					
		} else {
					
			if(playing) {
				
				double alpha = currentTime - lastUpdate;
					
				if(alpha > 1.0f / frames.get(frame).getSpeed()) {
						
					frame = (frame + (int) Math.floor(alpha / (1.0f / frames.get(frame).getSpeed()))) % frames.size();
					currentTime = currentTime - (alpha % (1.0f / frames.get(frame).getSpeed()));
						
				}
				
			} else {
					
				lastUpdate = currentTime;
					
			}
				
		}
		
	}
	
	public Shape getShape(float x, float y, int w, int h) {
			
		if(frames.isEmpty()) {
			
			return new TexturedRectangle(x, y, w, h);
			
		}
		
		return frames.get(frame).getShape(new Vector2f(x, y), new Vector2f(w, h));
		
	}
	
	public Shape getShape(Vector2f position, Vector2f size) {
			
		if(frames.isEmpty()) {
			
			return new TexturedRectangle(position.getX(), position.getY(), size.getX(), size.getY());
			
		}
		
		return frames.get(frame).getShape(position, size);
		
	}
	
	public void setSpeed(float speed) {
		
		for(AnimationFrame f : frames) {
			
			f.setSpeed(speed);
			
		}
		
	}
	
	public float getSpeed(int frame) {
		
		if(frame >= 0 && frame < frames.size()) {
			
			return frames.get(frame).getSpeed();
			
		}
		
		return -1;
		
	}
	
	public float getSpeed() {
		
		return frames.get(frame).getSpeed();
		
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
	
	public void setCurrentFrame(int frame) {
		
		long rounds = Math.floorDiv(frame, frames.size());
		
		frame -= rounds * (frames.size());
			
		this.frame = frame;
		
	}
	
	public int getCurrentFrame() {
		
		return frame;
		
	}
	
	public void setFrame(int index, AnimationFrame frame) {
		
		this.frames.set(index, frame);
		
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
			
			animationFrames[i] = new TexturedQuadAnimationFrame(Texture.loadTexture(decoder.getFrame(i)), (float) (1000000000D / (decoder.getDelay(i) * 10000000)));
			
		}
		
		return animationFrames;
		
	}
	
}