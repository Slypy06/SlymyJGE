package fr.slypy.slymyjge;

import java.util.Scanner;

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
		
		serverGame = new ExampleGameServer(52262, 52263, "Example Game Server", reg);
		
		server.start();
		
	}

	@Override
	public void init() {
		
		System.out.println("Server Init");
		scan = new Scanner(System.in);
		
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

}
