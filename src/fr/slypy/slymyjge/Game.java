package fr.slypy.slymyjge;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.GLU;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;

import fr.slypy.slymyjge.components.Button;
import fr.slypy.slymyjge.components.Component;
import fr.slypy.slymyjge.components.MoveableComponent;
import fr.slypy.slymyjge.font.SlymyTrueTypeFont;
import fr.slypy.slymyjge.inputs.KeyboardInputs;
import fr.slypy.slymyjge.network.NetworkRegister;
import fr.slypy.slymyjge.utils.Logger;

public abstract class Game extends KeyboardInputs {
	
	protected boolean resizable;
	
	protected boolean showFPS = false;

	protected boolean autoResizeHeight = true;
	protected boolean autoResizeWidth = true;
	
	protected String title;

	protected int startWidth;
	protected int startHeight;
	
	protected int lastWidth;
	protected int lastHeight;
	
	protected int width;
	protected int height;
	
	protected float widthDiff = 1;
	protected float heightDiff = 1;
	
	protected float xCam = 0;
	protected float yCam = 0;
	
	protected Color backgroundColor;

	protected static ArrayList<Component> components = new ArrayList<Component>();
	
	protected static Map<String, SlymyTrueTypeFont> fonts = new HashMap<String, SlymyTrueTypeFont>();
	protected static Map<String, SlymyTrueTypeFont> startFonts = new HashMap<String, SlymyTrueTypeFont>();
	
	protected static long wait = 0;
	
	protected static Client client;
	
	public Game(int width, int height, String title) {
		
		this(width, height, title, Color.black, true);
		
	}
	
	public Game(int width, int height, String title, Color backgroundColor) {
		
		this(width, height, title, backgroundColor, true);
		
	}
	
	public Game(int width, int height, String title, boolean resizable) {
		
		this(width, height, title, Color.black, true);
		
	}
	
	public Game(int width, int height, String title, Color backgroundColor, boolean resizable) {
		
		this.startWidth = width;
		this.startHeight = height;
		
		this.width = this.startWidth;
		this.height = this.startHeight;
		
		this.title = title;
		
		this.backgroundColor = backgroundColor;
		
		this.resizable = resizable;
		
	}
	
	public void setupClientForMultiplayer(NetworkRegister networkRegister) {
		
		new Thread() {
		
			public void run() {
			
				client = new Client();
				client.start();
				
				Kryo kryo = client.getKryo();
				
				for(Class<?> packetClass : networkRegister.getClasses()) {
					
					kryo.register(packetClass);
					
				}
			
			}
		
		}.start();
		
	}
	
	public List<InetAddress> searchLocalServers(int udpAdress) {
		
		return client.discoverHosts(5000, udpAdress);
		
	}
	
	public InetAddress searchFirstLocalServer(int udpAdress) {
		
		return client.discoverHost(5000, udpAdress);
		
	}
	
	public void connectToServer(String adress, int tcpPort, int udpPort) {
		
		new Thread() {
			
			public void run() {
				
				try {
					
					client.connect(5000, adress, tcpPort, udpPort);
					
				} catch (IOException e) {

					e.printStackTrace();
					
				}
				
			}
			
		}.start();
		
	}
	
	public void connectToServer(String adress, int tcpPort) {
		
		new Thread() {
			
			public void run() {
				
				try {
					
					client.connect(5000, adress, tcpPort);
					
				} catch (IOException e) {

					e.printStackTrace();
					
				}
				
			}
			
		}.start();
		
	}
	
	public Client getClient() {
		
		return client;
		
	}
	
	public void setEscapeGameKey(int escapeGameKey) {
		
		this.escapeGameKey = escapeGameKey;
		
	}
	
	public int getEscapeGameKey() {
		
		return escapeGameKey;
		
	}
	
	public void registerFont(Font font, String uuid) {
		
		registerFont(font, uuid, false);
		
	}
	
	public void registerFont(Font font, String uuid, boolean antiAlias) {
		
		if(fonts.containsKey(uuid)) {
			
			System.err.println("TrueTypeFont with uuid " + uuid + " is already register ! Overriding it...");
			fonts.remove(uuid);
			
		}
		
		fonts.put(uuid, new SlymyTrueTypeFont(font, false));
		startFonts.put(uuid, fonts.get(uuid));
		
	}
	
	public void start() {
		
		display();
		loop();
		
	}
	
	public void exit() {
		
		Logger.log("Arret du syst�me");
		
		Display.destroy();
		System.exit(0);
		
	}
	
