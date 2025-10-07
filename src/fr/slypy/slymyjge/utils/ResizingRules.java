package fr.slypy.slymyjge.utils;


public class ResizingRules {
	
	public static final ResizingRules NotResizable = null;
	public static final ResizingRules DefaultRules = new ResizingRules(true, true, false);

	private boolean resizeWidth;
	private boolean resizeHeight;
	private boolean fixedAspectRatio;
	
	public ResizingRules(boolean resizeWidth, boolean resizeHeight, boolean fixedAspectRatio) {

		this.fixedAspectRatio = fixedAspectRatio;
		
		if(fixedAspectRatio) {
			
			this.resizeHeight = resizeWidth && resizeHeight;
			this.resizeWidth = resizeWidth && resizeHeight;
			
		} else {
			
			this.resizeHeight = resizeHeight;
			this.resizeWidth = resizeWidth;
			
		}

	}

	public boolean isResizeWidth() {
	
		return resizeWidth;
	
	}

	
	public boolean isResizeHeight() {
	
		return resizeHeight;
	
	}

	
	public boolean isFixedAspectRatio() {
	
		return fixedAspectRatio;
	
	}
	
}
