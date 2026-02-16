package fr.slypy.test;

import java.awt.Color;
import java.io.File;

import javax.management.openmbean.OpenMBeanAttributeInfo;

import fr.slypy.slymyjge.Game;

public class RaymarchingGame extends Game {

	public RaymarchingGame(int width, int height, String title, Color backgroundColor, boolean resizable) {

		super(width, height, title, backgroundColor, resizable);

	}
	
	public static void main(String[] args) {
		
		System.setProperty("org.lwjgl.librarypath", new File("lib/natives").getAbsolutePath());

		new RaymarchingGame(3840, 2160, "Raymarching Game", Color.black, false).start();
		
	}

}
