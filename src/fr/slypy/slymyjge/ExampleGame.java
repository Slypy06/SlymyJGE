package fr.slypy.slymyjge;

import java.awt.Color;

import fr.slypy.slymyjge.graphics.Renderer;

public class ExampleGame extends Game {

	public static ExampleGame game;
	
	public ExampleGame(int width, int height, String title, Color backgroundColor, boolean resizable) {
		
		super(width, height, title, backgroundColor, resizable);

	}
	
	public static void main(String[] args) {
		
		game = new ExampleGame(1280, 720, "Example Game", Color.black, false);
		game.start();
		
	}

	@Override
	public void init() {
		
		game.setShowFPS(true);
		Renderer.init(game);
		Renderer.setRotation(15);
		
	}

	@Override
	public void update(double alpha) {
		
		
		
	}

	@Override
	public void render() {
		
		Renderer.renderTriangle(350, 150, 250, 350, 550, 350);
		
	}

	@Override
	public void stop() {
		
		
		
	}

}
