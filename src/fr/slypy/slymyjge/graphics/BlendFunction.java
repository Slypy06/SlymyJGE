package fr.slypy.slymyjge.graphics;

import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.GL_ZERO;
import static org.lwjgl.opengl.GL11.GL_SRC_COLOR;

public class BlendFunction {
	
	public static final BlendFunction AVERAGE = new BlendFunction(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	public static final BlendFunction MULTIPLICATIVE = new BlendFunction(GL_ZERO, GL_SRC_COLOR);
	public static final BlendFunction SOFT_ADDITIVE = new BlendFunction(GL_SRC_ALPHA, GL_ONE);
	public static final BlendFunction ADDITIVE = new BlendFunction(GL_ONE, GL_ONE);
	
	private final int src;
	private final int dst;
	
	private BlendFunction(int src, int dst) {
		
		this.src = src;
		this.dst = dst;
		
	}

	public int getSrc() {
	
		return src;
	
	}

	public int getDst() {
	
		return dst;
	
	}

}
