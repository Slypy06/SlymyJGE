package fr.slypy.slymyjge.graphics.shape;

import static org.lwjgl.opengl.GL11.glColor4ub;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.util.function.BinaryOperator;

import org.lwjgl.util.vector.Matrix2f;
import org.lwjgl.util.vector.Vector2f;

public interface Shape {
	
	public void fillBuffer(ByteBuffer b);
	
	public Vector2f[] getVertexes();
	public Color getColor();
	public Vector2f getCenter();
	
	public Shape transform(float[][] transform, Vector2f center);
	public Shape transform(Matrix2f transform, Vector2f center);
	public Shape transform(float[][] transform);
	public Shape transform(Matrix2f transform);
	public Shape sheer(Vector2f sheer, Vector2f center);
	public Shape sheer(Vector2f sheer);
	public Shape scale(Vector2f scale, Vector2f center);
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
     * Convert a float 2d array into a Matrix2f object
     *
     * @param matrix   the 2x2 float 2 dimensional array
     * @return a Matrix2f object corresponding to the input matrix or null if the input array is an invalid matrix
     */
    public static Matrix2f getMatrix(float[][] matrix) {

    	if(matrix.length != 2 || matrix[0].length != 2)
    		return null;
    	
    	Matrix2f mat = new Matrix2f();
    	mat.m00 = matrix[0][0];
    	mat.m01 = matrix[0][1];
    	mat.m10 = matrix[1][0];
    	mat.m11 = matrix[1][1];
    	
    	return mat;
        
    }
	
	/**
     * Applies a linear translation to an array of vertices.
     *
     * @param vertexes the array of vertices to transform
     * @param matrix   the 2x2 transformation matrix (m00 m01 / m10 m11)
     * @param translation optional translation vector, can be null
     * @return a new array of transformed vertices
     */
    public static Vector2f[] applyTranslate(Vector2f[] vertexes, Vector2f translation) {
    	
        Vector2f[] transformed = new Vector2f[vertexes.length];

        for (int i = 0; i < vertexes.length; i++) {
        	
        	transformed[i] = Vector2f.add(transformed[i], translation, new Vector2f());
        	
        }

        return transformed;
        
    }

    /**
     * Creates a rotation matrix for a given angle in radians.
     *
     * @param angle the rotation angle in radians
     * @return a 2x2 rotation matrix
     */
    public static Matrix2f rotationMatrix(float angle) {
    	
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);
        
    	Matrix2f mat = new Matrix2f();
    	mat.m00 = cos;
    	mat.m01 = -sin;
    	mat.m10 = sin;
    	mat.m11 = cos;
    	
    	return mat;
    	
    }

    /**
     * Creates a scaling matrix.
     *
     * @param sx horizontal scale factor (x-axis)
     * @param sy vertical scale factor (y-axis)
     * @return a 2x2 scaling matrix
     */
    public static Matrix2f scalingMatrix(float sx, float sy) {
    	
    	Matrix2f mat = new Matrix2f();
    	mat.m00 = sx;
    	mat.m01 = 0;
    	mat.m10 = 0;
    	mat.m11 = sy;
    	
    	return mat;
    	
    }

    /**
     * Creates a shear matrix.
     *
     * @param shX horizontal shear factor (x displacement proportional to y)
     * @param shY vertical shear factor (y displacement proportional to x)
     * @return a 2x2 shear matrix
     *
     * Example:
     * <pre>
     * shX = 1 → x' = x + 1*y
     * shY = 0.5 → y' = y + 0.5*x
     * </pre>
     */
    public static Matrix2f shearMatrix(float shX, float shY) {
    	
    	Matrix2f mat = new Matrix2f();
    	mat.m00 = 1;
    	mat.m01 = shX;
    	mat.m10 = shY;
    	mat.m11 = 1;
    	
    	return mat;
   
    }

    /**
     * Multiplies a 2x2 matrix with a 2D vector.
     *
     * @param matrix the 2x2 matrix
     * @param vec    the vector
     * @return a new Vector2f representing the result
     */
    private static Vector2f multiplyMatrixVector(Matrix2f matrix, Vector2f vec) {
 
        return Matrix2f.transform(matrix, vec, new Vector2f());
        
    }

    /**
     * Convenience method to create a transformation around a pivot point.
     * Applies translation(-pivot) → linear transform → translation(+pivot)
     *
     * @param vertexes the vertices
     * @param matrix   the 2x2 linear transformation
     * @param pivot    the pivot point to transform around
     * @return transformed vertices
     */
    public static Vector2f[] applyTransform(Vector2f[] vertexes, Matrix2f matrix, Vector2f pivot) {
    	
        Vector2f[] translated = new Vector2f[vertexes.length];
        for (int i = 0; i < vertexes.length; i++) {
            // move to origin
            Vector2f v = new Vector2f(vertexes[i].x - pivot.x, vertexes[i].y - pivot.y);
            // apply transform
            v = multiplyMatrixVector(matrix, v);
            // move back
            translated[i] = new Vector2f(v.x + pivot.x, v.y + pivot.y);
        }
        
        return translated;
        
    }

}