	public void loop() {
		
		Logger.log("Initialisation du jeu");
		
		escapeGameKey = getEscapeGameKey();
		
		init();
		
		long before = System.nanoTime();
		
		double alpha = System.nanoTime() - before;
		long fps = 0;
		
		long lastFpsUpdate = System.nanoTime();
		
		Logger.log("D�marage de la boucle principale du jeu");
		
		while(true) {
			
			if (Display.isCloseRequested() || this.isCloseRequested()) {
				
				Logger.log("Arret du jeu");
				stop();
				
				exit();
				
			}
			
			alpha = (double) (System.nanoTime() - before) / 1000000000D;
			
			if(wait > 0) {
				
				try {
					
					Thread.sleep(wait);
					
				} catch (InterruptedException e) {

					e.printStackTrace();
					
				}
				
				wait = 0;
				
			}
			
			before = System.nanoTime();
			
			Display.update();
			
			updateSize();
			
			view2D(width, height);
			
			translateView(xCam, yCam);
			
			keyUpdate();
			
			for(Component comp : components) {
				
				comp.update(getXCursor(), getYCursor(), this);
				
			}
			
			float wDiff = (float) width / (float) lastWidth;
			float hDiff = (float) height / (float) lastHeight;
			
			if(wDiff != 1 || hDiff != 1) {
				
				for(String uuid : startFonts.keySet()) {
					
					SlymyTrueTypeFont ttf = fonts.get(uuid);
					
					Font f = ttf.getFont();
					
					fonts.remove(uuid);
					
					if(widthDiff <= heightDiff) {
					
						ttf = new SlymyTrueTypeFont(f.deriveFont(startFonts.get(uuid).getFont().getSize() * widthDiff), ttf.antiAlias());
					
					} else {
						
						ttf = new SlymyTrueTypeFont(f.deriveFont(startFonts.get(uuid).getFont().getSize() * heightDiff), ttf.antiAlias());
						
					}
					
					fonts.put(uuid, ttf);
					
				}
				
			}
			
			update(alpha);
			
			glClear(GL_COLOR_BUFFER_BIT);
			glClearColor((float) backgroundColor.getRed() / 255F, (float) backgroundColor.getGreen() / 255F, (float) backgroundColor.getBlue() / 255F, 1);
			
			render();
			
			if (System.nanoTime() - lastFpsUpdate > 1000000000L) {

				fps = (long)(1D / alpha);
				
				if(showFPS) {
				
					Display.setTitle(title + " || FPS: " + fps);
				
				}
				
				lastFpsUpdate = System.nanoTime();

			}
			
		}
		
	}
	
	public void delay(long millis) {
		
		wait = millis;
		
	}
	
	public void setBackgroundColor(Color color) {
		
		this.backgroundColor = color;
		
	}
	
	public Color getBackgroundColor() {
		
		return this.backgroundColor;
		
	}
	
	public SlymyTrueTypeFont getFont(String uuid) {
		
		if(fonts.containsKey(uuid)) {
			
			return fonts.get(uuid);
			
		} else {
			
			System.err.println("Font with uuid " + uuid + " not found !");
			return null;
			
		}
		
	}
	
	public void setMouseGrabbed(boolean grabbed) {
		
		Mouse.setGrabbed(grabbed);
		
	}
	
	public void setFullscreen(boolean fullscreen) {
		 
		if(isFullscreen() == fullscreen) {
			
			return;
			
		}
		
	    try {
				
	    	if(fullscreen) {
	    	
	    		Display.setDisplayModeAndFullscreen(Display.getDesktopDisplayMode());
	    	
	    	} else {

	    		Display.setDisplayMode(new DisplayMode(width, height));
	    		
	    	}
	    	
	        Display.setResizable(!resizable);
	        Display.setResizable(resizable);
	        Display.setTitle(title);
	        
	    } catch (LWJGLException e) {

			e.printStackTrace();
		}
	}
	
	public boolean isFullscreen() {
		
		return Display.isFullscreen();
		
	}
	
	public void setResizable(boolean resizable) {
		 
		if(isResizable() == resizable) {
			
			return;
			
		}

		boolean fullscreen = Display.isFullscreen();
	 
	    try {
	 
	        
	    	if(fullscreen) {
		    	
	    		Display.setDisplayModeAndFullscreen(Display.getDesktopDisplayMode());
	    	
	    	} else {
	    		
	    		Display.setDisplayMode(new DisplayMode(width, height));
	    		
	    	}
	    	
	        Display.setResizable(resizable);
	        Display.setTitle(title);
	             
	    } catch (LWJGLException e) {
	    	
	        e.printStackTrace();
	        
	    }
	    
	}
	
	public boolean isResizable() {
		
		return Display.isResizable();
		
	}
	
