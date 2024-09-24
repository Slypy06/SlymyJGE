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
import fr.slypy.slymyjge.utils.Logger;
import uk.co.caprica.vlcj.player.base.State;

public class TestGame extends Game {

	public static Game game;
	
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
		
		game.setFrameCap(120);
		game.setTickCap(20);
		game.setShowFPS(true);
		game.setShowTPS(true);
		
		game.initSteam(true);
		
	}

	@Override
	public void update(double alpha) {

		System.out.println(game.getHeightDiff());
		
	}
	
	@Override
	public void render(double alpha) {
		
		Renderer.renderQuad(10, 10, game.getWidth() - 20, game.getHeight() - 20, Color.cyan);
		
		Renderer.renderQuad(100, 100, game.getWidth() - 200, game.getHeight() - 200, Color.blue);
		
	}

	@Override
	public void stop() {
		

		
	}
	
}
