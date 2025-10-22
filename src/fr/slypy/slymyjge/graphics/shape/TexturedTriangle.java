package fr.slypy.slymyjge.graphics.shape;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glEnd;

import java.awt.Color;
import java.nio.ByteBuffer;

import org.lwjgl.util.vector.Vector2f;

import fr.slypy.slymyjge.graphics.TexCoords;
import fr.slypy.slymyjge.graphics.Texture;

public class TexturedTriangle implements TexturedShape {
	
	public static final ShapeInfo INFOS = new ShapeInfo(3, true, GL_TRIANGLES);

	protected final Vector2f[] vertexes = new Vector2f[4];
	protected final Color color;
	private final int texture;
	private final Vector2f[] texCoords;
	
	public TexturedTriangle(Vector2f a, Vector2f b, Vector2f c, int tex, Color color, TexCoords coords) {
		
		vertexes[0] = a;
		vertexes[1] = b;
		vertexes[2] = c;
		this.color = color;
		
		this.texture = tex;
		
		if(coords.getCoords().length == vertexes.length)
			this.texCoords = coords.getCoords();
		else
			this.texCoords = TexCoords.QUAD_DEFAULT_COORDS.getCoords();
		
	}
	
	public TexturedTriangle(Vector2f a, Vector2f b, Vector2f c, Texture tex, Color color, TexCoords coords) {
		
		vertexes[0] = a;
		vertexes[1] = b;
		vertexes[2] = c;
		this.color = color;
		
		this.texture = tex.getId();
		
		if(coords.getCoords().length == vertexes.length)
			this.texCoords = coords.getCoords();
		else
			this.texCoords = TexCoords.QUAD_DEFAULT_COORDS.getCoords();
		
	}
	
	public TexturedTriangle(Vector2f a, Vector2f b, Vector2f c, Texture tex, Color color) {
		
		this(a, b, c, tex, color, TexCoords.QUAD_DEFAULT_COORDS);
		
	}
	
	public TexturedTriangle(Vector2f a, Vector2f b, Vector2f c, Texture tex) {
		
		this(a, b, c, tex, Color.white, TexCoords.QUAD_DEFAULT_COORDS);
		
	}
	
	public TexturedTriangle(Vector2f a, Vector2f b, Vector2f c, Color color) {
		
		vertexes[0] = a;
		vertexes[1] = b;
		vertexes[2] = c;
		this.color = color;
		
		this.texture = 0;
		this.texCoords = TexCoords.QUAD_DEFAULT_COORDS.getCoords();
		
	}
	
	public TexturedTriangle(Vector2f a, Vector2f b, Vector2f c) {
		
		this(a, b, c, Color.white);
		
	}
	
	@Override
	public void fillBuffer(ByteBuffer b) {
		
		b.putFloat(vertexes[0].x).putFloat(vertexes[0].y).putFloat(texCoords[0].x).putFloat(texCoords[0].y).put((byte) color.getRed()).put((byte) color.getGreen()).put((byte) color.getBlue()).put((byte) color.getAlpha());
		b.putFloat(vertexes[1].x).putFloat(vertexes[1].y).putFloat(texCoords[1].x).putFloat(texCoords[1].y).put((byte) color.getRed()).put((byte) color.getGreen()).put((byte) color.getBlue()).put((byte) color.getAlpha());
		b.putFloat(vertexes[2].x).putFloat(vertexes[2].y).putFloat(texCoords[2].x).putFloat(texCoords[2].y).put((byte) color.getRed()).put((byte) color.getGreen()).put((byte) color.getBlue()).put((byte) color.getAlpha());

	}
	
	@Override
	public Vector2f[] getVertexes() {
		
		return vertexes;
		
	}
	
	@Override
	public Color getColor() {
		
		return color;
		
	}
	
	@Override
	public Vector2f getCenter() {
		
		return new Vector2f((vertexes[0].x + vertexes[1].x + vertexes[2].x) / 3, (vertexes[0].y + vertexes[1].y + vertexes[2].y) / 3);
		
	}
	
	@Override
	public void glData() {
		
		glBindTexture(GL_TEXTURE_2D, texture);
		
		glBegin(INFOS.getGlMode());
			Shape.glColor(color);
			Shape.glVertexes(vertexes, texCoords);
		glEnd();
		
		glBindTexture(GL_TEXTURE_2D, 0);

	}
	
	@Override
	public TexturedTriangle rotate(float angle) {
		
		return rotate(angle, getCenter());
		
	}
	
	@Override
	public TexturedTriangle rotate(float angle, Vector2f center) {
		
		Vector2f[] rotatedVertexes = Shape.rotateVertexes(vertexes, center, angle);
		
		return new TexturedTriangle(rotatedVertexes[0], rotatedVertexes[1], rotatedVertexes[2], texture, color, new TexCoords(texCoords));
		
	}
	
	public int getTexture() {
		
		return texture;
		
	}

}
