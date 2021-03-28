package fr.slypy.slymyjge.components;

import fr.slypy.slymyjge.Game;
import fr.slypy.slymyjge.inputs.KeyboardInputs;
import fr.slypy.slymyjge.utils.RenderType;

public abstract class Component extends KeyboardInputs {

	protected Game game;
	
	public boolean activated = true;
	
	float x;
	float y;
	
	int w;
	int h;
	
	float hitboxX;
	float hitboxY;
	
	int hitboxW;
	int hitboxH;
	
	public RenderType getRenderType() {
		
		return renderType;
		
	}

	public void setRenderType(RenderType renderType) {
		
		this.renderType = renderType;
		
	}

	boolean hover;
	
	protected RenderType renderType;
	
	public Component(float x, float y, int w, int h, Game game) {
		
		this(x, y, w, h, game, RenderType.ONMAP);
		
	}
	
	public Component(float x, float y, int w, int h, Game game, RenderType type) {
		
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.setHitbox(x, y, w, h);
		this.game = game;
		this.renderType = type;
		
	}
	
	public float getX() {
		
		return x;
		
	}

	public void setX(float x) {
		
		float xDiff = x - this.x;
		this.x = x;
		setHitbox(hitboxX + xDiff, hitboxY, hitboxW, hitboxH);
		
	}

	public float getY() {
		
		return y;
		
	}

	public void setY(float y) {
		
		float yDiff = y - this.y;
		this.y = y;
		setHitbox(hitboxX, hitboxY + yDiff, hitboxW, hitboxH);
		
	}

	public int getW() {
		
		return w;
		
	}

	public void setW(int w) {
		
		int wDiff = w / this.w;
		this.w = w;
		setHitbox(hitboxX, hitboxY, hitboxW * wDiff, hitboxH);
		
	}

	public int getH() {
		
		return h;
		
	}

	public void setH(int h) {
		
		int hDiff = h / this.h;
		this.h = h;
		setHitbox(hitboxX, hitboxY, hitboxW, hitboxH * hDiff);
		
	}
	
	public float getHitboxX() {
		return hitboxX;
	}

	public void setHitboxX(float hitboxX) {
		
		this.hitboxX = hitboxX;
		
	}

	public float getHitboxY() {
		
		return hitboxY;
		
	}

	public void setHitboxY(float hitboxY) {
		this.hitboxY = hitboxY;
	}

	public int getHitboxW() {
		return hitboxW;
	}

	public void setHitboxW(int hitboxW) {
		this.hitboxW = hitboxW;
	}

	public int getHitboxH() {
		return hitboxH;
	}

	public void setHitboxH(int hitboxH) {
		this.hitboxH = hitboxH;
	}

	public boolean isHover() {
		return hover;
	}
	
	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public void update(float xCursor, float yCursor, Game game) {
		
		this.keyUpdate();
		
		float x = xCursor;
		float y = yCursor;
	
		if(renderType == RenderType.HUD) {
			
			x = xCursor + game.getXCam();
			y = yCursor + game.getYCam();
			
		}
		
		if((x >= hitboxX && x <= hitboxX + hitboxW) && (y >= hitboxY && y <= hitboxY + hitboxH)) {
			
			hover = true;
			
		} else {
			
			hover = false;
			
		}
		
		if(game.componentHover() != this) {
			
			hover = false;
			
		}
		
	}
	
	public abstract void render();
	
	public void setHitbox(float x, float y, int w, int h) {
		
		setHitboxX(x);
		setHitboxY(y);
		setHitboxW(w);
		setHitboxH(h);
		
	}
	
}
