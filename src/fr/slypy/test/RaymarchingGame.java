package fr.slypy.test;

import java.awt.Color;
import java.io.File;

import fr.slypy.slymyjge.Game;

public class RaymarchingGame extends Game {

	public RaymarchingGame(int width, int height, String title, Color backgroundColor, boolean resizable) {

		super(width, height, title, backgroundColor, resizable);

	}
	
	public static void main(String[] args) {
		
		System.setProperty("org.lwjgl.librarypath", new File("lib/natives").getAbsolutePath());

		new RaymarchingGame(3840, 2160, "Raymarching Game", Color.black, false).start();
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(double alpha) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(InitEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(double alpha) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exit(ExitEvent event) {
		// TODO Auto-generated method stub
		
	}

}
