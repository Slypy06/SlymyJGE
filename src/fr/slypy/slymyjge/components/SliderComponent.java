package fr.slypy.slymyjge.components;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import fr.slypy.slymyjge.Game;
import fr.slypy.slymyjge.utils.MouseButtons;
import fr.slypy.slymyjge.utils.RenderType;

public abstract class SliderComponent extends Component {

	public boolean focus;
	public float value;
	public float lastValue;
	public int padding;
	public int steps;
	public boolean clickStartedInside = false;
	public long downTime;
	public long upTime;
	
	public SliderComponent(float x, float y, int w, int h, Game game, RenderType type) {
		
		super(x, y, w, h, game, type);
		
		padding = h;
		
		this.addMouseButtonToListen(MouseButtons.LEFT_BUTTON);
		this.addKeyToListen(Keyboard.KEY_DOWN);
		this.addKeyToListen(Keyboard.KEY_UP);

	}
	
	public SliderComponent(float x, float y, int w, int h, Game game) {
		
		this(x, y, w, h, game, RenderType.ONMAP);
		
	}
	
	@Override
	public void mouseButtonPressed(int button) {
		
		if(!activated) {
			
			return;
			
		}
	
		if(button == MouseButtons.LEFT_BUTTON && hover && activated && !focus) {
			
			focus = true;
			focusGained();
			
		} else if(!hover && activated && focus) {
			
			focus = false;
			focusLost();
			
		}
		
	}
	
	@Override
	public void keyPressed(int key) {
		
		if(!activated || !focus) {
			
			return;
			
		}
		
		if(key == Keyboard.KEY_DOWN) {
			
			downTime = System.nanoTime() + 600000000;
			
			if(value > 0) {
				
				if(steps <= 0 && value >= 0.01f) {
					
					value -= 0.01f;
					
				} else {
				
					value -= (1.0f / steps);
				
				}
				
			}
			
		} else {
			
			upTime = System.nanoTime() + 600000000;
			
			if(steps <= 0 && value <= 0.99f) {
					
				value += 0.01f;
					
			} else if(value <= 1.0f - (1.0f / steps)){
				
				value += (1.0f / steps);
				
			}
			
		}
		
	}
	
	@Override
	public void keyReleased(int key) {
		
		if(!activated || !focus) {
			
			return;
			
		}
		
		if(key == Keyboard.KEY_DOWN) {
			
			downTime = -1;
			
		} else {
			
			upTime = -1;
			
		}
		
	}
	
	@Override
	public void componentUpdate(float xCursor, float yCursor, Game game) {
		
		if(!activated) {
			
			focus = false;
			
			return;
			
		}
		
		float xCur = xCursor;
	
		if(renderType == RenderType.HUD) {
			
			xCur = xCursor + game.getXCam();
			
		}
		
		xCur /= game.getWidthDiff();
		
		if(Mouse.isButtonDown(MouseButtons.LEFT_BUTTON)) {
			
			if(focus) {
				
				if(xCur < x + (padding / 2)) {
					
					xCur = x + (padding / 2);
					
				} else if(xCur > x + w - (padding / 2)) {
					
					xCur = x + w - (padding / 2);
					
				}
				
				value = (float) (xCur - x - (padding / 2)) / (float) (w - padding);
				
				if(steps > 0) {
				
					float stepSize = 1.0f / steps;
					
					float lastStep = (float) Math.floor(value / stepSize) * stepSize;
					float nextStep = lastStep + stepSize;
					
					if(value - lastStep <= nextStep - value) {
						
						value = lastStep;
						
					} else {
						
						value = nextStep;
						
					}
					
				}
				
			}
			
		}
		
		if(downTime > 0 && downTime <= System.nanoTime()) {
			
			downTime += 50000000;
			
			if(value > 0) {
				
				if(steps <= 0 && value >= 0.01f) {
					
					value -= 0.01f;
					
				} else {
				
					value -= (1.0f / steps);
				
				}
				
			}
			
		}
		
		if(upTime > 0 && upTime <= System.nanoTime()) {
			
			upTime += 50000000;
			
			if(steps <= 0 && value <= 0.99f) {
				
				value += 0.01f;
					
			} else if(value <= 1.0f - (1.0f / steps)){
				
				value += (1.0f / steps);
				
			}
			
		}
		
		value = Math.round(value * 100) / 100.0f;
		
		if(value != lastValue) {
			
			valueChanged();
			lastValue = value;
			
		}
		
	}
	
	public float getValue() {
		
		return value;
		
	}
	
	public void setValue(float value) {
		
		if(value < 0) {
			
			value = 0;
			
		}
		
		if(value > 1) {
			
			value = 1;
			
		}
		
		this.value = value;
		
	}
	
	public boolean isFocus() {
		
		return focus;
		
	}
	
	public void setFocus(boolean focus) {
		
		this.focus = focus;
		
	}
	
	public int getPadding() {
		
		return padding;
		
	}

	public void setPadding(int padding) {
		
		this.padding = padding;
		
	}

	public int getSteps() {
		
		return steps;
		
	}

	public void setSteps(int steps) {
		
		if(steps > 100) {
			
			steps = 100;
			
		}
		
		this.steps = steps;
			
	}

	public void valueChanged() {}
	
	public void focusGained() {}
	
	public void focusLost() {}

}
