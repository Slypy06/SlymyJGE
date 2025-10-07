package fr.slypy.test.imagematching;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import org.davidmoten.hilbert.HilbertCurve;
import org.davidmoten.hilbert.SmallHilbertCurve;
import org.lwjgl.util.vector.Vector2f;

public class Hilbert {

	public static BufferedImage transform(BufferedImage from, BufferedImage to) {
		
		if(from.getWidth() != to.getWidth() || from.getHeight() != to.getHeight()) {
			
			return null;
			
		}
		
		BufferedImage output = new BufferedImage(from.getWidth(), from.getHeight(), from.getType());
		
		Map<Vector2f, Vector2f> transformMap = getTransformMap(from, to);
		
		for(Vector2f originPixel : transformMap.keySet()) {
			
			Vector2f goalPixel = transformMap.get(originPixel);
			
			output.setRGB((int) goalPixel.getX(), (int) goalPixel.getY(), from.getRGB((int) originPixel.getX(), (int) originPixel.getY()));
			
		}
		
		return output;
		
	}

	public static Map<Vector2f, Vector2f> getTransformMap(BufferedImage from, BufferedImage to) {
		
		if(from.getWidth() != to.getWidth() || from.getHeight() != to.getHeight()) {
			
			return null;
			
		}
		
		List<Entry<Vector2f, Long>> fromList = toHilbertCurveSorted(from);
		List<Entry<Vector2f, Long>> toList = toHilbertCurveSorted(to);
		
		Map<Vector2f, Vector2f> output = new HashMap<Vector2f, Vector2f>();
		
		for(int i = 0; i < fromList.size(); i++) {
			
			output.put(fromList.get(i).getKey(), toList.get(i).getKey());
			
		}
		
		return output;
		
	}
	
	private static List<Entry<Vector2f, Long>> toHilbertCurveSorted(BufferedImage img) {
		
		int noiseRange = 1000;
		Random r = new Random();
		
		SmallHilbertCurve smallHilbertCurve = HilbertCurve.small().bits(8).dimensions(3);
		
		List<Entry<Vector2f, Long>> output = new ArrayList<Entry<Vector2f, Long>>();
		
		for(int i = 0; i < img.getHeight(); i++) {
			
			for(int j = 0; j < img.getWidth(); j++) {
				
				Color c = new Color(img.getRGB(j, i));
				
				output.add(new AbstractMap.SimpleEntry<Vector2f, Long>(new Vector2f(j, i), (r.nextInt(2*noiseRange) - noiseRange) + smallHilbertCurve.index(c.getRed(), c.getGreen(), c.getBlue())));
				
			}
			
		}
		
		output.sort(Map.Entry.comparingByValue());
		
		return output;
		
	}
	
}
