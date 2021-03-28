import java.awt.Color;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import fr.slypy.slymyjge.Game;
import fr.slypy.slymyjge.animations.SimpleAnimation;
import fr.slypy.slymyjge.audio.AudioMaster;
import fr.slypy.slymyjge.audio.AudioSource;
import fr.slypy.slymyjge.components.Button;
import fr.slypy.slymyjge.components.Component;
import fr.slypy.slymyjge.components.MoveableComponent;
import fr.slypy.slymyjge.graphics.HUDRenderer;
import fr.slypy.slymyjge.graphics.Renderer;
import fr.slypy.slymyjge.graphics.Texture;
import fr.slypy.slymyjge.utils.MouseButtons;

public class SlyDefence extends Game {

	protected static SlyDefence game;
	
	private Texture playButtonTexture;
	private Texture cursorTexture;
	private Texture classicCursorTexture;
	private Texture moveCursorTexture;
	private Texture clickCursorTexture;
	private Texture playerTexture;
	
	private SimpleAnimation playerAnim;
	
	private int playerDirection;
	
	boolean mouseClick = false;
	
	float originXCam = 0;
	float originYCam = 0;
	
	float xCursor = 0;
	float yCursor = 0;
	
	AudioSource spartaSource;
	
	Button playButton;
	
	MoveableComponent playerComp;

	public SlyDefence(int width, int height, String title, Color color) {
		
		super(width, height, title, color);
		
	}

	public static void main(String[] args) {
		
		game = new SlyDefence(1280, 720, "Sly Defence", new Color(190, 220, 255));
		game.addKeysToListen(new int[] {Keyboard.KEY_K, Keyboard.KEY_C, Keyboard.KEY_D});
		game.addMouseButtonsToListen(new int[] {MouseButtons.LEFT_BUTTON});
		game.setEscapeGameKey(Keyboard.KEY_ESCAPE);
		game.start();
		
	}
	
	@Override
	public void init() {
		
		Renderer.init(game);
		
		playButtonTexture = Texture.loadTexture("hud/play_button.png");
		classicCursorTexture = Texture.loadTexture("hud/mouse.png");
		moveCursorTexture = Texture.loadTexture("hud/move_mouse.png");
		clickCursorTexture = Texture.loadTexture("hud/click_mouse.png");
		cursorTexture = classicCursorTexture;
		playerTexture = Texture.loadTexture("sprites/player/player.png");
		
		playerAnim = new SimpleAnimation(playerTexture, 9);
		playerAnim.setSpeed(0.15F);
		
		AudioMaster.init();	
		AudioMaster.setListener(0, 0, 0);
		
		spartaSource = new AudioSource(AudioMaster.loadSound("audio/sparta.wav"));
		spartaSource.setVolume(0.1F);
		
		playerComp = new MoveableComponent(700, 100, 300, 300, game) {

			@Override
			public void render() {
				
				if(this.isHover()) {
					
					Renderer.renderSimpleAnimation(this.getX(), this.getY(), this.getW(), this.getH(), playerAnim, playerDirection, new Color(190, 190, 190));
					
				} else {
					
					Renderer.renderSimpleAnimation(this.getX(), this.getY(), this.getW(), this.getH(), playerAnim, playerDirection);
					
				}
				
			}

		};
		
		game.addComponent(playerComp);
		
		playButton = new Button(100, 500, 100, 100, game) {

			@Override
			public void componentActivated() {
				
				System.out.println("Coucou");
				
			}

			@Override
			public void render() {
				
				if(this.isHover()) {
					
					Renderer.renderTexturedQuad(this.getX(), this.getY(), this.getW(), this.getH(), playButtonTexture, new Color(190, 190, 190));
					
				} else {
					
					Renderer.renderTexturedQuad(this.getX(), this.getY(), this.getW(), this.getH(), playButtonTexture);
					
				}
				
			}
			
		};
		
		game.addComponent(playButton);
		
		Mouse.setGrabbed(true);
		
	}
	
	@Override
	public void update(double alpha) {
		
		System.out.println(xCursor + "    " + yCursor);
		
		if(Mouse.isGrabbed()) {
			
			xCursor = getXCursorOnScreen();
			yCursor = getYCursorOnScreen();
			
		}
		
		updateMousePosition();
		
		if(game.buttonHover() != null) {
			
			cursorTexture = clickCursorTexture;
			
		} else if(cursorTexture == clickCursorTexture) {
			
			cursorTexture = classicCursorTexture;
			
		}

	}

	@Override
	public void render() {
		
		Renderer.renderTexturedQuad(200, 150, 100, 100, clickCursorTexture, Color.white);
		Renderer.renderQuad(800, 500, 100, 150, Color.MAGENTA);
		
		for(Component comp : game.getComponents()) {
			
			if(comp instanceof MoveableComponent) {
				
				if(((MoveableComponent) comp).isMove) {
					
					continue;
					
				} else {
					
					comp.render();
					
				}
				
			} else {
				
				comp.render();
				
			}
			
		}
		
		for(Component comp : game.getComponents()) {
			
			if(comp instanceof MoveableComponent) {
				
				if(((MoveableComponent) comp).isMove) {
					
					comp.render();
					
				}
				
			}
			
		}
		
		if(Mouse.isGrabbed() && Display.isActive()) {
			
			HUDRenderer.renderTexturedQuad(xCursor, yCursor, cursorTexture == classicCursorTexture ? 14 : cursorTexture == clickCursorTexture || cursorTexture == clickCursorTexture ? 17 : 20, 20, cursorTexture, Color.white);
			
		}
		
	}
	
	@Override
	public void keyPressed(int key) {

		if(key == Keyboard.KEY_K) {
			
			spartaSource.play();
			
		} else if(key == Keyboard.KEY_C) {
			
			playerAnim.setAnime(!playerAnim.isAnime());
			
		} else if(key == Keyboard.KEY_D) {
			
			playerDirection++;
			
			if(playerDirection == 4) {
				
				playerDirection = 0;
				
			}
			
		}
		
	}
	
	@Override
	public void mouseButtonPressed(int button) {

		if(button == MouseButtons.LEFT_BUTTON) {
			
			if(!Mouse.isGrabbed()) {
				
				Mouse.setGrabbed(true);
				
			}
			
		}
		
	}

	public void updateMousePosition() {
		
		MoveableComponent comp = game.moveableComponentHover();
		
		if(Mouse.isButtonDown(MouseButtons.LEFT_BUTTON)) {
			
			if(game.buttonHover() == null) {
				
				if(comp == null) {
				
					if(!mouseClick) {
						
						cursorTexture = moveCursorTexture;
						
						originXCam = (Mouse.getX() / widthDiff) - game.getXCam();
						originYCam = (Mouse.getY() / heightDiff) + game.getYCam();
						
						mouseClick = true;
						
					}
					
					game.setXCam((Mouse.getX()/ widthDiff) - originXCam);
					game.setYCam(-((Mouse.getY()/ heightDiff) - originYCam));
				
				} else {
					
					if(!comp.mouseClick) {
						
						cursorTexture = moveCursorTexture;
						
					}
					
					comp.move();
					
				}
			
			}
			
		} else if(mouseClick || (comp != null && comp.mouseClick)){
			
			cursorTexture = classicCursorTexture;
			mouseClick = false;
			
			if(comp != null) {
				
				comp.setMove(false);
				
			}
			
		}
		
	}

	@Override
	public void stop() {
		
		
		
	}
	
}
