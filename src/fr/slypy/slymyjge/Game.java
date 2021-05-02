package fr.slypy.slymyjge;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
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
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import fr.slypy.slymyjge.components.Component;
import fr.slypy.slymyjge.font.SlymyFont;
import fr.slypy.slymyjge.graphics.Icon;
import fr.slypy.slymyjge.graphics.IconResolution;
import fr.slypy.slymyjge.graphics.NewDisplayMode;
import fr.slypy.slymyjge.inputs.KeyboardInputs;
import fr.slypy.slymyjge.network.AuthentifiedPacket;
import fr.slypy.slymyjge.network.NetworkRegister;
import fr.slypy.slymyjge.network.Packet;
import fr.slypy.slymyjge.utils.Logger;

public abstract class Game extends KeyboardInputs {
	
	protected boolean changeDisplaymode = false;
	
	protected NewDisplayMode newDisplay;
	
	protected boolean changeIcon = false;
	
	protected ByteBuffer[] icons;
	
	protected boolean resizable;
	
	protected boolean showFPS = false;
	
	protected  long showedFPS = 0;

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

	protected boolean showTPS = false;
	
	protected long showedTPS = 0;

	protected static Map<String, Component> components = new HashMap<String, Component>();
	
	protected static long wait = 0;
	
	protected static Client client;
	
	protected static long frameCap = Integer.MAX_VALUE;
	protected static long tickCap = Integer.MAX_VALUE;
	
	protected static boolean closeRequested = false;
	
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
		
		Game game = this;
			
		client = new Client();
		client.start();
				
		Kryo kryo = client.getKryo();
				
		for(Class<?> packetClass : networkRegister.getClasses()) {
					
			kryo.register(packetClass);
					
		}
				
		client.addListener(new Listener() {
					
			public void connected(Connection c) {
							
				game.connected(c);
						
			}
					
			public void disconnected(Connection c) {
						
				game.disconnected(c);
						
			}
					
			public void received(Connection c, Object o) {
					
				if(o instanceof AuthentifiedPacket) {
							
					AuthentifiedPacket p = (AuthentifiedPacket) o;
							
					if(p.authendtified) {
								
						game.authentified(c);
								
					} else {
								
						c.close();
								
					}
							
					return;
							
				}
						
				if(o instanceof Packet) {
							
					Packet packet = (Packet) o;
							
					game.packetReceived(c, packet, System.nanoTime() - packet.timestamp);
							
				}
						
			}
					
		});
		
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
	
