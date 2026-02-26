package fr.slypy.slymyjge.components;

import fr.slypy.slymyjge.Game;
import fr.slypy.slymyjge.utils.MouseButtons;

public abstract class ButtonComponent extends Component {
	
	private boolean directFire = false;
	private boolean pressed = false;
	
	public ButtonComponent(float x, float y, int w, int h, Game game) {
		
		super(x, y, w, h, game);
		
		addMouseButtonsToListen(new int[] {MouseButtons.LEFT_BUTTON});
		
	}
	
	public abstract void componentActivated();
	
	@Override
	public void componentUpdate() {
		
		pressed = activated && pressed && hover;
		
	}
	
	@Override
	public void mouseButtonPressed(int button) {
		
		if(button == MouseButtons.LEFT_BUTTON && hover && activated) {
			
			if(directFire) {
			
				componentActivated();
			
			}
			
			pressed = true;
			
		}
		
		super.mouseButtonPressed(button);
		
	}
	
	@Override
	public void mouseButtonReleased(int button) {
		
		if(button == MouseButtons.LEFT_BUTTON && hover && activated && pressed) {
			
			if(!directFire) {
			
				componentActivated();
			
			}
			
			pressed = false;
			
		}
		
		super.mouseButtonReleased(button);
		
	}

	public boolean isDirectFire() {
		
		return directFire;
		
	}

	public void setDirectFire(boolean directFire) {
		
		this.directFire = directFire;
		
	}

	public boolean isPressed() {
		
		return pressed;
		
	}
	
}
