package fr.slypy.slymyjge.test;

import java.awt.Color;

import fr.slypy.slymyjge.Game;
import fr.slypy.slymyjge.graphics.Renderer;

public class ExampleGame extends Game {

	public static ExampleGame game;

	public IntroGameState intro;
	public static int INTRO_ID;
	public MainGameState main;
	public static int MAIN_ID;
	
	public ExampleGame(int width, int height, String title, Color backgroundColor, boolean resizable) {
		
		super(width, height, title, backgroundColor, resizable);

	}
	
	public static void main(String[] args) {
		
		game = new ExampleGame(1280, 720, "Example", Color.white, false);
		game.start();
		
	}

	@Override
	public void init() {

		Renderer.init(game);
		
		game.setShowFPS(true);
		game.setShowTPS(true);
		game.setFrameCap(60);
		game.setTickCap(60);
		
		intro = new IntroGameState(game);
		main = new MainGameState(game);
		
		INTRO_ID = game.registerState(intro);
		MAIN_ID = game.registerState(main);
		
		game.setState(INTRO_ID);

	}

	@Override
	public void update(double alpha) {}

	@Override
	public void render(double alpha) {}

	@Override
	public void stop() {}
	
}
