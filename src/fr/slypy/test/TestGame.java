package fr.slypy.test;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.lwjgl.util.vector.Vector2f;

import fr.slypy.slymyjge.Game;
import fr.slypy.slymyjge.animations.framed.Animation;
import fr.slypy.slymyjge.graphics.Renderer;
import fr.slypy.slymyjge.graphics.Texture;
import fr.slypy.test.imagematching.Hilbert;

public class TestGame extends Game {

	public static Game game;
	
	private Texture baseTexture;
	private Texture goalTexture;
	private Animation anim;
	
	private List<Pixel> pixels = new ArrayList<>();
	private double transitionProgress = -0.2; // 0 to 1
	private double transitionSpeed = 1.0/8.0;  // 2 seconds transition
	private BufferedImage[] animImgs;
	private AtomicInteger counter = new AtomicInteger(1);
	
	private FloatBuffer buffer;
	private int bufferId;
	
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
		
		game.setFrameCap(360);
		game.setTickCap(20);
		game.setShowFPS(true);
		game.setShowTPS(true);

		baseTexture = Texture.loadTexture("origin1.png");
		goalTexture = Texture.loadTexture("origin2.png");
		//anim = new Animation(Animation.loadAnimationFromGif("square_rick.gif"));
		//goalTexture = ((TexturedAnimationFrame) anim.getFrames().get(0)).getTexture();
		//anim.setSpeed(10);
		
		//animImgs = new BufferedImage[anim.getFrames().size()];
		
	    //Map<Vector2f, Vector2f> transformMap = Genetic.geneticAssignment(baseTexture.getImage(), goalTexture.getImage(), 0.5f, 200000);
	    
	    //animImgs[0] = Genetic.applyTransformMap(baseTexture.getImage(), transformMap);
		
		/*for(int i = 1; i < anim.getFrames().size(); i++) {
			
			final int index = i;
			
			new Thread(() -> {
				
				BufferedImage img = Genetic.transform(animImgs[0], ((TexturedAnimationFrame) anim.getFrames().get(index)).getTexture().getImage(), 0.5f, 200000);
				animImgs[index] = img;
				counter.incrementAndGet();
				
			}).start();
			
		}
		
		//newTexture = Texture.loadTexture(transform(baseTexture.getImage(), goalTexture.getImage()));
	    
	    while(counter.get() < anim.getFrames().size()) {
	    	
	    	System.out.println(counter.get() + "/" + anim.getFrames().size() + " frames calculated");
	    	
	    	try {

				Thread.sleep(1000);

			} catch (InterruptedException e) {

				// TODO Auto-generated catch block
				e.printStackTrace();

			}
	    	
	    }
	    
	    for(int i = 0; i < anim.getFrames().size(); i++) {
	    	
	    	((TexturedAnimationFrame) anim.getFrames().get(i)).setTexture(Texture.loadTexture(animImgs[i]));
	    	
	    }*/
	    
	    Map<Vector2f, Vector2f> transformMap = Hilbert.getTransformMap(baseTexture.getImage(), goalTexture.getImage());
	    //Map<Vector2f, Vector2f> transformMap = Hungarian.optimalAssignment(baseTexture.getImage(), goalTexture.getImage(), 0);
	    
	    for (Map.Entry<Vector2f, Vector2f> entry : transformMap.entrySet()) {
	        Vector2f start = entry.getKey();
	        Vector2f end = entry.getValue();
	        Color c = new Color(baseTexture.getImage().getRGB((int) start.x, (int) start.y));
	        pixels.add(new Pixel(start.x, start.y, end.x, end.y, c));
	    } 
	    
	    //buffer = BufferUtils.createFloatBuffer(6*4*pixels.size());
	    //bufferId = Renderer.genBuffer(pixels.size());

	}

	@Override
	public void update(double alpha) {

	    transitionProgress += alpha * transitionSpeed;
	    if (transitionProgress > 1) {
	    	
	    	transitionProgress = 1;
	    	//anim.setPlaying(true);
	    	
	    }
	    
	    System.out.println(transitionProgress);

	    for (Pixel p : pixels) {
	        p.update(transitionProgress);
	    }
	    
	    //anim.update(alpha);
		
	}
	
	@Override
	public void render(double alpha) {
		
		//buffer.clear();
		
		//Renderer.renderTexturedQuad(0, 0, width, height, newTexture);
		
		/*if(anim.isPlaying()) {
			
			anim.render(0, 0, getWidth(), getHeight());
			
		} else {*/
		
		    //for (Pixel p : pixels) {
		    	
		       // p.render();
		        
		    //}
	    
		//}
		
	}

	@Override
	public void stop() {
		
		
		
	}
	
	public static class Pixel {
		
	    public float startX, startY;
	    public float targetX, targetY;
	    public Color color;
	    public float currentX, currentY;
	    
	    private float scale = 3f;

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