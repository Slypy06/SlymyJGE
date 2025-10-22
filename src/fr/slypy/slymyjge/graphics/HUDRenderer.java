package fr.slypy.slymyjge.graphics;

import static org.lwjgl.opengl.GL11.GL_LINE_STRIP;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLineWidth;

import java.awt.Color;

import fr.slypy.slymyjge.animations.framed.Animation;
import fr.slypy.slymyjge.font.SlymyFont;

public class HUDRenderer extends Renderer {
	
	public static void renderQuad(float x, float y, int w, int h, Color color) {
		
		x += game.getXCam();
		y += game.getYCam();
		
		x *= game.getWidthDiff();
		y *= game.getHeightDiff();
		
		w *= game.getWidthDiff();
		h *= game.getHeightDiff();
		
		if(d != 0.0D) {
			
			rotate(d, w, h, x, y);
			
		}
		
			glBegin(GL_QUADS);
				Renderer.quadData(x, y, w, h, color);
			glEnd();
			
		if(d != 0.0D) {
				
			rotate(-d, w, h, x, y);
				
		}
		
	}
	
	public static void renderQuad(float x, float y, int w, int h) {
		
		renderQuad(x, y, w, h, Color.white);
		
	}
	
	public static void renderBorder(float x, float y, int w, int h, Color color) {
		
		renderBorder(x, y, w, h, 1, color);
		
	}
	
	public static void renderBorder(float x, float y, int w, int h) {
		
		renderBorder(x, y, w, h, 1, Color.white);
		
	}
	
	public static void renderBorder(float x, float y, int w, int h, int width, Color color) {
		
		x += game.getXCam();
		y += game.getYCam();
		
		x *= game.getWidthDiff();
		y *= game.getHeightDiff();
		
		w *= game.getWidthDiff();
		h *= game.getHeightDiff();
		
		if(d != 0.0D) {
			
			rotate(d, w, h, x, y);
			
		}
		
		glLineWidth(width);
		glBegin(GL_LINE_STRIP);
		
			lineData(x, y, x + w, y, color);          // h h
			lineData(x + w, y - (width > 1 ? ((float) width / 2F) : 0), x + w, y + h + (width > 1 ? ((float) width / 2F) : 0), color);  // v d
			lineData(x + w, y + h, x, y + h, color);  // h b
			lineData(x, y + h + (width > 1 ? ((float) width / 2F) : 0), x, y - (width > 1 ? ((float) width / 2F) : 1), color);          // v g
		
		glEnd();
			
		if(d != 0.0D) {
				
			rotate(-d, w, h, x, y);
				
		}
		
	}
	
	public static void renderBorder(float x, float y, int w, int h, int width) {
		
		renderBorder(x, y, w, h, width, Color.white);
		
	}
	
	public static void renderTriangle(float ax, float ay, float bx, float by, float cx, float cy, Color color) {
		
		ax += game.getXCam();
		ay += game.getYCam();
		
		bx += game.getXCam();
		by += game.getYCam();
		
		cx += game.getXCam();
		cy += game.getYCam();
		
		ax *= game.getWidthDiff();
		ay *= game.getHeightDiff();
		
		bx *= game.getWidthDiff();
		by *= game.getHeightDiff();
		
		cx *= game.getWidthDiff();
		cy *= game.getHeightDiff();
		
		if(d != 0.0D) {
			
			triangleRotate(d, ax, ay, bx, by, cx, cy);
			
		}
		
			glBegin(GL_TRIANGLES);
				Renderer.triangleData(ax, ay, bx, by, cx, cy, color);
			glEnd();
			
		if(d != 0.0D) {
				
			triangleRotate(d, ax, ay, bx, by, cx, cy);
				
		}
		
	}
	
	public static void renderTriangle(float ax, float ay, float bx, float by, float cx, float cy) {
		
		renderTriangle(ax, ay, bx, by, cx, cy, Color.white);
		
	}
	
	public static void renderLine(float ax, float ay, float bx, float by, int width, Color color) {
		
		ax += game.getXCam();
		ay += game.getYCam();
		
		bx += game.getXCam();
		by += game.getYCam();
		
		ax *= game.getWidthDiff();
		ay *= game.getHeightDiff();
		
		bx *= game.getWidthDiff();
		by *= game.getHeightDiff();
		
		if(d != 0.0D) {
			
			lineRotate(d, ax, ay, bx, by);
			
		}
		
		glLineWidth(width);
		glBegin(GL_LINE_STRIP);
			Renderer.lineData(ax, ay, bx, by, color);
		glEnd();
			
		if(d != 0.0D) {
				
			lineRotate(-d, ax, ay, bx, by);
				
		}
		
	}
	
	public static void renderLine(float ax, float ay, float bx, float by, int width) {
		
		renderLine(ax, ay, bx, by, width, Color.white);
		
	}
	
