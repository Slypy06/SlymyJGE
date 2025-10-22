package fr.slypy.slymyjge;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glViewport;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JOptionPane;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;

import com.codedisaster.steamworks.SteamAPI;
import com.codedisaster.steamworks.SteamException;

import fr.slypy.slymyjge.graphics.Icon;
import fr.slypy.slymyjge.graphics.IconResolution;
import fr.slypy.slymyjge.graphics.NewDisplayMode;
import fr.slypy.slymyjge.graphics.Renderer;
import fr.slypy.slymyjge.graphics.Texture;
import fr.slypy.slymyjge.utils.Logger;
import fr.slypy.slymyjge.utils.RepeatedScheduler;
import fr.slypy.slymyjge.utils.ResizingRules;
import fr.slypy.slymyjge.utils.Synchronizer;

public abstract class Game extends GameState {
	
	protected boolean showFPS = false;
	protected long fps = 0;

	protected ResizingRules resizingRules;
	
	protected String title;

	protected int startWidth;
	protected int startHeight;
	
	protected int lastWidth;
	protected int lastHeight;
	
	protected int width;
	protected int height;
	
	protected float widthDiff = 1;
	protected float heightDiff = 1;
	
	protected int xDiff;
	protected int yDiff;

	protected boolean showTPS = false;
	protected long tps = 0;
	
	protected Map<Integer, GameState> states = new ConcurrentHashMap<>();
	
	protected GameState state;
	
	protected long wait = 0;
	
	//protected Client client;
	
	protected Synchronizer frameSync = new Synchronizer(Integer.MAX_VALUE);
	protected Synchronizer tickSync = new Synchronizer(Integer.MAX_VALUE);
	
	protected boolean closeRequested = false;
	
	protected Cursor defaultCursor = Mouse.getNativeCursor();
	
	protected Thread ut;
	
	protected ConcurrentLinkedQueue<Runnable> toExecute = new ConcurrentLinkedQueue<>();
	
	protected boolean steamLinked = false;
	
	private long renderThreadId;
	
	//TODO Modify the resizingRule
	
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
		
		if(resizable) {
			
			resizingRules = ResizingRules.DefaultRules;
			
		} else {
			
			resizingRules = ResizingRules.NotResizable;
			
		}
		
		states.put(0, this);
		
