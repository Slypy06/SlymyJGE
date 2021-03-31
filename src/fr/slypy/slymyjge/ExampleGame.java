package fr.slypy.slymyjge;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import fr.slypy.slymyjge.graphics.Renderer;
import fr.slypy.slymyjge.graphics.Texture;

public class ExampleGame extends Game {

	public static ExampleGame game;
	
	public ExampleGame(int width, int height, String title, Color backgroundColor, boolean resizable) {
		
		super(width, height, title, backgroundColor, resizable);

	}
	
	public static void main(String[] args) {
		
		game = new ExampleGame(1280, 720, "Example Game", Color.black, true);
		game.start();
		
	}

	@Override
	public void init() {

		try {
		
		Renderer.init(game);
		game.setShowTPS(true);
		game.setShowFPS(true);
		game.setFrameCap(60);
		game.setTickCap(50);
		game.setIcon(Texture.getIcon("icon.ico"));
		
		} catch(Exception e) {
			
			e.printStackTrace();
			
		}
		
	}

	@Override
	public void update(double alpha) {
		
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			
			Renderer.setRotation(Renderer.getRotation() + (45 * alpha));
			
		} else if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			
			Renderer.setRotation(Renderer.getRotation() - (45 * alpha));
			
		}
		
	}

	@Override
	public void stop() {
		
		
		
	}

	@Override
	public void render(double alpha) {
		
		Renderer.renderQuad(50, 50, 200, 200, Color.cyan);
		
	}

}
