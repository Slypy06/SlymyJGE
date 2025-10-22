package fr.slypy.slymyjge.graphics.shape;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.nio.ByteBuffer;

import org.lwjgl.util.vector.Vector2f;

public class Quad implements Shape {

	public static final ShapeInfo INFOS = new ShapeInfo(4, false, GL_QUADS);
	protected final Vector2f[] vertexes = new Vector2f[4];
	protected final Color color;
	
	public Quad(Vector2f a, Vector2f b, Vector2f c, Vector2f d, Color color) {
		
		vertexes[0] = a;
		vertexes[1] = b;
		vertexes[2] = c;
		vertexes[3] = d;
		this.color = color;
		
	}
	
	public Quad(Vector2f a, Vector2f b, Vector2f c, Vector2f d) {
		
		this(a, b, c, d, Color.black);
		
	}
	
	@Override
	public void fillBuffer(ByteBuffer b) {
		
		b.putFloat(vertexes[0].x).putFloat(vertexes[0].y).put((byte) color.getRed()).put((byte) color.getGreen()).put((byte) color.getBlue()).put((byte) color.getAlpha());
		b.putFloat(vertexes[1].x).putFloat(vertexes[1].y).put((byte) color.getRed()).put((byte) color.getGreen()).put((byte) color.getBlue()).put((byte) color.getAlpha());
		b.putFloat(vertexes[2].x).putFloat(vertexes[2].y).put((byte) color.getRed()).put((byte) color.getGreen()).put((byte) color.getBlue()).put((byte) color.getAlpha());
		b.putFloat(vertexes[3].x).putFloat(vertexes[3].y).put((byte) color.getRed()).put((byte) color.getGreen()).put((byte) color.getBlue()).put((byte) color.getAlpha());

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
	public Vector2f[] getVertexes() {

		return vertexes;

	}
	
	@Override
	public Quad rotate(float angle) {
		
		return rotate(angle, getCenter());
		
	}
	
	@Override
	public Quad rotate(float angle, Vector2f center) {
		
		Vector2f[] rotatedVertexes = Shape.rotateVertexes(vertexes, center, angle);
		
		return new Quad(rotatedVertexes[0], rotatedVertexes[1], rotatedVertexes[2], rotatedVertexes[3], color);
		
	}

	@Override
	public void glData() {
		
		glBegin(INFOS.getGlMode());
			Shape.glColor(color);
			Shape.glVertexes(vertexes);
		glEnd();

	}

}
