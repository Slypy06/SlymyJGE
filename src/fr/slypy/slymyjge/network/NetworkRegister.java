package fr.slypy.slymyjge.network;

import java.util.ArrayList;
import java.util.List;

public class NetworkRegister {

	protected List<Class<?>> classes = new ArrayList<Class<?>>();
	
	public void register(Class<?> packet) {
		
		classes.add(packet);
		
	}

	public List<Class<?>> getClasses() {
		
		return classes;
		
	}
	
}
