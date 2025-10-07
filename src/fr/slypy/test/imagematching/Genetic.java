package fr.slypy.test.imagematching;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.lwjgl.util.vector.Vector2f;

public class Genetic {
	
	public static BufferedImage transform(BufferedImage source, BufferedImage target, float spatialWeight, int iterations) {
		
	    int w = target.getWidth();
	    int h = target.getHeight();
	    BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	    
	    Map<Vector2f, Vector2f> assignment = geneticAssignment(source, target, spatialWeight, iterations);

	    for (Map.Entry<Vector2f, Vector2f> e : assignment.entrySet()) {
	        Vector2f srcPos = e.getKey();
	        Vector2f tgtPos = e.getValue();

	        int color = source.getRGB((int) srcPos.x, (int) srcPos.y);
	        out.setRGB((int) tgtPos.x, (int) tgtPos.y, color);
	    }

	    return out;
		
	}
	
	public static BufferedImage applyTransformMap(BufferedImage source, Map<Vector2f, Vector2f> transformMap) {
		
	    int w = source.getWidth();
	    int h = source.getHeight();
	    BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

	    for (Map.Entry<Vector2f, Vector2f> e : transformMap.entrySet()) {
	        Vector2f srcPos = e.getKey();
	        Vector2f tgtPos = e.getValue();

	        int color = source.getRGB((int) srcPos.x, (int) srcPos.y);
	        out.setRGB((int) tgtPos.x, (int) tgtPos.y, color);
	    }

	    return out;
		
	}

    public static Map<Vector2f, Vector2f> geneticAssignment(BufferedImage source, BufferedImage target, float spatialWeight, int iterations) {
    	
        Pixel[] srcPixels = ImageMatcher.extractPixels(source);
        Pixel[] tgtPixels = ImageMatcher.extractPixels(target);
        int w = source.getWidth(), h = source.getHeight();
        int n = tgtPixels.length;

        if (srcPixels.length != n)
            throw new IllegalArgumentException("Source and target must have same number of pixels");

        int[] assignment = new int[n];
        for (int i = 0; i < n; i++) assignment[i] = i; // initial identity

        ThreadLocalRandom rng = ThreadLocalRandom.current();
        int maxDist = w;

        boolean improved = true;
        
        while (improved) {
        	
            improved = false;
            
            for (int iter = 0; iter < iterations; iter++) {
            	
                int a = rng.nextInt(n);
                int ax = a%w, ay = a/w;
                int bx = Math.min(w - 1, Math.max(0, ax + rng.nextInt(2 * maxDist + 1) - maxDist));
                int by = Math.min(h - 1, Math.max(0, ay + rng.nextInt(2 * maxDist + 1) - maxDist));
                int b = by * w + bx;

                int srcA = assignment[a], srcB = assignment[b];
                int costA = ImageMatcher.heuristic(srcPixels[srcA], tgtPixels[a], spatialWeight);
                int costB = ImageMatcher.heuristic(srcPixels[srcB], tgtPixels[b], spatialWeight);
                int swapCostA = ImageMatcher.heuristic(srcPixels[srcB], tgtPixels[a], spatialWeight);
                int swapCostB = ImageMatcher.heuristic(srcPixels[srcA], tgtPixels[b], spatialWeight);

                if (swapCostA + swapCostB < costA + costB) {
                	
                    assignment[a] = srcB;
                    assignment[b] = srcA;
                    improved = true;
                    
                }
                
            }
            
            maxDist = Math.max(2, (int)(maxDist * 0.99));
            
        }
        
        System.out.println("[" + Thread.currentThread().getId() + "] Genetics done");
        
        Map<Vector2f, Vector2f> map = new HashMap<>();
        
        for (int i = 0; i < assignment.length; i++) {
        	
            int tgtX = i % w, tgtY = i / w;
            int srcIndex = assignment[i];
            int srcX = srcIndex % w, srcY = srcIndex / w;
            map.put(new Vector2f(srcX, srcY), new Vector2f(tgtX, tgtY));
            
        }
        
        return map;
        
    }
    
}