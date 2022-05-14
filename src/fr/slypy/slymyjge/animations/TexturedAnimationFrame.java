package fr.slypy.slymyjge.animations;

import java.awt.Color;

import fr.slypy.slymyjge.graphics.Renderer;
import fr.slypy.slymyjge.graphics.Texture;

public class TexturedAnimationFrame extends AnimationFrame {

	protected int xo;
	protected int yo;
	protected int maxXo;
	protected int maxYo;
	protected Texture texture;
	
	public TexturedAnimationFrame(int xo, int yo, int maxXo, int maxYo, Texture texture) {

		this.xo = xo;
		this.yo = yo;
		this.maxXo = maxXo;
		this.maxYo = maxYo;
		this.texture = texture;
		
	}
	
	public TexturedAnimationFrame(int xo, int yo, int maxXo, int maxYo, Texture texture, float speed) {

		this.xo = xo;
		this.yo = yo;
		this.maxXo = maxXo;
		this.maxYo = maxYo;
		this.texture = texture;
		
		super.setSpeed(speed);
		
	}
	
	public int getXo() {
		
		return xo;
		
	}

	public void setXo(int xo) {
		
		this.xo = xo;
		
	}

	public int getYo() {
		
		return yo;
		
	}

	public void setYo(int yo) {
		
		this.yo = yo;
		
	}

	public int getMaxYo() {
		
		return maxYo;
		
	}

	public void setMaxYo(int maxYo) {
		
		this.maxYo = maxYo;
		
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

	@Override
	public void render(float x, float y, int w, int h, Color c) {
		
		Renderer.renderTexturePart(x, y, w, h, c, xo, yo, maxXo, maxYo, texture);
		
	}
	
}
