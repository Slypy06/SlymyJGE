package fr.slypy.slymyjge.graphics.shape.dynamic;

import java.awt.Color;

import org.lwjgl.util.vector.Vector2f;

import fr.slypy.slymyjge.Game;
import fr.slypy.slymyjge.font.SlymyFont;
import fr.slypy.slymyjge.graphics.ISurface;
import fr.slypy.slymyjge.graphics.NewGenRenderer;
import fr.slypy.slymyjge.graphics.ShapeBundle;
import fr.slypy.slymyjge.graphics.Surface;
import fr.slypy.slymyjge.graphics.shape.TexturedQuad;
import fr.slypy.slymyjge.graphics.shape.TexturedRectangle;

public class StaticText {

	private SlymyFont font;
	private String text;
	private ISurface sur;
	private Game game;
	private ShapeBundle<TexturedRectangle> chars;
	private float size = 1;
	private float interline = 0.1f;
	
	public StaticText(SlymyFont font, String text, Game game) {
		
		this.font = font;
		this.text = text;
		this.game = game;
		this.sur = new Surface(font.getWidth(text), font.getHeight()*text.split("\n").length, game);
		this.chars = new ShapeBundle<>(text.length(), game, TexturedQuad.INFOS);
		
		chars.setTexture(font.getCharAtlas());
		int x = 0;
		int y = 0;
		for(char c : text.toCharArray()) {
			
			if(c == '\n') {
				
				y+=(1+interline)*font.getHeight()*size;
				x = 0;
				continue;
				
			}
			
			if(font.getCharData(c) == null)
				continue;
			
			System.out.println(c + " is at " + new Vector2f(x+font.getCharData(c).getAdvance(), y));
			
			chars.addShapes(new TexturedRectangle(x+font.getCharData(c).getAdvance()*size, y, font.getVisualWidth(c)*size, font.getHeight()*size, font.getCharAtlas(), Color.white, font.getCharData(c).getAtlasCoord()));
			x+=font.getWidth(c)*size;
			
		}
		
		chars.pushChanges();
		
		game.executeInRenderThread(this::draw);
		
	}
	
	public float getInterline() {
		
		return interline;
		
	}
	
	public void setInterline(float interline) {
		
		this.interline = interline;
		
	}
	
	public void updateBundle() {

		chars.free();
		
		chars = new ShapeBundle<>(text.length(), game, TexturedQuad.INFOS);
		chars.setTexture(font.getCharAtlas());
		int x = 0;
		int y = 0;
		for(char c : text.toCharArray()) {
			
			if(c == '\n') {
				
				y+=(1+interline)*font.getHeight()*size;
				x = 0;
				continue;
				
			}
			
			if(font.getCharData(c) == null)
				continue;
			
			System.out.println(c + " is at " + new Vector2f(x+font.getCharData(c).getAdvance(), y));
			
			chars.addShapes(new TexturedRectangle(x+size*font.getCharData(c).getAdvance(), y, size*font.getVisualWidth(c), size*font.getHeight(), font.getCharAtlas(), Color.white, font.getCharData(c).getAtlasCoord()));
			x+=font.getWidth(c)*size;
			
		}
		
		chars.pushChanges();
		
	}
	
	public void updateSurfaceSize() {
		
		sur.free();
		sur = new Surface((int) Math.max(1, Math.ceil(size*font.getWidth(text))), (int) Math.max(1, Math.ceil(size*font.getHeight()*text.split("\n").length)), game);
		
	}
	
	public void draw() {
		
		NewGenRenderer.renderOnSurface(() -> {
			
			NewGenRenderer.renderShapeBundle(chars);
			
		}, sur);
		
	}
	
	private void redraw() {
		
		updateSurfaceSize();
		updateBundle();
		draw();
		
	}
	
	public ISurface getSurface() {
		
		return sur;
		
	}
	
	public SlymyFont getFont() {
		
		return font;
		
	}
	
	public void setFont(SlymyFont font) {
		
		this.font = font;
		
		game.executeInRenderThread(this::redraw);
		
	}
	
	public ShapeBundle<TexturedRectangle> getCharacterBundle() {
		
		return chars;
		
	}
	
	public String getText() {
		
		return text;
		
	}
	
	public void setText(String text) {
		
		this.text = text;
		
		game.executeInRenderThread(this::redraw);
		
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
		
		game.executeInRenderThread(this::redraw);
		
	}
	
	public TexturedRectangle getTextShape(Vector2f position) {
		
		TexturedRectangle rect = new TexturedRectangle(position.getX(), position.getY(), size*font.getWidth(text), size*font.getHeight()*text.split("\n").length);
		rect.setTexture(sur.getTextureId());
		return rect;
		
	}
	
	public void free() {
		
		chars.free();
		sur.free();
		
	}
	
}
