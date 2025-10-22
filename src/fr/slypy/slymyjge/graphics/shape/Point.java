package fr.slypy.slymyjge.graphics.shape;

import static org.lwjgl.opengl.GL11.GL_POINTS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPointSize;

import java.awt.Color;
import java.nio.ByteBuffer;

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
	
	@Override
	public void fillBuffer(ByteBuffer b) {
		
		b.putFloat(vertex.x).putFloat(vertex.y).put((byte) color.getRed()).put((byte) color.getGreen()).put((byte) color.getBlue()).put((byte) color.getAlpha());

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
	public Point rotate(float angle) {
		
		return new Point(vertex, size, color);
		
	}
	
	@Override
	public Point rotate(float angle, Vector2f center) {
		
		Vector2f[] rotatedVertexes = Shape.rotateVertexes(getVertexes(), center, angle);
		
		return new Point(rotatedVertexes[0], size, color);
		
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
	
	public int getSize() {
		
		return size;
		
	}
	
}
