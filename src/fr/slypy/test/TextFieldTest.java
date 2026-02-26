package fr.slypy.test;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.Arrays;

import javax.swing.Renderer;

import fr.slypy.slymyjge.Game;
import fr.slypy.slymyjge.components.TextFieldComponent;
import fr.slypy.slymyjge.font.SlymyFont;
import fr.slypy.slymyjge.graphics.NewGenRenderer;
import fr.slypy.slymyjge.graphics.shape.Rectangle;
import fr.slypy.slymyjge.graphics.shape.composite.RectangleBorder;
import fr.slypy.slymyjge.utils.ResizingRules;

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
	public void init(InitEvent e) {

		game.setFrameCap(120);
		game.setTickCap(60);
		game.setShowFPS(true);
		game.setShowTPS(true);

		f = new SlymyFont(new Font("Arial", Font.PLAIN, 30), Color.white);
		
		TextFieldComponent t = new TextFieldComponent(100, 100, 500, 150, game, f) {
			
			@Override
			public void renderForeground() {
				
				NewGenRenderer.renderShape(new RectangleBorder(this.getPosition().getX(), this.getPosition().getY(), this.getSize().getX(), this.getSize().getY(), 2, Color.black));
				
			}
			
			@Override
			public void renderBackground() {
				
				NewGenRenderer.renderShape(new Rectangle(this.getPosition().getX(), this.getPosition().getY(), this.getSize().getX(), this.getSize().getY(), Color.lightGray));
				
			}
			
		};
		
		t.setAllowMultilines(true);
		
		t.setText(Arrays.asList("abcde", "fgh", "a"));
		
		game.addComponent(t, "t");

	}

	@Override
	public void update(double alpha) {

	}
	
	@Override
	public void render(double alpha) {
		
		game.getComponent("t").render();
		
	}

	@Override
	public void stop() {
		

		
	}

	@Override
	public void exit(ExitEvent event) {
		// TODO Auto-generated method stub
		
	}
	
}