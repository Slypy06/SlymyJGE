package fr.slypy.test;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.davidmoten.hilbert.HilbertCurve;
import org.davidmoten.hilbert.SmallHilbertCurve;
import org.lwjgl.util.vector.Vector2f;

import fr.slypy.slymyjge.Game;
import fr.slypy.slymyjge.graphics.Renderer;
import fr.slypy.slymyjge.graphics.Texture;

public class TestGame extends Game {

	public static Game game;
	
	private Texture baseTexture;
	private Texture goalTexture;
	
	private List<Pixel> pixels = new ArrayList<>();
	private double transitionProgress = -0.2; // 0 to 1
	private double transitionSpeed = 1.0/8.0;  // 2 seconds transition
	
	public TestGame(int width, int height, String title, Color backgroundColor, boolean resizable) {
		
		super(width, height, title, backgroundColor, resizable);

	}
	
	public static void main(String[] args) {
		
		System.setProperty("org.lwjgl.librarypath", new File("lib/natives").getAbsolutePath());
		
		game = new TestGame(1920, 1080, "Test Game Title", Color.white, true);
		game.start();
		
	}

	@Override
	public void init() {
		
		Renderer.init(game);
		
		game.setFrameCap(120);
		game.setTickCap(20);
		game.setShowFPS(true);
		game.setShowTPS(true);

		baseTexture = Texture.loadTexture("miniedt.png");
		goalTexture = Texture.loadTexture("minirick.png");
		//newTexture = Texture.loadTexture(transform(baseTexture.getImage(), goalTexture.getImage()));
		
	    Map<Vector2f, Vector2f> transformMap = getTransformMap(baseTexture.getImage(), goalTexture.getImage());

	    for (Map.Entry<Vector2f, Vector2f> entry : transformMap.entrySet()) {
	        Vector2f start = entry.getKey();
	        Vector2f end = entry.getValue();
	        Color c = new Color(baseTexture.getImage().getRGB((int) start.x, (int) start.y));
	        pixels.add(new Pixel(start.x, start.y, end.x, end.y, c));
	    }

	}

	@Override
	public void update(double alpha) {

	    transitionProgress += alpha * transitionSpeed;
	    if (transitionProgress > 1) transitionProgress = 1;

	    for (Pixel p : pixels) {
	        p.update(transitionProgress);
	    }
		
	}
	
	@Override
	public void render(double alpha) {
		
		//Renderer.renderTexturedQuad(0, 0, width, height, newTexture);
	    for (Pixel p : pixels) {
	        p.render();
	    }
		
	}

	@Override
	public void stop() {
		

		
	}
	
	private BufferedImage transform(BufferedImage from, BufferedImage to) {
		
		if(from.getWidth() != to.getWidth() || from.getHeight() != to.getHeight()) {
			
			return null;
			
		}
		
		BufferedImage output = new BufferedImage(from.getWidth(), from.getHeight(), from.getType());
		
		Map<Vector2f, Vector2f> transformMap = getTransformMap(from, to);
		
		for(Vector2f originPixel : transformMap.keySet()) {
			
			Vector2f goalPixel = transformMap.get(originPixel);
			
			output.setRGB((int) goalPixel.getX(), (int) goalPixel.getY(), from.getRGB((int) originPixel.getX(), (int) originPixel.getY()));
			
		}
		
		return output;
		
	}

	private Map<Vector2f, Vector2f> getTransformMap(BufferedImage from, BufferedImage to) {
		
		if(from.getWidth() != to.getWidth() || from.getHeight() != to.getHeight()) {
			
			return null;
			
		}
		
		List<Entry<Vector2f, Long>> fromList = toHilbertCurveSorted(from);
		List<Entry<Vector2f, Long>> toList = toHilbertCurveSorted(to);
		
		Map<Vector2f, Vector2f> output = new HashMap<Vector2f, Vector2f>();
		
		for(int i = 0; i < fromList.size(); i++) {
			
			output.put(fromList.get(i).getKey(), toList.get(i).getKey());
			
		}
		
		return output;
		
	}
	
	private List<Entry<Vector2f, Long>> toHilbertCurveSorted(BufferedImage img) {
		
		int noiseRange = 1000;
		Random r = new Random();
		
		SmallHilbertCurve smallHilbertCurve = HilbertCurve.small().bits(8).dimensions(3);
		
		List<Entry<Vector2f, Long>> output = new ArrayList<Entry<Vector2f, Long>>();
		
		for(int i = 0; i < img.getHeight(); i++) {
			
			for(int j = 0; j < img.getWidth(); j++) {
				
				Color c = new Color(img.getRGB(j, i));
				
				output.add(new AbstractMap.SimpleEntry<Vector2f, Long>(new Vector2f(j, i), (r.nextInt(2*noiseRange) - noiseRange) + smallHilbertCurve.index(c.getRed(), c.getGreen(), c.getBlue())));
				
			}
			
		}
		
		output.sort(Map.Entry.comparingByValue());
		
		return output;
		
	}
	
	public static class Pixel {
		
	    public float startX, startY;
	    public float targetX, targetY;
	    public Color color;
	    public float currentX, currentY;
	    
	    private float scale = 7.5f;

	    public Pixel(float startX, float startY, float targetX, float targetY, Color color) {
	    	
	        this.startX = startX;
	        this.startY = startY;
	        this.targetX = targetX;
	        this.targetY = targetY;
	        this.color = color;
	        this.currentX = startX;
	        this.currentY = startY;
	        
	    }

	    /** 
	     * alpha = 0 -> start, alpha = 1 -> target
	     */
	    public void update(double alpha) {
	    	
	    	alpha = Math.max(alpha, 0);
	    	
	    	alpha = 126*Math.pow(alpha,5) - 420*Math.pow(alpha,6) + 540*Math.pow(alpha,7) - 315*Math.pow(alpha,8) + 70*Math.pow(alpha,9); // smoothstep
	    	
	        currentX = (float) (startX + (targetX - startX) * alpha);
	        currentY = (float) (startY + (targetY - startY) * alpha);
	        
	    }

	    public void render() {
	    	
	        Renderer.renderQuad(currentX*scale-1, currentY*scale-1, (int) Math.ceil(scale + 2), (int) Math.ceil(scale + 2), color);
	        
	    }
	    
	}
	
}