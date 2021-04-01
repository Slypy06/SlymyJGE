package fr.slypy.slymyjge.server;

import java.io.IOException;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import fr.slypy.slymyjge.network.Network;
import fr.slypy.slymyjge.network.NetworkRegister;
import fr.slypy.slymyjge.network.Packet;
import fr.slypy.slymyjge.utils.Logger;

public abstract class ServerGame {
	
	protected static Server server;
	protected static boolean exit;
	protected int tickCap = Integer.MAX_VALUE;
	protected long showedTps = 0;
	
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
						
						if(Network.hasClientRegistering()) {
							
							Packet p = (Packet) o;
							
							if(o.getClass().equals(Network.getClientRegistering().getClientRegisteringPacketClass())) {
								
								if(!Network.getClientRegistering().checkRegisteringPacket(p)) {
									
									connect.close();
									
								}
								
							}
							
						}
						
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
		
		long tps = 0;
		
		long lastTpsUpdate = System.nanoTime();
		
		long before = System.nanoTime();
		
		double alpha = (double) (System.nanoTime() - before) / 1000000000D;
		
		double gamma = 1D / (double) tickCap;
		
		while(true) {
			
			alpha = (double) (System.nanoTime() - before) / 1000000000D;
			
			if(alpha > gamma) {
			
				tps++;
				
				if(exit) {
					
					exit();
					close();
					
				}
				
				update();
				
				if (System.nanoTime() - lastTpsUpdate > 1000000000L) {
					
					showedTps = tps;
					
					lastTpsUpdate = System.nanoTime();
					
					tps = 0;

				}
				
			}
			
		}
		
	}
	
	public int getTps() {
		
		return (int) showedTps;
		
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
