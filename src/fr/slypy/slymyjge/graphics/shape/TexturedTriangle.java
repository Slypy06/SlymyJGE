package fr.slypy.slymyjge.graphics.shape;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glEnd;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.util.function.BinaryOperator;

import org.lwjgl.util.vector.Matrix2f;
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
			this.texCoords = TexCoords.TRIANGLE_DEFAULT_COORDS.getCoords();
		
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
	public Vector2f getOrigin() {
		
		Vector2f origin = new Vector2f(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
		
		for(Vector2f vertex : getVertexes()) {
			
			origin.setX(Math.min(vertex.x, origin.x));
			origin.setY(Math.min(vertex.y, origin.y));
			
		}
		
		return origin;
		
	}
	
	@Override
	public float getWidth() {
		
		float min = Float.POSITIVE_INFINITY;
		float max = Float.NEGATIVE_INFINITY;
		
		for(Vector2f vertex : getVertexes()) {
			
			min = Math.min(vertex.x, min);
			max = Math.max(vertex.x, max);
			
		}
		
		return max-min;
		
	}
	
	@Override
	public float getHeight() {
		
		float min = Float.POSITIVE_INFINITY;
		float max = Float.NEGATIVE_INFINITY;
		
		for(Vector2f vertex : getVertexes()) {
			
			min = Math.min(vertex.y, min);
			max = Math.max(vertex.y, max);
			
		}
		
		return max-min;
		
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
	
	public int getTexture() {
		
		return texture;
		
	}

	@Override
	public Shape rotate(float angle) {
		
		return rotate(angle, getCenter());
		
	}
	
	@Override
	public Shape rotate(float angle, Vector2f center) {
		
		Vector2f[] rotatedVertexes = Shape.applyTransform(getVertexes(), Shape.rotationMatrix(angle), center);
		
		return new TexturedTriangle(rotatedVertexes[0], rotatedVertexes[1], rotatedVertexes[2], texture, color, new TexCoords(texCoords));
		
	}

	@Override
	public Shape transform(float[][] transform, Vector2f center) {
		
		return transform(Shape.getMatrix(transform), center);
		
	}

	@Override
	public Shape transform(Matrix2f transform, Vector2f center) {

		Vector2f[] newVertexes = Shape.applyTransform(getVertexes(), transform, center);
		
		return new TexturedTriangle(newVertexes[0], newVertexes[1], newVertexes[2], texture, color, new TexCoords(texCoords));
		
	}

	@Override
	public Shape transform(float[][] transform) {
		
		return transform(Shape.getMatrix(transform), getCenter());
		
	}

	@Override
	public Shape transform(Matrix2f transform) {

		return transform(transform, getCenter());
		
	}

	@Override
	public Shape sheer(Vector2f sheer, Vector2f center) {

		Vector2f[] newVertexes = Shape.applyTransform(getVertexes(), Shape.shearMatrix(sheer.getX(), sheer.getY()), center);
		
		return new TexturedTriangle(newVertexes[0], newVertexes[1], newVertexes[2], texture, color, new TexCoords(texCoords));
		
	}

	@Override
	public Shape sheer(Vector2f sheer) {

		return sheer(sheer, getCenter());
		
	}

	@Override
	public Shape scale(Vector2f scale, Vector2f center) {

		Vector2f[] newVertexes = Shape.applyTransform(getVertexes(), Shape.scalingMatrix(scale.getX(), scale.getY()), center);
		
		return new TexturedTriangle(newVertexes[0], newVertexes[1], newVertexes[2], texture, color, new TexCoords(texCoords));
		
	}

	@Override
	public Shape scale(Vector2f scale) {

		return scale(scale, getCenter());
		
	}

	@Override
	public Shape translate(Vector2f translation) {

		Vector2f[] newVertexes = Shape.applyTranslate(getVertexes(), translation);
		
		return new TexturedTriangle(newVertexes[0], newVertexes[1], newVertexes[2], texture, color, new TexCoords(texCoords));
		
	}

	@Override
	public Shape color(Color newColor) {

		return new TexturedTriangle(getVertexes()[0], getVertexes()[1], getVertexes()[2], texture, newColor, new TexCoords(texCoords));
		
	}

	@Override
	public Shape color(Color newColor, BinaryOperator<Color> blend) {

		return new TexturedTriangle(getVertexes()[0], getVertexes()[1], getVertexes()[2], texture, blend.apply(color, newColor), new TexCoords(texCoords));
		
	}

	@Override
	public TexCoords getTexCoords() {

		return new TexCoords(texCoords);
		
	}

}
