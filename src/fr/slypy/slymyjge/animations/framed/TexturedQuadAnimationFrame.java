package fr.slypy.slymyjge.animations.framed;

import java.awt.Color;

import org.lwjgl.util.vector.Vector2f;

import fr.slypy.slymyjge.graphics.TexCoords;
import fr.slypy.slymyjge.graphics.Texture;
import fr.slypy.slymyjge.graphics.shape.Shape;
import fr.slypy.slymyjge.graphics.shape.TexturedRectangle;

public class TexturedQuadAnimationFrame extends AnimationFrame {

	protected TexCoords texCoords;
	protected Texture texture;
	
	public TexturedQuadAnimationFrame(Texture texture) {

		this(texture, 1, TexCoords.QUAD_DEFAULT_COORDS);
		
	}
	
	public TexturedQuadAnimationFrame(Texture texture, TexCoords coords) {

		this(texture, 1, coords);
		
	}
	
	public TexturedQuadAnimationFrame(Texture texture, float speed) {

		this(texture, speed, TexCoords.QUAD_DEFAULT_COORDS);
		
	}
	
	public TexturedQuadAnimationFrame(Texture texture, float speed, TexCoords coords) {

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
	public Shape getShape(Vector2f pos, Vector2f size) {
		
		return new TexturedRectangle(pos.getX(), pos.getY(), size.getX(), size.getY(), texture, Color.white, texCoords);
		
	}
	
}