	public static void renderLine(float ax, float ay, float bx, float by, Color color) {
		
		renderLine(ax, ay, bx, by, 1, color);
		
	}
	
	public static void renderLine(float ax, float ay, float bx, float by) {
		
		renderLine(ax, ay, bx, by, 1, Color.white);
		
	}
	
	public static void renderTexturedQuad(float x, float y, int w, int h, Texture texture, Color color) {
		
		x += game.getXCam();
		y += game.getYCam();
		
		x *= game.getWidthDiff();
		y *= game.getHeightDiff();
		
		w *= game.getWidthDiff();
		h *= game.getHeightDiff();
		
		if(d != 0.0D) {
			
			rotate(d, w, h, x, y);
				
		}
		
			texture.bind();
			
				glBegin(GL_QUADS);
				
					Renderer.texturedQuadData(x, y, w, h, color);
					
				glEnd();
			
			texture.unbind();
			
		if(d != 0.0D) {
				
			rotate(-d, w, h, x, y);
					
		}
		
	}
	
	public static void renderTexturedQuad(float x, float y, int w, int h, Texture texture) {
		
		renderTexturedQuad(x, y, w, h, texture, Color.white);
		
	}
	
	public static void renderTexturePart(float x, float y, int w, int h, Color color, int xo, int yo, int maxXo, int maxYo, Texture texture) {
		
		x += game.getXCam();
		y += game.getYCam();
		
		x *= game.getWidthDiff();
		y *= game.getHeightDiff();
		
		w *= game.getWidthDiff();
		h *= game.getHeightDiff();
		
		if(d != 0.0D) {
			
			rotate(d, w, h, x, y);
				
		}
		
			texture.bind();
			
				glBegin(GL_QUADS);
				
					Renderer.texturePartData(x, y, w, h, color, xo, yo, maxXo, maxYo);
					
				glEnd();
			
			texture.unbind();
		
		if(d != 0.0D) {
				
			rotate(-d, w, h, x, y);
					
		}
			
	}
	
	public static void renderTexturePart(float x, float y, int w, int h, Color color, Texture texture) {
		
		renderTexturePart(x, y, w, h, color, 0, 0, 1, 1, texture);
		
	}
	
	public static void renderTexturePart(float x, float y, int w, int h, int xo, int yo, int maxXo, int maxYo, Texture texture) {
		
		renderTexturePart(x, y, w, h, Color.white, xo, yo, maxXo, maxYo, texture);
		
	}
	
	public static void renderTexturePart(float x, float y, int w, int h, Texture texture) {
		
		renderTexturePart(x, y, w, h, Color.white, texture);
		
	}
	
	public static void renderAnimation(float x, float y, int w, int h, Animation animation, Color color) {
		
		x += game.getXCam();
		y += game.getYCam();
		
		x *= game.getWidthDiff();
		y *= game.getHeightDiff();
		
		w *= game.getWidthDiff();
		h *= game.getHeightDiff();
		
		if(d != 0.0D) {
			
			rotate(d, w, h, x, y);
				
		}
		
		animation.render(x, y, w, h, color);
		
		if(d != 0.0D) {
			
			rotate(-d, w, h, x, y);
				
		}
		
	}
	
	public static void renderAnimation(float x, float y, int w, int h, Animation animation) {
		
		x += game.getXCam();
		y += game.getYCam();
		
		x *= game.getWidthDiff();
		y *= game.getHeightDiff();
		
		w *= game.getWidthDiff();
		h *= game.getHeightDiff();
		
		if(d != 0.0D) {
			
			rotate(d, w, h, x, y);
				
		}
		
		animation.render(x, y, w, h);
		
		if(d != 0.0D) {
			
			rotate(-d, w, h, x, y);
				
		}
		
	}
	
	public static void renderText(float x, float y, SlymyFont font, String text) {
		
		x += game.getXCam();
		y += game.getYCam();
		
		for(int i = 0; i < text.length(); i++) {
			
			Texture charTexture = font.getCharTexture(text.charAt(i));
			
			renderTexturedQuad(x, y, charTexture.getWidth(), charTexture.getHeight(), charTexture);
			
			x += font.getCharData(text.charAt(i)).getWidth();
			
		}
		
	}
	
	public static void renderText(float x, float y, SlymyFont font, String text, Color color) {
		
		x += game.getXCam();
		y += game.getYCam();
		
		for(int i = 0; i < text.length(); i++) {
			
			Texture charTexture = font.getCharTexture(text.charAt(i));
			
			renderTexturedQuad(x, y, charTexture.getWidth(), charTexture.getHeight(), charTexture, color);
			
			x += font.getCharData(text.charAt(i)).getWidth();
			
		}
		
	}
	
}
