package fr.slypy.slymyjge.graphics.shape.composite;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.util.function.BinaryOperator;
import java.util.function.IntFunction;

import org.lwjgl.util.vector.Matrix2f;
import org.lwjgl.util.vector.Vector2f;

import fr.slypy.slymyjge.graphics.shape.Shape;
import fr.slypy.slymyjge.graphics.shape.ShapeInfo;

public class Circle implements Shape {

	public static final IntFunction<ShapeInfo> INFOS = i -> new ShapeInfo(i*3, false, GL_TRIANGLES);
	
    private final Vector2f center;
    private final float radius;
    private final int segments;
    private final Color color;
    private final Vector2f[] vertexes;

    public Circle(Vector2f center, float radius, int segments, Color color) {
        this.center = center;
        this.radius = radius;
        this.segments = Math.max(3, segments);
        this.color = color;
        this.vertexes = generateVertexes();
    }

    public Circle(Vector2f center, float radius, int segments) {
        this(center, radius, segments, Color.black);
    }
    
    public Circle(Vector2f center, float radius) {
        this(center, radius, 36, Color.black);
    }
    
    private Circle(Vector2f[] vertexes, Color color) {
        this.vertexes = vertexes;
        this.color = color != null ? color : Color.white;
        this.segments = computeSegments();
        this.center = computeCenter();
        this.radius = computeRadius();
    }

    private Vector2f[] generateVertexes() {
        Vector2f[] verts = new Vector2f[segments * 3];

        for (int i = 0; i < segments; i++) {
            float angle1 = (float) (2 * Math.PI * i / segments);
            float angle2 = (float) (2 * Math.PI * (i + 1) / segments);

            Vector2f p1 = new Vector2f(
                    center.x + (float) Math.cos(angle1) * radius,
                    center.y + (float) Math.sin(angle1) * radius
            );

            Vector2f p2 = new Vector2f(
                    center.x + (float) Math.cos(angle2) * radius,
                    center.y + (float) Math.sin(angle2) * radius
            );

            verts[i * 3] = center;
            verts[i * 3 + 1] = p1;
            verts[i * 3 + 2] = p2;
        }

        return verts;
    }
    
    private Vector2f computeCenter() {
        float x = 0, y = 0;
        for (Vector2f v : vertexes) {
            x += v.x;
            y += v.y;
        }
        return new Vector2f(x / vertexes.length, y / vertexes.length);
    }
    
    /** Compute "radius" as the max distance from center to any vertex */
    private float computeRadius() {
        float maxDist = 0;
        for (Vector2f v : vertexes) {
            float dist = Vector2f.sub(v, center, null).length();
            if (dist > maxDist)
                maxDist = dist;
        }
        return maxDist;
    }

    /** Compute "segments" from the number of triangles (vertex count / 3) */
    private int computeSegments() {
        if (vertexes.length % 3 != 0) return 3; // fallback
        return vertexes.length / 3;
    }

    @Override
    public void fillBuffer(ByteBuffer b) {
        for (Vector2f v : vertexes) {
            b.putFloat(v.x).putFloat(v.y)
             .put((byte) color.getRed())
             .put((byte) color.getGreen())
             .put((byte) color.getBlue())
             .put((byte) color.getAlpha());
        }
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
        return new Vector2f(center);
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
        glBegin(GL_TRIANGLES);
        Shape.glColor(color);
        Shape.glVertexes(vertexes);
        glEnd();
    }

    /* ---------- TRANSFORMS ---------- */

    @Override
    public Shape rotate(float angle) {
        return rotate(angle, center);
    }

    @Override
    public Shape rotate(float angle, Vector2f pivot) {
        Vector2f[] newVerts = Shape.applyTransform(vertexes, Shape.rotationMatrix(angle), pivot);
        return new Circle(newVerts, color);
    }

    @Override
    public Shape translate(Vector2f translation) {
        Vector2f[] newVerts = Shape.applyTranslate(vertexes, translation);
        return new Circle(newVerts, color);
    }

    @Override
    public Shape scale(Vector2f scale) {
        return scale(scale, center);
    }

    @Override
    public Shape scale(Vector2f scale, Vector2f pivot) {
        Vector2f[] newVerts = Shape.applyTransform(vertexes, Shape.scalingMatrix(scale.x, scale.y), pivot);
        return new Circle(newVerts, color);
    }

    @Override
    public Shape transform(Matrix2f transform) {
        return transform(transform, center);
    }

    @Override
    public Shape transform(Matrix2f transform, Vector2f pivot) {
        Vector2f[] newVerts = Shape.applyTransform(vertexes, transform, pivot);
        return new Circle(newVerts, color);
    }

    @Override
    public Shape transform(float[][] transform) {
        return transform(Shape.getMatrix(transform), center);
    }

    @Override
    public Shape transform(float[][] transform, Vector2f pivot) {
        return transform(Shape.getMatrix(transform), pivot);
    }

    @Override
    public Shape sheer(Vector2f sheer) {
        return sheer(sheer, center);
    }

    @Override
    public Shape sheer(Vector2f sheer, Vector2f pivot) {
        Vector2f[] newVerts = Shape.applyTransform(vertexes, Shape.shearMatrix(sheer.x, sheer.y), pivot);
        return new Circle(newVerts, color);
    }

    @Override
    public Shape color(Color newColor) {
        return new Circle(vertexes, newColor);
    }

    @Override
    public Shape color(Color newColor, BinaryOperator<Color> blend) {
        return new Circle(vertexes, blend.apply(color, newColor));
    }
}
