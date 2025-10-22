package fr.slypy.slymyjge.graphics.shape;

import java.awt.Color;

import org.lwjgl.util.vector.Vector2f;

import fr.slypy.slymyjge.graphics.TexCoords;
import fr.slypy.slymyjge.graphics.Texture;

public class TexturedRectangle extends TexturedQuad {
	
	private final float w;
	private final float h;
	private final float x;
	private final float y;
	
	public TexturedRectangle(float x, float y, float w, float h, int tex, Color color, TexCoords coords) {
		
		super(new Vector2f(x, y), new Vector2f(x+w, y), new Vector2f(x+w, y+h), new Vector2f(x, y+h), tex, color, coords);
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;

	}

	public TexturedRectangle(float x, float y, float w, float h, Texture tex, Color color, TexCoords coords) {
		
		super(new Vector2f(x, y), new Vector2f(x+w, y), new Vector2f(x+w, y+h), new Vector2f(x, y+h), tex, color, coords);
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;

	}
	
	public TexturedRectangle(float x, float y, float w, float h, Texture tex, Color color) {
		
		this(x, y, w, h, tex, color, TexCoords.QUAD_DEFAULT_COORDS);
		
	}
	
	public TexturedRectangle(float x, float y, float w, float h, Texture tex) {
		
		this(x, y, w, h, tex, Color.white, TexCoords.QUAD_DEFAULT_COORDS);
		
	}
	
	public TexturedRectangle(float x, float y, float w, float h, Color color) {
		
		super(new Vector2f(x, y), new Vector2f(x+w, y), new Vector2f(x+w, y+h), new Vector2f(x, y+h), color);
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		
	}
	
	public TexturedRectangle(float x, float y, float w, float h) {
		
		this(x, y, w, h, Color.white);
		
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
