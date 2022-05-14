package fr.slypy.slymyjge.graphics;

public class NewDisplayMode {

	protected boolean fullscreen;
	protected boolean resizable;
	protected int w;
	protected int h;
	
	public NewDisplayMode(boolean fullscreen, boolean resizable, int w, int h) {

		this.fullscreen = fullscreen;
		this.resizable = resizable;
		this.w = w;
		this.h = h;
		
	}

	public boolean isFullscreen() {
		
		return fullscreen;
		
	}

	public void setFullscreen(boolean fullscreen) {
		
		this.fullscreen = fullscreen;
		
	}

	public boolean isResizable() {
		
		return resizable;
		
	}

	public void setResizable(boolean resizable) {
		
		this.resizable = resizable;
		
	}

	public int getW() {
		
		return w;
		
	}

	public void setW(int w) {
		
		this.w = w;
		
	}

	public int getH() {
		
		return h;
		
	}

	public void setH(int h) {
		
		this.h = h;
		
	}
	
}
