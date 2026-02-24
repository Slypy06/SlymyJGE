package fr.slypy.slymyjge.animations.dynamic;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import fr.slypy.slymyjge.graphics.shape.Shape;
import fr.slypy.slymyjge.utils.SimpleEntry;

public class Animator {

	private float t = 0;
	private List<SimpleEntry<BiFunction<Shape, ?, Shape>, AnimationTrack<?>>> tracks = new ArrayList<>();
	private float max = 0;
	private boolean loop = false;
	
	private float speed = 1;
	
	public <T> void addTrack(AnimationTrack<T> track, BiFunction<Shape, T, Shape> applicator) {
		
		tracks.add(new SimpleEntry<BiFunction<Shape, ?, Shape>, AnimationTrack<?>>(applicator, track));
		
		this.max = Math.max(max, track.getMaximum());
		
	}
	
	public Shape apply(Shape s, float delay) {
		
		float time = t + speed*delay;
		
		if(loop) {
			
			time = time%max > 0 ? time%max : (time%max)+max;
			
		} else if(time > max) {
			
			time = max;
			
		} else if(time < 0) {
			
			time = 0;
			
		}
		
		Shape ret = s;
		
		for(SimpleEntry<BiFunction<Shape, ?, Shape>, AnimationTrack<?>> entry : tracks) {
			
	        @SuppressWarnings("unchecked")
	        BiFunction<Shape, Object, Shape> func = (BiFunction<Shape, Object, Shape>) entry.getKey();
	        @SuppressWarnings("unchecked")
			AnimationTrack<Object> track = (AnimationTrack<Object>) entry.getValue();

	        ret = func.apply(ret, track.get(time));
			
		}
		
		return ret;
		
	}
	
	public Shape apply(Shape s) {
		
		return apply(s, 0);
		
	}
	
	public void step(float alpha) {
		
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
	
	public void reset() {
		
		t = 0;
		
	}
	
	public boolean isFinished() {
		
		return !loop && ((speed > 0 && t == max) || (speed < 0 && t == 0) || speed == 0);
		
	}

}
