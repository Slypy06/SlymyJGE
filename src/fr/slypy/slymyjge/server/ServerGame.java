package fr.slypy.slymyjge.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import fr.slypy.slymyjge.network.AuthentifiedPacket;
import fr.slypy.slymyjge.network.Network;
import fr.slypy.slymyjge.network.NetworkRegister;
import fr.slypy.slymyjge.network.Packet;
import fr.slypy.slymyjge.utils.Logger;

public abstract class ServerGame {
	
	protected static Server server;
	protected static boolean exit;
	protected int tickCap = Integer.MAX_VALUE;
	protected long showedTps = 0;
	protected Map<Connection, Long> connectionsTimeout = new HashMap<Connection, Long>();
	
	public ServerGame(int tcpPort, int udpPort, String name, NetworkRegister register) {
		
		ServerGame game = this;

		server = new Server();
		server.setName(name);
		
		server.start();
		
		try {
			
			server.bind(tcpPort, udpPort);
			
		} catch (IOException e) {

			e.printStackTrace();
			
		}
		
		Kryo kryo = server.getKryo();
		
		for(Class<?> packetClass : register.getClasses()) {
			
			kryo.register(packetClass);
			
		}
		
		server.addListener(new Listener() {
			
			public void connected(Connection connect) {
				
				fr.slypy.slymyjge.network.Connection c = (fr.slypy.slymyjge.network.Connection) connect;
				
				if(Network.hasClientRegistering()) {
				
					connectionsTimeout.put(connect, Network.getClientRegistering().getTimeoutDelay());
					c.setConnectedState(false);
				
					game.connected(c);
					
				} else {
					
					c.setConnectedState(true);
					
					game.connected(c);
					
					game.authentified(c);
					
					c.sendTCP(new AuthentifiedPacket());
					
				}
				
			}
			
			public void disconnected(Connection connect) {
				
				fr.slypy.slymyjge.network.Connection c = (fr.slypy.slymyjge.network.Connection) connect;
				
				game.disconnected(c);
				
			}
			
			public void received(Connection connect, Object o) {
				
				fr.slypy.slymyjge.network.Connection c = (fr.slypy.slymyjge.network.Connection) connect;
				
				if(Network.hasClientRegistering()) {
					
					Packet p = (Packet) o;
					
					if(o.getClass().equals(Network.getClientRegistering().getClientRegisteringPacketClass())) {
						
						if(!Network.getClientRegistering().checkRegisteringPacket(p)) {
							
							c.close();
							
						} else {
							
							game.authentified(c);
							connectionsTimeout.remove(c);
							c.sendTCP(new AuthentifiedPacket());
							
						}
						
						return;
						
					}
					
				}
				
				if(o instanceof Packet) {
					
					Packet packet = (Packet) o;
					
					game.packetReceived(c, packet, System.nanoTime() - packet.timestamp);
					
				}
				
			}
			
		});
		
	}
	
	public void start() {
		
		init();
		loop();
		
	}
	
	public abstract void init();
	
	public abstract void update(double alpha);
	
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
				
				for(Connection c : connectionsTimeout.keySet()) {
					
					long timeout = connectionsTimeout.get(c) - (long) (alpha * 1000000000D);
					
					if(timeout <= 0) {
						
						c.close();
						
					} else {
					
						connectionsTimeout.remove(c);
						
						connectionsTimeout.put(c, timeout);
					
					}
					
				}
				
				update(alpha);
				
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
	
	public void connected(fr.slypy.slymyjge.network.Connection connect) {}
	
	public void authentified(fr.slypy.slymyjge.network.Connection connect) {}
	
	public void disconnected(fr.slypy.slymyjge.network.Connection connect) {}
	
	public void packetReceived(fr.slypy.slymyjge.network.Connection connect, Packet packet, long ping) {}

	public int getTickCap() {
		
		return tickCap;
		
	}

	public void setTickCap(int tickCap) {
		
		this.tickCap = tickCap;
		
	}
	

}
