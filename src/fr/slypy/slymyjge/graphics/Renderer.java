package fr.slypy.slymyjge.graphics;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.awt.Color;

import fr.slypy.slymyjge.Game;
import fr.slypy.slymyjge.animations.SimpleAnimation;
import fr.slypy.slymyjge.font.SlymyTrueTypeFont;

public class Renderer {
	
	protected static Game game;
	
	public static void init(Game main) {
		
		game = main;
		
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
	
	protected static void entityData(float x, float y, int w, int h, Color color, int xo, int yo, int maxXo, int maxYo, Texture texture) {
		
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
		
		glBegin(GL_QUADS);
			Renderer.quadData(x, y, w, h, color);
		glEnd();
		
	}
	
	public static void renderTexturedQuad(float x, float y, int w, int h, Texture texture, Color color) {
		
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
	
	public static void renderEntity(float x, float y, int w, int h, Color color, int xo, int yo, int maxXo, int maxYo, Texture texture) {
		
		x *= game.getWidthDiff();
		y *= game.getHeightDiff();
		
		w *= game.getWidthDiff();
		h *= game.getHeightDiff();
		
		texture.bind();
		
			glBegin(GL_QUADS);
			
				Renderer.entityData(x, y, w, h, color, xo, yo, maxXo, maxYo, texture);
				
			glEnd();
		
		texture.unbind();
		
	}
	
	public static void renderEntity(float x, float y, int w, int h, Color color, Texture texture) {
		
		renderEntity(x, y, w, h, color, 0, 0, 1, 1, texture);
		
	}
	
	public static void renderEntity(float x, float y, int w, int h, int xo, int yo, int maxXo, int maxYo, Texture texture) {
		
		renderEntity(x, y, w, h, Color.white, xo, yo, maxXo, maxYo, texture);
		
	}
	
	public static void renderEntity(float x, float y, int w, int h, Texture texture) {
		
		renderEntity(x, y, w, h, Color.white, texture);
		
	}
	
	public static void renderSimpleAnimation(float x, float y, int w, int h, SimpleAnimation animation, int yo, Color color) {
		
		x *= game.getWidthDiff();
		y *= game.getHeightDiff();
		
		w *= game.getWidthDiff();
		h *= game.getHeightDiff();
		
		animation.draw(x, y, w, h, yo, color);
		
	}
	
	public static void renderSimpleAnimation(float x, float y, int w, int h, SimpleAnimation animation, int yo) {
		
		x *= game.getWidthDiff();
		y *= game.getHeightDiff();
		
		w *= game.getWidthDiff();
		h *= game.getHeightDiff();
		
		animation.draw(x, y, w, h, yo);
		
	}
	
	public static void renderString(float x, float y, SlymyTrueTypeFont font, String str) {
		
		x *= game.getWidthDiff();
		y *= game.getHeightDiff();
		
		font.drawString(x, y, str);
		
	}
	
	public static void renderString(float x, float y, SlymyTrueTypeFont font, String str, Color color) {
		
		x *= game.getWidthDiff();
		y *= game.getHeightDiff();
		
		font.drawString(x, y, str, new org.newdawn.slick.Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()));
		
	}
	
}
