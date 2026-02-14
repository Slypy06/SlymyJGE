package fr.slypy.slymyjge.graphics;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINE_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_POINT_SMOOTH;
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
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_COMPLETE;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glCheckFramebufferStatus;
import static org.lwjgl.opengl.GL30.glDeleteFramebuffers;
import static org.lwjgl.opengl.GL30.glFramebufferTexture2D;
import static org.lwjgl.opengl.GL30.glGenFramebuffers;

import java.awt.Color;
import java.nio.ByteBuffer;

import org.lwjgl.util.glu.GLU;

import fr.slypy.slymyjge.Game;

public class Surface implements ISurface {

    private final int fboId;
    private final int textureId;
    private final int width;
    private final int height;
    private final Game g;
    private final Color clearColor;

    public Surface(int width, int height, Color clearColor, Game g) {
    	
        this.width = width;
        this.height = height;
        this.g = g;
        this.clearColor = clearColor;
        
        // Create texture
        textureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureId);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer) null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glBindTexture(GL_TEXTURE_2D, 0);

        // Create FBO
        fboId = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fboId);

        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, textureId, 0);

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
            throw new RuntimeException("FBO setup failed!");

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public Surface(int width, int height, Game g) {
    	
    	this(width, height, new Color(0, 0, 0, 0), g);
    	
    }
    
    @Override
    public void bind() {
    	
        glBindFramebuffer(GL_FRAMEBUFFER, fboId);
        
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		
		glViewport(0, 0, width, height);
		GLU.gluOrtho2D(0, width, 0, height);

        // reset matrices for FBO rendering
        Game.cleanseMatrixForFbo();

        // enable needed flags for 2D rendering
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);   // only useful without MSAA
        glEnable(GL_POINT_SMOOTH);  // only useful without MSAA

        glClearColor(clearColor.getRed() / 255F, clearColor.getGreen() / 255F, clearColor.getBlue() / 255F, clearColor.getAlpha() / 255F);
        glClear(GL_COLOR_BUFFER_BIT);
        
    }

    @Override
    public void unbind() {
    	
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        Game.setViewport(g.getWidth(), g.getHeight());
        Game.restoreMatrix();
        
    }
    
    public void free() {
    	
    	glDeleteFramebuffers(fboId);
    	glDeleteTextures(textureId);
    	
    }

    @Override
    public int getFboId() { return fboId; }

    @Override
    public int getTextureId() { return textureId; }

    @Override
    public int getWidth() { return width; }

    @Override
    public int getHeight() { return height; }

}
