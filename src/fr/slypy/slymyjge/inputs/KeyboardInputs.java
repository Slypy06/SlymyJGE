package fr.slypy.slymyjge.inputs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.input.Keyboard;

public class KeyboardInputs extends MouseInputs {
	
	public int escapeGameKey = -1;

	private HashMap<Integer, Integer> keys = new HashMap<Integer, Integer>();
	
	private static List<KeyboardInputs> instances = new ArrayList<KeyboardInputs>();
	
	public KeyboardInputs() {
		
		instances.add(this);
		
	}
	
	public void addKeyToListen(int key) {
		
		keys.put(key, 0);
		
	}
	
	public void addKeysToListen(int[] keys) {
		
		for(int key : keys) {
			
			addKeyToListen(key);
			
		}
		
	}
	
	public void keyUpdate() {
		
		mouseUpdate();
		
		for(int key : keys.keySet()) {
			
			if(Keyboard.isKeyDown(key)) {
				
				if(keys.get(key) == 0) {
				
					keys.replace(key, 1);
					keyPressed(key);
				
				}
				
			} else if(keys.get(key) == 1) {
				
				keyReleased(key);
				keys.replace(key, 0);
				
			}
			
		}
		
		while(Keyboard.next()) {
			
			for(KeyboardInputs instance : instances) {
				
				instance.keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKeyState());
				
			}
			
		}
		
	}
	
	public void keyPressed(int key) {}
	
	public void keyReleased(int key) {}

	public boolean isCloseRequested() {

		return escapeGameKey != -1 && Keyboard.isKeyDown(escapeGameKey);
		
	}
	
	public void keyTyped(char key, boolean eventState) {}
	
}
