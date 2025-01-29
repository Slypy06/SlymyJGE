package fr.slypy.slymyjge.components;

import fr.slypy.slymyjge.Game;
import fr.slypy.slymyjge.utils.MouseButtons;
import fr.slypy.slymyjge.utils.RenderType;

public abstract class ButtonComponent extends Component {
	
	private boolean directFire = false;
	private boolean pressed = false;
	
	public ButtonComponent(float x, float y, int w, int h, Game game) {
		
		this(x, y, w, h, game, RenderType.ONMAP);
		
	}
	
	public ButtonComponent(float x, float y, int w, int h, Game game, RenderType type) {
		
		super(x, y, w, h, game, type);
		
		addMouseButtonsToListen(new int[] {MouseButtons.LEFT_BUTTON});
		
	}
	
	public void componentActivated() {}
	
	@Override
	public void componentUpdate(float x, float y, Game g) {
		
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
