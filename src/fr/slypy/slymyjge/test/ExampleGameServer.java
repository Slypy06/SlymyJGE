package fr.slypy.slymyjge.test;

import java.util.Scanner;

import fr.slypy.slymyjge.network.Connection;
import fr.slypy.slymyjge.network.NetworkRegister;
import fr.slypy.slymyjge.server.ServerGame;

public class ExampleGameServer extends ServerGame {

	public static ExampleGameServer serverGame;
	public Scanner scan;
	
	public ExampleGameServer(int tcpPort, int udpPort, String name, NetworkRegister register) {
		
		super(tcpPort, udpPort, name, register);

	}
	
	public static void main(String[] args) {
		
		NetworkRegister reg = new NetworkRegister();
		
		serverGame = new ExampleGameServer(22222, 22222, "Puissance 4 Server", reg);

		serverGame.start();
		
	}

	@Override
	public void init() {
		
		System.out.println("Server Init");
		scan = new Scanner(System.in);
		
		serverGame.setTickCap(20);
		
	}

	@Override
	public void update(double alpha) {
		
		if(scan.hasNext()) {
			
			String s = scan.next();
			
			if(s.equalsIgnoreCase("stop")) {
				
				serverGame.stop();
				
			}
			
		}
		
	}

	@Override
	public void exit() {
		
		System.out.println("Server Stop");
		
	}
	
	@Override
	public void connected(Connection con) {
		
		System.out.println("connected");
		
	}

}
