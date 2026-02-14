package fr.slypy.slymyjge.animations.dynamic;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import org.lwjgl.util.vector.Vector2f;
public class AnimationTrack<T> {

	private List<KeyFrame<T>> keyFrames = new ArrayList<>();
	private Interpolator<T> interpolator;
	private float max;
	
	public AnimationTrack(Interpolator<T> interpolator) {
		
		this.interpolator = interpolator;
		
	}
	
	public T get(float t) {
		
		if(t >= max)
			return keyFrames.get(keyFrames.size() - 1).getValue();
		
		KeyFrame<T> lastKf = keyFrames.get(0);
		
		for(KeyFrame<T> kf : keyFrames) {
			
			if(kf.getT() == t)
				return kf.getValue();
			
			if(kf.getT() >= t) {
				
				return interpolator.interpolate(lastKf.getValue(), kf.getValue(), (t-lastKf.getT()) / (kf.getT() - lastKf.getT()));
				
			}
			
			lastKf = kf;
			
		}
		
		return keyFrames.get(keyFrames.size() - 1).getValue();
		
	}
	
	public void addKeyFrame(KeyFrame<T> kf) {
		
		if(kf.getT() > max)
			max = kf.getT();
		
		for(KeyFrame<T> key : keyFrames) {
			
			if(kf.getT() <= key.getT()) {
				
				keyFrames.add(keyFrames.indexOf(key), kf);
				return;
				
			}
			
		}
		
		keyFrames.add(kf);
		
	}
	
	public float getMaximum() {
		
		return max;
		
	}
	
	public List<KeyFrame<T>> getKeyFrames() {
		
		return new ArrayList<>(keyFrames);
		
	}
	
	public static class KeyFrame<T> {
		
		private final float t;
		private final T value;
		
		public KeyFrame(float t, T value) {
			this.t = t;
			this.value = value;
		}

		public float getT() {
			return t;
		}

		public T getValue() {
			return value;
		}
		
	}
	
	public static interface Interpolator<T> {
		
		public static final UnaryOperator<Float> LINEAR = f -> f;
		
		public static final UnaryOperator<Float> SMOOTHSTEP = f -> f * f * (3 - 2 * f);
		public static final UnaryOperator<Float> SMOOTHERSTEP = f -> f * f * f * (f * (f * 6 - 15) + 10);
		
		public static final UnaryOperator<Float> EASE_IN_QUAD = f -> f * f;
		public static final UnaryOperator<Float> EASE_OUT_QUAD = f -> 1 - (1 - f) * (1 - f);
		public static final UnaryOperator<Float> EASE_IN_OUT_QUAD = f -> (f < 0.5f) ? 2 * f * f : 1 - (float) Math.pow(-2 * f + 2, 2) / 2;
		
		public T interpolate(T a, T b, float t);
		
	}
	
	public static class FloatInterpolator implements Interpolator<Float> {
		
		private UnaryOperator<Float> func = f -> f;
		
		public FloatInterpolator() {}
		
	    public FloatInterpolator(UnaryOperator<Float> func) {
	    	
	    	this.func = func;
	    	
	    }
		
	    public Float interpolate(Float a, Float b, float t) {
	        return a + (b - a) * func.apply(t);
	    }
	    
	}

	public static class Vector2Interpolator implements Interpolator<Vector2f> {
		
		private UnaryOperator<Float> func = f -> f;
		
		public Vector2Interpolator() {}
		
	    public Vector2Interpolator(UnaryOperator<Float> func) {
	    	
	    	this.func = func;
	    	
	    }
		
	    public Vector2f interpolate(Vector2f a, Vector2f b, float t) {
	        return new Vector2f(a.x + (b.x - a.x) * func.apply(t),
	                            a.y + (b.y - a.y) * func.apply(t));
	    }
	    
	}

	public static class ColorInterpolator implements Interpolator<Color> {
		
		private UnaryOperator<Float> func = f -> f;
		
		public ColorInterpolator() {}
		
	    public ColorInterpolator(UnaryOperator<Float> func) {
	    	
	    	this.func = func;
	    	
	    }
		
	    public Color interpolate(Color a, Color b, float t) {
	    	System.out.println("A : " + a.getRed() + " / b : " + b.getRed() + " t : " + t + " / func : " + func.apply(t));
	    	System.out.println((int) (a.getRed() + (b.getRed() - a.getRed()) * func.apply(t)));
	        return new Color(
	            (int) (a.getRed() + (b.getRed() - a.getRed()) * func.apply(t)),
	            (int) (a.getGreen() + (b.getGreen() - a.getGreen()) * func.apply(t)),
	            (int) (a.getBlue() + (b.getBlue() - a.getBlue()) * func.apply(t)),
	            (int) (a.getAlpha() + (b.getAlpha() - a.getAlpha()) * func.apply(t))
	        );
	    }
	    
	}
	
}
