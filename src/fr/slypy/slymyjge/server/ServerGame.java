package fr.slypy.slymyjge.server;

import java.io.IOException;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import fr.slypy.slymyjge.network.NetworkRegister;
import fr.slypy.slymyjge.network.Packet;
import fr.slypy.slymyjge.utils.Logger;

public abstract class ServerGame {
	
	protected static Server server;
	protected static boolean exit;
	
	public ServerGame(int tcpPort, int udpPort, String name, NetworkRegister register) {
		
		ServerGame game = this;
		
		new Thread() {
		
			public void run() {
			
				server = new Server();
				server.setName(name);
				server.start();
				Kryo kryo = server.getKryo();
				
				for(Class<?> packetClass : register.getClasses()) {
					
					kryo.register(packetClass);
					
				}
				
				try {
					
					server.bind(tcpPort, udpPort);
					
				} catch (IOException e) {

					e.printStackTrace();
					
				}
				
				server.addListener(new Listener() {
					
					public void connected(Connection connect) {
						
						game.connected(connect);
						
					}
					
					public void disconnected(Connection connect) {
						
						game.disconnected(connect);
						
					}
					
					public void received(Connection connect, Object o) {
						
						if(o instanceof Packet) {
							
							Packet packet = (Packet) o;
							
							game.packetReceived(connect, packet, System.nanoTime() - packet.timestamp);
							
						}
						
					}
					
				});
		
			}
		
		}.start();
		
	}
	
	public void start() {
		
		init();
		loop();
		
	}
	
	public abstract void init();
	
	public abstract void update();
	
	public abstract void exit();
	
	protected void loop() {
		
		while(true) {
			
			if(exit) {
				
				exit();
				close();
				
			}
			
			update();
			
		}
		
	}
	
	protected void close() {
		
		Logger.log("Arret du server !");
		System.exit(0);
		
	}
	
	public void stop() {
		
		exit = true;
		
	}
	
	public void connected(Connection connect) {}
	
	public void disconnected(Connection connect) {}
	
	public void packetReceived(Connection connect, Packet packet, long ping) {}

}
