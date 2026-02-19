package fr.slypy.slymyjge.graphics;

import static org.lwjgl.opengl.GL11.GL_COLOR_ARRAY;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SCISSOR_TEST;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_COORD_ARRAY;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColorPointer;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDisableClientState;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glScissor;
import static org.lwjgl.opengl.GL11.glTexCoordPointer;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_WRITE_ONLY;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glMapBuffer;
import static org.lwjgl.opengl.GL15.glUnmapBuffer;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import fr.slypy.slymyjge.graphics.shape.Shape;
import fr.slypy.slymyjge.graphics.shape.dynamic.DynamicText;

public class NewGenRenderer {

	public static void renderShapeBundle(ShapeBundle<?> bundle) {

		if(bundle.getSize() == 0) 
			return;
		
        glEnableClientState(GL_VERTEX_ARRAY);
        if(bundle.getInfos().useTexCoords())glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);

        glBindBuffer(GL_ARRAY_BUFFER, bundle.getBufferId());
        
        if(bundle.awaitingPush()) {
        
	        ByteBuffer mapped = glMapBuffer(GL_ARRAY_BUFFER, GL_WRITE_ONLY, null);
	        	bundle.fillBuffer(mapped);
	        glUnmapBuffer(GL_ARRAY_BUFFER);
        
        }
        
        glVertexPointer(2, GL_FLOAT, bundle.getInfos().vertexSize(), 0);
        if(bundle.getInfos().useTexCoords()) 
        	glTexCoordPointer(2, GL_FLOAT, bundle.getInfos().vertexSize(), 2*Float.BYTES);
        glColorPointer(4, GL_UNSIGNED_BYTE, bundle.getInfos().vertexSize(), bundle.getInfos().colorPointerOffset());

        glBindTexture(GL_TEXTURE_2D, bundle.getTexture());
        
        glDrawArrays(bundle.getInfos().getGlMode(), 0, bundle.getInfos().getVertexCount()*bundle.getSize());
        
        glBindTexture(GL_TEXTURE_2D, 0);
        
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        
        glDisableClientState(GL_VERTEX_ARRAY);
        if(bundle.getInfos().useTexCoords()) glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_COLOR_ARRAY);
		
	}
	
	public static void renderShape(Shape shape) {
		
		shape.glData();
		
	}
	
	public static void renderOnSurface(Runnable render, ISurface surface) {
		
		surface.bind();
		
			render.run();
		
		surface.unbind();
		
	}
	
	public static void renderWithBlendFunction(Runnable render, BlendFunction func) {
		
		glBlendFunc(func.getSrc(), func.getDst());
		
			render.run();
		
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
	}
	
	public static void renderInsideArea(int x, int y, int w, int h, Runnable render) {
		
		glScissor(x, y, w, h);
		glEnable(GL_SCISSOR_TEST);

			render.run();
		
		glDisable(GL_SCISSOR_TEST);
	}
	
	public static void renderWithTransform(Transform t, Runnable render) {
		
		glPushMatrix();
		t.transform();
		
			render.run();
		
		glPopMatrix();
		
	}

	public static void renderText(DynamicText text, Vector2f position) {
		
		NewGenRenderer.renderWithTransform(new Transform().translate(position.getX(), position.getY()), () -> 
		
			NewGenRenderer.renderShapeBundle(text.getCharacterBundle())
		
		);
		
	}
	
	public static class Transform {
		
		private List<Runnable> transforms = new ArrayList<>();
		
		public void transform() {
			
			transforms.forEach(t -> t.run());
			
		}
		
		public Transform identity() {
			
			transforms.add(() -> glLoadIdentity());
			
			return this;
			
		}
		
		public Transform translate(float x, float y) {
			
			transforms.add(() -> {
				
				glTranslatef(x, y, 0);
				
			});
			
			return this;
			
		}
		
		public Transform rotate(float angle, float x, float y) {
			
			transforms.add(() -> {
				
				glTranslatef(x, y, 0);
				glRotatef(angle, 0, 0, 1);
				glTranslatef(-x, -y, 0);
				
			});
			
			return this;
			
		}
		
		public Transform scale(float dx, float dy, float x, float y) {
			
			transforms.add(() -> {
				
				glTranslatef(x, y, 0);
				glScalef(dx, dy, 1);
				glTranslatef(-x, -y, 0);
				
			});
			
			return this;
			
		}
		
	}
	
}
