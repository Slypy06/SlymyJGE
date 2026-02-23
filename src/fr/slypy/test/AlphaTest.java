package fr.slypy.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import javax.naming.CompositeName;

import fr.slypy.slymyjge.Game;
import fr.slypy.slymyjge.graphics.ISurface;
import fr.slypy.slymyjge.graphics.NewGenRenderer;
import fr.slypy.slymyjge.graphics.Surface;
import fr.slypy.slymyjge.graphics.Texture;
import fr.slypy.slymyjge.graphics.shape.TexturedRectangle;

public class AlphaTest extends Game {
	
	private static final BiFunction<BufferedImage, Double, BiFunction<Dimension, ByteBuffer, BiConsumer<Point, Integer>>> CUSTOM_MASK = (originalImg, db) -> (targetSize, buffer) -> {

		BufferedImage scaled = new BufferedImage(targetSize.width, targetSize.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = scaled.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.drawImage(originalImg, 0, 0, targetSize.width, targetSize.height, null);
		g2d.dispose();

		return (coord, rgb) -> {
			
			int maskRgb = scaled.getRGB(coord.x, coord.y);
			
			double intensity = db;
			
			if(intensity<0) {
				
				intensity = 0.0d;
				
			} else if(intensity>1) {
				
				intensity = 1.0d;
				
			}

			int r1 = (rgb >> 16) & 0xFF;
			int g1 = (rgb >> 8) & 0xFF;
			int b1 = (rgb) & 0xFF;
			int a1 = (rgb >> 24) & 0xFF;
			int r2 = (maskRgb >> 16) & 0xFF;
			int g2 = (maskRgb >> 8) & 0xFF;
			int b2 = (maskRgb) & 0xFF;
			int a2 = (maskRgb >> 24) & 0xFF;
			
			int r = (int) ((r1*intensity + r2*(1-intensity)));
			int g = (int) ((g1*intensity + g2*(1-intensity)));
			int b = (int) ((b1*intensity + b2*(1-intensity)));
			int a = (int) ((a1*intensity + a2*(1-intensity)));

			buffer.put((byte) r);
			buffer.put((byte) g);
			buffer.put((byte) b);
			buffer.put((byte) a);
			
		};
		    
	};
	
	private Texture mask;
	private Texture tex;
	private Texture tex2;
	private ISurface sur;
	private TexturedRectangle surfaceQuad;
	private double intensity = 0;
	
	public AlphaTest(int width, int height, String title, Color backgroundColor, boolean resizable) {
		
		super(width, height, title, backgroundColor, resizable);

	}
	
	public static void main(String[] args) {
		
		System.setProperty("org.lwjgl.librarypath", new File("lib/natives").getAbsolutePath());
		
		new AlphaTest(1920, 1080, "Test", Color.white, false).start();
		
	}

	@Override
	public void init() {
		
		setShowFPS(true);
		setShowTPS(true);
		
		setTickCap(20);
		setFrameCap(100);
		
		mask = Texture.loadTexture("origin3.png");
		tex = Texture.loadTexture("origin1.png", Texture.MASK.apply(mask.getImage()));
		tex2 = Texture.loadTexture("origin1.png");
		//tex = Texture.loadTexture("origin1.png");
		
		sur = new Surface(500, 500, this);
		surfaceQuad = new TexturedRectangle(100, 100, 500, 500);
		surfaceQuad.setTexture(sur.getTextureId());
		
	}

	@Override
	public void update(double alpha) {

		intensity += alpha / 2;
		
		if(intensity > 1)
			intensity = 1;
		
	}
	
	@Override
	public void render(double alpha) {
		
		/*NewGenRenderer.renderOnSurface(() -> {
			
			NewGenRenderer.renderShape(new TexturedRectangle(0, 0, 500, 500, tex, Color.white));
			
			NewGenRenderer.renderWithBlendFunction(() -> {
				
				NewGenRenderer.renderShape(new TexturedRectangle(0, 0, 500, 500, mask, Color.white));
				
			}, BlendFunction.MULTIPLICATIVE);
			
		}, sur);
		
		NewGenRenderer.renderShape(surfaceQuad);*/
		
		NewGenRenderer.renderShape(new TexturedRectangle(100, 100, 500, 500, tex, Color.white));
		NewGenRenderer.renderShape(new TexturedRectangle(700, 100, 500, 500, tex2, Color.white));
		NewGenRenderer.renderShape(new TexturedRectangle(700, 100, 500, 500, mask, new Color(255, 255, 255, (int) (255*intensity))));
		
	}
	
	@Override
	public void stop() {
		
		
		
	}

}
