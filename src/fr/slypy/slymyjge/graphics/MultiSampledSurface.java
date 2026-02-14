package fr.slypy.slymyjge.graphics;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_DRAW_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_COMPLETE;
import static org.lwjgl.opengl.GL30.GL_READ_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_RENDERBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glBindRenderbuffer;
import static org.lwjgl.opengl.GL30.glBlitFramebuffer;
import static org.lwjgl.opengl.GL30.glCheckFramebufferStatus;
import static org.lwjgl.opengl.GL30.glDeleteFramebuffers;
import static org.lwjgl.opengl.GL30.glDeleteRenderbuffers;
import static org.lwjgl.opengl.GL30.glFramebufferRenderbuffer;
import static org.lwjgl.opengl.GL30.glFramebufferTexture2D;
import static org.lwjgl.opengl.GL30.glGenFramebuffers;
import static org.lwjgl.opengl.GL30.glGenRenderbuffers;
import static org.lwjgl.opengl.GL30.glRenderbufferStorageMultisample;

import java.awt.Color;
import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import fr.slypy.slymyjge.Game;

public class MultiSampledSurface implements ISurface {

    private final int msFboId;
    private final int msColorRBO;
    private final int resolveFboId;
    private final int textureId;
    private final int width;
    private final int height;
    private final Game g;
    private final int samples;
    private final Color color;

    public MultiSampledSurface(int width, int height, Color background, int samples, Game g) {
    	
    	this.color = background;
    	
        this.width = width;
        this.height = height;
        this.g = g;
        this.samples = samples;

        // --- MSAA FBO ---
        msFboId = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, msFboId);

        msColorRBO = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, msColorRBO);
        glRenderbufferStorageMultisample(GL_RENDERBUFFER, samples, GL_RGBA8, width, height);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_RENDERBUFFER, msColorRBO);

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
            throw new RuntimeException("MSAA FBO incomplete!");

        // --- Resolve FBO ---
        resolveFboId = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, resolveFboId);

        textureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureId);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer) null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, textureId, 0);
        glBindTexture(GL_TEXTURE_2D, 0);

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
            throw new RuntimeException("Resolve FBO incomplete!");

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }
    
    public MultiSampledSurface(int width, int height, int samples, Game g) {
    	
    	this(width, height, new Color(0,0,0,0), samples, g);
    	
    }

    @Override
    public void bind() {
    	
        glBindFramebuffer(GL_FRAMEBUFFER, msFboId);
        
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		
		glViewport(0, 0, width, height);
		GLU.gluOrtho2D(0, width, 0, height);

        Game.cleanseMatrixForFbo();

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glClearColor(0f, 0f, 0f, 0f);
        glClear(GL_COLOR_BUFFER_BIT);
        
    }

    /** Resolve MSAA buffer to texture */
    public void resolve() {
    	
        glBindFramebuffer(GL_READ_FRAMEBUFFER, msFboId);
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, resolveFboId);
        glBlitFramebuffer(0, 0, width, height, 0, 0, width, height, GL_COLOR_BUFFER_BIT, GL_LINEAR);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        
    }

    @Override
    public void unbind() {
    	
        resolve();  // automatically resolve before unbinding
        
        Game.setViewport(g.getWidth(), g.getHeight());
        Game.restoreMatrix();
        
        GL11.glFinish();
        
    }
    
    public void free() {
    	
    	glDeleteFramebuffers(msFboId);
    	glDeleteFramebuffers(resolveFboId);
    	glDeleteRenderbuffers(msColorRBO);
    	glDeleteTextures(textureId);
    	
    }
    
    public int getSamples() {
    	
    	return samples;
    	
    }

    @Override
    public int getFboId() { 
    	
    	return resolveFboId; 
    	
    }  // use resolved texture FBO

    @Override
    public int getTextureId() { 
    	
    	return textureId; 
    	
    }

    @Override
    public int getWidth() { 
    	
    	return width; 
    	
    }

    @Override
    public int getHeight() {
    	
    	return height; 
    	
    }
    
}
