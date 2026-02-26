package fr.slypy.slymyjge.components;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import fr.slypy.slymyjge.Game;
import fr.slypy.slymyjge.utils.MouseButtons;

public abstract class SliderComponent extends Component {
	
	public float value;
	public float lastValue;
	public int padding;
	public int steps;
	public boolean clickStartedInside = false;
	public long downTime;
	public long upTime;
	
	public SliderComponent(float x, float y, int w, int h, Game game) {
		
		super(x, y, w, h, game);
		
		padding = h;
		
		this.addMouseButtonToListen(MouseButtons.LEFT_BUTTON);
		this.addKeyToListen(Keyboard.KEY_DOWN);
		this.addKeyToListen(Keyboard.KEY_UP);

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
	public void componentUpdate() {
		
		if(!activated) {
			
			focus = false;
			
			return;
			
		}
		
		float xCur = absoluteCoordinate ? game.getAbsoluteXCursor() : game.getRelativeXCursor();
		
		if(Mouse.isButtonDown(MouseButtons.LEFT_BUTTON)) {
			
			if(focus) {
				
				if(xCur < position.getX() + (padding / 2)) {
					
					xCur = position.getX() + (padding / 2);
					
				} else if(xCur > position.getX() + size.getX() - (padding / 2)) {
					
					xCur = position.getX() + size.getX() - (padding / 2);
					
				}
				
				value = (float) (xCur - size.getX() - (padding / 2)) / (float) (size.getX() - padding);
				
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

}
