package fr.slypy.slymyjge.components;

import java.awt.Color;

import org.lwjgl.util.vector.Vector2f;

import fr.slypy.slymyjge.Game;
import fr.slypy.slymyjge.graphics.ISurface;
import fr.slypy.slymyjge.graphics.MultiSampledSurface;
import fr.slypy.slymyjge.graphics.Surface;
import fr.slypy.slymyjge.graphics.TexCoords;
import fr.slypy.slymyjge.graphics.shape.TexturedRectangle;
import fr.slypy.slymyjge.inputs.InputsHandler;
import fr.slypy.slymyjge.utils.Logger;

public abstract class Component extends InputsHandler {

	protected Game game;
	
	protected boolean activated = true;
	
	protected Vector2f position;
	protected Vector2f size;
	
	protected Vector2f hitboxPosition;
	protected Vector2f hitboxSize;
	
	protected boolean visible = true;
	
	protected boolean absoluteCoordinate = true;
	
	protected boolean hover;
	
	protected ISurface sur = null;
	
	protected boolean focus = false;
	protected boolean lastFocus = false;
	
	public Component(float x, float y, int w, int h, Game game) {

		this.position = new Vector2f(x, y);
		this.size = new Vector2f(w, h);
		this.setHitbox(x, y, w, h);
		this.game = game;
		
	}
	
	public void setupSurface(int samples) {
		
		if(samples == 1) {
			
			sur = new Surface((int) size.getX(), (int) size.getY(), game);
			
		} else {
			
			sur = new MultiSampledSurface((int) size.getX(), (int) size.getY(), samples, game);
			
		}
		
	}
	
	public void setupSurface() {
		
		setupSurface(1);
		
	}
	
	public Vector2f getPosition() {
		
		return position;
		
	}
	
	public Vector2f getSize() {
		
		return size;
		
	}

	public void setPosition(Vector2f pos) {
		
		float xDiff = this.position.getX() - pos.getX();
		float yDiff = this.position.getY() - pos.getY();
		setHitbox(hitboxPosition.getX() + xDiff, hitboxPosition.getY() + yDiff, hitboxSize.getX(), hitboxSize.getY());
		
	}

	public void setSize(Vector2f size) {
		
		float xDiff = this.size.getX() / size.getX();
		float yDiff = this.size.getY() / size.getY();
		setHitbox(hitboxPosition.getX(), hitboxPosition.getY(), hitboxSize.getX()/xDiff, hitboxSize.getY()/yDiff);
		
	}
	
	public Vector2f getHitboxPosition() {
		
		return hitboxPosition;
		
	}
	
	public Vector2f getHitboxSize() {
		
		return hitboxSize;
		
	}
	
	public void setHitboxPosition(Vector2f pos) {
		
		setHitbox(pos.getX(), pos.getY(), hitboxSize.getX(), hitboxSize.getY());
		
	}
	
	public void getHitboxSize(Vector2f size) {
		
		setHitbox(hitboxPosition.getX(), hitboxPosition.getY(), size.getX(), size.getY());
		
	}

	public boolean isHover() {
		return hover;
	}
	
	public boolean isPossiblyHover() {
		
		float x = absoluteCoordinate ? game.getAbsoluteXCursor() : game.getRelativeXCursor();
		float y = absoluteCoordinate ? game.getAbsoluteYCursor() : game.getRelativeYCursor();
		
		if((x >= hitboxPosition.getX() && x <= hitboxPosition.getX() + hitboxSize.getX()) && (y >= hitboxPosition.getY() && y <= hitboxPosition.getY() + hitboxSize.getY())) {
			
			return true;
			
		} else {
			
			return false;
			
		}
		
	}
	
	public boolean isActivated() {
		
		return activated;
	
	}

	public void setActivated(boolean activated) {
		
		this.activated = activated;
	
	}

	public void update() {
		
		if(!activated) {
			
			return;
			
		}
		
		this.updateInputs();
		this.mouseUpdate();
		
		if(focus != lastFocus) {
			
			if(focus) {
				
				focusGained();
				
			} else {
				
				focusLost();
				
			}
			
		}
		
		lastFocus = focus;
		
		boolean lastHover = hover;
		
		hover = game.getComponentHover() == this;
		
		if(lastHover != hover) {
			
			if(hover) {
				
				onMouseEntering();
				
			} else {
				
				onMouseExiting();
				
			}
			
		}
		
		componentUpdate();
		
	}
	
	public void focusGained() {}

	public void focusLost() {}

	public abstract void componentUpdate();
	
	public abstract void render();
	
	public void setHitbox(float x, float y, float w, float h) {

		hitboxPosition = new Vector2f(x, y);
		hitboxSize = new Vector2f(w, h);
		
	}

	public void setVisible(boolean visible) {
		
		this.visible = visible;
		
	}
	
	public boolean isVisible() {
		
		return visible;
		
	}
	
	public boolean usesSurface() {
		
		return sur != null;
		
	}
	
	public ISurface getSurface() {
		
		return sur;
		
	}
	
	public TexturedRectangle getShape() {
		
		if(sur == null) {
			
			Logger.warn("Trying to get shape object from a non surfaced component");
			return null;
			
		}
		
		return new TexturedRectangle(position.getX(), position.getY(), size.getX(), size.getY(), sur.getTextureId(), Color.white, TexCoords.QUAD_DEFAULT_COORDS);
		
	}
	
	public boolean isFocused() {
		
		return focus;
		
	}
	
	public void setFocus(boolean focus) {
		
		this.focus = focus;
		
	}
	
	public boolean isAbsoluteCoordinate() {
		
		return absoluteCoordinate;
		
	}
	
	public void useAbsoluteCoordinate(boolean absolute) {
		
		this.absoluteCoordinate = absolute;
		
	}
	
	public void onMouseEntering() {}
	
	public void onMouseExiting() {}
	
}
