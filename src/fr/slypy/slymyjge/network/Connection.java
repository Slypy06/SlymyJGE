package fr.slypy.slymyjge.network;

public class Connection extends com.esotericsoftware.kryonet.Connection {

	public boolean connectedState = false;

	public boolean getConnectedState() {
		
		return connectedState;
		
	}

	public void setConnectedState(boolean connected) {
		
		this.connectedState = connected;
		
	}

}
