package fr.slypy.slymyjge;

import java.awt.Color;

public class ExampleGame extends Game {

	public static ExampleGame game;
	
	public ExampleGame(int width, int height, String title, Color backgroundColor, boolean resizable) {
		
		super(width, height, title, backgroundColor, resizable);

	}
	
	public static void main(String[] args) {
		
		game = new ExampleGame(1280, 720, "Example Game", Color.black, true);
		
		
	}

	@Override
	public void init() {
		
		
		
	}

	@Override
	public void update(double alpha) {
		
		
		
	}

	@Override
	public void render() {
		
		
		
	}

	@Override
	public void stop() {
		
		
		
	}

}
