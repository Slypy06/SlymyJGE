package fr.slypy.slymyjge;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import com.esotericsoftware.kryonet.Connection;

import fr.slypy.slymyjge.graphics.Renderer;
import fr.slypy.slymyjge.network.NetworkRegister;
import fr.slypy.slymyjge.server.ServerGame;

public class ExampleGame {
	
	public static ExampleGameClient clientGame;
	public static ExampleGameServer serverGame;
	
	public static void main(String[] args) {
		
		new Thread() {
			
			public void run() {
				
				clientGame = new ExampleGameClient(1280, 720, "Example Game Client", Color.black, false);
				clientGame.start();
				
			}
			
		}.start();
		
		new Thread() {
			
			public void run() {
				
				NetworkRegister reg = new NetworkRegister();
				
				serverGame = new ExampleGameServer(25566, 25567, "Example Game Server", reg);
				serverGame.start();
				
			}
			
		}.start();
		
	}
	
	public static class ExampleGameClient extends Game {

		public int state = -1;
		
		public ExampleGameClient(int width, int height, String title, Color backgroundColor, boolean resizable) {
			
			super(width, height, title, backgroundColor, resizable);

		}

		@Override
		public void init() {

			setupClientForMultiplayer(new NetworkRegister());
			
			setFrameCap(60);
			setTickCap(50);
			Renderer.init(this);
			
		}

		@Override
		public void update(double alpha) {

			if(state == -1 && Keyboard.isKeyDown(Keyboard.KEY_E)) {
				
				connectToServer("localhost", 25566, 25567);
				
			}
			
		}

		@Override
		public void render(double alpha) {

			switch(state) {
			
			case -1:
				
				Renderer.renderQuad(100, 100, 100, 100, Color.BLACK);
				break;
			
			case 0:
				
				Renderer.renderQuad(100, 100, 100, 100, Color.DARK_GRAY);
				break;
				
			case 1:
				
				Renderer.renderQuad(100, 100, 100, 100, Color.LIGHT_GRAY);
				break;
				
			case 2:
				
				Renderer.renderQuad(100, 100, 100, 100, Color.WHITE);
				break;
			
			}
			
		}

		@Override
		public void stop() {

			
			
		}
		
		@Override
		public void authentified(Connection c) {
			
			state = 2;
			
		}
		
		@Override
		public void connected(Connection c) {
			
			state = 1;
			
		}
		
	}

	public static class ExampleGameServer extends ServerGame {

		public ExampleGameServer(int tcpPort, int udpPort, String name, NetworkRegister register) {
			
			super(tcpPort, udpPort, name, register);

		}

		@Override
		public void init() {

			setTickCap(50);
			
		}

		@Override
		public void update(double alpha) {
			
			
			
		}
		
		@Override
		public void exit() {

			
			
		}
		
	}
	
}
