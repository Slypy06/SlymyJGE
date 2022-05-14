package fr.slypy.slymyjge;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.input.Mouse;

import fr.slypy.slymyjge.components.Component;
import fr.slypy.slymyjge.inputs.KeyboardInputs;

public abstract class GameState extends KeyboardInputs {
	
	public abstract void render(double alpha);
	public abstract void init();
	public abstract void update(double alpha);
	
	public abstract Game getGame();
	
	public abstract InitType getInitType();
	
	protected boolean isInitialised = false;
	
	protected Map<String, Component> components = new HashMap<String, Component>();
	
	protected float xCam = 0;
	protected float yCam = 0;
	
	protected Color backgroundColor;
	
	public Component getComponentHover() {
		
		for(Component comp : components.values()) {
			
			if(comp.isPossiblyHover(Mouse.getX(), getGame().getHeight() - Mouse.getY()) && comp.isVisible()) {
				
				return comp;
				
			}
			
		}
		
		return null;
		
	}
	
	public void addComponent(Component comp, String key) {
		
		components.put(key, comp);
		
	}
	
	public void removeComponent(String key) {
		
		if(components.containsKey(key)) {
			
			components.remove(key);
			
			return;
			
		} else {
			
			for(String s : components.keySet()) {
				
				if(s.equalsIgnoreCase(key)) {
					
					components.remove(s);
					
					return;
					
				}
				
			}
			
		}
		
	}
	
	public Component getComponent(String key) {
		
		if(components.containsKey(key)) {
			
			return components.get(key);
			
		} else {
			
			for(String s : components.keySet()) {
				
				if(s.equalsIgnoreCase(key)) {
					
					return components.get(s);
					
				}
				
			}
			
			return null;
			
		}
		
	}
	
	public void componentsUpdate() {
		
		for(Component comp : components.values()) {
			
			if(comp.isActivated()) {
				
				comp.update(Mouse.getX(), getGame().getHeight() - Mouse.getY(), getGame());
				
			}
			
		}
		
	}
	
	public void componentsRender() {
		
		for(Component comp : components.values()) {
			
			if(comp.isVisible()) {
				
				comp.render();
				
			}
			
		}
		
	}
	
	public void translateCam(float xa, float ya) {
		
		setXCam(xCam + xa);
		setYCam(yCam + ya);
		
	}
	
	public float getXCam() {
		
		return xCam;
		
	}

	public float getYCam() {
		
		return yCam;
		
	}
	
	public void setXCam(float xCam) {
		
		this.xCam = xCam;
		
	}

	public void setYCam(float yCam) {
		
		this.yCam = yCam;
		
	}
	
}
