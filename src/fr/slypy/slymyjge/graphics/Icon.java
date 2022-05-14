package fr.slypy.slymyjge.graphics;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class Icon {

	private Map<IconResolution, ByteBuffer> icons = new HashMap<IconResolution, ByteBuffer>();
	
	public void addIcon(IconResolution res, ByteBuffer buf) {
		
		icons.put(res, buf);
		
	}

	public boolean hasIcon(IconResolution res) {
		
		return icons.containsKey(res);
		
	}
	
	public ByteBuffer getIcon(IconResolution res) {
		
		return icons.get(res);
		
	}
	
	public int icons() {
		
		return icons.size();
		
	}
	
}
