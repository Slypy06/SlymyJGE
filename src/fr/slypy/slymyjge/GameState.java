package fr.slypy.slymyjge;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.slypy.slymyjge.components.Component;
import fr.slypy.slymyjge.inputs.InputsHandler;

public abstract class GameState extends InputsHandler {
	
	public abstract void render(double alpha);
	public abstract void init(InitEvent event);
	public abstract void update(double alpha);
	public abstract void exit(ExitEvent event);
	
	public abstract Game getGame();
	
	public abstract InitType getInitType();
	
	protected boolean isInitialised = false;
	
	protected Map<String, Component> components = new HashMap<String, Component>();
	
	protected float xCam = 0;
	protected float yCam = 0;
	
	protected Color backgroundColor;
	
	public Component getComponentHover() {
		
		for(Component comp : components.values()) {
			
			if(comp.isPossiblyHover(getGame().getRelativeXCursor(), getGame().getRelativeYCursor()) && comp.isVisible()) {
				
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
				
				comp.update(getGame().getRelativeXCursor(), getGame().getRelativeYCursor(), getGame());
				
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
	
	public enum InitType {

		INIT_ON_LOAD,
		INIT_ON_REGISTER,
		MANUALY_INIT,
		INIT_ON_LOAD_AND_ON_REGISTER
		
	}
	
	public static class InitEvent {

		public static final InitEvent registerEvent = new InitEvent(InitType.INIT_ON_REGISTER);
		public static final InitEvent manualEvent = new InitEvent(InitType.MANUALY_INIT);
		
		private InitType eventType;
		private Map<String, Object> resources = new HashMap<>();
		private int previousState;

		public InitEvent(InitType eventType, Map<String, Object> resources, int previousState) {

			this.eventType = eventType;
			this.resources = resources;
			this.previousState = previousState;

		}
		
		public InitEvent(InitType eventType) {

			this.eventType = eventType;
			this.previousState = -1;

		}

		public InitType getEventType() {
		
			return eventType;
		
		}
		
		public Map<String, Object> getResources() {
		
			return resources;
		
		}
		
		
		public List<String> getResourcesList() {
			
			return new ArrayList<String>(resources.keySet());
			
		}
		
		@SuppressWarnings("unchecked")
		public <T> T getResource(String name, Class<T> objClass) {
			
			if(!resources.containsKey(name) || !objClass.isInstance(resources.get(name))) {
				
				return null;
				
			}
			
			return (T) resources.get(name); 
			
		}
		
		public int getPreviousState() {
		
			return previousState;
		
		}
		
	}
	
	public enum ExitType {

		STOPPING_GAME,
		SWITCHING_STATE,
		MANUAL_EXIT
		
	}
	
	public static class ExitEvent {
		
		public static final ExitEvent stoppingEvent = new ExitEvent(ExitType.STOPPING_GAME);
		public static final ExitEvent manualEvent = new ExitEvent(ExitType.MANUAL_EXIT);

		private ExitType eventType;
		private Map<String, Object> resources = new HashMap<>();
		private int nextState;

		public ExitEvent(ExitType eventType, int nextState) {

			this.eventType = eventType;
			this.nextState = nextState;

		}
		
		public ExitEvent(ExitType eventType) {

			this.eventType = eventType;
			this.nextState = -1;

		}

		public ExitType getEventType() {
		
			return eventType;
		
		}
		
		public void addResources(String name, Object o) {
		
			if(eventType != ExitType.SWITCHING_STATE)
				return;
			
			resources.put(name, o);
		
		}
		
		public int getNextState() {
		
			return nextState;
		
		}
		
		public Map<String, Object> getResources() {
			
			return resources;
			
		}
		
	}
	
}
