package fr.slypy.slymyjge.graphics.shape;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.util.function.BinaryOperator;

import org.lwjgl.util.vector.Matrix2f;
import org.lwjgl.util.vector.Vector2f;

import fr.slypy.slymyjge.graphics.TexCoords;
import fr.slypy.slymyjge.graphics.Texture;

public class TexturedQuad implements TexturedShape {
	
	public static final ShapeInfo INFOS = new ShapeInfo(4, true, GL_QUADS);

	protected final Vector2f[] vertexes = new Vector2f[4];
	protected Color color;
	private int texture;
	private final Vector2f[] texCoords;
	
	public TexturedQuad(Vector2f a, Vector2f b, Vector2f c, Vector2f d, int tex, Color color, TexCoords coords) {
		
		vertexes[0] = a;
		vertexes[1] = b;
		vertexes[2] = c;
		vertexes[3] = d;
		this.color = color;
		
		this.texture = tex;
		
		if(coords.getCoords().length == getVertexes().length)
			this.texCoords = coords.getCoords();
		else
			this.texCoords = TexCoords.QUAD_DEFAULT_COORDS.getCoords();
		
	}
	
	public TexturedQuad(Vector2f a, Vector2f b, Vector2f c, Vector2f d, Texture tex, Color color, TexCoords coords) {
		
		vertexes[0] = a;
		vertexes[1] = b;
		vertexes[2] = c;
		vertexes[3] = d;
		this.color = color;
		
		this.texture = tex.getId();
		
		if(coords.getCoords().length == getVertexes().length)
			this.texCoords = coords.getCoords();
		else
			this.texCoords = TexCoords.QUAD_DEFAULT_COORDS.getCoords();
		
	}
	
	public TexturedQuad(Vector2f a, Vector2f b, Vector2f c, Vector2f d, Texture tex, Color color) {
		
		this(a, b, c, d, tex, color, TexCoords.QUAD_DEFAULT_COORDS);
		
	}
	
	public TexturedQuad(Vector2f a, Vector2f b, Vector2f c, Vector2f d, Texture tex) {
		
		this(a, b, c, d, tex, Color.white, TexCoords.QUAD_DEFAULT_COORDS);
		
	}
	
	public TexturedQuad(Vector2f a, Vector2f b, Vector2f c, Vector2f d, Color color) {
		
		vertexes[0] = a;
		vertexes[1] = b;
		vertexes[2] = c;
		vertexes[3] = d;
		this.color = color;
		
		this.texture = 0;
		this.texCoords = TexCoords.QUAD_DEFAULT_COORDS.getCoords();
		
	}
	
	public TexturedQuad(Vector2f a, Vector2f b, Vector2f c, Vector2f d) {
		
		this(a, b, c, d, Color.white);
		
	}
	
	@Override
	public void fillBuffer(ByteBuffer b) {
		
		b.putFloat(vertexes[0].x).putFloat(vertexes[0].y).putFloat(texCoords[0].x).putFloat(texCoords[0].y).put((byte) color.getRed()).put((byte) color.getGreen()).put((byte) color.getBlue()).put((byte) color.getAlpha());
		b.putFloat(vertexes[1].x).putFloat(vertexes[1].y).putFloat(texCoords[1].x).putFloat(texCoords[1].y).put((byte) color.getRed()).put((byte) color.getGreen()).put((byte) color.getBlue()).put((byte) color.getAlpha());
		b.putFloat(vertexes[2].x).putFloat(vertexes[2].y).putFloat(texCoords[2].x).putFloat(texCoords[2].y).put((byte) color.getRed()).put((byte) color.getGreen()).put((byte) color.getBlue()).put((byte) color.getAlpha());
		b.putFloat(vertexes[3].x).putFloat(vertexes[3].y).putFloat(texCoords[3].x).putFloat(texCoords[3].y).put((byte) color.getRed()).put((byte) color.getGreen()).put((byte) color.getBlue()).put((byte) color.getAlpha());

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
		
		return new Vector2f((vertexes[0].x + vertexes[1].x + vertexes[2].x + vertexes[3].x) / 4, (vertexes[0].y + vertexes[1].y + vertexes[2].y + vertexes[3].y) / 4);
		
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
	public TexturedQuad color(Color newColor) {
		
		return color(newColor, (c1, c2) -> c2);
		
	}
	
	@Override
	public TexturedQuad color(Color newColor, BinaryOperator<Color> blendFunction) {
		
		return new TexturedQuad(vertexes[0], vertexes[1], vertexes[2], vertexes[3], texture, blendFunction.apply(color, newColor), new TexCoords(texCoords));
		
	}
	
	@Override
	public TexturedQuad rotate(float angle) {
		
		return rotate(angle, getCenter());
		
	}
	
	@Override
	public TexturedQuad rotate(float angle, Vector2f center) {
		
		Vector2f[] rotatedVertexes = Shape.applyTransform(vertexes, Shape.rotationMatrix(angle), center);
		
		return new TexturedQuad(rotatedVertexes[0], rotatedVertexes[1], rotatedVertexes[2], rotatedVertexes[3], texture, color, new TexCoords(texCoords));
		
	}
	
	public void setTexture(Texture tex) {
		
		this.texture = tex.getId();
		
	}
	
	
	public void setTexture(int tex) {
		
		this.texture = tex;
		
	}
	
	public int getTexture() {
		
		return texture;
		
	}
	
	

	@Override
	public TexturedQuad transform(float[][] transform) {
		
		return transform(Shape.getMatrix(transform), getCenter());
		
	}

	@Override
	public TexturedQuad transform(Matrix2f transform) {

		return transform(transform, getCenter());
		
	}
	
	@Override
	public TexturedQuad transform(float[][] transform, Vector2f center) {
		
		return transform(Shape.getMatrix(transform), center);
		
	}

	@Override
	public TexturedQuad transform(Matrix2f transform, Vector2f center) {

		Vector2f[] transformedVertexes = Shape.applyTransform(vertexes, transform, center);
		
		return new TexturedQuad(transformedVertexes[0], transformedVertexes[1], transformedVertexes[2], transformedVertexes[3], texture, color, new TexCoords(texCoords));
		
	}
	
	@Override
	public TexturedQuad sheer(Vector2f sheer, Vector2f center) {
		
		return transform(Shape.shearMatrix(sheer.x, sheer.y),center);
		
	}

	@Override
	public TexturedQuad sheer(Vector2f sheer) {
		
		return sheer(sheer, getCenter());
		
	}

	@Override
	public TexturedQuad scale(Vector2f scale) {

		return scale(scale, getCenter());
		
	}
	
	@Override
	public TexturedQuad scale(Vector2f scale, Vector2f center) {

		return transform(Shape.scalingMatrix(scale.x, scale.y), center);
		
	}

	@Override
	public TexturedQuad translate(Vector2f translation) {
		
		Vector2f[] translatedVertexes = Shape.applyTranslate(vertexes, translation);
		
		return new TexturedQuad(translatedVertexes[0], translatedVertexes[1], translatedVertexes[2], translatedVertexes[3], texture, color, new TexCoords(texCoords));
		
	}

}
