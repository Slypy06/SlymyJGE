package fr.slypy.slymyjge.graphics;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.slypy.slymyjge.Game;
import fr.slypy.slymyjge.graphics.shape.Shape;
import fr.slypy.slymyjge.graphics.shape.ShapeInfo;

public class ShapeBundle<T extends Shape> {
	
	private List<T> shapes = new ArrayList<>();
	private final long size;
	private int bufferId = 0;
	private boolean awaitingPush = false;
	private final ShapeInfo infos;
	private int texture = 0;
	
	public ShapeBundle(int maxSize, Game g, ShapeInfo infos) {
		
		this.size = maxSize;
		this.infos = infos;
		
		g.executeInRenderThread(() -> {
			
			int id = glGenBuffers();
			glBindBuffer(GL_ARRAY_BUFFER, id);
			glBufferData(GL_ARRAY_BUFFER, size*infos.getVertexCount()*infos.vertexSize(), GL_DYNAMIC_DRAW);
			glBindBuffer(GL_ARRAY_BUFFER, 0);
			
			bufferId = id;
			
		});
		
	}
	
	public void setTexture(Texture texture) {
		
		this.texture = texture.getId(); 
		
	}
	
	public int getTexture() {
		
		return texture;
		
	}
	
	@SafeVarargs
	public final void addShapes(T... t) {
		
		if(shapes.size() + t.length <= size) {
			
			shapes.addAll(Arrays.asList(t));
			
		} else {
			
			throw new IllegalArgumentException("Adding " + t.length + " shapes to the bundle containing " + shapes.size() + " exceed the size limit of " + size);
			
		}
		
	}
	
	public final void removeShapes(int... indexes) {
		
		for(int id : indexes) {
			
			if(id >= 0 && id < shapes.size())
				shapes.remove(id);
			
		}
		
	}
	
	public List<T> getShapes() {
		
		return shapes;
		
	}
	
	public void setShape(int id, T shape) {
		
		if(id >= 0 && id < shapes.size())
			shapes.set(id, shape);
		
	}
	
	public boolean isAwaitingPush() {
		
		return awaitingPush;
		
	}
	
	public boolean awaitingPush() {
		
		if(awaitingPush) {
			
			awaitingPush = false;
			return true;
			
		}
		
		return false;
		
	}
	
	public void pushChanges() {

		awaitingPush = true;
		
	}
	
	public int getBufferId() {
		
		return bufferId;
		
	}
	
	public void fillBuffer(ByteBuffer b) {
		
		for(T shape : shapes) {
			
			shape.fillBuffer(b);
			
		}
		
	}
	
	public void free() {
		
		glDeleteBuffers(bufferId);
		
	}
	
	public ShapeInfo getInfos() {
		
		return infos;
		
	}

	public int getSize() {

		return shapes.size();

	}
	
	public long getMaxSize() {

		return size;

	}
	
	public void empty() {
		
		shapes.clear();
		
	}
	
	@Override
	public String toString() {
		
		StringBuilder b = new StringBuilder();
		
		b.append("ShapeBundle(").append(shapes.size()).append("/").append(size).append(") content :\n");
		
		for(Shape s : shapes) {
			
			b.append(s).append('\n');
			
		}
		
		return b.toString();
		
	}

}
