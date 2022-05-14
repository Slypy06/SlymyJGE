package fr.slypy.slymyjge.server;

import com.esotericsoftware.kryonet.Connection;

public class Server extends com.esotericsoftware.kryonet.Server {

	protected String name;

	public String getName() {
		
		return name;
		
	}

	public void setName(String name) {
		
		this.name = name;

	}
	
	protected Connection newConnection () {

		return new fr.slypy.slymyjge.network.Connection();
		
	}

}
