package fr.slypy.slymyjge;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

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
			
			font = new SlymyFont(Font.createFont(Font.TRUETYPE_FONT, new File("C:\\Users\\Enzo\\Desktop\\font.ttf")).deriveFont(30f), Color.black);
			
		} catch (Exception e) {

			e.printStackTrace();
			
		}
		
		game.setShowFPS(true);
		
		Renderer.init(game);
		
		comp = new TextFieldComponent(100, 100, 300, 50, game) {
			
			@Override
			public void renderField() {
				
				Renderer.renderLine(this.getX(), this.getY(), this.getX() + this.getW(), this.getY(), Color.black);
				Renderer.renderLine(this.getX(), this.getY(), this.getX(), this.getY() + this.getH(), Color.black);
				Renderer.renderLine(this.getX() + this.getW(), this.getY(), this.getX() + this.getW(), this.getY() + this.getH(), Color.black);
				Renderer.renderLine(this.getX(), this.getY() + this.getH(), this.getX() + this.getW(), this.getY() + this.getH(), Color.black);
				
				if(this.isFocus()) {
					
					Renderer.renderLine(this.getX() + 12 + font.getWidth(this.getText()), this.getY() + 10, this.getX() + 12 + font.getWidth(this.getText()), this.getY() + this.getH() - 10, Color.black);
					
				}

			}
			
		};
		comp.setCap(10);
		
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
