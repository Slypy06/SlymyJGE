package fr.slypy.slymyjge;

import java.awt.Color;

import fr.slypy.slymyjge.network.NetworkRegister;
import fr.slypy.slymyjge.server.ServerGame;

public class ExampleGame {
	
	public static ExampleGameClient clientGame;
	public static ExampleGameClient serverGame;
	
	public static void main(String args) {
		
		new Thread() {
			
			public void run() {
				
				clientGame = new ExampleGameClient(width, height, title, backgroundColor, resizable)
				
			}
			
		}.start();
		
		new Thread() {
			
			public void run() {
				
				serverGame = new ExampleGameClient(width, height, title, backgroundColor, resizable)
				
			}
			
		}.start();
		
	}
	
	public class ExampleGameClient extends Game {

		public ExampleGameClient(int width, int height, String title, Color backgroundColor, boolean resizable) {
			
			super(width, height, title, backgroundColor, resizable);

		}

		@Override
		public void init() {

			
			
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

	public class ExampleGameServer extends ServerGame {

		public ExampleGameServer(int tcpPort, int udpPort, String name, NetworkRegister register) {
			
			super(tcpPort, udpPort, name, register);

		}

		@Override
		public void init() {

			
			
		}

		@Override
		public void update() {

			
			
		}

		@Override
		public void exit() {

			
			
		}
		
	}
	
}