	public void connected(Connection c) {}
	public void disconnected(Connection c) {}
	public void packetReceived(Connection c, Packet p, long ping) {}
	public void authentified(Connection c) {}
	
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
		

		
	}
	
	public void start() {
		
		display();
		loop();
		
	}
	
	public void exit() {
		
		Logger.log("Arret du syst�me");
		
		closeRequested = true;
		
		System.exit(0);
		
	}
	
	public void loop() {
		
		
		Logger.log("Initialisation du jeu");
		
		escapeGameKey = getEscapeGameKey();
		
		init();
		
		Logger.log("D�marage de la boucle principale du jeu");
		
		Game game = this;
		
		Thread ut = new Thread() {
			
			public void run() {
				
				long lastTpsUpdate = System.nanoTime();
				
				long tps = 0;
				
				long before = System.nanoTime();
				
				double alpha = (double) (System.nanoTime() - before) / 1000000000D;
				
				double gamma = 1D / (double) tickCap;
				
				while(true) {
					
					alpha = (double) (System.nanoTime() - before) / 1000000000D;
					
					if(alpha > gamma) {

						gamma = 1D / (double) tickCap;
						
						alpha = (double) (System.nanoTime() - before) / 1000000000D;
						
						before = System.nanoTime();
						
						if(game.isCloseRequested()) {
							
							Logger.log("Arret du jeu");
							game.stop();
							game.exit();
							
						}
						
						tps++;
						
						keyUpdate();
						
						for(Component comp : components.values()) {
							
							comp.update(getXCursor(), getYCursor(), game);
							
						}
						
						update(alpha);
						
						if (System.nanoTime() - lastTpsUpdate > 1000000000L) {
							
							showedTPS = (int) tps;
							
							lastTpsUpdate = System.nanoTime();
							
							tps = 0;
	
						}
					
					}
					
				}
				
			}
			
		};
		
		ut.start();
				
		long fps = 0;
				
		long lastFpsUpdate = System.nanoTime();
				
		long before = System.nanoTime();
				
		double alpha = (double) (System.nanoTime() - before) / 1000000000D;
		double gamma = 1D / (double) frameCap;
		
		while(true) {
			
			alpha = (double) (System.nanoTime() - before) / 1000000000D;
			
			if(alpha > gamma) {

				if(changeDisplaymode) {
					
					changeDisplayMode(newDisplay);
					changeDisplaymode = false;
					
				}
				
				if(changeIcon) {
					
					changeIcon(icons);
					changeIcon = false;
					
				}
				
				gamma = 1D / (double) frameCap;
				
				alpha = (double) (System.nanoTime() - before) / 1000000000D;
				
				before = System.nanoTime();
			
				if (Display.isCloseRequested()) {
					
					Logger.log("Arret du jeu");
					game.stop();
					game.exit();
					
				}
				
				if(closeRequested) {
					
					Display.destroy();
					
				}
				
				fps++;
						
				Display.update();
						
				updateSize();
						
				view2D(width, height);
						
				translateView(xCam, yCam);
						
				float wDiff = (float) width / (float) lastWidth;
				float hDiff = (float) height / (float) lastHeight;
						
				if(wDiff != 1 || hDiff != 1) {
							

							
				}
						
				glClear(GL_COLOR_BUFFER_BIT);
				glClearColor((float) backgroundColor.getRed() / 255F, (float) backgroundColor.getGreen() / 255F, (float) backgroundColor.getBlue() / 255F, 1);
				
				render(alpha);
				
				for(Component comp : components.values()) {
					
					comp.render();
					
				}
						
				if (System.nanoTime() - lastFpsUpdate > 1000000000L) {
							
					showedFPS = fps;
							
					lastFpsUpdate = System.nanoTime();
					
					fps = 0;
	
				}
				
				Display.setTitle(title + (showFPS ? " || FPS: " + showedFPS : "") + (showTPS ? " || TPS: " + showedTPS : ""));
			
			}
					
		}
		
	}
	
	public void delay(long millis) {
		
		wait = millis;
		
	}
	
	public long getFps() {
		
		return showedFPS;
		
	}
	
	public long getTps() {
		
		return showedTPS;
		
	}
	
	public void setBackgroundColor(Color color) {
		
		this.backgroundColor = color;
		
	}
	
	public Color getBackgroundColor() {
		
		return this.backgroundColor;
		
	}
	
	public SlymyFont getFont(String uuid) {
		
		return null;
		
	}
	
	public void setMouseGrabbed(boolean grabbed) {
		
		Mouse.setGrabbed(grabbed);
		
	}
	
	private void changeIcon(ByteBuffer[] icons) {
		
		Display.setIcon(icons);
		
	}
	
	public void setIcon(Icon icon) {

		if(icon.icons() <= 0) {
			
			return;
			
		}
		
		ByteBuffer[] icons = new ByteBuffer[icon.icons()];
		
		int iconsStoraged = 0;
		
		if(icon.hasIcon(IconResolution.X16)) {
			
			icons[iconsStoraged] = icon.getIcon(IconResolution.X16);
			iconsStoraged++;
			
		}
		
		if(icon.hasIcon(IconResolution.X32)) {
			
			icons[iconsStoraged] = icon.getIcon(IconResolution.X32);
			iconsStoraged++;
			
		}
		
		if(icon.hasIcon(IconResolution.X128)) {
			
			icons[iconsStoraged] = icon.getIcon(IconResolution.X128);
			iconsStoraged++;
			
		}
		
		changeIcon = true;
		
		this.icons = icons;
		
	}
	
	private void changeDisplayMode(NewDisplayMode newDisplaymode) {
		
	    try {
			
	    	if(newDisplaymode.isFullscreen()) {
	    	
	    		Display.setDisplayModeAndFullscreen(Display.getDesktopDisplayMode());
	    	
	    	} else {

	    		Display.setDisplayMode(new DisplayMode(newDisplaymode.getW(), newDisplaymode.getH()));
	    		
	    	}
	    	
	        Display.setResizable(!newDisplaymode.isResizable());
	        Display.setResizable(newDisplaymode.isResizable());
	        Display.setTitle(title);
	        
	    } catch (LWJGLException e) {

			e.printStackTrace();
		}
		
	}
	
	public void setFullscreen(boolean fullscreen) {
		
		if(isFullscreen() == fullscreen) {
			
			return;
			
		}
		
		newDisplay = new NewDisplayMode(fullscreen, resizable, width, height);
		
		changeDisplaymode = true;
		
	}
	
	public boolean isFullscreen() {
		
		return Display.isFullscreen();
		
	}
	
	public void setResizable(boolean resizable) {
		 
		if(isResizable() == resizable) {
			
			return;
			
		}

		boolean fullscreen = Display.isFullscreen();
		
		newDisplay = new NewDisplayMode(fullscreen, resizable, width, height);
		
		changeDisplaymode = true;
	    
	}
	
	public boolean isResizable() {
		
		return Display.isResizable();
		
	}
	
	public void setSize(int width, int height) {
		
		boolean fullscreen = Display.isFullscreen();

		if((getWidth() == width && getHeight() == height) || fullscreen) {
			
			return;
			
		}
		
		newDisplay = new NewDisplayMode(false, resizable, width, height);
		
		changeDisplaymode = true;
		
	}
	
	public int getWidth() {
		
		return width;
		
	}
	
	public int getHeight() {
		
		return height;
		
	}

	private void updateSize() {
		
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
	
	public abstract void render(double alpha);
	
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
	
	private void translateView(float xa, float ya) {
		
		glTranslatef(xa * getWidthDiff(), ya * getHeightDiff(), 0);
		
	}
	
	public void translateCam(float xa, float ya) {
		
		setXCam(xCam + (xa * getWidthDiff()));
		setYCam(yCam + (ya * getHeightDiff()));
		
	}
	
	public void addComponent(String uuid, Component comp) {
		
		if(components.containsKey(uuid)) {
			
			System.err.println("Component with uuid " + uuid + " is already register ! Overriding it...");
			components.remove(uuid);
			
		}
		
		components.put(uuid, comp);
		
	}
	
	public void addComponents(Map<String, Component> comps) {
		
		for(String uuid : comps.keySet()) {
			
			addComponent(uuid, comps.get(uuid));
			
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
	
	public void setShowTPS(boolean showTPS) {
		
		this.showTPS = showTPS;
		
	}
	
	public boolean showTPS() {
		
		return showTPS;
		
	}
	
	public Component getComponent(String uuid) {
		
		for(String key : components.keySet()) {
			
			if(key.equalsIgnoreCase(uuid)) {
				
				return components.get(key);
				
			}
			
		}
		
		return null;
		
	}
	
	public void removeComponent(String uuid) {
		
		for(String key : components.keySet()) {
			
			if(key.equalsIgnoreCase(uuid)) {
				
				components.remove(key);
				
			}
			
		}
		
	}
	
	public Map<String, Component> getComponents() {
		
		return components;
		
	}
	
	public Component componentHover() {

		Component component = null;
		
		for(Component comp : components.values()) {
			
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
	
	public void setFrameCap(long cap) {
		
		frameCap = cap;
		
	}
	
	public long getFrameCap() {
		
		return frameCap;
		
	}
	
	public void setTickCap(long cap) {
		
		tickCap = cap;
		
	}
	
	public long getTickCap() {
		
		return tickCap;
		
	}
	
}