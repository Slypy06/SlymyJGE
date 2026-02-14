package fr.slypy.slymyjge.graphics;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

import net.sf.image4j.codec.ico.ICODecoder;
import net.sf.image4j.codec.ico.ICOImage;

public class Texture {
	
	public static final BiFunction<Dimension, ByteBuffer, BiConsumer<Point, Integer>> DEFAULT = (dim, buffer) -> ((coord, rgb) -> {
		
		buffer.put((byte) ((rgb >> 16) & 0xFF));
		buffer.put((byte) ((rgb >> 8) & 0xFF));
		buffer.put((byte) ((rgb) & 0xFF));
		buffer.put((byte) ((rgb >> 24) & 0xFF));
		
	});
	
	public static final BiFunction<Dimension, ByteBuffer, BiConsumer<Point, Integer>> BLACK_AND_WHITE = (dim, buffer) -> ((coord, rgb) -> {
		
		int color = (((rgb >> 16) & 0xFF) + ((rgb >> 8) & 0xFF) + ((rgb) & 0xFF)) / 3;
		
		buffer.put((byte) color);
		buffer.put((byte) color);
		buffer.put((byte) color);
		buffer.put((byte) ((rgb >> 24) & 0xFF));
		
	});
	
	public static final Function<BufferedImage, BiFunction<Dimension, ByteBuffer, BiConsumer<Point, Integer>>> MASK = (originalImg) -> (targetSize, buffer) -> {

		BufferedImage scaled = new BufferedImage(targetSize.width, targetSize.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = scaled.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.drawImage(originalImg, 0, 0, targetSize.width, targetSize.height, null);
		g2d.dispose();

		return (coord, rgb) -> {
			
			int maskRgb = scaled.getRGB(coord.x, coord.y);

			int r = ((rgb >> 16) & 0xFF) * ((maskRgb >> 16) & 0xFF) / 255;
			int g = ((rgb >> 8) & 0xFF) * ((maskRgb >> 8) & 0xFF) / 255;
			int b = (rgb & 0xFF) * (maskRgb & 0xFF) / 255;
			int a = ((rgb >> 24) & 0xFF) * ((maskRgb >> 24) & 0xFF) / 255;

			buffer.put((byte) r);
			buffer.put((byte) g);
			buffer.put((byte) b);
			buffer.put((byte) a);
			
		};
		    
	};
	
	private final int width;
	private final int height;
	private final int id;
	private final BufferedImage image;
	private final ByteBuffer byteBuffer;
	
	public Texture(int width, int height, int id, BufferedImage image, ByteBuffer byteBuffer) {
		
		this.width = width;
		this.height = height;
		this.id = id;
		this.image = image;
		this.byteBuffer = byteBuffer;
		
	}
	
	public int getWidth() {
		
		return width;
		
	}
	
	public int getHeight() {
		
		return height;
		
	}
	
	public BufferedImage getImage() {
		
		return image;
		
	}
	
	public ByteBuffer getBuffer() {
		
		return byteBuffer;
		
	}
	
	public int getId() {
		
		return id;
		
	}
	
	public void bind() {
		
		glBindTexture(GL_TEXTURE_2D, id);
		
	}
	
	public void unbind() {
		
		glBindTexture(GL_TEXTURE_2D, 0);

	}
	
	public void free() {
		
		glDeleteTextures(id);
		
	}
	
	/**
	 * Returns a subregion of the current texture as a new {@link Texture} object.
	 * <p>
	 * This method extracts a rectangular portion of the texture defined by the specified
	 * coordinates and dimensions. The subregion starts at the top-left corner
	 * (<code>x</code>, <code>y</code>) and spans a width of <code>w</code> pixels
	 * and a height of <code>h</code> pixels. The resulting {@link Texture} will share
	 * the same texture data but represent only the selected area.
	 * </p>
	 *
	 * <p>
	 * This overload uses the default texture sampling or wrapping mode defined by
	 * {@link #DEFAULT}.
	 * </p>
	 *
	 * @param x the x-coordinate of the top-left corner of the subregion, in pixels
	 * @param y the y-coordinate of the top-left corner of the subregion, in pixels
	 * @param w the width of the subregion, in pixels
	 * @param h the height of the subregion, in pixels
	 * @return a new {@link Texture} representing the specified subregion of the current texture
	 */
	public Texture getTexturePart(int x, int y, int w, int h) {
		
		return getTexturePart(x, y, w, h, DEFAULT);
		
	}
	
	/**
	 * Extracts a portion of the current image and converts it into a texture.
	 *
	 * <p>This method allows you to specify a rectangular area within the image using
	 * the (x, y) coordinates for the top-left corner and the width (w) and height (h)
	 * of the area. It applies a custom pixel processing function provided via a
	 * {@link BiFunction} that generates a {@link BiConsumer} to handle each pixel's
	 * color data before creating the texture.</p>
	 *
	 * @param x the x-coordinate of the top-left corner of the texture part
	 * @param y the y-coordinate of the top-left corner of the texture part
	 * @param w the width of the texture part
	 * @param h the height of the texture part
	 * @param consummer a function that accepts the dimensions and a byte buffer, returning
	 *                  a consumer that processes each pixel's color data
	 * @return a {@link Texture} representing the specified portion of the image, including
	 *         its dimensions, pixel data, and texture ID
	 */
	public Texture getTexturePart(int x, int y, int w, int h, BiFunction<Dimension, ByteBuffer, BiConsumer<Point, Integer>> consummer) {
		
		BufferedImage img = image.getSubimage(x, y, w, h);
		
		int[] pixels = new int[w * h];
		img.getRGB(0, 0, w, h, pixels, 0, w);
		
		ByteBuffer buffer = BufferUtils.createByteBuffer(w * h * 4);
		
		BiConsumer<Point, Integer> pixelConsumer = consummer.apply(new Dimension(w, h), buffer);
			
		for (int yPos = 0; yPos < h; yPos++) {
				
			for (int xPos = 0; xPos < w; xPos++) {
					
				pixelConsumer.accept(new Point(xPos, yPos), pixels[xPos + yPos * w]);
					
			}
				
		}	
		
		buffer.flip();
		
		int id = loadTexture(buffer, w, h);
		
		return new Texture(w, h, id, img, buffer);
		
	}
	
	public static Texture loadTexture(String path) {
		
		return loadTexture(path, DEFAULT);
		
	}
	
	public static Texture loadTexture(String path, BiFunction<Dimension, ByteBuffer, BiConsumer<Point, Integer>> consumer) {
		
		BufferedImage image = null;
		
		try {

			image = ImageIO.read(Texture.class.getResource("/" + path));

		} catch (IOException e) {
			
			e.printStackTrace();
			return null;
			
		}
		
		return Texture.loadTexture(image, consumer);
		
	}
	
	public static Texture loadTexture(BufferedImage image) {
		
		return loadTexture(image, DEFAULT);
		
	}
	
	public static Texture loadTexture(BufferedImage img, BiFunction<Dimension, ByteBuffer, BiConsumer<Point, Integer>> consummer) {
		
		int w = img.getWidth();
		int h = img.getHeight();
		
		int[] pixels = new int[w * h];
		img.getRGB(0, 0, w, h, pixels, 0, w);
		
		ByteBuffer buffer = BufferUtils.createByteBuffer(w * h * 4);
		
		BiConsumer<Point, Integer> pixelConsumer = consummer.apply(new Dimension(w, h), buffer);
		
		for (int y = 0; y < h; y++) {
				
			for (int x = 0; x < w; x++) {
					
				pixelConsumer.accept(new Point(x, y), pixels[x + y * w]);
					
			}
				
		}
		
		buffer.flip();
		
		int id = loadTexture(buffer, w, h);
		
		return new Texture(w, h, id, img, buffer);
		
	}
	
	public static int loadTexture(ByteBuffer b, int w, int h) {
		
		int id = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, id);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, b);
		
