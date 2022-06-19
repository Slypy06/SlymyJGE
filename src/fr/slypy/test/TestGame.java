package fr.slypy.test;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;

import fr.slypy.slymyjge.Game;
import fr.slypy.slymyjge.audio.AudioMaster;
import fr.slypy.slymyjge.audio.AudioSource;
import fr.slypy.slymyjge.components.TextFieldComponent;
import fr.slypy.slymyjge.font.SlymyFont;
import fr.slypy.slymyjge.graphics.Renderer;
import fr.slypy.slymyjge.graphics.Texture;
import fr.slypy.slymyjge.media.MediaPlayer;
import uk.co.caprica.vlcj.player.base.State;

public class TestGame extends Game {

	public static Game game;
	public static SlymyFont f;
	public static Texture img;
	public static BufferedImage baseImg;
	public static AudioSource textClick;
	public static Texture background;
	public static Texture logo;
	public static Texture textfield;
	
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
		
		AudioMaster.init();
		textClick = new AudioSource(AudioMaster.loadSound("click.wav"));
		textClick.setPitch(2.5f);
		
		TextFieldComponent username = new TextFieldComponent(490, 200, 300, 75, game, new SlymyFont(new Font("", 0, 30), Color.black)) {
			
			@Override
			public void renderForeground() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void renderBackground() {
				
				Renderer.renderTexturedQuad(getX(), getY(), getW(), getH(), textfield);
				
			}
			
			@Override
			public void textChanged() {
				
				textClick.play();
				
			}
			
		};
		
		username.setMargin(16);
		
		game.addComponent(username, "username");
		
		background = Texture.loadTexture("background.jpg");
		logo = Texture.loadTexture("logo.png");
		textfield = Texture.loadTexture("textfield.png");
		
	}

	@Override
	public void update(double alpha) {

		
		
	}
	
	@Override
	public void render(double alpha) {
		
		Renderer.renderTexturedQuad(0, 0, 1280, 720, background);
		Renderer.renderTexturedQuad(384, 150, 512, 64, logo);

	}
	
	@Override
	public void stop() {
		
		AudioMaster.cleanUp();
		
	}
	
	@Override
	public void keyPressed(int key) {
		
		
		
	}

}
