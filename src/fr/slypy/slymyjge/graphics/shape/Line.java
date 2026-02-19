package fr.slypy.slymyjge.graphics.shape;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.util.function.BinaryOperator;

import org.lwjgl.util.vector.Matrix2f;
import org.lwjgl.util.vector.Vector2f;

public class Line implements Shape {
	
	public static final ShapeInfo INFOS = new ShapeInfo(2, false, GL_LINES);

	protected final Vector2f[] vertexes = new Vector2f[2];
	protected final int width;
	protected final Color color;
	
	public Line(Vector2f a, Vector2f b, int width, Color color) {
		
		vertexes[0] = a;
		vertexes[1] = b;
		this.width = width;
		this.color = color;
		
	}
	
	public Line(Vector2f a, Vector2f b, Color color) {
		
		this(a, b, 1, color);
		
	}
	
	public Line(Vector2f a, Vector2f b, int width) {
		
		this(a, b, width, Color.black);
		
	}

	public Line(Vector2f a, Vector2f b) {
		
		this(a, b, 1, Color.black);
		
	}
	
	@Override
	public void fillBuffer(ByteBuffer b) {
		
		b.putFloat(vertexes[0].x).putFloat(vertexes[0].y).put((byte) color.getRed()).put((byte) color.getGreen()).put((byte) color.getBlue()).put((byte) color.getAlpha());
		b.putFloat(vertexes[1].x).putFloat(vertexes[1].y).put((byte) color.getRed()).put((byte) color.getGreen()).put((byte) color.getBlue()).put((byte) color.getAlpha());

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
		
		return new Vector2f((vertexes[0].x + vertexes[1].x) / 2, (vertexes[0].y + vertexes[1].y) / 2);
		
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
	public Shape rotate(float angle) {
		
		return rotate(angle, getCenter());
		
	}
	
	@Override
	public Shape rotate(float angle, Vector2f center) {
		
		Vector2f[] rotatedVertexes = Shape.applyTransform(vertexes, Shape.rotationMatrix(angle), center);
		
		return new Line(rotatedVertexes[0], rotatedVertexes[1], width, color);
		
	}

	@Override
	public void glData() {
		
		glBegin(INFOS.getGlMode());
			Shape.glColor(color);
			Shape.glVertexes(vertexes);
		glEnd();

	}

	@Override
	public Shape transform(float[][] transform, Vector2f center) {
		
		return transform(Shape.getMatrix(transform), center);
		
	}

	@Override
	public Shape transform(Matrix2f transform, Vector2f center) {

		Vector2f[] newVertexes = Shape.applyTransform(getVertexes(), transform, center);
		
		return new Line(newVertexes[0], newVertexes[1], width, color);
		
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
		
		return new Line(newVertexes[0], newVertexes[1], width, color);
		
	}

	@Override
	public Shape sheer(Vector2f sheer) {

		return sheer(sheer, getCenter());
		
	}

	@Override
	public Shape scale(Vector2f scale, Vector2f center) {

		Vector2f[] newVertexes = Shape.applyTransform(getVertexes(), Shape.scalingMatrix(scale.getX(), scale.getY()), center);
		
		return new Line(newVertexes[0], newVertexes[1], width, color);
		
	}

	@Override
	public Shape scale(Vector2f scale) {

		return scale(scale, getCenter());
		
	}

	@Override
	public Shape translate(Vector2f translation) {

		Vector2f[] newVertexes = Shape.applyTranslate(getVertexes(), translation);
		
		return new Line(newVertexes[0], newVertexes[1], width, color);
		
	}
	
	public int getLineWidth() {
		
		return width;
		
	}

	@Override
	public Shape color(Color newColor) {

		return new Line(getVertexes()[0], getVertexes()[1], width, newColor);
		
	}

	@Override
	public Shape color(Color newColor, BinaryOperator<Color> blend) {

		return new Line(getVertexes()[0], getVertexes()[1], width, blend.apply(color, newColor));
		
	}

}
