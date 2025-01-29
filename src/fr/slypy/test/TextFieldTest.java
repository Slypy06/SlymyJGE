package fr.slypy.test;

import java.awt.Color;
import java.awt.Font;
import java.io.File;

import fr.slypy.slymyjge.Game;
import fr.slypy.slymyjge.components.ButtonComponent;
import fr.slypy.slymyjge.components.TextFieldComponent;
import fr.slypy.slymyjge.font.SlymyFont;
import fr.slypy.slymyjge.graphics.Renderer;

public class TextFieldTest extends Game {

	public static Game game;
	public SlymyFont f;
	
	public TextFieldTest(int width, int height, String title, Color backgroundColor, boolean resizable) {
		
		super(width, height, title, backgroundColor, resizable);

	}
	
	public static void main(String[] args) {
		
		System.setProperty("org.lwjgl.librarypath", new File("lib/natives").getAbsolutePath());
		
		game = new TextFieldTest(1280, 720, "Test Game Title", Color.white, true);
		game.start();
		
	}

	@Override
	public void init() {
		
		Renderer.init(game);
		
		game.setFrameCap(120);
		game.setTickCap(60);
		game.setShowFPS(true);
		game.setShowTPS(true);

		game.setAutoResizeType(true, true, true);
		
		f = new SlymyFont(new Font("Arial", Font.PLAIN, 30), Color.white);
		
		System.out.println(f.getWidth("€"));
		
		TextFieldComponent t = new TextFieldComponent(100, 100, 500, 150, game, f) {
			
			@Override
			public void renderForeground() {
				
				Renderer.renderBorder(this.getX(), this.getY(), this.getW(), this.getH(), 2, Color.black);
				
			}
			
			@Override
			public void renderBackground() {
				
				Renderer.renderQuad(this.getX(), this.getY(), this.getW(), this.getH(), Color.lightGray);
				
			}
			
		};
		
		t.setAllowMultilines(true);
		
		t.setText(f.getCharset());
		
		game.addComponent(t, "t");

	}

	@Override
	public void update(double alpha) {

	}
	
	@Override
	public void render(double alpha) {
		

	}

	@Override
	public void stop() {
		

		
	}
	
}