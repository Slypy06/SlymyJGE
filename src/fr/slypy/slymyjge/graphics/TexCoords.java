package fr.slypy.slymyjge.graphics;

import org.lwjgl.util.vector.Vector2f;

public class TexCoords {
	
	public static final TexCoords QUAD_DEFAULT_COORDS = new TexCoords(new Vector2f[] {new Vector2f(0, 0), new Vector2f(1, 0), new Vector2f(1, 1), new Vector2f(0, 1)});
	public static final TexCoords TRIANGLE_DEFAULT_COORDS = new TexCoords(new Vector2f[] {new Vector2f(0, 0), new Vector2f(1, 0), new Vector2f(1, 1)});
	
	private final Vector2f[] coords;

	public TexCoords(Vector2f[] coords) {

		this.coords = coords;
		
	}

	public Vector2f[] getCoords() {
		return coords;
	}
	
	public TexCoords multiply(Vector2f v) {
		
		Vector2f[] newCoords = new Vector2f[coords.length];
		
		for(int i = 0; i < coords.length; i++) {
			
			newCoords[i] = new Vector2f(coords[i].x * v.x, coords[i].y * v.y);
			
		}
		
		return new TexCoords(newCoords);
		
	}
	
	@Override
	public String toString() {
		
		StringBuilder b = new StringBuilder();
		
		for(Vector2f coord : coords) {
			
			b.append(coord).append('\n');
			
		}
		
		return b.toString();
		
	}

}
