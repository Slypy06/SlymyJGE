package fr.slypy.test.imagematching;

import java.awt.image.BufferedImage;

public class ImageMatcher {

    // Flatten BufferedImage into array of Pixels
    public static Pixel[] extractPixels(BufferedImage img) {
    	
        int w = img.getWidth(), h = img.getHeight();
        Pixel[] pixels = new Pixel[w * h];
        int idx = 0;
        
        for (int y = 0; y < h; y++) {
        	
            for (int x = 0; x < w; x++) {
            	
                int rgb = img.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                pixels[idx++] = new Pixel(r, g, b, x, y);
                
            }
            
        }
        
        return pixels;
        
    }

    // Heuristic cost between source pixel and target position
    public static int heuristic(Pixel src, Pixel tgt, float spatialWeight) {
    	
    	//return 0;
    	
        int dr = src.r - tgt.r;
        int dg = src.g - tgt.g;
        int db = src.b - tgt.b;
        int color = dr * dr + dg * dg + db * db;

        int dx = src.x - tgt.x;
        int dy = src.y - tgt.y;
        int spatial = dx * dx + dy * dy;

        return color + (int) (spatialWeight * spatial);
        
    }
    
}


class Pixel {
	
    int r, g, b;
    int x, y;
    
    Pixel(int r, int g, int b, int x, int y) {
    	
        this.r = r; this.g = g; this.b = b; this.x = x; this.y = y;
        
    }
    
}