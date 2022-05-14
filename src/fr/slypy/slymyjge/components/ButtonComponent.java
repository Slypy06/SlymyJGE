package fr.slypy.slymyjge.components;

import fr.slypy.slymyjge.Game;
import fr.slypy.slymyjge.utils.MouseButtons;
import fr.slypy.slymyjge.utils.RenderType;

public abstract class ButtonComponent extends Component {
	
	public ButtonComponent(float x, float y, int w, int h, Game game) {
		
		this(x, y, w, h, game, RenderType.ONMAP);
		
	}
	
	public ButtonComponent(float x, float y, int w, int h, Game game, RenderType type) {
		
		super(x, y, w, h, game, type);
		
		addMouseButtonsToListen(new int[] {MouseButtons.LEFT_BUTTON});
		
	}
	
	public void componentActivated() {}
	
	@Override
	public void mouseButtonPressed(int button) {
		
		if(button == MouseButtons.LEFT_BUTTON && hover && activated) {
			
			componentActivated();
			
		}
		
	}
	
}
