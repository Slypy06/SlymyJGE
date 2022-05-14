package fr.slypy.test;

import java.awt.Color;
import java.awt.Font;
import java.io.File;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

import fr.slypy.slymyjge.Game;
import fr.slypy.slymyjge.components.TextFieldComponent;
import fr.slypy.slymyjge.font.SlymyFont;
import fr.slypy.slymyjge.graphics.Renderer;
import fr.slypy.slymyjge.utils.RenderType;

public class TestGame extends Game {

	public static Game game;
	public static SlymyFont f;
	
	public TestGame(int width, int height, String title, Color backgroundColor, boolean resizable) {
		
		super(width, height, title, backgroundColor, resizable);

	}
	
	public static void main(String[] args) {
		
		System.setProperty("org.lwjgl.librarypath", new File("lib/natives").getAbsolutePath());
		
		game = new TestGame(1280, 720, "Test Game Title", Color.white, true);
		game.start();
		
	}

	@Override
	public void init() {
		
		Renderer.init(game);
		
		game.setFrameCap(60);
		game.setTickCap(20);
		game.setShowFPS(true);
		game.setShowTPS(true);
		
		game.setFullscreen(true);
		
		f = new SlymyFont(new Font("", Font.PLAIN, 32), Color.black);
		
		TextFieldComponent comp = new TextFieldComponent(500, 200, 250, 75, game, f, RenderType.HUD) {
			
			@Override
			public void renderForeground() {
				
				
				
			}
			
			@Override
			public void renderBackground() {
				
				Renderer.renderRoundedQuad(getX(), getY(), getW(), getH(), 10, Color.gray);
				Renderer.renderRoundedBorder(getX(), getY(), getW(), getH(), 10, 3, Color.darkGray);
				
			}
		};
		
		game.addComponent(comp, "comp");
		
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
	
	@Override
	public void keyPressed(int key) {
		
		
		
	}

}