		glBindTexture(GL_TEXTURE_2D, 0);
		
		return id;
		
	}
	
	public static Icon getIcon(String path) {

		if(!path.endsWith(".ico")) {
			
			return null;
			
		}
		
		InputStream inputStream = null;
		Icon icon = new Icon();
		
		try {
			
	        inputStream = Texture.class.getResource("/" + path).openStream();
	        
	        List<ICOImage> icons = ICODecoder.readExt(inputStream);
	        
	        for(ICOImage ico : icons) {
	        	
	        	if(ico.getWidth() == ico.getHeight()) {
	        		
	        		if(ico.getWidth() == 16 || ico.getWidth() == 32 || ico.getWidth() == 128) {
	        			
	        			BufferedImage image = ico.getImage();
	        			
	        			int w = image.getWidth();
	        			int h = image.getHeight();
	        			
	        			int[] pixels = new int[w * h];
	        			image.getRGB(0, 0, w, h, pixels, 0, w);
	        			
	        			ByteBuffer buffer = BufferUtils.createByteBuffer(w * h * 4);
	        			
	        			BiConsumer<Point, Integer> pixelConsumer = DEFAULT.apply(new Dimension(w, h), buffer);

	        			for (int y = 0; y < h; y++) {
	        				
	        				for (int x = 0; x < w; x++) {
	        					
	        					pixelConsumer.accept(new Point(x, y), pixels[x + y * w]);
	        					
	        				}
	        				
	        			}
	        			
	        			buffer.flip();
	        			
	        			icon.addIcon(switch(ico.getWidth()) {case 32 -> IconResolution.X32; case 128-> IconResolution.X128; default->IconResolution.X16; }, buffer);
	        			
	        		}
	        		
	        	}
	        	
	        }
	        
	        return icon;

		} catch (IOException e1) {

			e1.printStackTrace();
			
		}
		
		return null;
		
	}
	
}
