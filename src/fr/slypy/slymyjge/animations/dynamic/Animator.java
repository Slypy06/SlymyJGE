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
	
	public Animator(Shape s) {
		
		this.s = s;
		
	}
	
	public <T> void addTrack(AnimationTrack<T> track, BiFunction<Shape, T, Shape> applicator) {
		
		tracks.put(applicator, track);
		
		this.max = Math.max(max, track.getMaximum());
		
	}
	
	public Shape apply() {
		
		Shape ret = s;
		
		for(Entry<BiFunction<Shape, ?, Shape>, AnimationTrack<?>> entry : tracks.entrySet()) {
			
	        @SuppressWarnings("unchecked")
	        BiFunction<Shape, Object, Shape> func = (BiFunction<Shape, Object, Shape>) entry.getKey();
	        @SuppressWarnings("unchecked")
			AnimationTrack<Object> track = (AnimationTrack<Object>) entry.getValue();

	        ret = func.apply(ret, track.get(t));
			
		}
		
		return ret;
		
	}
	
	public void update(float alpha) {
		
		t += alpha;
		
		if(loop) {
			
			t = t % max;
			
		} else if(t > max) {
			
			t = max;
			
		}
		
	}
	
	public void setLooping(boolean loop) {
		
		this.loop = loop;
		
	}

}
