package fr.slypy.slymyjge.utils;

import org.lwjgl.util.vector.Vector2f;

public interface ResizingRules {
	
	public static ResizingRules DO_NOTHING = (size, originalSize) -> new TriEntry<Vector2f, Vector2f, Vector2f>(
																		size,
																		new Vector2f(0, 0), 
																		size
																	);
	
	public static ResizingRules CENTERED_CLIPPED = (size, originalSize) -> new TriEntry<Vector2f, Vector2f, Vector2f>(
																		originalSize,
																		new Vector2f((size.getX() - originalSize.getX()) / 2, (size.getY() - originalSize.getY()) / 2), 
																		originalSize
																	);
	
	public static ResizingRules TOPLEFT_CLIPPED = (size, originalSize) -> new TriEntry<Vector2f, Vector2f, Vector2f>(
																		originalSize,
																		new Vector2f(0, 0), 
																		originalSize
																	);
	
	public static ResizingRules ORIGINAL_RESIZED = (size, originalSize) -> new TriEntry<Vector2f, Vector2f, Vector2f>(
																		originalSize,
																		new Vector2f(0, 0), 
																		size
																	);
	
	public static ResizingRules WIDTH_RESIZED_HEIGHT_CENTERED = (size, originalSize) -> new TriEntry<Vector2f, Vector2f, Vector2f>(
																		originalSize,
																		new Vector2f(0, (size.getY() - originalSize.getY()) / 2), 
																		new Vector2f(size.getX(), originalSize.getY())
																	);
	
	public static ResizingRules WIDTH_RESIZED = (size, originalSize) -> new TriEntry<Vector2f, Vector2f, Vector2f>(
																		originalSize,
																		new Vector2f(0, 0), 
																		new Vector2f(size.getX(), originalSize.getY())
																	);
	
	public static ResizingRules HEIGHT_RESIZED_WIDTH_CENTERED = (size, originalSize) -> new TriEntry<Vector2f, Vector2f, Vector2f>(
																		originalSize,
																		new Vector2f((size.getX() - originalSize.getX()) / 2, 0), 
																		new Vector2f(originalSize.getX(), size.getY())
																	);

	public static ResizingRules HEIGHT_RESIZED = (size, originalSize) -> new TriEntry<Vector2f, Vector2f, Vector2f>(
																		originalSize,
																		new Vector2f(0, 0), 
																		new Vector2f(originalSize.getX(), size.getY())
																	);
	
	public static ResizingRules ASPECT_RATIO_CONSERVED_CENTERED = (size, originalSize) -> {

	    float targetAspect = originalSize.getX() / originalSize.getY();
	    float currentAspect = size.getX() / size.getY();

	    float width;
	    float height;

	    if (currentAspect > targetAspect) {
	        height = size.getY();
	        width = height * targetAspect;
	    } else {
	        width = size.getX();
	        height = width / targetAspect;
	    }

	    Vector2f viewportSize = new Vector2f(width, height);
	    Vector2f viewportPos = new Vector2f(
	            (size.getX() - width) / 2f,
	            (size.getY() - height) / 2f
	    );

	    return new TriEntry<>(
	            originalSize,
	            viewportPos,
	            viewportSize
	    );
	    
	};
	
	public static ResizingRules ASPECT_RATIO_CONSERVED = (size, originalSize) -> {

	    float targetAspect = originalSize.getX() / originalSize.getY();
	    float currentAspect = size.getX() / size.getY();

	    float width;
	    float height;

	    if (currentAspect > targetAspect) {
	        height = size.getY();
	        width = height * targetAspect;
	    } else {
	        width = size.getX();
	        height = width / targetAspect;
	    }

	    Vector2f viewportSize = new Vector2f(width, height);

	    // IMPORTANT:
	    // Because glViewport uses bottom-left origin,
	    // top-left anchoring requires this conversion:

	    Vector2f viewportPos = new Vector2f(
	            0f,
	            size.getY() - height
	    );

	    return new TriEntry<>(
	            originalSize,
	            viewportPos,
	            viewportSize
	    );
	    
	};
	
	public TriEntry<Vector2f, Vector2f, Vector2f> getView(Vector2f windowSize, Vector2f originalSize);
	
}
