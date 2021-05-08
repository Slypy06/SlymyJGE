package fr.slypy.slymyjge;

import java.awt.Color;
import java.awt.Font;
import java.io.File;

import org.lwjgl.input.Keyboard;

import fr.slypy.slymyjge.components.TextFieldComponent;
import fr.slypy.slymyjge.font.SlymyFont;
import fr.slypy.slymyjge.graphics.Renderer;
import fr.slypy.slymyjge.graphics.Texture;

public class ExampleGame extends Game {

	public static ExampleGame game;
	public Texture background;
	
	public ExampleGame(int width, int height, String title, Color backgroundColor, boolean resizable) {
		
		super(width, height, title, backgroundColor, resizable);

	}
	
	public static void main(String[] args) {
		
		game = new ExampleGame(1280, 720, "Aot Launcher", Color.white, false);
		game.start();
		
	}

	@Override
	public void init() {
		
		Renderer.init(game);
		background = Texture.loadTexture("background.png");
		
	}

	@Override
	public void update(double alpha) {
		
		
		
	}

	@Override
	public void render(double alpha) {

		Renderer.renderTexturedQuad(0, 0, 1280, 720, background);
		
	}

	@Override
	public void stop() {
		
		System.out.println("stop");
		
	}
	
}
