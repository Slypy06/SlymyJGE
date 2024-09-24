package fr.slypy.slymyjge.graphics;

import static org.lwjgl.opengl.GL11.GL_LINE_STRIP;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glRotated;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslated;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.awt.Color;

import org.lwjgl.util.vector.Vector2f;

import fr.slypy.slymyjge.Game;
import fr.slypy.slymyjge.animations.Animation;
import fr.slypy.slymyjge.font.SlymyFont;

public class Renderer {
	
	protected static Game game;
	protected static double d = 0;
	protected static boolean resize = true;
	
	public static void init(Game main) {
		
		game = main;
		
	}
	
	public static void doNotResize() {
		
		resize = false;
		
	}
	
	public static void nowYouCanResize() {
		
		resize = true;
		
	}
	
	protected static void rotate(double d, int w, int h, float x, float y) {
		
        glTranslated(x + (w / 2), y + (h / 2), 0);
        glRotated(d, 0, 0, 1);
        glTranslated(-(x + (w / 2)), -(y + (h / 2)), 0);

	}
	
	protected static void rotate(double d, float x, float y) {
		
        glTranslated(x, y, 0);
        glRotated(d, 0, 0, 1);
        glTranslated(-x, -y, 0);

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
	
	protected static void texturePartData(float x, float y, int w, int h, Color color, int xo, int yo, int maxXo, int maxYo) {
		
		glColor4f((float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F);
		glTexCoord2f((0F + xo) / (float) maxXo, (0F + yo) / (float) maxYo); glVertex2f(x, y);
		glTexCoord2f((1F + xo) / (float) maxXo, (0F + yo) / (float) maxYo); glVertex2f(x + w, y);
		glTexCoord2f((1F + xo) / (float) maxXo, (1F + yo) / (float) maxYo); glVertex2f(x + w, y + h);
		glTexCoord2f((0F + xo) / (float) maxXo, (1F + yo) / (float) maxYo); glVertex2f(x, y + h);
		
	}
	
	public static void renderQuad(float x, float y, int w, int h, Color color) {
		
		if(resize) {
		
			x *= game.getWidthDiff();
			y *= game.getHeightDiff();
			
			x += game.getxDiff();
			y += game.getyDiff();
		
			w *= game.getWidthDiff();
			h *= game.getHeightDiff();
		
		}
		
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
		
		if(resize) {
			
			x *= game.getWidthDiff();
			y *= game.getHeightDiff();
		
			x += game.getxDiff();
			y += game.getyDiff();
			
			w *= game.getWidthDiff();
			h *= game.getHeightDiff();
		
		}
		
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
		
		if(resize) {
			
			ax *= game.getWidthDiff();
			ay *= game.getHeightDiff();
			
			ax += game.getxDiff();
			ay += game.getyDiff();
		
			bx *= game.getWidthDiff();
			by *= game.getHeightDiff();
			
			bx += game.getxDiff();
			by += game.getyDiff();
		
			cx *= game.getWidthDiff();
			cy *= game.getHeightDiff();
			
			cx += game.getxDiff();
			cy += game.getyDiff();
		
		}
		
		if(d != 0.0D) {
			
			triangleRotate(d, ax, ay, bx, by, cx, cy);
			
		}
		
			glBegin(GL_TRIANGLES);
				Renderer.triangleData(ax, ay, bx, by, cx, cy, color);
			glEnd();
			
		if(d != 0.0D) {
				
			triangleRotate(-d, ax, ay, bx, by, cx, cy);
				
		}
		
	}
	
	public static void renderTriangle(float ax, float ay, float bx, float by, float cx, float cy) {
		
		renderTriangle(ax, ay, bx, by, cx, cy, Color.white);
		
	}
	
	public static void renderLine(float ax, float ay, float bx, float by, int width, Color color) {
		
		if(resize) {
		
			ax *= game.getWidthDiff();
			ay *= game.getHeightDiff();
			
			ax += game.getxDiff();
			ay += game.getyDiff();
		
			bx *= game.getWidthDiff();
			by *= game.getHeightDiff();
			
			bx += game.getxDiff();
			by += game.getyDiff();
		
		}
		
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
		
		if(resize) {
		
			x *= game.getWidthDiff();
			y *= game.getHeightDiff();
			
			x += game.getxDiff();
			y += game.getyDiff();
		
			w *= game.getWidthDiff();
			h *= game.getHeightDiff();
					
		}
		
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
	
	protected static void renderSimpleTexturedQuad(float x, float y, int w, int h, Texture texture, Color color) {

		texture.bind();
			
			glBegin(GL_QUADS);
				
				Renderer.texturedQuadData(x, y, w, h, color);
					
			glEnd();
			
		texture.unbind();
		
	}
	
	public static void renderTexturedQuad(float x, float y, int w, int h, Texture texture) {
		
		renderTexturedQuad(x, y, w, h, texture, Color.white);
		
	}
	
	public static void renderTexturedQuad(float x, float y, int w, int h, int texture, Color color) {
		
		if(resize) {
			
			x *= game.getWidthDiff();
			y *= game.getHeightDiff();
		
			x += game.getxDiff();
			y += game.getyDiff();
			
			w *= game.getWidthDiff();
			h *= game.getHeightDiff();
					
		}
		
		if(d != 0.0D) {
			
			rotate(d, w, h, x, y);
				
		}
		
			glBindTexture(GL_TEXTURE_2D, texture);
			
				glBegin(GL_QUADS);
				
					Renderer.texturedQuadData(x, y, w, h, color);
					
				glEnd();
			
			glBindTexture(GL_TEXTURE_2D, 0);
			
		if(d != 0.0D) {
				
			rotate(-d, w, h, x, y);
					
		}
		
	}
	
	public static void renderTexturedQuad(float x, float y, int w, int h, int texture) {
		
		renderTexturedQuad(x, y, w, h, texture, Color.white);
		
	}
	
	public static void renderTexturePart(float x, float y, int w, int h, Color color, int xo, int yo, int maxXo, int maxYo, Texture texture) {
		
		if(resize) {
			
			x *= game.getWidthDiff();
			y *= game.getHeightDiff();
			
			x += game.getxDiff();
			y += game.getyDiff();
		
			w *= game.getWidthDiff();
			h *= game.getHeightDiff();
					
		}
		
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
		
		animation.render(x, y, w, h, color);
		
	}
	
	public static void renderAnimation(float x, float y, int w, int h, Animation animation) {
		
		animation.render(x, y, w, h);
		
	}
	
	public static void renderText(float x, float y, SlymyFont font, String text) { 
		
		renderText(x, y, font, text, Color.white);
		
	}
	
	public static void renderText(float x, float y, SlymyFont font, String text, Color color) { //TODO
		
		if(resize) {
			
			x *= game.getWidthDiff();
			y *= game.getHeightDiff();
			
			x += game.getxDiff();
			y += game.getyDiff();
					
		}
		
		if(d != 0) {
			
			rotate(d, font.getWidth(text), font.getHeight(), x, y);
			
		}
		
		for(int i = 0; i < text.length(); i++) {
			
			Texture charTexture = font.getCharTexture(text.charAt(i));
			
			renderSimpleTexturedQuad(x, y, (int) (charTexture.getWidth() * (resize ? game.getWidthDiff() : 1)), (int) (charTexture.getHeight() * (resize ? game.getHeightDiff() : 1)), charTexture, color);
			
			x += font.getCharData(text.charAt(i)).getWidth() * (resize ? game.getWidthDiff() : 1);
			
		}
		
		if(d != 0) {
			
			rotate(-d, font.getWidth(text), font.getHeight(), x, y);
			
		}
		
	}
	
	public static void renderCross(float x, float y, int w, int h) {
		
		renderCross(x, y, w, h, 1, Color.white);
		
	}
	
	public static void renderCross(float x, float y, int w, int h, Color color) {
		
		renderCross(x, y, w, h, 1, color);
		
	}
	
	public static void renderCross(float x, float y, int w, int h, int size) {
		
		renderLine(x - (w / 2), y, x + (w / 2), y, size, Color.white);
		renderLine(x, y - (h / 2), x, y + (h / 2), size, Color.white);
		
	}
	
	public static void renderCross(float x, float y, int w, int h, int size, Color color) {
		
		renderLine(x - (w / 2), y, x + (w / 2), y, size, color);
		renderLine(x, y - (h / 2), x, y + (h / 2), size, color);
		
	}
	
	public static void renderDisk(float x, float y, int radius) {
		
		renderFilledEllipse(x, y, radius, radius, Color.white);
		
	}
	
	public static void renderDisk(float x, float y, int radius, Color c) {
		
		renderFilledEllipse(x, y, radius, radius, c);
		
	}
	
	public static void renderDiskArc(float x, float y, int radius, int startingAngle, int arcAngle) {
		
		renderFilledEllipseArc(x, y, radius, radius, startingAngle, arcAngle, Color.white);
		
	}
	
	public static void renderDiskArc(float x, float y, int radius, int startingAngle, int arcAngle, Color c) {

		renderFilledEllipseArc(x, y, radius, radius, startingAngle, arcAngle, c);
		
	}
	
	public static void renderCircle(float x, float y, int radius) {
		
		renderEllipse(x, y, radius, radius, 2, Color.white);
		
	}
	
	public static void renderCircle(float x, float y, int radius, Color c) {
		
		renderEllipse(x, y, radius, radius, 2, c);
		
	}
	
	public static void renderCircle(float x, float y, int radius, int width) {
		
		renderEllipse(x, y, radius, radius, width, Color.white);
		
	}
	
	public static void renderCircle(float x, float y, int radius, int width, Color c) {
		
		renderEllipse(x, y, radius, radius, width, c);
		
	}
	
	public static void renderCircleArc(float x, float y, int radius, int startingAngle, int arcAngle) {
		
		renderEllipseArc(x, y, radius, radius, startingAngle, arcAngle, 2, Color.white);
		
	}
	
	public static void renderCircleArc(float x, float y, int radius, int startingAngle, int arcAngle, Color c) {
		
		renderEllipseArc(x, y, radius, radius, startingAngle, arcAngle, 2, c);
		
	}
	
	public static void renderCircleArc(float x, float y, int radius, int startingAngle, int arcAngle, int width) {
		
		renderEllipseArc(x, y, radius, radius, startingAngle, arcAngle, width, Color.white);
		
	}
	
	public static void renderCircleArc(float x, float y, int radius, int startingAngle, int arcAngle, int width, Color c) {
		
		renderEllipseArc(x, y, radius, radius, startingAngle, arcAngle, width, c);
		
	}
	
	public static void renderFilledEllipse(float x, float y, int horizontalRadius, int verticalRadius) {
		
		renderFilledEllipse(x, y, horizontalRadius, verticalRadius, Color.white);
		
	}
	
	public static void renderFilledEllipse(float x, float y, int horizontalRadius, int verticalRadius, Color c) {
		
		if(resize) {
			
			x *= game.getWidthDiff();
			y *= game.getHeightDiff();
			
			x += game.getxDiff();
			y += game.getyDiff();
			
			horizontalRadius *= game.getWidthDiff();
			verticalRadius *= game.getHeightDiff();
					
		}
		
		int precision = 360;
		
		float step = 360.0F / (float) precision;
		
		for(int i = 0; i < precision; i++) {
			
			float angle = (float) i * step;

			Vector2f p2 = calculateEllipsePoint(horizontalRadius, verticalRadius, angle);
			Vector2f p3 = calculateEllipsePoint(horizontalRadius, verticalRadius, angle + step);

			if(d != 0) {
				
				rotate(d, x, y);
				
			}
			
				glBegin(GL_TRIANGLES);
					Renderer.triangleData(x, y, p2.x + x, p2.y + y, p3.x + x, p3.y + y, c);
				glEnd();
			
			if(d != 0) {
				
				rotate(-d, x, y);
				
			}
			
		}
		
	}
	
	public static void renderSimpleFilledEllipse(float x, float y, int horizontalRadius, int verticalRadius, Color c) {
		
		int precision = 360;
		
		float step = 360.0F / (float) precision;
		
		for(int i = 0; i < precision; i++) {
			
			float angle = (float) i * step;

			Vector2f p2 = calculateEllipsePoint(horizontalRadius, verticalRadius, angle);
			Vector2f p3 = calculateEllipsePoint(horizontalRadius, verticalRadius, angle + step);

			glBegin(GL_TRIANGLES);
				Renderer.triangleData(x, y, p2.x + x, p2.y + y, p3.x + x, p3.y + y, c);
			glEnd();
			
		}
		
	}
	
	public static void renderFilledEllipseArc(float x, float y, int horizontalRadius, int verticalRadius, int startingAngle, int arcAngle) {
		
		renderFilledEllipseArc(x, y, horizontalRadius, verticalRadius, startingAngle, arcAngle, Color.white);
		
	}
	
	public static void renderFilledEllipseArc(float x, float y, int horizontalRadius, int verticalRadius, int startingAngle, int arcAngle, Color c) {

		if(resize) {
		
			x *= game.getWidthDiff();
			y *= game.getHeightDiff();
			
			x += game.getxDiff();
			y += game.getyDiff();
		
			horizontalRadius *= game.getWidthDiff();
			verticalRadius *= game.getHeightDiff();
		
		}
		
		int precision = 360;
		
		if(arcAngle > 360 || arcAngle < -360) {
			
			arcAngle = 360;
			
		}
		
		if(arcAngle < 0) {
			
			startingAngle += arcAngle;
			arcAngle = -arcAngle;
			
		}
		
		startingAngle -= 90;
		
		while(startingAngle < 360) {
			
			startingAngle += 360;
			
		}
		
		while(startingAngle > 360) {
			
			startingAngle -= 360;
			
		}
		
		if(startingAngle < 0) {
			
			startingAngle = 360 + startingAngle;
			
		}
		
		float step = (float) arcAngle / (float) precision;
		
		float lowestX = x;
		float lowestY = y;
		float highestX = x;
		float highestY = y;
		
		if(d != 0) {
			
			for(int i = 0; i < precision; i++) {
				
				float angle = (float) i * step + startingAngle;
				
				Vector2f p2 = calculateEllipsePoint(horizontalRadius, verticalRadius, angle);
				
				if(p2.x + x < lowestX) {
					
					lowestX = p2.x + x;
					
				}
				
				if(p2.y + y < lowestY) {
					
					lowestY = p2.y + y;
					
				}
				
				if(p2.x + x > highestX) {
					
					highestX = p2.x + x;
					
				}
				
				if(p2.y + y > highestY) {
					
					highestY = p2.y + y;
					
				}
				
			}
			
		}
		
		for(int i = 0; i < precision; i++) {
			
			float angle = (float) i * step + startingAngle;

			Vector2f p2 = calculateEllipsePoint(horizontalRadius, verticalRadius, angle);
			Vector2f p3 = calculateEllipsePoint(horizontalRadius, verticalRadius, angle + step);
			
			if(d != 0) {
				
				rotate(d, (int) (highestX - lowestX), (int) (highestY - lowestY), lowestX, lowestY);
				
			}
			
				glBegin(GL_TRIANGLES);
					Renderer.triangleData(x, y, p2.x + x, p2.y + y, p3.x + x, p3.y + y, c);
				glEnd();
			
			if(d != 0) {
				
				rotate(-d, (int) (highestX - lowestX), (int) (highestY - lowestY), lowestX, lowestY);
				
			}
			
		}
		
	}
	
	public static void renderSimpleFilledEllipseArc(float x, float y, int horizontalRadius, int verticalRadius, int startingAngle, int arcAngle, Color c) {
		
		int precision = 360;
		
		if(arcAngle > 360 || arcAngle < -360) {
			
			arcAngle = 360;
			
		}
		
		if(arcAngle < 0) {
			
			startingAngle += arcAngle;
			arcAngle = -arcAngle;
			
		}
		
		startingAngle -= 90;
		
		while(startingAngle < 360) {
			
			startingAngle += 360;
			
		}
		
		while(startingAngle > 360) {
			
			startingAngle -= 360;
			
		}
		
		if(startingAngle < 0) {
			
			startingAngle = 360 + startingAngle;
			
		}
		
		float step = (float) arcAngle / (float) precision;
		
		for(int i = 0; i < precision; i++) {
			
			float angle = (float) i * step + startingAngle;

			Vector2f p2 = calculateEllipsePoint(horizontalRadius, verticalRadius, angle);
			Vector2f p3 = calculateEllipsePoint(horizontalRadius, verticalRadius, angle + step);
			
			glBegin(GL_TRIANGLES);
				Renderer.triangleData(x, y, p2.x + x, p2.y + y, p3.x + x, p3.y + y, c);
			glEnd();
			
		}
		
	}
	
	public static void renderEllipse(float x, float y, int horizontalRadius, int verticalRadius) {
		
		renderEllipse(x, y, horizontalRadius, verticalRadius, 2, Color.white);
		
	}
	
	public static void renderEllipse(float x, float y, int horizontalRadius, int verticalRadius, Color c) {
		
		renderEllipse(x, y, horizontalRadius, verticalRadius, 2, c);
		
	}
	
	public static void renderEllipse(float x, float y, int horizontalRadius, int verticalRadius, int width) {
		
		renderEllipse(x, y, horizontalRadius, verticalRadius, width, Color.white);
		
	}
	
	public static void renderEllipse(float x, float y, int horizontalRadius, int verticalRadius, int width, Color c) {
		
		if(resize) {

			x *= game.getWidthDiff();
			y *= game.getHeightDiff();
			
			x += game.getxDiff();
			y += game.getyDiff();
		
			horizontalRadius *= game.getWidthDiff();
			verticalRadius *= game.getHeightDiff();
		
		}
		
		int precision = 180;
		
		float step = 360.0F / (float) precision;
		
		if(d != 0) {
			
			rotate(d, x, y);
			
		}
		
		glLineWidth(width);
		glBegin(GL_LINE_STRIP);
		
		for(int i = 0; i < precision; i++) {
			
			float angle = (float) i * step;

			Vector2f p2 = calculateEllipsePoint(horizontalRadius, verticalRadius, angle);
			Vector2f p3 = calculateEllipsePoint(horizontalRadius, verticalRadius, angle + step);

			Renderer.lineData(p2.x + x, p2.y + y, p3.x + x, p3.y + y, c);
			
		}
		
		glEnd();
		
		if(d != 0) {
			
			rotate(-d, x, y);
			
		}
		
	}
	
	public static void renderEllipseArc(float x, float y, int horizontalRadius, int verticalRadius, int startingAngle, int arcAngle) {
		
		renderEllipseArc(x, y, horizontalRadius, verticalRadius, startingAngle, arcAngle, 2, Color.white);
		
	}
	
	public static void renderEllipseArc(float x, float y, int horizontalRadius, int verticalRadius, int startingAngle, int arcAngle, Color c) {
		
		renderEllipseArc(x, y, horizontalRadius, verticalRadius, startingAngle, arcAngle, 2, c);
		
	}
	
	public static void renderEllipseArc(float x, float y, int horizontalRadius, int verticalRadius, int startingAngle, int arcAngle, int width) {
		
		renderEllipseArc(x, y, horizontalRadius, verticalRadius, startingAngle, arcAngle, width, Color.white);
		
	}
	
	public static void renderEllipseArc(float x, float y, int horizontalRadius, int verticalRadius, int startingAngle, int arcAngle, int width, Color c) {

		if(resize) {
		
			x *= game.getWidthDiff();
			y *= game.getHeightDiff();
			
			x += game.getxDiff();
			y += game.getyDiff();
		
			horizontalRadius *= game.getWidthDiff();
			verticalRadius *= game.getHeightDiff();
		
		}
		
		int precision = 360;
		
		if(arcAngle > 360 || arcAngle < -360) {
			
			arcAngle = 360;
			
		}
		
		if(arcAngle < 0) {
			
			startingAngle += arcAngle;
			arcAngle = -arcAngle;
			
		}
		
		startingAngle -= 90;
		
		while(startingAngle < 360) {
			
			startingAngle += 360;
			
		}
		
		while(startingAngle > 360) {
			
			startingAngle -= 360;
			
		}
		
		if(startingAngle < 0) {
			
			startingAngle = 360 + startingAngle;
			
		}
		
		float step = (float) arcAngle / (float) precision;
		
		float lowestX = x;
		float lowestY = y;
		float highestX = x;
		float highestY = y;
		
		if(d != 0) {
			
			for(int i = 0; i < precision; i++) {
				
				float angle = (float) i * step + startingAngle;
				
				Vector2f p2 = calculateEllipsePoint(horizontalRadius, verticalRadius, angle);
				
				if(p2.x + x < lowestX) {
					
					lowestX = p2.x + x;
					
				}
				
				if(p2.y + y < lowestY) {
					
					lowestY = p2.y + y;
					
				}
				
				if(p2.x + x > highestX) {
					
					highestX = p2.x + x;
					
				}
				
				if(p2.y + y > highestY) {
					
					highestY = p2.y + y;
					
				}
				
			}
			
		}
		
		glLineWidth(width);
		glBegin(GL_LINE_STRIP);
		
		for(int i = 0; i < precision; i++) {
			
			float angle = (float) i * step + startingAngle;

			Vector2f p2 = calculateEllipsePoint(horizontalRadius, verticalRadius, angle);
			Vector2f p3 = calculateEllipsePoint(horizontalRadius, verticalRadius, angle + step);
			
			if(d != 0) {
				
				rotate(d, (int) (highestX - lowestX), (int) (highestY - lowestY), lowestX, lowestY);
				
			}
			
			Renderer.lineData(p2.x + x, p2.y + y, p3.x + x, p3.y + y, c);
			
			if(d != 0) {
				
				rotate(-d, (int) (highestX - lowestX), (int) (highestY - lowestY), lowestX, lowestY);
				
			}
			
		}
		
		glEnd();
		
	}
	
	public static void renderSimpleEllipseArc(float x, float y, int horizontalRadius, int verticalRadius, int startingAngle, int arcAngle, int width, Color c) {
		
		int precision = 360;
		
		if(arcAngle > 360 || arcAngle < -360) {
			
			arcAngle = 360;
			
		}
		
		if(arcAngle < 0) {
			
			startingAngle += arcAngle;
			arcAngle = -arcAngle;
			
		}
		
		startingAngle -= 90;
		
		while(startingAngle < 360) {
			
			startingAngle += 360;
			
		}
		
		while(startingAngle > 360) {
			
			startingAngle -= 360;
			
		}
		
		if(startingAngle < 0) {
			
			startingAngle = 360 + startingAngle;
			
		}
		
		float step = (float) arcAngle / (float) precision;
		
		float lowestX = x;
		float lowestY = y;
		float highestX = x;
		float highestY = y;
		
		if(d != 0) {
			
			for(int i = 0; i < precision; i++) {
				
				float angle = (float) i * step + startingAngle;
				
				Vector2f p2 = calculateEllipsePoint(horizontalRadius, verticalRadius, angle);
				
				if(p2.x + x < lowestX) {
					
					lowestX = p2.x + x;
					
				}
				
				if(p2.y + y < lowestY) {
					
					lowestY = p2.y + y;
					
				}
				
				if(p2.x + x > highestX) {
					
					highestX = p2.x + x;
					
				}
				
				if(p2.y + y > highestY) {
					
					highestY = p2.y + y;
					
				}
				
			}
			
		}
		
		glLineWidth(width);
		glBegin(GL_LINE_STRIP);
		
		for(int i = 0; i < precision; i++) {
			
			float angle = (float) i * step + startingAngle;

			Vector2f p2 = calculateEllipsePoint(horizontalRadius, verticalRadius, angle);
			Vector2f p3 = calculateEllipsePoint(horizontalRadius, verticalRadius, angle + step);
			
			Renderer.lineData(p2.x + x, p2.y + y, p3.x + x, p3.y + y, c);
			
		}
		
		glEnd();
		
	}
	
	private static Vector2f calculateEllipsePoint(float a, float b, double d) {
		
		Vector2f point = new Vector2f();
		
		if(d > 90 && d <= 270) {
			
			double radAngle = Math.toRadians(d);
			
			point.x = (float) -((double) (a*b) / Math.sqrt(b*b + a*a * Math.tan(radAngle)*Math.tan(radAngle)));
			point.y = (float) -((double) (a*b * Math.tan(radAngle)) / Math.sqrt(b*b + a*a * Math.tan(radAngle)*Math.tan(radAngle)));
			
		} else {
			
			double radAngle = Math.toRadians(d);
			
			point.x = (float) ((double) (a*b) / Math.sqrt(b*b + a*a * Math.tan(radAngle)*Math.tan(radAngle)));
			point.y = (float) ((double) (a*b * Math.tan(radAngle)) / Math.sqrt(b*b + a*a * Math.tan(radAngle)*Math.tan(radAngle)));
			
		}
		
		return point;
		
	}
	
	public static void renderRoundedQuad(float x, float y, int w, int h, int radius, Color c) {
				
		if(resize) {
		
			x *= game.getWidthDiff();
			y *= game.getHeightDiff();
		
			x += game.getxDiff();
			y += game.getyDiff();
			
			w *= game.getWidthDiff();
			h *= game.getHeightDiff();
		
		}
		
		int hradius = (int) (radius * game.getWidthDiff());
		int vradius = (int) (radius * game.getHeightDiff());
		
		if(d != 0) {
			
			rotate(d, w, h, x, y);
			
		}
		
		glBegin(GL_QUADS);
			Renderer.quadData(x + hradius, y, w - 2 * hradius, h, c);
		glEnd();
		
		glBegin(GL_QUADS);
			Renderer.quadData(x, y + vradius, w, h - 2 * vradius, c);
		glEnd();
		
		Renderer.renderSimpleFilledEllipse(x + hradius, y + vradius, hradius, vradius, c);
		Renderer.renderSimpleFilledEllipse(x + w - hradius, y + vradius, hradius, vradius, c);
		Renderer.renderSimpleFilledEllipse(x + hradius, y + h - vradius, hradius, vradius, c);
		Renderer.renderSimpleFilledEllipse(x + w - hradius, y + h - vradius, hradius, vradius, c);
		
		if(d != 0) {
			
			rotate(-d, w, h, x, y);
			
		}
		
	}
	
	public static void renderRoundedQuad(float x, float y, int w, int h, int radius) {
		
		renderRoundedQuad(x, y, w, h, radius, Color.white);
		
	}
	
	public static void renderRoundedBorder(float x, float y, int w, int h, int radius, int width, Color c) {

		if(resize) {
		
			x *= game.getWidthDiff();
			y *= game.getHeightDiff();
			
			x += game.getxDiff();
			y += game.getyDiff();
		
			w *= game.getWidthDiff();
			h *= game.getHeightDiff();
		
		}
		
		int hradius = (int) (radius * game.getWidthDiff());
		int vradius = (int) (radius * game.getHeightDiff());
		
		if(d != 0) {
			
			rotate(d, w, h, x, y);
			
		}
		
		glLineWidth(width);
		glBegin(GL_LINE_STRIP);
		
			lineData(x + hradius, y, x + w - hradius, y, c);          // h h
			
		glEnd();
		
		glLineWidth(width);
		glBegin(GL_LINE_STRIP);
			lineData(x + w, y - (width > 1 ? ((float) width / 2F) : 0) + vradius, x + w, y + h + (width > 1 ? ((float) width / 2F) : 0) - vradius, c);  // v d
		
		glEnd();
		
		glLineWidth(width);
		glBegin(GL_LINE_STRIP);
			lineData(x + w - hradius, y + h, x + hradius, y + h, c);  // h b
		
		glEnd();
		
		glLineWidth(width);
		glBegin(GL_LINE_STRIP);
			lineData(x, y + h + (width > 1 ? ((float) width / 2F) : 0) - vradius, x, y - (width > 1 ? ((float) width / 2F) : 1) + vradius, c);          // v g
		
		glEnd();
		
		Renderer.renderSimpleEllipseArc(x + hradius, y + vradius, hradius, vradius, 270, 90, width, c);
		Renderer.renderSimpleEllipseArc(x + w - hradius, y + vradius, hradius, vradius, 0, 90, width, c);
		Renderer.renderSimpleEllipseArc(x + hradius, y + h - vradius, hradius, vradius, 180, 90, width, c);
		Renderer.renderSimpleEllipseArc(x + w - hradius, y + h - vradius, hradius, vradius, 90, 90, width, c);
		
		if(d != 0) {
			
			rotate(-d, w, h, x, y);
			
		}
		
	}
	
	public static void renderRoundedBorder(float x, float y, int w, int h, int radius, Color c) {
		
		renderRoundedBorder(x, y, w, h, radius, 2, c);
		
	}
	
	public static void renderRoundedBorder(float x, float y, int w, int h, int radius, int width) {
		
		renderRoundedBorder(x, y, w, h, radius, width, Color.white);
		
	}
	
	public static void renderRoundedBorder(float x, float y, int w, int h, int radius) {
		
		renderRoundedBorder(x, y, w, h, radius, 2, Color.white);
		
	}
	
}
