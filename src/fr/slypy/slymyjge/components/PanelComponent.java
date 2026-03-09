package fr.slypy.slymyjge.components;

import java.util.HashMap;
import java.util.Map;

import fr.slypy.slymyjge.Game;
import fr.slypy.slymyjge.graphics.NewGenRenderer;

public class PanelComponent extends Component {

	public Map<String, Component> components = new HashMap<String, Component>();
	public Game game;
	
	public PanelComponent(float x, float y, int w, int h, Game game) {
		
		super(x, y, w, h, game);
		this.game = game;
		
	}

	@Override
	public void render() {
		
		for(Component comp : components.values()) {
			
			if(comp.isVisible()) {
				
				NewGenRenderer.renderComponent(comp);
				
			}
			
		}

	}
	
	@Override
	public void componentUpdate(double alpha) {
		
		for(Component comp : components.values()) {
			
			comp.update(alpha);
			
			if(usesSurface() && comp.usesSurface() && comp.peekNeedsRedrawing())
				redraw();
			
		}
		
	}
	
	public void addComponent(String key, Component value) {
		
		components.put(key, value);
		value.setActivated(activated);
		
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

}
