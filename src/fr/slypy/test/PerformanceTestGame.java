package fr.slypy.test;

import java.awt.Color;
import java.io.File;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import fr.slypy.slymyjge.Game;
import fr.slypy.slymyjge.graphics.ISurface;
import fr.slypy.slymyjge.graphics.MultiSampledSurface;
import fr.slypy.slymyjge.graphics.NewGenRenderer;
import fr.slypy.slymyjge.graphics.ShapeBundle;
import fr.slypy.slymyjge.graphics.Surface;
import fr.slypy.slymyjge.graphics.Texture;
import fr.slypy.slymyjge.graphics.shape.Quad;
import fr.slypy.slymyjge.graphics.shape.Rectangle;
import fr.slypy.slymyjge.graphics.shape.TexturedQuad;
import fr.slypy.slymyjge.graphics.shape.TexturedRectangle;


//using vbo improve cpu->gpu transfer but increase cpu load. It does not change gpu load
//using vbo for heavy load is unecessary
//using vbo for heavy cpu load (really fast refresh rate) is bad
//using vbo is extremely efficient when transfering a lot of data that is not heavy to render (batch of pixels)

public class PerformanceTestGame extends Game {
	
	public static final int NUM = 100000;
	public static final int W = 4;
	public static final int H = 2;
	
	public Quad[] quads = new Quad[NUM];
	public ShapeBundle<Quad> bundledQuads = new ShapeBundle<>(NUM, this, Rectangle.INFOS);
	
	public int mode = 0;
	public int bufferId;
	
	private ISurface sur;
	
	@SuppressWarnings("unused")
	private Texture tex;
	
	private TexturedQuad quad;
	
	private Random r = new Random();

	public PerformanceTestGame(int width, int height, String title, Color backgroundColor, boolean resizable) {
		
		super(width, height, title, backgroundColor, resizable);

	}
	
	public static void main(String[] args) {
		
		System.setProperty("org.lwjgl.librarypath", new File("lib/natives").getAbsolutePath());
		
		new PerformanceTestGame(1920, 1080, "Test", Color.white, false).start();
		
	}

	@Override
	public void init() {
		
		setShowFPS(true);
		setShowTPS(true);
		
		setTickCap(20);
		
		for(int i = 0; i < NUM; i++) {
			
			quads[i] = new Rectangle(r.nextInt(getWidth()), r.nextInt(getHeight()), r.nextInt(W/4, W), r.nextInt(H/4, H), new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
			
		}
		
		bundledQuads.addShapes(quads);
		
		bundledQuads.pushChanges();
		
		tex = Texture.loadTexture("origin1.png");
		
		sur = new Surface(1920, 1080, this);
		
		quad = new TexturedRectangle(0, 0, 1920, 1080);
		quad.setTexture(sur.getTextureId());
		
		addKeyToListen(Keyboard.KEY_S);
		
	}

	@Override
	public void update(double alpha) {
		
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			
			quads[quads.length - 1] = new Rectangle(r.nextInt(getWidth()), r.nextInt(getHeight()), 50, 20, new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
			bundledQuads.setShape(quads.length-1, quads[quads.length - 1]);
			bundledQuads.pushChanges();
			
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_X)) {
			
			for(int i = 0; i < quads.length; i++) {
				
				quads[i] = quads[i].rotate((float) alpha);
				bundledQuads.setShape(i, quads[i]);
				bundledQuads.pushChanges();
				
			}
			
		}
		
	}
	
	@Override
	public void render(double alpha) {
		
		switch(mode) {
			
			case 0: 
			
				for(Quad q : quads) {
					
					NewGenRenderer.renderShape(q);
					
				}
				
				break;

			case 1: 
			
				NewGenRenderer.renderShapeBundle(bundledQuads);
				break;
				
			case 2:
				
				if(bundledQuads.isAwaitingPush()) {
					
					NewGenRenderer.renderOnSurface(() -> {
						
						NewGenRenderer.renderShapeBundle(bundledQuads);
						
					}, sur);
					
				}
				
				NewGenRenderer.renderShape(quad);
			
		}

		
	}
	
	@Override
	public void stop() {
		
		
		
	}
	
	@Override
	public void keyPressed(int k) {
		
		if(k == Keyboard.KEY_S) {
			
			mode = (mode+1)%3;
			bundledQuads.pushChanges();
			
			System.out.println("new mode : " + mode);
			
		}
		
	}

}
