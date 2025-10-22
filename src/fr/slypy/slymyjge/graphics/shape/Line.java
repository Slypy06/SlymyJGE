package fr.slypy.slymyjge.graphics.shape;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLineWidth;

import java.awt.Color;
import java.nio.ByteBuffer;

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
	public Line rotate(float angle) {
		
		return rotate(angle, getCenter());
		
	}
	
	@Override
	public Line rotate(float angle, Vector2f center) {
		
		Vector2f[] rotatedVertexes = Shape.rotateVertexes(vertexes, center, angle);
		
		return new Line(rotatedVertexes[0], rotatedVertexes[1], width, color);
		
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
	
	public int getWidth() {
		
		return width;
		
	}

}
