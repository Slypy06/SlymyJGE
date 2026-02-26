package fr.slypy.slymyjge.graphics.shape.dynamic;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import fr.slypy.slymyjge.Game;
import fr.slypy.slymyjge.font.SlymyFont;
import fr.slypy.slymyjge.graphics.ShapeBundle;
import fr.slypy.slymyjge.graphics.shape.TexturedQuad;
import fr.slypy.slymyjge.graphics.shape.TexturedRectangle;

public class DynamicText {

	private SlymyFont font;
	private String text;
	private Game game;
	private ShapeBundle<TexturedQuad> charBundle;
	private List<TexturedQuad> chars = new ArrayList<TexturedQuad>();
	private float size = 1;
	private float lineSpacing = 0.1f;
	
	public DynamicText(SlymyFont font, String text, Game game) {

		this.font = font;
		this.text = text;
		this.game = game;
		this.charBundle = new ShapeBundle<>(text.length(), game, TexturedQuad.INFOS);
		
		charBundle.setTexture(font.getCharAtlas());
		int x = 0;
		int y = 0;
		for(char c : text.toCharArray()) {
			
			if(c == '\n') {
				
				y+=font.getHeight()*(1+lineSpacing)*size;
				x = 0;
				continue;
				
			}
				
			if(font.getCharData(c) == null)
				continue;
			
			charBundle.addShapes(new TexturedRectangle(x+font.getCharData(c).getAdvance()*size, y, font.getVisualWidth(c)*size, font.getHeight()*size, font.getCharAtlas(), Color.white, font.getCharData(c).getAtlasCoord()));
			chars.add(new TexturedRectangle(x+font.getCharData(c).getAdvance()*size, y, font.getVisualWidth(c)*size, font.getHeight()*size, font.getCharAtlas(), Color.white, font.getCharData(c).getAtlasCoord()));
			x+=font.getWidth(c)*size;
			
		}
		
		charBundle.pushChanges();
		
	}
	
	public float getLineSpacing() {
		
		return lineSpacing;
		
	}
	
	public void setLineSpacing(float lineSpacing) {
		
		this.lineSpacing = lineSpacing;
		resetBundle();
		
	}
	
	public void softResetBundle() {
		
		charBundle.free();
		chars.clear();
		
		List<TexturedQuad> newChars = new ArrayList<>();
		
		charBundle = new ShapeBundle<>(text.length(), game, TexturedQuad.INFOS);
		charBundle.setTexture(font.getCharAtlas());
		int x = 0;
		int y = 0;
		for(char c : text.toCharArray()) {
			
			if(c == '\n') {
				
				y+=font.getHeight()*(1+lineSpacing)*size;
				x = 0;
				continue;
				
			}
				
			if(font.getCharData(c) == null)
				continue;
			
			charBundle.addShapes(new TexturedRectangle(x+font.getCharData(c).getAdvance()*size, y, font.getVisualWidth(c)*size, font.getHeight()*size, font.getCharAtlas(), Color.white, font.getCharData(c).getAtlasCoord()));
			newChars.add(new TexturedRectangle(x+font.getCharData(c).getAdvance()*size, y, font.getVisualWidth(c)*size, font.getHeight()*size, font.getCharAtlas(), Color.white, font.getCharData(c).getAtlasCoord()));
			x+=font.getWidth(c)*size;
			
		}
		
		chars = newChars;
		
	}
	
	public void resetBundle() {

		softResetBundle();
		charBundle.pushChanges();
		
	}
	
	public SlymyFont getFont() {
		
		return font;
		
	}
	
	public void setFont(SlymyFont font) {
		
		this.font = font;
		
		game.executeInRenderThread(this::resetBundle);
		
	}
	
	public ShapeBundle<TexturedQuad> getCharacterBundle() {
		
		return charBundle;
		
	}
	
	public List<TexturedQuad> getOriginalChars() {
		
		return chars;
		
	}
	
	public String getText() {
		
		return text;
		
	}
	
	public void setText(String text) {
		
		this.text = text;
		
		game.executeInRenderThread(this::resetBundle);
		
	}
	
	public int getSize() {
		
		return (int) (size*font.getHeight());
		
	}
	
	public int getHeight() {
		
		return (int) (size*font.getHeight()*text.split("\n").length);
		
	}
	
	public int getWidth() {
		
		return (int) (size*font.getWidth(text));
		
	}
	
	public void setSize(int size) {
		
		this.size = (float) size / font.getHeight();
		
		game.executeInRenderThread(this::resetBundle);
		
	}
	
	public void perCharacterTransform(BiFunction<Integer, TexturedQuad, TexturedQuad> transformer) {
		
		for(int i = 0; i < chars.size(); i++) {
			
			charBundle.setShape(i, transformer.apply(i, chars.get(i)));
			
		}
		
		charBundle.pushChanges();
		
	}
	
	public void free() {
		
		charBundle.free();
		
	}
	
}
