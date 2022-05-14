package fr.slypy.slymyjge.network;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NetworkRegister {

	protected List<Class<?>> classes = new ArrayList<Class<?>>();
	
	public NetworkRegister() {
		
		classes.add(AuthentifiedPacket.class);
		
	}
	
	public void register(Class<?> packet) {
		
		classes.add(packet);
		
	}
	
	public void registerAll(List<Class<?>> packets) {
		
		classes.addAll(packets);
		
	}
	
	public void registerAll(Class<?>[] packets) {
		
		classes.addAll(Arrays.asList(packets));
		
	}

	public List<Class<?>> getClasses() {
		
		return classes;
		
	}
	
}
