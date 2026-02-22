package fr.slypy.slymyjge.graphics.shape.composite;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLineWidth;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.util.function.BinaryOperator;

import org.lwjgl.util.vector.Matrix2f;
import org.lwjgl.util.vector.Vector2f;

import fr.slypy.slymyjge.graphics.shape.Shape;
import fr.slypy.slymyjge.graphics.shape.ShapeInfo;

public class QuadBorder implements Shape {

	public static final ShapeInfo INFOS = new ShapeInfo(8, false, GL_LINES);
	
	protected final Vector2f[] vertexes = new Vector2f[8];
	protected final Color color;
	protected final int width;
	
	public QuadBorder(Vector2f a, Vector2f b, Vector2f c, Vector2f d, int width, Color color) {
		
		vertexes[0] = a;
		vertexes[1] = b;
		vertexes[2] = b;
		vertexes[3] = c;
		vertexes[4] = c;
		vertexes[5] = d;
		vertexes[6] = d;
		vertexes[7] = a;
		this.width = width;
		this.color = color;
		
	}
	
	public QuadBorder(Vector2f a, Vector2f b, Vector2f c, Vector2f d, int width) {
		
		this(a, b, c, d, width, Color.black);
		
	}
	
	public QuadBorder(Vector2f a, Vector2f b, Vector2f c, Vector2f d, Color color) {
		
		this(a, b, c, d, 1, color);
		
	}
	
	public QuadBorder(Vector2f a, Vector2f b, Vector2f c, Vector2f d) {
		
		this(a, b, c, d, 1, Color.black);
		
	}
	
	@Override
	public void fillBuffer(ByteBuffer b) {
		
		for(int i = 0; i < 8; i++) {
			
			b.putFloat(vertexes[i].x).putFloat(vertexes[i].y).put((byte) color.getRed()).put((byte) color.getGreen()).put((byte) color.getBlue()).put((byte) color.getAlpha());
			
		}
		
	}
	
	public int getLineWidth() {
		
		return width;
		
	}
	
	@Override
	public Color getColor() {
		
		return color;
		
	}
	
	@Override
	public Vector2f getCenter() {
		
		return new Vector2f((vertexes[0].x + vertexes[2].x + vertexes[4].x + vertexes[6].x) / 4, (vertexes[0].y + vertexes[2].y + vertexes[4].y + vertexes[6].y) / 4);
		
	}

	@Override
	public Vector2f[] getVertexes() {

		return vertexes;

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
	public QuadBorder color(Color newColor) {
		
		return color(newColor, (c1, c2) -> c2);
		
	}
	
	@Override
	public QuadBorder color(Color newColor, BinaryOperator<Color> blendFunction) {
		
		return new QuadBorder(vertexes[0], vertexes[2], vertexes[4], vertexes[6], blendFunction.apply(color, newColor));
		
	}
	
	@Override
	public Shape rotate(float angle) {
		
		return rotate(angle, getCenter());
		
	}
	
	@Override
	public Shape rotate(float angle, Vector2f center) {
		
		Vector2f[] rotatedVertexes = Shape.applyTransform(getVertexes(), Shape.rotationMatrix(angle), center);
		
		return new QuadBorder(rotatedVertexes[0], rotatedVertexes[2], rotatedVertexes[4], rotatedVertexes[6], color);
		
	}

	@Override
	public void glData() {
		
		glLineWidth(width);
			glBegin(INFOS.getGlMode());
				Shape.glColor(color);
				Shape.glVertexes(vertexes);
			glEnd();
		glLineWidth(1);
			
	}

	@Override
	public Shape transform(float[][] transform, Vector2f center) {
		
		return transform(Shape.getMatrix(transform), center);
		
	}

	@Override
	public Shape transform(Matrix2f transform, Vector2f center) {

		Vector2f[] newVertexes = Shape.applyTransform(getVertexes(), transform, center);
		
		return new QuadBorder(newVertexes[0], newVertexes[2], newVertexes[4], newVertexes[6], color);
		
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
		
		return new QuadBorder(newVertexes[0], newVertexes[2], newVertexes[4], newVertexes[6], color);
		
	}

	@Override
	public Shape sheer(Vector2f sheer) {

		return sheer(sheer, getCenter());
		
	}

	@Override
	public Shape scale(Vector2f scale, Vector2f center) {

		Vector2f[] newVertexes = Shape.applyTransform(getVertexes(), Shape.scalingMatrix(scale.getX(), scale.getY()), center);
		
		return new QuadBorder(newVertexes[0], newVertexes[2], newVertexes[4], newVertexes[6], color);
		
	}

	@Override
	public Shape scale(Vector2f scale) {

		return scale(scale, getCenter());
		
	}

	@Override
	public Shape translate(Vector2f translation) {

		Vector2f[] newVertexes = Shape.applyTranslate(getVertexes(), translation);
		
		return new QuadBorder(newVertexes[0], newVertexes[2], newVertexes[4], newVertexes[6], color);
		
	}

}
