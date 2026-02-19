package fr.slypy.slymyjge.graphics.shape;

import fr.slypy.slymyjge.graphics.TexCoords;

public interface TexturedShape extends Shape {
	
	public int getTexture();
	public TexCoords getTexCoords();

}
