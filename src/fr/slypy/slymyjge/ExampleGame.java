package fr.slypy.slymyjge;

import java.awt.Color;
import java.awt.Font;
import java.io.File;

import org.lwjgl.input.Keyboard;

import fr.slypy.slymyjge.components.TextFieldComponent;
import fr.slypy.slymyjge.font.SlymyFont;
import fr.slypy.slymyjge.graphics.Renderer;

public class ExampleGame extends Game {

	public static ExampleGame game;
	public TextFieldComponent comp;
	public SlymyFont font;
	
	public ExampleGame(int width, int height, String title, Color backgroundColor, boolean resizable) {
		
		super(width, height, title, backgroundColor, resizable);

	}
	
	public static void main(String[] args) {
		
		game = new ExampleGame(1280, 720, "Client Game", Color.white, false);
		game.start();
		
	}

	@Override
	public void init() {
		
		try {
			
			font = new SlymyFont(Font.createFont(Font.TRUETYPE_FONT, new File("C:\\Users\\Enzo\\Desktop\\font.ttf")).deriveFont(28f), Color.black);
			
		} catch (Exception e) {

			e.printStackTrace();
			
		}
		
		game.setShowFPS(true);
		
		Renderer.init(game);
		
		comp = new TextFieldComponent(100, 100, 800, 150, game, font) {

			@Override
			public void renderBackground() {
				
				Renderer.renderLine(this.getX(), this.getY(), this.getX() + this.getW(), this.getY(), Color.black);
				Renderer.renderLine(this.getX(), this.getY(), this.getX(), this.getY() + this.getH(), Color.black);
				Renderer.renderLine(this.getX() + this.getW(), this.getY(), this.getX() + this.getW(), this.getY() + this.getH(), Color.black);
				Renderer.renderLine(this.getX(), this.getY() + this.getH(), this.getX() + this.getW(), this.getY() + this.getH(), Color.black);
				
			}

			@Override
			public void renderForeground() {
				
				if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
					
					System.out.println(this.getTextSize());
					
				}
				
			}
			
		};
		
		comp.setCap(100);
		comp.setAllowMultilines(true);
		
		game.addComponent("textField", comp);
		
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