	public void setSize(int width, int height) {
		
		boolean fullscreen = Display.isFullscreen();

		if((getWidth() == width && getHeight() == height) || fullscreen) {
			
			return;
			
		}
	 
	    try {

	    	Display.setDisplayMode(new DisplayMode(width, height));
	        Display.setResizable(resizable);
	        Display.setTitle(title);
	             
	    } catch (LWJGLException e) {
	        
	    	e.printStackTrace();
	    	
	    }
		
	}
	
	public int getWidth() {
		
		return width;
		
	}
	
	public int getHeight() {
		
		return height;
		
	}

	public void updateSize() {
		
		lastWidth = width;
		lastHeight = height;
		
		width = Display.getWidth();
		height = Display.getHeight();
		
		if(autoResizeWidth) {
		
			widthDiff = width / (float) startWidth;
		
		} else {
			
			widthDiff = 1;
			
		}
		
		if(autoResizeHeight) {
		
			heightDiff = height / (float) startHeight;
		
		} else {
			
			heightDiff = 1;
		
		}
		
	}
	
	public abstract void init();
	
	public abstract void update(double alpha);
	
	public abstract void render();
	
	public abstract void stop();
	
	public void display() {
		
		Logger.log("Cr�ation de la fen�tre");
		
		try {
			
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setResizable(resizable);
			Display.setTitle(title);
			Display.create();
			
		} catch (LWJGLException e) {
			
			e.printStackTrace();
			
		}
		
	}
	
	private void view2D(int width, int height) {
		
		glViewport(0, 0, width, height);
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		GLU.gluOrtho2D(0, width, height, 0);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
		glEnable(GL_TEXTURE_2D);
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
	}
	
	public void translateView(float xa, float ya) {
		
		glTranslatef(xa * getWidthDiff(), ya * getHeightDiff(), 0);
		
	}
	
	public void addComponent(Component comp) {
		
		components.add(comp);
		
	}
	
	public void addComponents(Component[] comps) {
		
		for(Component comp : comps) {
			
			components.add(comp);
			
		}
		
	}
	
	public float getXCursor() {
		
		return (Mouse.getX() / getWidthDiff()) - xCam;
		
	}
	
	public float getYCursor() {
		
		return ((-Mouse.getY() + Display.getHeight()) / getHeightDiff()) - yCam;
		
	}
	
	public float getXCursorOnScreen() {
		
		return (Mouse.getX() / getWidthDiff());
		
	}
	
	public float getYCursorOnScreen() {
		
		return ((-Mouse.getY() + Display.getHeight()) / getHeightDiff());
		
	}
	
	public float getXCam() {
		
		return xCam;
		
	}

	public float getYCam() {
		
		return yCam;
		
	}
	
	public void setXCam(float xCam) {
		
		this.xCam = xCam;
		
	}

	public void setYCam(float yCam) {
		
		this.yCam = yCam;
		
	}
	
	public void setShowFPS(boolean showFPS) {
		
		this.showFPS = showFPS;
		
	}
	
	public boolean showFPS() {
		
		return showFPS;
		
	}
	
	public ArrayList<Component> getComponents() {
		
		return components;
		
	}

	public Button buttonHover() {

		Button button = null;
		
		for(Component comp : components) {
				
			if(comp instanceof MoveableComponent) {
				
				if(((MoveableComponent) comp).isMove()) {
					
					return null;
					
				}
				
			}
			
			if(comp.isHover()) {
				
				button = comp instanceof Button ? (Button) comp : null;
					
			}
			
		}
		
		return button;
		
	}
	
	public MoveableComponent moveableComponentHover() {

		MoveableComponent moveableComponent = null;
		
		for(Component comp : components) {
				
			if(comp instanceof MoveableComponent) {
				
				if(((MoveableComponent) comp).isMove()) {
					
					return (MoveableComponent) comp;
					
				}
				
			}
			
			if(comp.isHover()) {
					
				moveableComponent = comp instanceof MoveableComponent ? (MoveableComponent) comp : null;
					
			}
			
		}
		
		return moveableComponent;
		
	}
	
	public Component componentHover() {

		Component component = null;
		
		for(Component comp : components) {
				
			if(comp instanceof MoveableComponent) {
				
				if(((MoveableComponent) comp).isMove()) {
					
					return comp;
					
				}
				
			}
			
			if(comp.isHover()) {
					
				component = comp;
					
			}
			
		}
		
		return component;
		
	}

	public float getWidthDiff() {
		
		return widthDiff;
		
	}

	public float getHeightDiff() {
		
		return heightDiff;
		
	}
	
}