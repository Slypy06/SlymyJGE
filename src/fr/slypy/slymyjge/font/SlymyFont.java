package fr.slypy.slymyjge.font;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.util.vector.Vector2f;

import fr.slypy.slymyjge.graphics.TexCoords;
import fr.slypy.slymyjge.graphics.Texture;
import fr.slypy.slymyjge.graphics.Texture.TextureFilterMode;

public class SlymyFont {
	
	public static final String DEFAULT_CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 .,!?@#&()_-+=:;'\\\"[]{}<>|/~`áàâäãåāçćčđéèêëēėęíìîïīłñńóòôöõøōśšúùûüūÿýżžÁÀÂÄÃÅĀÇĆČĐÉÈÊËĒĖĘÍÌÎÏĪŁÑŃÓÒÔÖÕØŌŚŠÚÙÛÜŪŸÝŻŽ€$£µ§%$²°¤*";

	private static final double HEIGHT_MARGIN = 0.1;
	
	private final Map<Character, CharData> characters = new HashMap<>();
	private final Texture charAtlas;
	
	private final Font font;
	private final int height;
	
	private Color color;
	
	private final String charset;
	
	public SlymyFont(Font f, Color c) {
		
		this(f, c, DEFAULT_CHARSET);
		
	}
	
	public SlymyFont(Font f, Color c, String charset) {
		
		this.font = f;
		this.color = c;
		this.charset = charset;
		
		BufferedImage tempImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D tempG = tempImg.createGraphics();
		tempG.setFont(font);
		tempG.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		tempG.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		tempG.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		FontRenderContext frc = tempG.getFontRenderContext();
		FontMetrics fm = tempG.getFontMetrics();
		
		this.height = fm.getHeight();
		int heightMargin = (int) (height * HEIGHT_MARGIN);

		String[] atlas = getAtlasString(fm, charset).split("\n");

		int width = 0;
		for (String line : atlas) {
			
		    int lineWidth = atlasLineLength(font, frc, line);
		    width = Math.max(lineWidth, width);
		    
		}
		
		int totalHeight = height * atlas.length + heightMargin * (atlas.length - 1);
		BufferedImage atlasImg = new BufferedImage(width, totalHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = atlasImg.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setFont(font);
		g.setColor(color);

		for (int i = 0; i < atlas.length; i++) {
			
		    String line = atlas[i];
		    
		    float y = i*(height+heightMargin);
		    float x = 0;

		    for(int j = 0; j < line.length(); j++) {
		    	
		    	GlyphVector charVector = font.createGlyphVector(frc, "" + line.charAt(j));
		    	
		    	int charWidth = fm.charWidth(line.charAt(j));
		    	int visualWidth = (int) Math.ceil(charVector.getGlyphOutline(0).getBounds2D().getWidth()) + 1; //The +1 is to account for rounding error of the advance of the glyph
		    	
		    	g.fill(charVector.getGlyphOutline(0, x - (int) Math.floor(charVector.getGlyphOutline(0).getBounds2D().getX()), y+fm.getAscent()));
		    	
		        Vector2f[] coords = new Vector2f[4];
		        coords[0] = new Vector2f(x / width, y / totalHeight);
		        coords[1] = new Vector2f((x + visualWidth) / width, y / totalHeight);
		        coords[2] = new Vector2f((x + visualWidth) / width, (y + height) / totalHeight);
		        coords[3] = new Vector2f(x / width, (y + height) / totalHeight);
		    	
		        characters.put(line.charAt(j), new CharData(charWidth,  visualWidth, (int) Math.floor(charVector.getGlyphOutline(0).getBounds2D().getX()), height, line.charAt(j), new TexCoords(coords)));
		        
		    	x+=visualWidth+2;
		    	
		    }
		    
		}
		
		Texture.setFilterMode(TextureFilterMode.ALIASED);
		
		charAtlas = Texture.loadTexture(atlasImg);
		
		Texture.resetFilterMode();
		
	}
	
	public String getCharset() {

		return charset;
		
	}
	
	public Color getColor() {
		
		return color;
		
	}

	public Map<Character, CharData> getCharacters() {
		
		return characters;
		
	}

	public Font getFont() {
		
		return font;
		
	}
	
	public Texture getCharAtlas() {
		
		return charAtlas;
		
	}
	
	public int getHeight() {

		return height;
		
	}
	
	public int getVisualWidth(char c) {
		
		CharData data = getCharData(c);

		if(data != null)
			return data.getVisualWidth();
		else 
			return 0;
		
	}
	
	public int getWidth(char c) {
		
		CharData data = getCharData(c);

		if(data != null)
			return data.getWidth();
		else 
			return 0;
		
	}
	
	public int getWidth(String text) {
		
		int width = 0;
		
		for(char c : text.toCharArray())
			width += getWidth(c);
		
		return width;
		
	}
	
	public CharData getCharData(char c) {
		
		if(charset.contains(c + "")) {
			
			return characters.get(c);
			
		}
		
		return null;
		
	}
	
	public static String getAtlasString(FontMetrics f, String charset) {
		
		int height = f.getHeight();
		
		int totalLength = 0;
		for(int i = 0; i < charset.length(); i++)
			totalLength += f.charWidth(charset.charAt(i));
		
		int sqrt = (int) Math.ceil(Math.sqrt(height*totalLength));
		
		int target = sqrt - (sqrt % height) + height;
		int length = target;
		
		StringBuilder build = new StringBuilder();
		
		int lineLength = 0;
		
		for(char c : charset.toCharArray()) {
			
			if(lineLength < target) {
				
				lineLength += f.charWidth(c);
				build.append(c);
				
				if(lineLength > length) {
					
					length = lineLength;
					
				}
				
			} else {
				
				build.append('\n').append(c);
				lineLength = f.charWidth(c);
				
				if(lineLength > length) {
					
					length = lineLength;
					
				}

			}
			
		}
		
		return build.toString();
		
	}
	
	private int atlasLineLength(Font f, FontRenderContext frc, String line) {
		
		int width = 0;
		
	    for(int j = 0; j < line.length(); j++) {
	    	
	    	GlyphVector charVector = font.createGlyphVector(frc, "" + line.charAt(j));
	    	
	    	int visualWidth = (int) Math.ceil(charVector.getGlyphOutline(0).getBounds().getWidth());
	    	
	    	
	    	
	    	width += visualWidth + (j < line.length()-1 ? 10 : 0);
	    	
	    }
	    
	    return width;
		
	}
	
	public static class CharData {

		public final int width;
		public final int visualWidth;
		public final int height;
		public final char character;
		public final int advance;
		public final TexCoords atlasCoord;
		
		public CharData(int width, int visualWidth, int advance, int height, char character, TexCoords atlasCoord) {

			this.width = width;
			this.visualWidth = visualWidth;
			this.advance = advance;
			this.height = height;
			this.character = character;
			this.atlasCoord = atlasCoord;
			
		}
		
		public CharData(int width, int height, char character, TexCoords atlasCoord) {

			this(width, width, 0, height, character, atlasCoord);
			
		}

		public int getWidth() {
			
			return width;
			
		}
		
		public int getVisualWidth() {
			
			return visualWidth;
			
		}
		
		public int getHeight() {
			
			return height;
			
		}
		
		public char getCharacter() {
			
			return character;
			
		}
		
		public int getAdvance() {
			
			return advance;
			
		}

		public TexCoords getAtlasCoord() {
			return atlasCoord;
		}
		
	}
	
}
