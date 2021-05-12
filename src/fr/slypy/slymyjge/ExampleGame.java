package fr.slypy.slymyjge;

import java.awt.Color;
import java.awt.Font;

import fr.slypy.slymyjge.components.TextFieldComponent;
import fr.slypy.slymyjge.font.SlymyFont;
import fr.slypy.slymyjge.graphics.Renderer;

public class ExampleGame extends Game {

	public static ExampleGame game;
	public TextFieldComponent textField;
	public SlymyFont font;
	
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
		
		font = new SlymyFont(new Font("", 0, 20), Color.red);
		
		textField = new TextFieldComponent(100, 100, 500, 200, game, font) {
			
			@Override
			public void renderForeground() {
				
				
				
			}
			
			@Override
			public void renderBackground() {
				
				Renderer.renderQuad(getX(), getY(), getW(), getH(), Color.black);
				
			}
			
		};
		
		textField.setAllowMultilines(true);
		
		game.addComponent("field", textField);
		
	}

	@Override
	public void update(double alpha) {
		
		
		
	}

	@Override
	public void render(double alpha) {


		
	}

	@Override
	public void stop() {
		
		System.out.println("stop");
		
	}
	
}
