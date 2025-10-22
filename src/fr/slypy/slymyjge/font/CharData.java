package fr.slypy.slymyjge.font;

import fr.slypy.slymyjge.graphics.TexCoords;

public class CharData {

	public final int width;
	public final int visualWidth;
	public final int height;
	public final char character;
	public final int advance;
	public final TexCoords atlasCoord;
	
	public CharData(int width, int visualWidth, int advance, int height, char character, TexCoords atlasCoord) {

		this.width = width;
		this.visualWidth = visualWidth;
		this.advance = advance;
		this.height = height;
		this.character = character;
		this.atlasCoord = atlasCoord;
		
	}
	
	public CharData(int width, int height, char character, TexCoords atlasCoord) {

		this(width, width, 0, height, character, atlasCoord);
		
	}

	public int getWidth() {
		
		return width;
		
	}
	
	public int getVisualWidth() {
		
		return visualWidth;
		
	}
	
	public int getHeight() {
		
		return height;
		
	}
	
	public char getCharacter() {
		
		return character;
		
	}
	
	public int getAdvance() {
		
		return advance;
		
	}

	public TexCoords getAtlasCoord() {
		return atlasCoord;
	}
	
}
