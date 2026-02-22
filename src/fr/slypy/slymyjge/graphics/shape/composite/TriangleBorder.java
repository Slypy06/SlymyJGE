package fr.slypy.slymyjge.graphics.shape.composite;

import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.GL_LINES;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.util.function.BinaryOperator;

import org.lwjgl.util.vector.Matrix2f;
import org.lwjgl.util.vector.Vector2f;

import fr.slypy.slymyjge.graphics.shape.Shape;
import fr.slypy.slymyjge.graphics.shape.ShapeInfo;

public class TriangleBorder implements Shape {
	
	public static final ShapeInfo INFOS = new ShapeInfo(6, false, GL_LINES);

	protected final Vector2f[] vertexes = new Vector2f[6];
	protected final Color color;
	protected final int width;
	
	public TriangleBorder(Vector2f a, Vector2f b, Vector2f c, int width, Color color) {
		
		vertexes[0] = a;
		vertexes[1] = b;
		vertexes[2] = b;
		vertexes[3] = c;
		vertexes[4] = c;
		vertexes[5] = a;
		this.width = width;
		this.color = color;
		
	}
	
	public TriangleBorder(Vector2f a, Vector2f b, Vector2f c, int width) {
		
		this(a, b, c, width, Color.black);
		
	}
	
	public TriangleBorder(Vector2f a, Vector2f b, Vector2f c, Color color) {
		
		this(a, b, c, 1, color);
		
	}
	
	public TriangleBorder(Vector2f a, Vector2f b, Vector2f c) {
		
		this(a, b, c, Color.black);
		
	}
	
	@Override
	public void fillBuffer(ByteBuffer b) {
		
		for(int i = 0; i < 6; i++) {
			
			b.putFloat(vertexes[i].x).putFloat(vertexes[i].y).put((byte) color.getRed()).put((byte) color.getGreen()).put((byte) color.getBlue()).put((byte) color.getAlpha());
			
		}
	
	}
	
	public int getLineWidth() {
		
		return width;
		
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
		
		return new Vector2f((vertexes[0].x + vertexes[2].x + vertexes[4].x) / 3, (vertexes[0].y + vertexes[2].y + vertexes[4].y) / 3);
		
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
		
		glBegin(INFOS.getGlMode());
			Shape.glColor(color);
			Shape.glVertexes(vertexes);
		glEnd();

	}
	
	@Override
	public Shape rotate(float angle) {
		
		return rotate(angle, getCenter());
		
	}
	
	@Override
	public Shape rotate(float angle, Vector2f center) {
		
		Vector2f[] rotatedVertexes = Shape.applyTransform(getVertexes(), Shape.rotationMatrix(angle), center);
		
		return new TriangleBorder(rotatedVertexes[0], rotatedVertexes[2], rotatedVertexes[4], color);
		
	}

	@Override
	public Shape transform(float[][] transform, Vector2f center) {
		
		return transform(Shape.getMatrix(transform), center);
		
	}

	@Override
	public Shape transform(Matrix2f transform, Vector2f center) {

		Vector2f[] newVertexes = Shape.applyTransform(getVertexes(), transform, center);
		
		return new TriangleBorder(newVertexes[0], newVertexes[2], newVertexes[4], color);
		
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
		
		return new TriangleBorder(newVertexes[0], newVertexes[2], newVertexes[4], color);
		
	}

	@Override
	public Shape sheer(Vector2f sheer) {

		return sheer(sheer, getCenter());
		
	}

	@Override
	public Shape scale(Vector2f scale, Vector2f center) {

		Vector2f[] newVertexes = Shape.applyTransform(getVertexes(), Shape.scalingMatrix(scale.getX(), scale.getY()), center);
		
		return new TriangleBorder(newVertexes[0], newVertexes[2], newVertexes[4], color);
		
	}

	@Override
	public Shape scale(Vector2f scale) {

		return scale(scale, getCenter());
		
	}

	@Override
	public Shape translate(Vector2f translation) {

		Vector2f[] newVertexes = Shape.applyTranslate(getVertexes(), translation);
		
		return new TriangleBorder(newVertexes[0], newVertexes[2], newVertexes[4], color);
		
	}

	@Override
	public Shape color(Color newColor) {

		return new TriangleBorder(getVertexes()[0], getVertexes()[2], getVertexes()[4], newColor);
		
	}

	@Override
	public Shape color(Color newColor, BinaryOperator<Color> blend) {

		return new TriangleBorder(getVertexes()[0], getVertexes()[2], getVertexes()[4], blend.apply(color, newColor));
		
	}

}
