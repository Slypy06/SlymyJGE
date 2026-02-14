package fr.slypy.slymyjge.animations.dynamic;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiFunction;

import fr.slypy.slymyjge.graphics.shape.Shape;

public class Animator {
	
	private final Shape s;
	private float t = 0;
	private Map<BiFunction<Shape, ?, Shape>, AnimationTrack<?>> tracks = new HashMap<>();
	private float max = 0;
	private boolean loop = false;
	
	private float speed = 1;
	
	public Animator(Shape s) {
		
		this.s = s;
		
	}
	
	public <T> void addTrack(AnimationTrack<T> track, BiFunction<Shape, T, Shape> applicator) {
		
		tracks.put(applicator, track);
		
		this.max = Math.max(max, track.getMaximum());
		
	}
	
	public Shape apply() {
		
		float snapshot = t;
		
		Shape ret = s;
		
		for(Entry<BiFunction<Shape, ?, Shape>, AnimationTrack<?>> entry : tracks.entrySet()) {
			
	        @SuppressWarnings("unchecked")
	        BiFunction<Shape, Object, Shape> func = (BiFunction<Shape, Object, Shape>) entry.getKey();
	        @SuppressWarnings("unchecked")
			AnimationTrack<Object> track = (AnimationTrack<Object>) entry.getValue();

	        ret = func.apply(ret, track.get(snapshot));
			
		}
		
		return ret;
		
	}
	
	public void update(float alpha) {
		
		float time = t + speed*alpha;
		
		if(loop) {
			
			time = time%max > 0 ? time%max : (time%max)+max;
			
		} else if(time > max) {
			
			time = max;
			
		} else if(time < 0) {
			
			time = 0;
			
		}
		
		t = time;
		
	}
	
	public void setSpeed(float speed) {
		
		this.speed = speed;
		
	}
	
	public float getSpeed() {
		
		return speed;
		
	}
	
	public void reverse() {
		
		speed = -speed;
		
	}
	
	public void setLooping(boolean loop) {
		
		this.loop = loop;
		
	}
	
	public float getTime() {
		
		return t;
		
	}
	
	public float getDuration() {
		
		return max;
		
	}
	
	public void setDuration(float duration) {
		
		max = duration;
		
	}
	
	public boolean isFinished() {
		
		return !loop && ((speed > 0 && t == max) || (speed < 0 && t == 0) || speed == 0);
		
	}

}
