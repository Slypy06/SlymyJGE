package fr.slypy.slymyjge.font;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import fr.slypy.slymyjge.graphics.Texture;

public class SlymyFont {

	public Map<Integer, Texture> characters = new HashMap<Integer, Texture>();
	
	public Font f;
	
	public Color c;
	
	public SlymyFont(Font f, Color c) {
		
		this.f = f;
		
		this.c = c;
		
		for(int i = 0; i < 256; i++) {
			
			createCharTexture(i);
			
		}
		
	}
	
	public Color getColor() {
		
		return c;
		
	}

	public Map<Integer, Texture> getCharacters() {
		
		return characters;
		
	}

	public void setCharacters(Map<Integer, Texture> characters) {
		
		this.characters = characters;
		
	}

	public Font getF() {
		
		return f;
		
	}

	public void setF(Font f) {
		
		this.f = f;
		
	}
	
	public CharData getCharData(char c) {
		
		int i = (int) c;
		
		if(i > 0 && i < 256) {
			
			Texture charTexture = characters.get(i);
			
			return new CharData(charTexture.getWidth(), charTexture.getHeight(), c);
			
		}
		
		return null;
		
	}
	
	public Texture getCharTexture(char c) {
		
		int i = (int) c;
		
		if(i > 0 && i < 256) {
			
			return characters.get(i);
			
		}
		
		return null;
		
	}
	
	public int getHeight() {
		
		BufferedImage tempImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D tempG = (Graphics2D) tempImg.getGraphics();
		tempG.setFont(f);
		FontMetrics fontMetrics = tempG.getFontMetrics();
		
		int charheight = fontMetrics.getHeight();
		
		if (charheight <= 0) {
			
			charheight = f.getSize();
			
		}
		
		return charheight;
		
	}
	
	public int getWidth(String text) {
		
		int width = 0;
		
		for(char c : text.toCharArray()) {
			
			int i = (int) c;
			
			if(i > 0 && i < 256) {
				
				width += getCharData(c).getWidth();
				
			}
			
		}
		
		return width;
		
	}
	
	private void createCharTexture(int i) {
		
		BufferedImage tempImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D tempG = (Graphics2D) tempImg.getGraphics();
		tempG.setFont(f);
		FontMetrics fontMetrics = tempG.getFontMetrics();
		int charwidth = fontMetrics.charWidth((char) i);

		if (charwidth <= 0) {
			
			charwidth = 1;
			
		}
		
		int charheight = fontMetrics.getHeight();
		
		if (charheight <= 0) {
			
			charheight = f.getSize();
			
		}

		
		tempImg = new BufferedImage(charwidth, charheight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) tempImg.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setFont(f);
		
		g.setColor(c);
		int charx = 0;
		int chary = 0;
		g.drawString(String.valueOf((char) i), (charx), (chary) + fontMetrics.getAscent());
		
		characters.put(i, Texture.loadTexture(tempImg));
		
	}
	
}