		state = this;
		
	}
	
	public static void setBorderless() {
		
		System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
		
	}
	
	public abstract void stop();
	
	public void setResizingRules(ResizingRules rules) {
		
		this.resizingRules = rules;
		
		if(resizingRules == null)
			return;
		
		if(!resizingRules.isFixedAspectRatio()) {
			
			xDiff = 0;
			yDiff = 0;
			
		}
		
		if(!resizingRules.isResizeHeight()) {
			
			heightDiff = 1;
			
		}
		
		if(!resizingRules.isResizeWidth()) {
			
			widthDiff = 1;
			
		}
		
	}
	
	public InitType getInitType() {
		
		return InitType.MANUALY_INIT;
		
	}
	
	public Game getGame() { //Useless looking function here but mandatory since Game extends from GameState (if not present, the user will need to implement it which is even sillier)
		
		return this;  //THIS FUNCTION MAY SOUND SILLY BUT DO NOT REMOVE, THIS FUNCTION IS MANDATORY
		
	}
	
	public void initSteam(boolean optional) {
		
		try {
			
		    SteamAPI.loadLibraries();
		    
		    if (!SteamAPI.init() && !optional) {
		        
		    	JOptionPane.showMessageDialog(null, "Impossible to initialize steamworks API. Make sure Steam is running.\n", "Steamworks Error", JOptionPane.ERROR_MESSAGE);
		    	System.exit(0);
		    	
		    }
		    
		} catch (SteamException e) {
		    
			e.printStackTrace();
			System.exit(0);
			
		}
		
	}
	
	public int registerState(GameState s) {
		
		states.put(states.size(), s);
		
		if(s.getInitType() == InitType.INIT_ON_REGISTER || s.getInitType() == InitType.INIT_ON_LOAD_AND_ON_REGISTER) {
			
			s.init();
			
		}
		
		return states.size() - 1;
		
	}
	
	public void setState(int id) {
		
		if(states.containsKey(id)) {
			
			executeInRenderThread(() -> {
				
				state.isInitialised = false;
				
				state = states.get(id);
				
				if(state.getInitType() == InitType.INIT_ON_LOAD || state.getInitType() == InitType.INIT_ON_LOAD_AND_ON_REGISTER) {
					
					state.init();
					
				}
				
				state.isInitialised = true;
				
			});
			
		}
		
	}
	
	/*public void setupClientForMultiplayer(NetworkRegister networkRegister) {
		
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
	
	public void connectToServer(String adress, int tcpPort, int udpPort) throws IOException {
				
		client.connect(5000, adress, tcpPort, udpPort);
		
	}
	
	public void connectToServer(String adress, int tcpPort) throws IOException {
		
		client.connect(5000, adress, tcpPort);
		
	}
	
	public Client getClient() {
		
		return client;
		
	}
	
	public void connected(Connection c) {}
	public void disconnected(Connection c) {}
	public void packetReceived(Connection c, Packet p, long ping) {}
	public void authentified(Connection c) {}*/
	
	public void setEscapeGameKey(int escapeGameKey) {
		
		this.escapeGameKey = escapeGameKey;
		
	}
	
	public int getEscapeGameKey() {
		
		return escapeGameKey;
		
	}
	
	public void start() {
		
		display();
		loop();
		
	}
	
	public void exit() {
		
		Logger.log("Arret du syst�me");
		closeRequested = true;
		
	}
	
	public void loop() {
		
		Logger.log("Initialisation du jeu");
		
		Renderer.init(this);
		
		init();
		
		Logger.log("D�marage de la boucle principale du jeu");
		
		tickSync.start();
		frameSync.start();
		
		renderThreadId = Thread.currentThread().getId();
		
		Game game = this;
		
		isInitialised = true;
		
		ut = new Thread() {
			
			public void run() {
				
				RepeatedScheduler tpsUpdateScheduler = new RepeatedScheduler(1);
				long tps = 0;

				while(!closeRequested && !Thread.interrupted()) {

					if(steamLinked && SteamAPI.isSteamRunning()) {
							
						SteamAPI.runCallbacks();
							
					}
						
					tps++;
						
					if(state.isInitialised) {
						
						state.updateInputs();
							
						state.componentsUpdate();
							
						state.update(tickSync.getDelta());
						
					}
						
					if (tpsUpdateScheduler.isReady()) {
							
						Game.this.tps = tps;
							
						tps = 0;
	
					}
						
					if(game.isCloseRequested()) {
							
						Logger.log("Arret du jeu (keyboard)");
						game.stop();
						game.exit(); //this will update closeRequested and exit the while
							
					}
					
					tickSync.sync();
					
				}
				
			}
			
		};
		
		ut.start();
				
		RepeatedScheduler fpsUpdateScheduler = new RepeatedScheduler(1);
		long fps = 0;
		
		while(!Thread.interrupted()) {
			
			frameSync.sync();
			
			if (Display.isCloseRequested()) {
					
				Logger.log("Arret du jeu (display)");
				game.stop();
				game.exit();
					
			}
				
			if(closeRequested) {
					
				Display.destroy();
				System.exit(0);
					
			}
				
			fps++;
						
			Display.update();
			
			while(!toExecute.isEmpty()) {
				
				toExecute.poll().run();
					
			}
						
			updateSize();
						
			resetModelViewMatrix();
						
			translateView(state.getXCam(), state.getYCam());
						
			glClearColor((float) backgroundColor.getRed() / 255F, (float) backgroundColor.getGreen() / 255F, (float) backgroundColor.getBlue() / 255F, 1);
			glClear(GL_COLOR_BUFFER_BIT);
			
			state.render(frameSync.getDelta());
				
			state.componentsRender();
						
			if (fpsUpdateScheduler.isReady()) {
							
				this.fps = fps;
					
				fps = 0;
	
			}
				
			Display.setTitle(title + (showFPS ? " || FPS: " + this.fps : "") + (showTPS ? " || TPS: " + this.tps : ""));
					
		}
		
		Display.destroy();
		
	}
	
	public void delay(long millis) {
		
		wait = millis;
		
	}
	
	public long getFps() {
		
		return fps;
		
	}
	
	public long getTps() {
		
		return tps;
		
	}
	
	public void setBackgroundColor(Color color) {
		
		this.backgroundColor = color;
		
	}
	
	public Color getBackgroundColor() {
		
		return this.backgroundColor;
		
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
		
		int iconsStored = 0;
		
		if(icon.hasIcon(IconResolution.X16)) {
			
			icons[iconsStored] = icon.getIcon(IconResolution.X16);
			iconsStored++;
			
		}
		
		if(icon.hasIcon(IconResolution.X32)) {
			
			icons[iconsStored] = icon.getIcon(IconResolution.X32);
			iconsStored++;
			
		}
		
		if(icon.hasIcon(IconResolution.X128)) {
			
			icons[iconsStored] = icon.getIcon(IconResolution.X128);
			iconsStored++;
			
		}
		
		executeInRenderThread(() -> {
			
			changeIcon(icons);
			
		});
		
	}
	
	private void changeCursor(Cursor c) {
		
		try {
			
			Mouse.setNativeCursor(c);
			
		} catch (LWJGLException e) {

			e.printStackTrace();
			
		}
		
	}
	
	public void setCursor(int w, int h, int pointerX, int pointerY, Texture c) {
		
		executeInRenderThread(() -> {
			
			try {
				
				changeCursor(new Cursor(w, h, pointerX, pointerY, 1, c.getBuffer().asIntBuffer(), null));
				
			} catch (LWJGLException e) {

				e.printStackTrace();
				
			}
			
		});

	}
	
	private void changeDisplayMode(NewDisplayMode newDisplaymode) throws LWJGLException {

		if(newDisplaymode.isFullscreen()) {
	    	
			Display.setDisplayModeAndFullscreen(Display.getDesktopDisplayMode());
	    	
	    } else {

	    	Display.setDisplayMode(new DisplayMode(newDisplaymode.getW(), newDisplaymode.getH()));
	    		
	    }
	    	
		Display.setResizable(!newDisplaymode.isResizable());
		Display.setResizable(newDisplaymode.isResizable());
		Display.setTitle(title);
		
		updateSize();
		
		setView2D(width, height);
		
	}
	
	public void setFullscreen(boolean fullscreen) {
		
		if(isFullscreen() == fullscreen) {
			
			return;
			
		}
		
		executeInRenderThread(() -> {
			
			try {
				
				changeDisplayMode(new NewDisplayMode(fullscreen, resizingRules != null && !fullscreen, width, height));
				
			} catch (LWJGLException e) {

				e.printStackTrace();
				
			}
			
		});
		
	}
	
	public boolean isFullscreen() {
		
		return Display.isFullscreen();
		
	}
	
	public void setResizable(boolean resizable) {
		 
		if(isResizable() == resizable) {
			
			return;
			
		}

		boolean fullscreen = Display.isFullscreen();
		
		executeInRenderThread(() -> {
			
			try {
				
				changeDisplayMode(new NewDisplayMode(fullscreen, resizable, width, height));
				
			} catch (LWJGLException e) {

				e.printStackTrace();
				
			}
			
		});
	    
	}
	
	public boolean isResizable() {
		
		return Display.isResizable();
		
	}
	
	public void setSize(int width, int height) {
		
		boolean fullscreen = Display.isFullscreen();

		if((getWidth() == width && getHeight() == height) || fullscreen) {
			
			return;
			
		}
		
		executeInRenderThread(() -> {
			
			try {
				
				changeDisplayMode(new NewDisplayMode(false, resizingRules != null, width, height));
				
			} catch (LWJGLException e) {

				e.printStackTrace();
				
			}
			
		});
		
	}
	
	public int getWidth() {
		
		return startWidth;
		
	}
	
	public int getHeight() {
		
		return startHeight;
		
	}
	
	public int getCurrentWidth() {
		
		return width;
		
	}
	
	public int getCurrentHeight() {
		
		return height;
		
	}

	private void updateSize() {
		
		lastWidth = width;
		lastHeight = height;
		
		width = Display.getWidth();
		height = Display.getHeight();
		
		if(resizingRules != null && resizingRules.isResizeWidth()) {
		
			widthDiff = width / (float) startWidth;
		
		} else {
			
			widthDiff = 1;
			
		}
		
		if(resizingRules != null && resizingRules.isResizeHeight()) {
		
			heightDiff = height / (float) startHeight;
		
		} else {
			
			heightDiff = 1;
		
		}

		
		if(resizingRules != null && resizingRules.isFixedAspectRatio()) {
			
			if(widthDiff < heightDiff) {
				
				heightDiff = widthDiff;
				xDiff = 0;
				yDiff = (int) ((height - startHeight*heightDiff) / 2.0d);
				
			} else if(widthDiff > heightDiff) {
				
				widthDiff = heightDiff;
				yDiff = 0;
				xDiff = (int) ((width - startWidth*widthDiff) / 2.0d);
				
			}
			
		}
		
	}
	
	public void display() {
		
		Logger.log("Cr�ation de la fen�tre");
		
		try {
			
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setResizable(resizingRules != null);
			Display.setTitle(title);
			Display.create(new PixelFormat(32, 0, 24, 0, 8));
			
			updateSize();
		 
			setView2D(width, height);
			
		} catch (LWJGLException e) {
			
			e.printStackTrace();
			
		}
		
	}
	
	public static void resetModelViewMatrix() {
		
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
	}
	
	public static void setView2D(int width, int height) {
		
		setViewport(width, height);
		
		resetModelViewMatrix();
		
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
	}
	
	public static void setViewport(int width, int height) {
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		
		glViewport(0, 0, width, height);
		GLU.gluOrtho2D(0, width, height, 0);
		
	}
	
	public static void cleanseMatrixForFbo() {
		
		glMatrixMode(GL_MODELVIEW);
		glPushMatrix();
		glLoadIdentity();
		
	}
	
	public static void restoreMatrix() {
		
		glMatrixMode(GL_MODELVIEW);
		glPopMatrix();
		
	}
	
	private void translateView(float xa, float ya) {
	
		glTranslatef(-xa * getWidthDiff(), -ya * getHeightDiff(), 0);
		
	}
	
	public float getXCursor() {
		
		return (Mouse.getX() / getWidthDiff()) + xCam;
		
	}
	
	public float getYCursor() {
		
		return ((-Mouse.getY() + Display.getHeight()) / getHeightDiff()) + yCam;
		
	}
	
	public float getXCursorOnScreen() {
		
		return (Mouse.getX() / getWidthDiff());
		
	}
	
	public float getYCursorOnScreen() {
		
		return ((-Mouse.getY() + Display.getHeight()) / getHeightDiff());
		
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
	
	public float getWidthDiff() {
		
		return widthDiff;
		
	}

	public float getHeightDiff() {
		
		return heightDiff;
		
	}
	
	public int getxDiff() {
		
		return xDiff;
		
	}

	public int getyDiff() {
		
		return yDiff;
		
	}

	public void setFrameCap(int cap) {
		
		frameSync.setRate(cap);
		
	}
	
	public long getFrameCap() {
		
		return (long) frameSync.getRate();
		
	}
	
	public void setTickCap(int cap) {
		
		tickSync.setRate(cap);
		
	}
	
	public long getTickCap() {
		
		return (long) tickSync.getRate();
		
	}
	
	public void executeInRenderThread(Runnable run) {
		
		if(Thread.currentThread().getId() == renderThreadId) {
			
			run.run();
			
		} else {
		
			toExecute.add(run);
		
		}
		
	}
	
	public void executeInRenderThreadNextFrame(Runnable run) {
		
		toExecute.add(run);
		
	}
	
}