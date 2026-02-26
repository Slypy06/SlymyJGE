package fr.slypy.slymyjge.graphics.shape;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.util.function.BinaryOperator;

import org.lwjgl.util.vector.Matrix2f;
import org.lwjgl.util.vector.Vector2f;

public class EmptyShape implements Shape {

	@Override
	public void fillBuffer(ByteBuffer b) {}

	@Override
	public Vector2f[] getVertexes() {

		return new Vector2f[] {};
		
	}

	@Override
	public Color getColor() {

		return Color.white;
		
	}

	@Override
	public Vector2f getCenter() {

		return new Vector2f(0, 0);
		
	}

	@Override
	public Shape transform(Matrix2f transform, Vector2f center) {

		return new EmptyShape();
		
	}

	@Override
	public Shape sheer(Vector2f sheer, Vector2f center) {

		return new EmptyShape();
		
	}

	@Override
	public Shape scale(Vector2f scale, Vector2f center) {

		return new EmptyShape();
		
	}

	@Override
	public Shape translate(Vector2f translation) {

		return new EmptyShape();
		
	}

	@Override
	public Shape color(Color newColor, BinaryOperator<Color> blend) {
		
		return new EmptyShape();
		
	}

	@Override
	public Shape rotate(float angle, Vector2f center) {
		
		return new EmptyShape();
		
	}

	@Override
	public void glData() {}

}
