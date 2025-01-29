package fr.slypy.test;

import java.awt.Color;
import java.awt.Font;
import java.io.File;

import fr.slypy.slymyjge.Game;
import fr.slypy.slymyjge.components.ButtonComponent;
import fr.slypy.slymyjge.font.SlymyFont;
import fr.slypy.slymyjge.graphics.Renderer;

public class TestGame extends Game {

	public static Game game;
	public int m;
	public int n;
	public SlymyFont f;
	public SlymyFont f2;
	public SlymyFont f3;
	
	public static double Gamma = 0.80;
	public static double IntensityMax = 255;
	
	public int color = 0;
	public Color[] colors = {
        new Color(148, 0, 211),  // Violet
        new Color(75, 0, 130),   // Indigo
        new Color(0, 0, 255),    // Blue
        new Color(0, 255, 0),    // Green
        new Color(255, 255, 0),  // Yellow
        new Color(255, 127, 0),  // Orange
        new Color(255, 0, 0)     // Red
    };
	
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

		game.setAutoResizeType(true, true, true);
		
		f = new SlymyFont(new Font("", Font.PLAIN, 30), Color.white);
		f2 = new SlymyFont(new Font("", Font.BOLD, 120), Color.white);
		f3 = new SlymyFont(new Font("", Font.PLAIN, 60), Color.white);
		
		ButtonComponent comp = new ButtonComponent(180, 400, 150, 75, game) {
			
			@Override
			public void render() {
				
				Renderer.renderQuad(this.getX(), this.getY(), this.getW(), this.getH(), this.isHover() ? (this.isPressed() ? new Color(120, 120, 120) : new Color(180, 180, 180)) : new Color(220, 220, 220));
				Renderer.renderBorder(this.getX(), this.getY(), this.getW(), this.getH(), 2, Color.DARK_GRAY);
				
				String text = "YES";
				
				Renderer.renderText(this.getX() + (this.getW() - f.getWidth(text)) / 2, this.getY() + (this.getH() - f.getHeight()) / 2, f, text);

			}
			
			@Override
			public void componentActivated() {
				
				this.setVisible(false);
				this.setActivated(false);
				
			}
			
		};
		
		game.addComponent(comp, "yes");
		
		ButtonComponent comp2 = new ButtonComponent(950, 400, 150, 75, game) {
			
			@Override
			public void render() {
				
				Renderer.renderQuad(this.getX(), this.getY(), this.getW(), this.getH(), this.isHover() ? (this.isPressed() ? new Color(120, 120, 120) : new Color(180, 180, 180)) : new Color(220, 220, 220));
				Renderer.renderBorder(this.getX(), this.getY(), this.getW(), this.getH(), 2, Color.DARK_GRAY);
				
				String text = "NO";
				
				Renderer.renderText(this.getX() + (this.getW() - f.getWidth(text)) / 2, this.getY() + (this.getH() - f.getHeight()) / 2, f, text);

				
			}
			
			@Override
			public void onMouseEntering() {
				
				super.onMouseEntering();
				
				this.setActivated(false);
				this.setVisible(false);
				
			}
			
		};
		
		game.addComponent(comp2, "no");

	}

	@Override
	public void update(double alpha) {

		color += alpha * 400;
		
		ButtonComponent no = (ButtonComponent) game.getComponent("no");
		
		int x = (int) (no.getX() + (no.getW() / 2.0f));
		int y = (int) (no.getY() + (no.getH() / 2.0f));
		
		int xCursor = (int) game.getXCursor();
		int yCursor = (int) game.getYCursor();
		
		int deltaX = x - xCursor;
		int deltaY = y - yCursor;

		// Calculate the distance between the object and the cursor
		double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

		// Define the maximum distance for movement and a speed factor for tuning
		double maxDistance = 350;
		double speedFactor = 400;

		// Check if the distance is within the threshold
		if (distance < maxDistance && distance > 10) {  // Move only within a reasonable distance range
		    // Calculate the movement strength inversely related to the distance
		    double strength = 1 / (Math.pow(distance / speedFactor, 4));

		    // Move x and y away from the cursor by inverting the direction of deltaX and deltaY
		    x += (int) (strength * (deltaX / distance));
		    y += (int) (strength * (deltaY / distance));
		    
		    x -= (no.getW() / 2.0f);
		    y -= (no.getH() / 2.0f);
		    
		    if(x < 0) {
		    	
		    	x = 0;
		    	
		    }
		    
		    if(y < 0) {
		    	
		    	y = 0;
		    	
		    }
		    
		    if(x > 1130) {
		    	
		    	x = 1130;
		    	
		    }
		    
		    if(y > 645) {
		    	
		    	y = 645;
		    	
		    }
		    
		    no.setX(x);
		    no.setY(y);
		    
		}
		
	}
	
	@Override
	public void render(double alpha) {
		
		Renderer.renderText(640 - f3.getWidth("Are you gay ?") / 2, 100, f3, "Are you gay ?", Color.black);
		
		if(!game.getComponent("no").isVisible()) {
			
			Renderer.renderText(640 - f.getWidth("Get fucked sucker") / 2, 50, f, "Get fucked sucker", Color.red);
			
		}
		
		if(!game.getComponent("yes").isVisible()) {

	        for (int i = 0; i < 1000; i++) {
	            // Determine the current and next color for interpolation
	            Renderer.renderQuad(i*1.28f, 0, 2, 720, sineWaveColor((((i+color) % 1500)/1500.0f)));
	            
	        }
			
			Renderer.renderText(640 - f2.getWidth("I KNEW IT") / 2, 150, f2, "I KNEW IT", Color.white);
			
		}
		
	}

	@Override
	public void stop() {
		

		
	}
	
    public static Color sineWaveColor(double t) {
        // Adjust 't' to cycle from 0 to 1, where t can be a value from 0 to 1
        double frequency = 5*Math.PI/6.0f;  // Full cycle for smoother transitions
        
        // Apply sine waves to hue, saturation, and brightness to smooth out transitions
        float hue = (float) (0.5 + 0.5 * Math.sin(frequency * t + Math.PI/2+Math.PI/12));  // Centered around 0.5, oscillating [0, 1]
        float saturation = 1;  // Oscillates around 0.8
        float brightness = (float) 1;  // Oscillates around 0.9
        
        // Convert to Color
        return Color.getHSBColor(hue, saturation, brightness);
    }
	
}
/*
		ButtonComponent c = new ButtonComponent(540, 500, 200, 100, game) {
			
			@Override
			public void render() {
				
				Renderer.renderQuad(this.getX(), this.getY(), this.getW(), this.getH(), this.isHover() ? (this.isPressed() ? new Color(120, 120, 120) : new Color(180, 180, 180)) : new Color(220, 220, 220));
				Renderer.renderBorder(this.getX(), this.getY(), this.getW(), this.getH(), 2, Color.DARK_GRAY);
				
				String text = "CONFIRM";
				
				Renderer.renderText(this.getX() + (this.getW() - f.getWidth(text)) / 2, this.getY() + (this.getH() - f.getHeight()) / 2, f, text);
				
			}
			
			@Override
			public void componentActivated() {
				
				this.setVisible(false);
				this.setActivated(false);
				
				m = Integer.parseInt(t1.getText().isEmpty() ? "1" : t2.getText());
				n = Integer.parseInt(t2.getText().isEmpty() ? "1" : t2.getText());
				
				t1.setVisible(false);
				t1.setActivated(false);
				
				t2.setVisible(false);
				t2.setActivated(false);
				
			}
			
		};*/