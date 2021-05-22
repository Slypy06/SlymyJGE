package fr.slypy.slymyjge;

import java.awt.Color;
import java.awt.Font;

import org.lwjgl.input.Mouse;

import com.esotericsoftware.kryonet.Connection;

import fr.slypy.slymyjge.components.ButtonComponent;
import fr.slypy.slymyjge.components.Component;
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
	
	public ExampleGame(int width, int height, String title, Color backgroundColor, boolean resizable) {
		
		super(width, height, title, backgroundColor, resizable);

	}
	
	public static void main(String[] args) {
		
		game = new ExampleGame(400, 400, "Example", Color.white, false);
		game.start();
		
	}

	@Override
	public void init() {
		
		Renderer.init(game);
		
		game.setupClientForMultiplayer(new NetworkRegister());
		
		game.setTickCap(1);
		game.setFrameCap(60);
		
	}

	@Override
	public void update(double alpha) {
		
	
		
	}

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

		
		
	}
	
	@Override
	public void disconnected(Connection con) {
		
		Logger.log("Client disconnected !");
		
	}
	
}
