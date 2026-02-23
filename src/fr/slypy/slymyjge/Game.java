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
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
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

	protected int width;
	protected int height;

	protected boolean showTPS = false;
	protected long tps = 0;
	
	protected Map<Integer, GameState> states = new ConcurrentHashMap<>();
	
	protected int state;
	
	protected long wait = 0;
	
	protected Synchronizer frameSync = new Synchronizer(Integer.MAX_VALUE);
	protected Synchronizer tickSync = new Synchronizer(Integer.MAX_VALUE);
	
	protected boolean closeRequested = false;
	
	protected Cursor defaultCursor = Mouse.getNativeCursor();
	
	protected Thread ut;
	
	protected ConcurrentLinkedQueue<Runnable> toExecute = new ConcurrentLinkedQueue<>();
	
	protected boolean steamLinked = false;
	
	private long renderThreadId;
	
	private boolean resizable;
	
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
		
		this.width = width;
		this.height = height;
		
		this.title = title;
		
		this.backgroundColor = backgroundColor;
		
		resizingRules = ResizingRules.DefaultRules;
		
		this.resizable = resizable;
		
		states.put(0, this);
		
		state = 0;
		
	}
	
	public static void setBorderless() {
		
		System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
		
	}
	
	public abstract void stop();
	
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
			
			s.init(InitEvent.registerEvent);
			
		}
		
		return states.size() - 1;
		
	}
	
	public void setState(int id) {
		
		if(states.containsKey(id)) {
			
			executeInRenderThread(() -> {
				
				ExitEvent event = new ExitEvent(ExitType.SWITCHING_STATE, id);
				
				states.get(state).exit(event);
				
				int prevState = state;
				state = id;
				
				GameState newState = states.get(state);
				
				if(newState.getInitType() == InitType.INIT_ON_LOAD || newState.getInitType() == InitType.INIT_ON_LOAD_AND_ON_REGISTER) {
					
					InitEvent e = new InitEvent(InitType.INIT_ON_LOAD, event.getResources(), prevState);
					
					newState.init(e);
					
				}
				
				newState.isInitialised = true;
				
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
		
		init(InitEvent.manualEvent);
		
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
					
					GameState s = states.get(state);
						
					if(s.isInitialised) {
						
						s.updateInputs();
							
						s.componentsUpdate();
							
						s.update(tickSync.getDelta());
						
					}
						
					if (tpsUpdateScheduler.isReady()) {
							
						Game.this.tps = tps;
							
						tps = 0;
	
					}
						
					if(game.isCloseRequested()) {
							
						Logger.log("Arret du jeu (keyboard)");
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
				
				game.exit();
					
			}
				
			if(closeRequested) {
				
				try {

					ut.join();

				} catch (InterruptedException e) {

					e.printStackTrace();

				}

				for(GameState s : states.values()) {
					
					s.exit(ExitEvent.stoppingEvent);
					
				}
				
				game.stop();

				Display.destroy();

				return;
				
			}
			
			GameState s = states.get(state);
				
			fps++;
						
			updateView2D();
						
			translateView(s.getXCam(), s.getYCam());
						
			glClearColor(backgroundColor.getRed() / 255F, backgroundColor.getGreen() / 255F, backgroundColor.getBlue() / 255F, 1);
			glClear(GL_COLOR_BUFFER_BIT);
			
			while(!toExecute.isEmpty()) {
				
				toExecute.poll().run();
					
			}
			
			s.render(frameSync.getDelta());
				
			//TODO remove s.componentsRender();
						
			if (fpsUpdateScheduler.isReady()) {
							
				this.fps = fps;
					
				fps = 0;
	
			}
				
			Display.setTitle(title + (showFPS ? " || FPS: " + this.fps : "") + (showTPS ? " || TPS: " + this.tps : ""));
			
			Display.update();
					
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
			
		}
		
		executeInRenderThread(() -> changeIcon(icons));
		
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
		
		Display.setResizable(newDisplaymode.isResizable());
		resizable = newDisplaymode.isResizable();
		
		Display.setTitle(title);
		
		updateView2D();
		
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
		
		executeInRenderThread(() -> {
			
			try {
				
				changeDisplayMode(new NewDisplayMode(Display.isFullscreen(), resizable, width, height));
				
			} catch (LWJGLException e) {

				e.printStackTrace();
				
			}
			
		});
	    
	}
	
	public boolean isResizable() {
		
		return Display.isResizable();
		
	}
	
	public void setVirtualSize(int width, int height) {

		GLU.gluOrtho2D(0, width, height, 0);
		
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
		
		return width;
		
	}
	
	public int getHeight() {
		
		return height;
		
	}
	
	public int getRealWidth() {
		
		return Display.getWidth();
		
	}
	
	public int getRealHeight() {
		
		return Display.getHeight();
		
	}

	public void display() {
		
		Logger.log("Cr�ation de la fen�tre");
		
		try {
			
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setResizable(resizable);
			Display.setTitle(title);
			Display.create(new PixelFormat(32, 0, 24, 0, 8));
			
			updateView2D();
			
		} catch (LWJGLException e) {
			
			e.printStackTrace();
			
		}
		
	}
	
	public static void resetModelViewMatrix() {
		
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
	}
	
	public void updateView2D() {
		
		int x = 0;
		int y = 0;
		int w = Display.getWidth();
		int h = Display.getHeight();
		
		if(!resizingRules.resizeWidth()) {
			
			x = (w - width) / 2;
			x = x < 0 ? 0 : x;
			
			w = width;
			
		}
		
		if(!resizingRules.resizeHeight()) {
			
			y = (h - height) / 2;
			y = y < 0 ? 0 : y;
			
			h = height;
			
		} else if(resizingRules.fixedAspectRatio()) {
			
			float coef = w / (float) width;
			coef = Math.min(h / (float) height, coef);
			
			x = (w - (int) (width*coef)) / 2;
			x = x < 0 ? 0 : x;
			
			w = (int) (width*coef);
			
			y = (h - (int) (height*coef)) / 2;
			y = y < 0 ? 0 : y;
			
			h = (int) (height*coef);
			
		}
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		
		setVirtualSize(width, height);
		
		setView2D(x, y, w, h);
		
	}
	
	public static void setView2D(int x, int y, int width, int height) {

		glViewport(x, y, width, height);
		
		resetModelViewMatrix();
		
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
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
	
	public void translateView(float xa, float ya) {
	
		glTranslatef(-xa, -ya, 0);
		
	}
	
	public void rotateView(float angle, float x, float y) {
		
		glTranslatef(x, y, 0);
		glRotatef(-angle, 0, 0, 1);
		glTranslatef(-x, -y, 0);
		
	}
	
	public void scaleView(float dx, float dy, float x, float y) {
		
		glTranslatef(x, y, 0);
		glScalef(dx, dy, 1);
		glTranslatef(-x, -y, 0);
		
	}
	
	public float getRelativeXCursor() {
		
		return (Mouse.getX() / (float) getRealWidth()) * getWidth() + xCam;
		
	}
	
	public float getRelativeYCursor() {
		
		return ((-Mouse.getY() + Display.getHeight()) / (float) getRealHeight()) * getHeight() + yCam;
		
	}
	
	public float getAbsoluteXCursor() {
		
		return (Mouse.getX() / (float) getRealWidth()) * getWidth();
		
	}
	
	public float getAbsoluteYCursor() {
		
		return ((-Mouse.getY() + Display.getHeight()) / (float) getRealHeight()) * getHeight();
		
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