package fr.slypy.slymyjge.graphics.shape;

import static org.lwjgl.opengl.GL11.glColor4ub;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.util.function.BinaryOperator;

import org.lwjgl.util.vector.Vector2f;

public interface Shape {
	
	public void fillBuffer(ByteBuffer b);
	
	public Vector2f[] getVertexes();
	
	public Color getColor();
	
	public Vector2f getCenter();
	
	public Shape scale(Vector2f scale);
	public Shape translate(Vector2f translation);
	public Shape color(Color newColor);
	public Shape color(Color newColor, BinaryOperator<Color> blend);
	public Shape rotate(float angle);
	public Shape rotate(float angle, Vector2f center);
	
	/**
	 * Send the data in immediate mode
	 */
	public void glData();
	
	/**
	 * Send a vertex color data in immediate mode
	 * For internal use
	 */
	public static void glColor(Color c) {
		
		glColor4ub((byte) c.getRed(), (byte) c.getGreen(), (byte) c.getBlue(), (byte) c.getAlpha());
		
	}
	
	/**
	 * Send a vertex data in immediate mode
	 * For internal use
	 */
	public static void glVertexes(Vector2f[] vertexes) {
		
		for(Vector2f v : vertexes) {
			
			glVertex2f(v.x, v.y);
			
		}
		
	}
	
	/**
	 * Send a vertex data in immediate mode using texture coordinate
	 * Internal use only
	 */
	public static void glVertexes(Vector2f[] vertexes, Vector2f[] texCoord) {
		
		if(vertexes.length != texCoord.length)
			throw new IllegalArgumentException("Vertex array must be the same size as Texture Coordinates array");
		
		for(int i = 0; i < vertexes.length; i++) {
			
			glTexCoord2f(texCoord[i].x, texCoord[i].y);
			glVertex2f(vertexes[i].x, vertexes[i].y);
			
		}
		
	}
	
	/**
	 * Rotate an array of vertexes arround a center point
	 * @param vertexes  a vertex array
	 * @param center    the center point to rotate arround
	 * @param angle     the angle of rotation in radians
	 * @return The array containing the rotated vertexes
	 */
	public static Vector2f[] rotateVertexes(Vector2f[] vertexes, Vector2f center, float angle) {
		
	    float sin = (float) Math.sin(angle);
	    float cos = (float) Math.cos(angle);

	    Vector2f[] newVertexes = new Vector2f[vertexes.length];

	    for (int i = 0; i < vertexes.length; i++) {
	        
	        float dx = vertexes[i].x - center.x;
	        float dy = vertexes[i].y - center.y;

	        float rx = dx * cos - dy * sin;
	        float ry = dx * sin + dy * cos;

	        newVertexes[i] = new Vector2f(center.x + rx, center.y + ry);
	        
	    }

	    return newVertexes;
		
	}

}


