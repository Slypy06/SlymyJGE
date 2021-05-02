package fr.slypy.slymyjge.graphics;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;

import java.awt.Color;

import fr.slypy.slymyjge.animations.SimpleAnimation;

public class HUDRenderer extends Renderer {
	
	public static void renderQuad(float x, float y, int w, int h, Color color) {
		
		x -= game.getXCam();
		y -= game.getYCam();
		
		x *= game.getWidthDiff();
		y *= game.getHeightDiff();
		
		w *= game.getWidthDiff();
		h *= game.getHeightDiff();
		
		glBegin(GL_QUADS);
			Renderer.quadData(x, y, w, h, color);
		glEnd();
		
	}
	
	public static void renderTexturedQuad(float x, float y, int w, int h, Texture texture, Color color) {
		
		x -= game.getXCam();
		y -= game.getYCam();
		
		x *= game.getWidthDiff();
		y *= game.getHeightDiff();
		
		w *= game.getWidthDiff();
		h *= game.getHeightDiff();
		
		texture.bind();
		
			glBegin(GL_QUADS);
			
				Renderer.texturedQuadData(x, y, w, h, color);
				
			glEnd();
		
		texture.unbind();
		
	}
	
	public static void renderTexturedQuad(float x, float y, int w, int h, Texture texture) {
		
		renderTexturedQuad(x, y, w, h, texture, Color.white);
		
	}
	
	public static void renderSimpleAnimation(float x, float y, int w, int h, SimpleAnimation animation, int yo, Color color) {
		
		x -= game.getXCam();
		y -= game.getYCam();
		
		animation.draw(x, y, w, h, yo, color);
		
	}
	
	public static void renderSimpleAnimation(float x, float y, int w, int h, SimpleAnimation animation, int yo) {
	
		x -= game.getXCam();
		y -= game.getYCam();
		
		animation.draw(x, y, w, h, yo);
		
	}
	
}
