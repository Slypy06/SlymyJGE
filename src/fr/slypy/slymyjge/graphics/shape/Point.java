package fr.slypy.slymyjge.graphics.shape;

import static org.lwjgl.opengl.GL11.GL_POINTS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPointSize;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.util.function.BinaryOperator;

import org.lwjgl.util.vector.Matrix2f;
import org.lwjgl.util.vector.Vector2f;

public class Point implements Shape {
	
	public static final ShapeInfo INFOS = new ShapeInfo(1, false, GL_POINTS);

	protected final Vector2f vertex;
	protected final int size;
	protected final Color color;
	
	public Point(Vector2f a, int size, Color color) {
		
		vertex = a;
		this.size = size;
		this.color = color;
		
	}
	
	public Point(Vector2f a, Color color) {
		
		this(a, 1, color);
		
	}
	
	public Point(Vector2f a, int size) {
		
		this(a, size, Color.black);
		
	}

	public Point(Vector2f a) {
		
		this(a, 1, Color.black);
		
	}
	
	public int getSize() {
		
		return size;
		
	}
	
	@Override
	public void fillBuffer(ByteBuffer b) {
		
		b.putFloat(vertex.x).putFloat(vertex.y).put((byte) color.getRed()).put((byte) color.getGreen()).put((byte) color.getBlue()).put((byte) color.getAlpha());

	}
	
	
	@Override
	public void glData() {
		
		glPointSize(size);
		
		glBegin(INFOS.getGlMode());
			Shape.glColor(color);
			Shape.glVertexes(getVertexes());
		glEnd();
		
		glPointSize(1);

	}
	
	@Override
	public Vector2f[] getVertexes() {
		
		return new Vector2f[] {vertex};
		
	}
	
	@Override
	public Color getColor() {
		
		return color;
		
	}
	
	@Override
	public Vector2f getCenter() {
		
		return vertex;
		
	}
	
	@Override
	public Vector2f getOrigin() {
		
		return vertex;
		
	}
	
	@Override
	public float getWidth() {
		
		return 0;
		
	}
	
	@Override
	public float getHeight() {

		return 0;
		
	}
	
	@Override
	public Point rotate(float angle) {
		
		return new Point(vertex, size, color);
		
	}
	
	@Override
	public Point rotate(float angle, Vector2f center) {
		
		Vector2f[] rotatedVertexes = Shape.applyTransform(getVertexes(), Shape.rotationMatrix(angle), center);
		
		return new Point(rotatedVertexes[0], size, color);
		
	}

	@Override
	public Shape transform(Matrix2f transform, Vector2f center) {

		return new Point(Shape.applyTransform(getVertexes(), transform, center)[0], size, color);
		
	}

	@Override
	public Shape sheer(Vector2f sheer, Vector2f center) {

		return new Point(Shape.applyTransform(getVertexes(), Shape.shearMatrix(sheer.getX(), sheer.getY()), center)[0], size, color);
		
	}

	@Override
	public Shape scale(Vector2f scale, Vector2f center) {

		return new Point(Shape.applyTransform(getVertexes(), Shape.scalingMatrix(scale.getX(), scale.getY()), center)[0], size, color);
		
	}

	@Override
	public Shape translate(Vector2f translation) {

		return new Point(Shape.applyTranslate(getVertexes(), translation)[0], size, color);
		
	}

	@Override
	public Shape color(Color newColor, BinaryOperator<Color> blend) {

		return new Point(getVertexes()[0], size, blend.apply(color, newColor));
		
	}
	
}
