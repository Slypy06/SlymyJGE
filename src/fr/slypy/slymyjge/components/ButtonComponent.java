package fr.slypy.slymyjge.components;

import fr.slypy.slymyjge.Game;
import fr.slypy.slymyjge.utils.MouseButtons;

public abstract class Button extends Component {
	
	public Button(float x, float y, int w, int h, Game game) {
		
		super(x, y, w, h, game);
		
		addMouseButtonsToListen(new int[] {MouseButtons.LEFT_BUTTON});
		
	}
	
	public abstract void componentActivated();
	
	@Override
	public void mouseButtonPressed(int button) {
		
		if(!activated) {
			
			return;
			
		}
		
		if(button == MouseButtons.LEFT_BUTTON && hover) {
			
			componentActivated();
			
		}
		
	}
	
}
