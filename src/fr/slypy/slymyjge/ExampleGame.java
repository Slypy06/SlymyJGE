package fr.slypy.slymyjge;

import java.awt.Color;

import fr.slypy.slymyjge.network.NetworkRegister;
import fr.slypy.slymyjge.server.ServerGame;

public class ExampleGame {
	
	public static ExampleGameClient clientGame;
	public static ExampleGameServer serverGame;
	
	public static void main(String args) {
		
		new Thread() {
			
			public void run() {
				
				clientGame = new ExampleGameClient(1280, 720, "Example Game Client", Color.black, false);
				clientGame.start();
				
			}
			
		}.start();
		
		/*new Thread() {
			
			public void run() {
				
				NetworkRegister reg = new NetworkRegister();
				
				serverGame = new ExampleGameServer(25566, 25567, "Example Game Server", reg);
				serverGame.start();
				
			}
			
		}.start();*/
		
	}
	
	public static class ExampleGameClient extends Game {

		public ExampleGameClient(int width, int height, String title, Color backgroundColor, boolean resizable) {
			
			super(width, height, title, backgroundColor, resizable);

		}

		@Override
		public void init() {

			setFrameCap(60);
			setTickCap(50);
			
		}

		@Override
		public void update(double alpha) {

			
			
		}

		@Override
		public void render(double alpha) {

			
			
		}

		@Override
		public void stop() {

			
			
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
