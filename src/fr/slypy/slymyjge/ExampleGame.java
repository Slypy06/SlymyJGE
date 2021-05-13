package fr.slypy.slymyjge;

import java.awt.Color;
import java.awt.Font;

import org.lwjgl.input.Mouse;

import com.esotericsoftware.kryonet.Connection;

import fr.slypy.slymyjge.components.ButtonComponent;
import fr.slypy.slymyjge.components.PanelComponent;
import fr.slypy.slymyjge.components.TextFieldComponent;
import fr.slypy.slymyjge.font.SlymyFont;
import fr.slypy.slymyjge.graphics.Renderer;
import fr.slypy.slymyjge.network.NetworkRegister;
import fr.slypy.slymyjge.utils.Logger;
import fr.slypy.slymyjge.utils.MouseButtons;

public class ExampleGame extends Game {

	public static ExampleGame game;
	public SlymyFont font;
	
	
	public PanelComponent connectionPanel;
	
	public TextFieldComponent textField;
	public TextFieldComponent ipField;
	public ButtonComponent button;
	
	public ExampleGame(int width, int height, String title, Color backgroundColor, boolean resizable) {
		
		super(width, height, title, backgroundColor, resizable);

	}
	
	public static void main(String[] args) {
		
		game = new ExampleGame(400, 400, "Puissance 4", Color.white, false);
		game.start();
		
	}

	@Override
	public void init() {
		
		Renderer.init(game);
		
		game.setupClientForMultiplayer(new NetworkRegister());
		
		game.setTickCap(20);
		game.setFrameCap(20);
		
		initConnectionPanel();
		
	}

	@Override
	public void update(double alpha) {}

	@Override
	public void render(double alpha) {}

	@Override
	public void stop() {}
	
	@Override
	public void connected(Connection con) {
		
		Logger.log("Client connected !");
		
	}
	
	@Override
	public void authentified(Connection con) {
		
		Logger.log("Client authentified !");
		
	}
	
	@Override
	public void disconnected(Connection con) {
		
		Logger.log("Client disconnected !");
		
	}
	
	public void initConnectionPanel() {
		
		connectionPanel = new PanelComponent(game);
		
		font = new SlymyFont(new Font("", 0, 20), Color.black);
		
		textField = new TextFieldComponent(25, 100, 350, 50, game, font) {
			
			@Override
			public void renderForeground() {
				
				
				
			}
			
			@Override
			public void renderBackground() {
				
				Renderer.renderQuad(getX(), getY(), getW(), getH(), new Color(240, 240, 240));
				Renderer.renderBorder(getX(), getY(), getW(), getH(), 2, new Color(50, 50, 50));
				
			}
			
		};
		
		ipField = new TextFieldComponent(25, 175, 350, 50, game, font) {
			
			@Override
			public void renderForeground() {
				
				
				
			}
			
			@Override
			public void renderBackground() {
				
				Renderer.renderQuad(getX(), getY(), getW(), getH(), new Color(240, 240, 240));
				Renderer.renderBorder(getX(), getY(), getW(), getH(), 2, new Color(50, 50, 50));
				
			}
			
		};
		
		font = new SlymyFont(new Font("", 0, 20), Color.LIGHT_GRAY);
		
		textField.setCap(15);
		textField.setGhostText("Username");
		textField.setGhostF(font);
		
		ipField.setCap(28);
		ipField.setGhostText("IP");
		ipField.setGhostF(font);
		
		font = new SlymyFont(new Font("", Font.BOLD, 20), Color.black);
		
		button = new ButtonComponent(150, 250, 100, 40, game) {
			
			@Override
			public void render() {
				
				if(isHover() && Mouse.isButtonDown(MouseButtons.LEFT_BUTTON)) {
					
					Renderer.renderQuad(getX(), getY(), getW(), getH(), Color.gray);
					Renderer.renderBorder(getX() + 1, getY() + 1, getW() - 2, getH() - 2, 2, new Color(50, 50, 50));
					
				} else {
					
					Renderer.renderQuad(getX(), getY(), getW(), getH(), Color.lightGray);
					Renderer.renderBorder(getX(), getY(), getW(), getH(), 2, new Color(50, 50, 50));
					
				}
				
				Renderer.renderText(getX() + ((getW() - font.getWidth("JOIN")) / 2), getY() + ((getH() - font.getHeight()) / 2), font, "JOIN");
				
			}
			
			@Override
			public void componentActivated() {
				
				try {
				
					if(textField.length() >= 3 && ipField.length() >= 3) {
						
						String adress = "";
						String port = "22222";
						
						if(ipField.getText().contains(":")) {
							
							String[] split = ipField.getText().split(":");
							
							port = split[split.length - 1];
							adress = split[0];
							
						} else {
							
							adress = ipField.getText();
							
						}
						
						game.connectToServer(adress, Integer.parseInt(port), Integer.parseInt(port));
						
						game.removeComponentsPanel(connectionPanel);
						game.setSize(1280, 720);
						//game.addComponentPanel(connectionPanel);
						
					}
				
				} catch(Exception e) {
					
					System.out.println("Une erreur est survenue");
					e.printStackTrace();
					
				}
				
			}
			
		};
		
		connectionPanel.addComponent("textfield", textField);
		connectionPanel.addComponent("ipField", ipField);
		connectionPanel.addComponent("button", button);
		
		game.addComponentPanel(connectionPanel);
		
	}
	
	public void initMainPanel() {
		
		
		
	}
	
	public void initGamePanel() {
		
		
		
	}
	
}
