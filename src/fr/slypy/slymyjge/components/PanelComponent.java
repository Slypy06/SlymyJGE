package fr.slypy.slymyjge.components;

import java.util.HashMap;
import java.util.Map;

import fr.slypy.slymyjge.Game;

public class PanelComponent {

	public Map<String, Component> components = new HashMap<String, Component>();
	public Game game;
	public boolean visible;
	public boolean activated;
	
	public PanelComponent(Game game) {
		
		this.game = game;

	}

	public void render() {
		
		for(Component comp : components.values()) {
			
			comp.render();
			
		}

	}
	
	public void update(float xCursor, float yCursor, Game game) {
		
		for(Component comp : components.values()) {
			
			comp.update(xCursor, yCursor, game);
			
		}
		
	}
	
	public void addComponent(String key, Component value) {
		
		components.put(key, value);
		
	}
	
	public Map<String, Component> getComponents() {
		
		return components;
		
	}
	
	public Component getComponent(String key) {
		
		if(!components.containsKey(key)) {
			
			return null;
			
		}
		
		return components.get(key);
		
	}

	public boolean isVisible() {
		
		return visible;
		
	}

	public void setVisible(boolean visible) {
		
		for(Component comp : components.values()) {
			
			comp.setVisible(visible);
			
		}
		
	}

	public boolean isActivated() {
		
		return activated;
		
	}

	public void setActivated(boolean activated) {
		
		for(Component comp : components.values()) {
			
			comp.setActivated(activated);
			
		}
		
	}

}
