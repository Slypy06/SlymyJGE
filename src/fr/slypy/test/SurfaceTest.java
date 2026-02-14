package fr.slypy.test;

import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glRotatef;

import java.awt.Color;
import java.io.File;

import org.lwjgl.util.vector.Vector2f;

import fr.slypy.slymyjge.Game;
import fr.slypy.slymyjge.graphics.NewGenRenderer;
import fr.slypy.slymyjge.graphics.Surface;
import fr.slypy.slymyjge.graphics.shape.Point;
import fr.slypy.slymyjge.graphics.shape.TexturedRectangle;
import fr.slypy.slymyjge.graphics.shape.Triangle;

public class SurfaceTest extends Game {
	
	private Surface s1;
	private Surface s2;
	private TexturedRectangle s1Rect;
	private TexturedRectangle s2Rect;

	public SurfaceTest(int width, int height, String title, Color backgroundColor, boolean resizable) {
		
		super(width, height, title, backgroundColor, resizable);
		
	}
	
	public static void main(String[] args) {
		
		System.setProperty("org.lwjgl.librarypath", new File("lib/natives").getAbsolutePath());
		
		new SurfaceTest(1920, 1080, "Test", Color.white, false).start();
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(double alpha) {
		
		NewGenRenderer.renderOnSurface(() -> {
			
			glTranslatef(10, 10, 0);
			
			NewGenRenderer.renderShape(new Point(new Vector2f(0+10, 0+10), 20, Color.red));
			NewGenRenderer.renderShape(new Point(new Vector2f(512-10, 512-10), 20, Color.red));
			NewGenRenderer.renderShape(new Point(new Vector2f(512-10, 0+10), 20, Color.red));
			NewGenRenderer.renderShape(new Point(new Vector2f(0+10, 512-10), 20, Color.red));
			
		}, s1);
		
		NewGenRenderer.renderOnSurface(() -> {
			
			NewGenRenderer.renderInsideArea(0, 0, 60, 150, () -> {
				
				glRotatef(20, 0, 0, 1);
				
				NewGenRenderer.renderShape(new Point(new Vector2f(0+10, 0+10), 20, Color.green));
				NewGenRenderer.renderShape(new Point(new Vector2f(256-10, 256-10), 20, Color.green));
				NewGenRenderer.renderShape(new Point(new Vector2f(256-10, 0+10), 20, Color.green));
				NewGenRenderer.renderShape(new Point(new Vector2f(0+10, 256-10), 20, Color.green));
				NewGenRenderer.renderShape(new Triangle(new Vector2f(50, 200), new Vector2f(100, 100), new Vector2f(25, 75), Color.pink));
				
			});
			
		}, s2);
		
		NewGenRenderer.renderShape(s1Rect);
		NewGenRenderer.renderShape(s2Rect);
		
	}

	@Override
	public void init() {
		
		setShowFPS(true);
		setShowTPS(true);
		
		setTickCap(20);
		setFrameCap(240);
		
		s1 = new Surface(512, 512, this);
		s2 = new Surface(256, 256, new Color(0, 0, 128, 255), this);
		s1Rect = new TexturedRectangle(50, 30, 512, 512);
		s2Rect = new TexturedRectangle(400, 50, 800, 200);
		s1Rect.setTexture(s1.getTextureId());
		s2Rect.setTexture(s2.getTextureId());
		
	}

	@Override
	public void update(double alpha) {
		// TODO Auto-generated method stub
		
	}

}
