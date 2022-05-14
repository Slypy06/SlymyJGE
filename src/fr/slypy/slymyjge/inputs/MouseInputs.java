package fr.slypy.slymyjge.inputs;

import java.util.HashMap;

import org.lwjgl.input.Mouse;

public class MouseInputs {

	private HashMap<Integer, Integer> buttons = new HashMap<Integer, Integer>();
	
	public void addMouseButtonToListen(int button) {
		
		buttons.put(button, 0);
		
	}
	
	public void addMouseButtonsToListen(int[] buttons) {
		
		for(int button : buttons) {
			
			addMouseButtonToListen(button);
			
		}
		
	}
	
	public void mouseUpdate() {
		
		for(int button : buttons.keySet()) {
			
			if(Mouse.isButtonDown(button)) {
				
				if(buttons.get(button) == 0) {
				
					buttons.replace(button, 1);
					mouseButtonPressed(button);
				
				}
				
			} else if(buttons.get(button) == 1) {
				
				mouseButtonReleased(button);
				buttons.replace(button, 0);
				
			}
			
		}
		
	}
	
	public void mouseButtonPressed(int button) {}
	
	public void mouseButtonReleased(int button) {}
	
}
