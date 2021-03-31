package fr.slypy.slymyjge.graphics;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;

import fr.slypy.slymyjge.Game;
import fr.slypy.slymyjge.animations.SimpleAnimation;
import fr.slypy.slymyjge.font.SlymyTrueTypeFont;

public class Renderer {
	
	protected static Game game;
	protected static double d = 0;
	
	public static void init(Game main) {
		
		game = main;
		
	}
	
	protected static void rotate(double d, int w, int h, float x, float y) {
		
        glTranslated(x + (w / 2), y + (h / 2), 0);
        glRotated(d, 0, 0, 1);
        glTranslated(-(x + (w / 2)), -(y + (h / 2)), 0);

	}
	
	protected static void lineRotate(double d, float ax, float ay, float bx, float by) {
		
		float x = ((float) ax + (float) bx) / (float) 2;
		float y = ((float) ay + (float) by) / (float) 2;
		
        glTranslated(x, y, 0);
        glRotated(d, 0, 0, 1);
        glTranslated(-x, -y, 0);

	}
	
	protected static void triangleRotate(double d, float ax, float ay, float bx, float by, float cx, float cy) {
		
		float x = ((float) ax + (float) bx + (float) cx) / (float) 3;
		float y = ((float) ay + (float) by + (float) cy) / (float) 3;
		
        glTranslated(x, y, 0);
        glRotated(d, 0, 0, 1);
        glTranslated(-x, -y, 0);

	}
	
	public static void setRotation(double degres) {
		
		d = degres;
		
	}
	
	public static double getRotation() {
		
		return d;
		
	}

	protected static void texturedQuadData(float x, float y, int w, int h, Color color) {
		
		glColor4f((float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F);
		glTexCoord2f(0, 0); glVertex2f(x, y);
		glTexCoord2f(1, 0); glVertex2f(x + w, y);
		glTexCoord2f(1, 1); glVertex2f(x + w, y + h);
		glTexCoord2f(0, 1); glVertex2f(x, y + h);
		
	}
	
	protected static void quadData(float x, float y, int w, int h, Color color) {
		
		glColor4f((float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F);
		glVertex2f(x, y);
		glVertex2f(x + w, y);
		glVertex2f(x + w, y + h);
		glVertex2f(x, y + h);
		
	}
	
	protected static void triangleData(float ax, float ay, float bx, float by, float cx, float cy, Color color) {
		
		glColor4f((float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F);
		glVertex2f(ax, ay);
		glVertex2f(bx, by);
		glVertex2f(cx, cy);
		
	}
	
	protected static void lineData(float ax, float ay, float bx, float by, Color color) {
		
		glColor4f((float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F);
		glVertex2f(ax, ay);
		glVertex2f(bx, by);
		
	}
	
	protected static void texturePartData(float x, float y, int w, int h, Color color, int xo, int yo, int maxXo, int maxYo, Texture texture) {
		
		glColor4f((float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F);
		glTexCoord2f((0F + xo) / (float) maxXo, (0F + yo) / (float) maxYo); glVertex2f(x, y);
		glTexCoord2f((1F + xo) / (float) maxXo, (0F + yo) / (float) maxYo); glVertex2f(x + w, y);
		glTexCoord2f((1F + xo) / (float) maxXo, (1F + yo) / (float) maxYo); glVertex2f(x + w, y + h);
		glTexCoord2f((0F + xo) / (float) maxXo, (1F + yo) / (float) maxYo); glVertex2f(x, y + h);
		
	}
	
	public static void renderQuad(float x, float y, int w, int h, Color color) {
		
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
	
	public static void renderTriangle(float ax, float ay, float bx, float by, float cx, float cy, Color color) {
		
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
				
			lineRotate(d, ax, ay, bx, by);
				
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
		
		x *= game.getWidthDiff();
		y *= game.getHeightDiff();
		
		w *= game.getWidthDiff();
		h *= game.getHeightDiff();
		
		if(d != 0.0D) {
			
			rotate(d, w, h, x, y);
				
		}
		
			texture.bind();
			
				glBegin(GL_QUADS);
				
					Renderer.texturePartData(x, y, w, h, color, xo, yo, maxXo, maxYo, texture);
					
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
	
	public static void renderSimpleAnimation(float x, float y, int w, int h, SimpleAnimation animation, int yo, Color color) {
		
		x *= game.getWidthDiff();
		y *= game.getHeightDiff();
		
		w *= game.getWidthDiff();
		h *= game.getHeightDiff();
		
		if(d != 0.0D) {
			
			rotate(d, w, h, x, y);
				
		}
		
			animation.draw(x, y, w, h, yo, color);
		
		if(d != 0.0D) {
			
			rotate(-d, w, h, x, y);
				
		}
		
	}
	
	public static void renderSimpleAnimation(float x, float y, int w, int h, SimpleAnimation animation, int yo) {
		
		x *= game.getWidthDiff();
		y *= game.getHeightDiff();
		
		w *= game.getWidthDiff();
		h *= game.getHeightDiff();
		
		if(d != 0.0D) {
			
			rotate(d, w, h, x, y);
				
		}
		
			animation.draw(x, y, w, h, yo);
		
		if(d != 0.0D) {
			
			rotate(-d, w, h, x, y);
				
		}
		
	}
	
	public static void renderString(float x, float y, SlymyTrueTypeFont font, String str) {
		
		x *= game.getWidthDiff();
		y *= game.getHeightDiff();
		
		if(d != 0.0D) {
			
			rotate(d, font.getWidth(str), font.getHeight(), x, y);
				
		}
		
			font.drawString(x, y, str);
		
		if(d != 0.0D) {
			
			rotate(d, font.getWidth(str), font.getHeight(), x, y);
				
		}
		
	}
	
	public static void renderString(float x, float y, SlymyTrueTypeFont font, String str, Color color) {
		
		x *= game.getWidthDiff();
		y *= game.getHeightDiff();
		
		if(d != 0.0D) {
			
			rotate(d, font.getWidth(str), font.getHeight(), x, y);
				
		}
		
			font.drawString(x, y, str, new org.newdawn.slick.Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()));
		
		if(d != 0.0D) {
			
			rotate(-d, font.getWidth(str), font.getHeight(), x, y);
				
		}
		
	}
	
}
