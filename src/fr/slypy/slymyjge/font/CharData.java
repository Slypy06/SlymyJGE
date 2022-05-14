package fr.slypy.slymyjge.font;

public class CharData {

	public int width;
	public int height;
	public char character;
	
	public CharData(int width, int height, char character) {

		this.width = width;
		this.height = height;
		this.character = character;
		
	}

	public int getWidth() {
		
		return width;
		
	}
	
	public void setWidth(int width) {
		
		this.width = width;
		
	}
	
	public int getHeight() {
		
		return height;
		
	}
	
	public void setHeight(int height) {
		
		this.height = height;
		
	}
	
	public char getCharacter() {
		
		return character;
		
	}
	
	public void setCharacter(char character) {
		
		this.character = character;
		
	}
	
}
