package fr.slypy.slymyjge.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glColorPointer;
import static org.lwjgl.opengl.GL11.glDisableClientState;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glTexCoordPointer;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_WRITE_ONLY;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glMapBuffer;
import static org.lwjgl.opengl.GL15.glUnmapBuffer;

import java.nio.ByteBuffer;

import fr.slypy.slymyjge.graphics.shape.Shape;

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
        
        return;
		
	}
	
	public static void renderShape(Shape shape) {
		
		shape.glData();
		
	}
	
	public static boolean renderOnSurface(Runnable render, ISurface surface) {
		
		surface.bind();
		
			render.run();
		
		surface.unbind();
		
		return true;
		
	}
	
	public static void renderWithBlendFunction(Runnable render, BlendFunction func) {
		
		glBlendFunc(func.getSrc(), func.getDst());
		
			render.run();
		
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
	}
	
}
