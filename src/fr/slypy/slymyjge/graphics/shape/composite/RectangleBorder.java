package fr.slypy.slymyjge.graphics.shape.composite;

import java.awt.Color;

import org.lwjgl.util.vector.Vector2f;

public class RectangleBorder extends QuadBorder {
	
	private final float w;
	private final float h;
	private final float x;
	private final float y;

	public RectangleBorder(float x, float y, float w, float h, int width, Color color) {
		
		super(new Vector2f(x, y), new Vector2f(x+w, y), new Vector2f(x+w, y+h), new Vector2f(x, y+h), width, color);
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;

	}
	
	public RectangleBorder(float x, float y, float w, float h, int width) {
		
		this(x, y, w, h, width, Color.black);

	}
	
	public RectangleBorder(float x, float y, float w, float h, Color color) {
		
		this(x, y, w, h, 1, color);

	}
	
	public RectangleBorder(float x, float y, float w, float h) {
		
		this(x, y, w, h, 1, Color.black);

	}

	public float getW() {
		
		return w;
		
	}

	public float getH() {
		
		return h;
		
	}

	public float getX() {
		
		return x;
		
	}

	public float getY() {
		
		return y;
		
	}

}
