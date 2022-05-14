package fr.slypy.slymyjge.network;

public interface NetworkClientRegistering {

	public Class<? extends Packet> getClientRegisteringPacketClass();
	
	public void sendPacketToRegister();
	
	public boolean checkRegisteringPacket(Packet p);
	
	public long getTimeoutDelay();
	
}
