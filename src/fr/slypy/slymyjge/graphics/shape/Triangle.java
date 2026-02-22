package fr.slypy.slymyjge.graphics.shape;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.util.function.BinaryOperator;

import org.lwjgl.util.vector.Matrix2f;
import org.lwjgl.util.vector.Vector2f;

public class Triangle implements Shape {
	
	public static final ShapeInfo INFOS = new ShapeInfo(3, false, GL_TRIANGLES);

	protected final Vector2f[] vertexes = new Vector2f[3];
	protected final Color color;
	
	public Triangle(Vector2f a, Vector2f b, Vector2f c, Color color) {
		
		vertexes[0] = a;
		vertexes[1] = b;
		vertexes[2] = c;
		this.color = color;
		
	}
	
	public Triangle(Vector2f a, Vector2f b, Vector2f c) {
		
		this(a, b, c, Color.black);
		
	}
	
	@Override
	public void fillBuffer(ByteBuffer b) {
		
		b.putFloat(vertexes[0].x).putFloat(vertexes[0].y).put((byte) color.getRed()).put((byte) color.getGreen()).put((byte) color.getBlue()).put((byte) color.getAlpha());
		b.putFloat(vertexes[1].x).putFloat(vertexes[1].y).put((byte) color.getRed()).put((byte) color.getGreen()).put((byte) color.getBlue()).put((byte) color.getAlpha());
		b.putFloat(vertexes[2].x).putFloat(vertexes[2].y).put((byte) color.getRed()).put((byte) color.getGreen()).put((byte) color.getBlue()).put((byte) color.getAlpha());
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
		
		glBegin(INFOS.getGlMode());
			Shape.glColor(color);
			Shape.glVertexes(vertexes);
		glEnd();

	}
	
	@Override
	public Shape rotate(float angle, Vector2f center) {
		
		Vector2f[] rotatedVertexes = Shape.applyTransform(getVertexes(), Shape.rotationMatrix(angle), center);
		
		return new Triangle(rotatedVertexes[0], rotatedVertexes[1], rotatedVertexes[2], color);
		
	}

	@Override
	public Shape transform(Matrix2f transform, Vector2f center) {

		Vector2f[] newVertexes = Shape.applyTransform(getVertexes(), transform, center);
		
		return new Triangle(newVertexes[0], newVertexes[1], newVertexes[2], color);
		
	}

	@Override
	public Shape sheer(Vector2f sheer, Vector2f center) {

		Vector2f[] newVertexes = Shape.applyTransform(getVertexes(), Shape.shearMatrix(sheer.getX(), sheer.getY()), center);
		
		return new Triangle(newVertexes[0], newVertexes[1], newVertexes[2], color);
		
	}

	@Override
	public Shape scale(Vector2f scale, Vector2f center) {

		Vector2f[] newVertexes = Shape.applyTransform(getVertexes(), Shape.scalingMatrix(scale.getX(), scale.getY()), center);
		
		return new Triangle(newVertexes[0], newVertexes[1], newVertexes[2], color);
		
	}

	@Override
	public Shape translate(Vector2f translation) {

		Vector2f[] newVertexes = Shape.applyTranslate(getVertexes(), translation);
		
		return new Triangle(newVertexes[0], newVertexes[1], newVertexes[2], color);
		
	}

	@Override
	public Shape color(Color newColor) {

		return new Triangle(getVertexes()[0], getVertexes()[1], getVertexes()[2], newColor);
		
	}

	@Override
	public Shape color(Color newColor, BinaryOperator<Color> blend) {

		return new Triangle(getVertexes()[0], getVertexes()[1], getVertexes()[2], blend.apply(color, newColor));
		
	}

}
