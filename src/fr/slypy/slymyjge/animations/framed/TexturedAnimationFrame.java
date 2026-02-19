package fr.slypy.slymyjge.animations.framed;

import java.awt.Color;

import fr.slypy.slymyjge.graphics.NewGenRenderer;
import fr.slypy.slymyjge.graphics.TexCoords;
import fr.slypy.slymyjge.graphics.Texture;
import fr.slypy.slymyjge.graphics.shape.TexturedRectangle;

public class TexturedAnimationFrame extends AnimationFrame {

	protected TexCoords texCoords;
	protected Texture texture;
	
	public TexturedAnimationFrame(Texture texture) {

		this(texture, 1, TexCoords.QUAD_DEFAULT_COORDS);
		
	}
	
	public TexturedAnimationFrame(Texture texture, TexCoords coords) {

		this(texture, 1, coords);
		
	}
	
	public TexturedAnimationFrame(Texture texture, float speed) {

		this(texture, speed, TexCoords.QUAD_DEFAULT_COORDS);
		
	}
	
	public TexturedAnimationFrame(Texture texture, float speed, TexCoords coords) {

		this.texture = texture;
		this.texCoords = coords;
		super.setSpeed(speed);
		
	}

	public Texture getTexture() {
		
		return texture;
		
	}

	public void setTexture(Texture texture) {
		
		this.texture = texture;
		
	}

	@Override
	public void render(float x, float y, int w, int h, Color c) {
		
		NewGenRenderer.renderShape(new TexturedRectangle(x, y, w, h, texture, c, texCoords));
		
	}
	
}
