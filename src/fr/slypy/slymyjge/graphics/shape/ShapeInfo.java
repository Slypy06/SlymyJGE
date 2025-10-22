package fr.slypy.slymyjge.graphics.shape;


public class ShapeInfo {
	
	private final int vertexCount;
	private final boolean useTexCoords;
	private final int glMode;

	public ShapeInfo(int vertexCount, boolean useTexCoords, int glMode) {

		this.vertexCount = vertexCount;
		this.useTexCoords = useTexCoords;
		this.glMode = glMode;

	}

	public int getVertexCount() {
	
		return vertexCount;
	
	}
	
	public boolean useTexCoords() {
	
		return useTexCoords;
	
	}
	
	public int getGlMode() {
	
		return glMode;
	
	}
	
	public int vertexSize() {
		
		return 2*Float.BYTES + (useTexCoords ? 2*Float.BYTES : 0) + 4;
		
	}
	
	public int colorPointerOffset() {
		
		return 2*Float.BYTES + (useTexCoords ? 2*Float.BYTES : 0);
		
	}
	
}
