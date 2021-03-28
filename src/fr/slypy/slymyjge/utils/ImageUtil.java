package fr.slypy.slymyjge.utils;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ImageUtil {

	public static BufferedImage replaceColors(Color before, Color after, BufferedImage image) {
		
		int w = image.getWidth();
		int h = image.getHeight();
		
		for(int i = 0; i < w; i++) {
			
			for(int j = 0; j < h; j++) {
				
				if(image.getRGB(i, j) == before.getRGB()) {
					
					image.setRGB(i, j, after.getRGB());
					
				}
				
			}
			
		}
		
		return image;
		
	}
	
}
