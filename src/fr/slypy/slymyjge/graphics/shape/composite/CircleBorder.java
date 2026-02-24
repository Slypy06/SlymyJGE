package fr.slypy.slymyjge.graphics.shape.composite;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLineWidth;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.util.function.BinaryOperator;
import java.util.function.IntFunction;

import org.lwjgl.util.vector.Matrix2f;
import org.lwjgl.util.vector.Vector2f;

import fr.slypy.slymyjge.graphics.shape.Shape;
import fr.slypy.slymyjge.graphics.shape.ShapeInfo;

public class CircleBorder implements Shape {

	public static final IntFunction<ShapeInfo> INFOS = i -> new ShapeInfo(i*2, false, GL_LINES);
	
    private final Vector2f center;
    private final float radius;
    private final int segments;
    private final Color color;
    private final int width;
    private final Vector2f[] vertexes;

    public CircleBorder(Vector2f center, float radius, int width, int segments, Color color) {
        this.center = center;
        this.radius = radius;
        this.segments = Math.max(3, segments);
        this.width = width;
        this.color = color;
        this.vertexes = generateVertexes();
    }

    public CircleBorder(Vector2f center, float radius, int width, int segments) {
        this(center, radius, width, segments, Color.black);
    }
    
    public CircleBorder(Vector2f center, float radius, int width) {
        this(center, radius, width, 36, Color.black);
    }
    
    public CircleBorder(Vector2f center, float radius, int width, Color c) {
        this(center, radius, width, 36, Color.black);
    }
    
    public CircleBorder(Vector2f center, float radius) {
        this(center, radius, 1, 36, Color.black);
    }
    
    public CircleBorder(Vector2f center, float radius, Color c) {
        this(center, radius, 1, 36, c);
    }
    
    private CircleBorder(Vector2f[] vertexes, int width, Color color) {
        this.vertexes = vertexes;
        this.color = color != null ? color : Color.white;
        this.width = width;
        this.segments = computeSegments();
        this.center = computeCenter();
        this.radius = computeRadius();
    }

    private Vector2f[] generateVertexes() {
        Vector2f[] verts = new Vector2f[segments * 2];

        for (int i = 0; i < segments; i++) {
            float angle1 = (float) (2 * Math.PI * (i-0.1) / segments);
            float angle2 = (float) (2 * Math.PI * (i + 1.1) / segments);

            Vector2f p1 = new Vector2f(
                    center.x + (float) Math.cos(angle1) * radius,
                    center.y + (float) Math.sin(angle1) * radius
            );

            Vector2f p2 = new Vector2f(
                    center.x + (float) Math.cos(angle2) * radius,
                    center.y + (float) Math.sin(angle2) * radius
            );
            verts[i * 2] = p1;
            verts[i * 2 + 1] = p2;
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
        if (vertexes.length % 2 != 0) return 3; // fallback
        return vertexes.length / 2;
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
    public void glData() {
    	
    	glLineWidth(width);
	        glBegin(GL_LINES);
		        Shape.glColor(color);
		        Shape.glVertexes(vertexes);
	        glEnd();
        glLineWidth(1);
        
    }
    
    public int getLineWidth() {
    	
    	return width;
    	
    }

    /* ---------- TRANSFORMS ---------- */

    @Override
    public Shape rotate(float angle, Vector2f pivot) {
        Vector2f[] newVerts = Shape.applyTransform(vertexes, Shape.rotationMatrix(angle), pivot);
        return new CircleBorder(newVerts, width, color);
    }

    @Override
    public Shape translate(Vector2f translation) {
        Vector2f[] newVerts = Shape.applyTranslate(vertexes, translation);
        return new CircleBorder(newVerts, width, color);
    }

    @Override
    public Shape scale(Vector2f scale, Vector2f pivot) {
        Vector2f[] newVerts = Shape.applyTransform(vertexes, Shape.scalingMatrix(scale.x, scale.y), pivot);
        return new CircleBorder(newVerts, width, color);
    }

    @Override
    public Shape transform(Matrix2f transform, Vector2f pivot) {
        Vector2f[] newVerts = Shape.applyTransform(vertexes, transform, pivot);
        return new CircleBorder(newVerts, width, color);
    }

    @Override
    public Shape sheer(Vector2f sheer, Vector2f pivot) {
        Vector2f[] newVerts = Shape.applyTransform(vertexes, Shape.shearMatrix(sheer.x, sheer.y), pivot);
        return new CircleBorder(newVerts, width, color);
    }

    @Override
    public Shape color(Color newColor, BinaryOperator<Color> blend) {
        return new CircleBorder(vertexes, width, blend.apply(color, newColor));
    }
}
