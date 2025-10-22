package fr.slypy.slymyjge.graphics.shape;

import java.awt.Color;

import org.lwjgl.util.vector.Vector2f;

public class Rectangle extends Quad {
	
	private final float w;
	private final float h;
	private final float x;
	private final float y;

	public Rectangle(float x, float y, float w, float h, Color color) {
		
		super(new Vector2f(x, y), new Vector2f(x+w, y), new Vector2f(x+w, y+h), new Vector2f(x, y+h), color);
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;

	}
	
	public Rectangle(float x, float y, float w, float h) {
		
		this(x, y, w, h, Color.black);

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
