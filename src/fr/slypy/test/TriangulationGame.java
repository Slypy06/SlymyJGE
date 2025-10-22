package fr.slypy.test;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.slypy.slymyjge.Game;
import fr.slypy.slymyjge.graphics.Renderer;
import fr.slypy.slymyjge.graphics.Texture;
import io.github.jdiemke.triangulation.DelaunayTriangulator;
import io.github.jdiemke.triangulation.NotEnoughPointsException;
import io.github.jdiemke.triangulation.Triangle2D;
import io.github.jdiemke.triangulation.Vector2D;

public class TriangulationGame extends Game {

	private List<Node> nodes = new ArrayList<Node>();
	private List<Triangle2D> triangles = new ArrayList<>();
	private final int minBrightness = 50;
	private final int maxBrightness = 200;
	private int difference = maxBrightness-minBrightness;
	
	public TriangulationGame(int width, int height, String title, Color backgroundColor, boolean resizable) {

		super(width, height, title, backgroundColor, resizable);

	}

	public static void main(String[] args) {
		
		System.setProperty("org.lwjgl.librarypath", new File("lib/natives").getAbsolutePath());
		new TriangulationGame(1920 , 1080, "Triangulation Game", Color.white, true).start();
		
	}

	@Override
	public void render(double alpha) {
		
		
		for(Triangle2D t : triangles) {
			
			float y = ((float) t.a.y + (float) t.b.y + (float) t.c.y) / (float) (3 * getHeight());
			float x = ((float) t.a.x + (float) t.b.x + (float) t.c.x) / (float) (3 * getWidth());
			
			//Renderer.renderTriangle((float) t.a.x, (float) t.a.y, (float) t.b.x, (float) t.b.y, (float) t.c.x, (float) t.c.y, new Color(minBrightness + (int) (difference*y), minBrightness + (int) (difference*(Math.abs(x-y))), minBrightness + (int) (difference*x)));
			//Renderer.renderTriangle((float) t.a.x, (float) t.a.y, (float) t.b.x, (float) t.b.y, (float) t.c.x, (float) t.c.y, new Color(minBrightness + (int) (difference*y), 0, minBrightness + (int) (difference*x)));
			//Renderer.renderTriangle((float) t.a.x, (float) t.a.y, (float) t.b.x, (float) t.b.y, (float) t.c.x, (float) t.c.y, new Color(Color.HSBtoRGB(x, y, maxBrightness/255.0f)));
			
		}
		
		for(Node n : nodes) {
			
			n.render();
			
		}

	}

	@Override
	public void init() {

		setShowFPS(true);
		setShowTPS(true);
		
		setFrameCap(150);
		setTickCap(150);
		
		Random r = new Random();
		
		for(int i = 0; i < 150; i++) {

			nodes.add(new Node(r.nextInt(getWidth()), r.nextInt(getHeight()), r.nextInt(360), r.nextInt(1,25), r.nextInt(3, 7)));
			
		}
		
		nodes.add(new Node(-50,-50,0,0,0));
		nodes.add(new Node(getWidth()+50,-50,0,0,0));
		nodes.add(new Node(-50,getHeight()+50,0,0,0));
		nodes.add(new Node(getWidth()+50,getHeight()+50,0,0,0));
		
	

	}

	@Override
	public void update(double alpha) {

		List<Vector2D> vector = new ArrayList<>();
		
		for(Node n : nodes) {
			
			if(n.size > 0) {

				n.x += (alpha*n.speed*Math.cos(Math.toRadians(n.direction)));
				n.y -= (alpha*n.speed*Math.sin(Math.toRadians(n.direction)));
				
				if(n.x < 0) {
					
					n.x = -n.x;
					n.direction = n.direction>180 ? 540-n.direction : 180-n.direction;
					
				}
				
				if(n.x > getWidth()) {
					
					n.x -= n.x-getWidth();
					n.direction = n.direction>180 ? 540-n.direction : 180-n.direction;
					
				}
				
				if(n.y < 0) {
					
					n.y = -n.y;
					n.direction = 360-n.direction;
					
				}
				
				if(n.y > getHeight()) {
					
					n.y -= n.y-getHeight();
					n.direction = 360-n.direction;
					
				}
				
				float a = n.x - getXCursorOnScreen();
				float b = getYCursorOnScreen() - n.y;
				
				if(Math.sqrt(a*a+b*b) < 100) {
					
					Vector2D pushAway = toDirectionalVector(new Vector2D(a, b));
					pushAway.y = (100 - pushAway.y) / 25;
					
					Vector2D oldVec = new Vector2D(Math.toRadians(n.direction), n.speed);
					
					Vector2D newVec = addVectorToDirectionalVector(oldVec, pushAway);
	
					n.direction = (int) Math.toDegrees(newVec.x);
					n.speed = (int) newVec.y;
					
				}
				
				n.speed = 5*(float) Math.pow(n.speed/5, Math.pow(0.95, alpha));
			
			}
			
			vector.add(new Vector2D(n.x, n.y));

			
		}

		try {

			DelaunayTriangulator delaunayTriangulator = new DelaunayTriangulator(vector);
			delaunayTriangulator.triangulate();
			
			triangles = delaunayTriangulator.getTriangles();

		} catch (NotEnoughPointsException e) {

			e.printStackTrace();

		}

	}
	
	@Override
	public void stop() {

		// TODO Auto-generated method stub

	}
	
	public Vector2D addVectorToDirectionalVector(Vector2D base, Vector2D add) {
		
		Vector2D convertedBase = new Vector2D(base.y * Math.cos(base.x), base.y * Math.sin(base.x));
		Vector2D convertedAdd = new Vector2D(add.y * Math.cos(add.x), add.y * Math.sin(add.x));
		convertedBase = convertedBase.add(convertedAdd);
		return toDirectionalVector(convertedBase);
		
	}
	
	public Vector2D toDirectionalVector(Vector2D v) {
		
		return new Vector2D(Math.atan2(v.y, v.x), Math.sqrt(v.x*v.x + v.y*v.y));
		
	}
	
	public static class Node {
		
		public float x;
		public float y;
		public int direction;
		public float speed;
		public int size;
		
		public Node(float x, float y, int direction, float speed, int size) {

			this.x = x;
			this.y = y;
			this.direction = direction;
			this.speed = speed;
			this.size = size;

		}
		
		public void render() {
			
			if(size > 0)
				Renderer.renderDisk(x, y, size, Color.black);
			
		}
		
	}

}
