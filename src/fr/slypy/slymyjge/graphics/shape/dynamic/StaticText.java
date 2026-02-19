package fr.slypy.slymyjge.graphics.shape.dynamic;

import java.awt.Color;

import org.lwjgl.util.vector.Vector2f;

import fr.slypy.slymyjge.Game;
import fr.slypy.slymyjge.font.SlymyFont;
import fr.slypy.slymyjge.graphics.NewGenRenderer;
import fr.slypy.slymyjge.graphics.ShapeBundle;
import fr.slypy.slymyjge.graphics.Surface;
import fr.slypy.slymyjge.graphics.shape.TexturedQuad;
import fr.slypy.slymyjge.graphics.shape.TexturedRectangle;

public class StaticText {

	private SlymyFont font;
	private String text;
	private Surface sur;
	private Game game;
	private ShapeBundle<TexturedRectangle> chars;
	private float size = 1;
	
	public StaticText(SlymyFont font, String text, Game game) {

		this.font = font;
		this.text = text;
		this.game = game;
		this.sur = new Surface(font.getWidth(text), font.getHeight(), game);
		this.chars = new ShapeBundle<>(text.length(), game, TexturedQuad.INFOS);
		
		chars.setTexture(font.getCharAtlas());
		int x = 0;
		for(char c : text.toCharArray()) {
			
			chars.addShapes(new TexturedRectangle(x+font.getCharData(c).getAdvance(), 0, font.getVisualWidth(c), font.getHeight(), font.getCharAtlas(), Color.white, font.getCharData(c).getAtlasCoord()));
			x+=font.getWidth(c);
			
		}
		
		chars.pushChanges();
		
		game.executeInRenderThread(this::render);
		
	}
	
	public void updateBundle() {

		chars.free();
		
		chars = new ShapeBundle<>(text.length(), game, TexturedQuad.INFOS);
		chars.setTexture(font.getCharAtlas());
		int x = 0;
		for(char c : text.toCharArray()) {
			
			chars.addShapes(new TexturedRectangle(x+font.getCharData(c).getAdvance(), 0, font.getVisualWidth(c), font.getHeight(), font.getCharAtlas(), Color.white, font.getCharData(c).getAtlasCoord()));
			x+=font.getWidth(c);
			
		}
		
		chars.pushChanges();
		
	}
	
	public void updateSurfaceSize() {
		
		sur.free();
		sur = new Surface((int) Math.max(1, Math.ceil(size*font.getWidth(text))), (int) Math.max(1, Math.ceil(size*font.getHeight())), game);
		
	}
	
	public void render() {
		
		NewGenRenderer.renderOnSurface(() -> {
			
			NewGenRenderer.renderShapeBundle(chars);
			
		}, sur);
		
	}
	
	private void redraw() {
		
		updateSurfaceSize();
		updateBundle();
		render();
		
	}
	
	public Surface getSurface() {
		
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
	
	public int getWidth() {
		
		return (int) (size*font.getWidth(text));
		
	}
	
	public void setSize(int size) {
		
		this.size = (float) size / font.getHeight();
		
		game.executeInRenderThread(this::redraw);
		
	}
	
	public TexturedRectangle getTextShape(Vector2f position) {
		
		TexturedRectangle rect = new TexturedRectangle(position.getX(), position.getY(), size*font.getWidth(text), size*font.getHeight());
		rect.setTexture(sur.getTextureId());
		return rect;
		
	}
	
}
