package fr.slypy.slymyjge;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

import java.awt.Color;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import fr.slypy.slymyjge.audio.AudioMaster;
import fr.slypy.slymyjge.audio.AudioSource;
import fr.slypy.slymyjge.graphics.Renderer;
import fr.slypy.slymyjge.graphics.Texture;

public class PasswordGame extends Game {

	public static Game game;
	public static Font font;
	public static int characters = 0;
	public static String pass = "";
	public static boolean passEnter = false;
	public static boolean firstEnter = false;
	public static Texture pranked;
	public static AudioSource sad;
	public static int firstCharacters = 0;
	
	public PasswordGame(int width, int height, String title, Color backgroundColor, boolean resizable) {
		
		super(width, height, title, backgroundColor, resizable);

	}
	
	public static void main(String[] args) {
		
		game = new PasswordGame(1280, 720, "Example Game", Color.black, true);
		game.start();
		
	}

	@Override
	public void init() {
		
		AudioMaster.init();	
		AudioMaster.setListener(0, 0, 0);
		
		sad = new AudioSource(AudioMaster.loadSound("sad.wav"));
		sad.setVolume(0.5f);
		
		Renderer.init(game);
		
	    try {
	        InputStream inputStream = new FileInputStream(new File("C:\\Users\\ENZO\\Desktop\\dos.ttf"));
	         
	        font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
	        font = font.deriveFont(25f); // set font size
			game.registerFont(font, "exampleFont");     
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    }   
	    
	    try {
	        InputStream inputStream = new FileInputStream(new File("C:\\Users\\ENZO\\Desktop\\flexi.ttf"));
	         
	        font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
	        font = font.deriveFont(30f);
			game.registerFont(font, "exampleFont2");
	             
	    } catch (Exception e) {
	        e.printStackTrace();
	    }   
	    
	    try {
	        InputStream inputStream = new FileInputStream(new File("C:\\Users\\ENZO\\Desktop\\flexi.ttf"));
	         
	        Map<TextAttribute, Integer> att = new HashMap<TextAttribute, Integer>();
	        att.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
	        
	        font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
	        font = font.deriveFont(30f).deriveFont(Font.BOLD).deriveFont(att);
			game.registerFont(font, "exampleFont3");
	             
	    } catch (Exception e) {
	        e.printStackTrace();
	    }  
	    
	    pranked = Texture.loadTexture("pranked.png");
		
	    game.setMouseGrabbed(true);
		game.setFullscreen(true);
		game.setShowFPS(true);
		game.addKeyToListen(Keyboard.KEY_RETURN);
		game.addKeyToListen(Keyboard.KEY_NUMPADENTER);
		
		
	}

	@Override
	public void update(double alpha) {
		
		if(Keyboard.next() && Keyboard.getEventKeyState() && !passEnter) {
			
			if(Keyboard.getEventKey() == 14) {
				
				if(characters > 0) {
				
					characters--;
				
				
				}
				
				if(pass.length() > 0) {
				
					pass = pass.substring(0, pass.length() - 1);
					
				}
				
			} else if(Keyboard.getEventKey() != Keyboard.KEY_RETURN && Keyboard.getEventKey() != Keyboard.KEY_LSHIFT && Keyboard.getEventKey() != Keyboard.KEY_RSHIFT && Keyboard.getEventKey() != Keyboard.KEY_NUMLOCK && Keyboard.getEventKey() != Keyboard.KEY_NUMPADENTER && Keyboard.getEventKey() != Keyboard.KEY_CAPITAL) {
			
				characters++;
				pass += Keyboard.getEventCharacter();
			
			}
			
		}
		
		if(!firstEnter) {
		
			firstCharacters = characters;
		
		}
		
	}

	@Override
	public void render() {

		String firstCharactersStr = "";
		String charactersStr = "";
		
		for(int i = 0; i < firstCharacters; i++) {
			
			firstCharactersStr += "*";
			
		}
		
		for(int i = 0; i < characters; i++) {
			
			charactersStr += "*";
			
		}
			
		if(passEnter) {
			
			Renderer.renderString(75, 75, game.getFont("exampleFont2"), "Ce n'etait pas le BIOS mais un logiciel fait avec mon API de", Color.black);
			Renderer.renderString(75, 125, game.getFont("exampleFont2"), "developement de jeu video :) ", Color.black);
			Renderer.renderString(75, 175, game.getFont("exampleFont2"), "Ton mot de passe est donc : ", Color.black);
			Renderer.renderString(435, 175, game.getFont("exampleFont3"), pass, Color.black);
			Renderer.renderTexturedQuad(425, 250, 400, 400, pranked);
			
		} else {
			
			Renderer.renderString(15, 25, game.getFont("exampleFont"), "enter password: " + firstCharactersStr + (firstEnter ? "" : "_"), passEnter ? Color.black : Color.white);
			Renderer.renderString(15, 55, game.getFont("exampleFont"), (firstEnter ? "password invalid" : ""), passEnter ? Color.black : Color.white);
			
			if(firstEnter) {
				
				Renderer.renderString(15, 85, game.getFont("exampleFont"), "enter password: " + charactersStr + (passEnter ? "" : "_"), passEnter ? Color.black : Color.white);
				Renderer.renderString(15, 115, game.getFont("exampleFont"), passEnter ? "_" : "", passEnter ? Color.black : Color.white);
				
			}
			
		}
		
	}

	@Override
	public void stop() {
		
		AudioMaster.cleanUp();
		
	}
	
	@Override
	public void keyPressed(int key) {
		
		if((key == Keyboard.KEY_RETURN || key == Keyboard.KEY_NUMPADENTER) && !passEnter) {
				
			if(firstEnter && pass.length() > 2) {
			
				System.out.println(pass);
				passEnter = true;
				game.setBackgroundColor(Color.white);
				game.setFullscreen(false);
				game.setMouseGrabbed(false);
				sad.play();
			
			} else {
				
				firstEnter = true;
				characters = 0;
				pass = "";
				
				glClear(GL_COLOR_BUFFER_BIT);
				glClearColor((float) backgroundColor.getRed() / 255F, (float) backgroundColor.getGreen() / 255F, (float) backgroundColor.getBlue() / 255F, 1);
				
				String firstCharactersStr = "";
				
				for(int i = 0; i < firstCharacters; i++) {
					
					firstCharactersStr += "*";
					
				}
				
				Renderer.renderString(15, 25, game.getFont("exampleFont"), "enter password: " + firstCharactersStr + (firstEnter ? "" : "_"), passEnter ? Color.black : Color.white);
				Renderer.renderString(15, 55, game.getFont("exampleFont"), (firstEnter ? "_" : ""), passEnter ? Color.black : Color.white);
				Display.update();
				
				try {
					
					Thread.sleep(1500);
					
				} catch (InterruptedException e) {

					e.printStackTrace();
					
				}
				
			}
			
		}
		
	}
	
}
