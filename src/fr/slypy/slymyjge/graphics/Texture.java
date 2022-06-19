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
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

import net.sf.image4j.codec.ico.ICODecoder;
import net.sf.image4j.codec.ico.ICOImage;

public class Texture {
	
	int width;
	int height;
	int id;
	BufferedImage image;
	ByteBuffer byteBuffer;
	
	public Texture(int width, int height, int id, BufferedImage image, ByteBuffer byteBuffer) {
		
		this.width = width;
		this.height = height;
		this.id = id;
		this.image = image;
		this.byteBuffer = byteBuffer;
		
	}
	
	public static Texture loadTexture(String path) {
		
		return loadTexture(path, false);
		
	}
	
	public static Texture loadTexture(String path, boolean blackAndWhite) {
		
		BufferedImage image = null;
		
		try {

			image = ImageIO.read(Texture.class.getResource("/" + path));

		} catch (IOException e) {
			
			e.printStackTrace();
			
		}
		
		int w = image.getWidth();
		int h = image.getHeight();
		
		int[] pixels = new int[w * h];
		image.getRGB(0, 0, w, h, pixels, 0, w);
		
		ByteBuffer buffer = BufferUtils.createByteBuffer(w * h * 4);
		
		if(blackAndWhite) {
			
			for (int y = 0; y < h; y++) {
				
				for (int x = 0; x < w; x++) {
					
					int i = pixels[x + y * w];
					
					int color = (((i >> 16) & 0xFF) + ((i >> 8) & 0xFF) + ((i) & 0xFF)) / 3;
					
					buffer.put((byte) color);
					buffer.put((byte) color);
					buffer.put((byte) color);
					buffer.put((byte) ((i >> 24) & 0xFF));
					
				}
				
			}	
			
		} else {
		
			for (int y = 0; y < h; y++) {
				
				for (int x = 0; x < w; x++) {
					
					int i = pixels[x + y * w];
					
					
					buffer.put((byte) ((i >> 16) & 0xFF));
					buffer.put((byte) ((i >> 8) & 0xFF));
					buffer.put((byte) ((i) & 0xFF));
					buffer.put((byte) ((i >> 24) & 0xFF));
					
				}
				
			}
		
		}
		
		buffer.flip();
		
		int id = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, id);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		
		glBindTexture(GL_TEXTURE_2D, 0);
		
		return new Texture(w, h, id, image, buffer);
		
	}
	
	public static Texture loadTexture(BufferedImage image) {
		
		return loadTexture(image, false);
		
	}
	
	public static Texture loadTexture(BufferedImage image, boolean blackAndWhite) {
		
		int w = image.getWidth();
		int h = image.getHeight();
		
		int[] pixels = new int[w * h];
		image.getRGB(0, 0, w, h, pixels, 0, w);
		
		ByteBuffer buffer = BufferUtils.createByteBuffer(w * h * 4);
		
		if(blackAndWhite) {
			
			for (int y = 0; y < h; y++) {
				
				for (int x = 0; x < w; x++) {
					
					int i = pixels[x + y * w];
					
					int color = (((i >> 16) & 0xFF) + ((i >> 8) & 0xFF) + ((i) & 0xFF)) / 3;
					
					buffer.put((byte) color);
					buffer.put((byte) color);
					buffer.put((byte) color);
					buffer.put((byte) ((i >> 24) & 0xFF));
					
				}
				
			}	
			
		} else {
		
			for (int y = 0; y < h; y++) {
				
				for (int x = 0; x < w; x++) {
					
					int i = pixels[x + y * w];
					
					
					buffer.put((byte) ((i >> 16) & 0xFF));
					buffer.put((byte) ((i >> 8) & 0xFF));
					buffer.put((byte) ((i) & 0xFF));
					buffer.put((byte) ((i >> 24) & 0xFF));
					
				}
				
			}
		
		}
		
		buffer.flip();
		
		int id = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, id);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		
		glBindTexture(GL_TEXTURE_2D, 0);
		
		return new Texture(w, h, id, image, buffer);

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
	
	public Texture getTexturePart(int x, int y, int endX, int endY) {
		
		return getTexturePart(x, y, endX, endY, false);
		
	}
	
	public Texture getTexturePart(int x, int y, int w, int h, boolean blackAndWhite) {
		
		BufferedImage img = image.getSubimage(x, y, w, h);
		
		int[] pixels = new int[w * h];
		img.getRGB(0, 0, w, h, pixels, 0, w);
		
		ByteBuffer buffer = BufferUtils.createByteBuffer(w * h * 4);
		
		if(blackAndWhite) {
			
			for (int yPos = 0; yPos < h; yPos++) {
				
				for (int xPos = 0; xPos < w; xPos++) {
					
					int i = pixels[xPos + yPos * w];
					
					int color = (((i >> 16) & 0xFF) + ((i >> 8) & 0xFF) + ((i) & 0xFF)) / 3;
					
					buffer.put((byte) color);
					buffer.put((byte) color);
					buffer.put((byte) color);
					buffer.put((byte) ((i >> 24) & 0xFF));
					
				}
				
			}	
			
		} else {
		
			for (int yPos = 0; yPos < h; yPos++) {
				
				for (int xPos = 0; xPos < w; xPos++) {
					
					int i = pixels[xPos + yPos * w];
					
					buffer.put((byte) ((i >> 16) & 0xFF));
					buffer.put((byte) ((i >> 8) & 0xFF));
					buffer.put((byte) ((i) & 0xFF));
					buffer.put((byte) ((i >> 24) & 0xFF));
					
				}
				
			}
		
		}
		
		buffer.flip();
		
		int id = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, id);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		
		glBindTexture(GL_TEXTURE_2D, 0);
		
		return new Texture(w, h, id, img, buffer);
		
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
	        		
	        		if(ico.getWidth() == 16) {
	        			
	        			BufferedImage image = ico.getImage();
	        			
	        			int w = image.getWidth();
	        			int h = image.getHeight();
	        			
	        			int[] pixels = new int[w * h];
	        			image.getRGB(0, 0, w, h, pixels, 0, w);
	        			
	        			ByteBuffer buffer = BufferUtils.createByteBuffer(w * h * 4);

	        			for (int y = 0; y < h; y++) {
	        				
	        				for (int x = 0; x < w; x++) {
	        					
	        					int i = pixels[x + y * w];
	        					
	        					
	        					buffer.put((byte) ((i >> 16) & 0xFF));
	        					buffer.put((byte) ((i >> 8) & 0xFF));
	        					buffer.put((byte) ((i) & 0xFF));
	        					buffer.put((byte) ((i >> 24) & 0xFF));
	        					
	        				}
	        				
	        			}
	        			
	        			buffer.flip();
	        			
	        			icon.addIcon(IconResolution.X16, buffer);
	        			
	        		} else if(ico.getWidth() == 32) {
	        			
	        			BufferedImage image = ico.getImage();
	        			
	        			int w = image.getWidth();
	        			int h = image.getHeight();
	        			
	        			int[] pixels = new int[w * h];
	        			image.getRGB(0, 0, w, h, pixels, 0, w);
	        			
	        			ByteBuffer buffer = BufferUtils.createByteBuffer(w * h * 4);

	        			for (int y = 0; y < h; y++) {
	        				
	        				for (int x = 0; x < w; x++) {
	        					
	        					int i = pixels[x + y * w];
	        					
	        					
	        					buffer.put((byte) ((i >> 16) & 0xFF));
	        					buffer.put((byte) ((i >> 8) & 0xFF));
	        					buffer.put((byte) ((i) & 0xFF));
	        					buffer.put((byte) ((i >> 24) & 0xFF));
	        					
	        				}
	        				
	        			}
	        			
	        			buffer.flip();
	        			
	        			icon.addIcon(IconResolution.X32, buffer);
	        			
	        		} else if(ico.getWidth() == 128) {
	        			
	        			BufferedImage image = ico.getImage();
	        			
	        			int w = image.getWidth();
	        			int h = image.getHeight();
	        			
	        			int[] pixels = new int[w * h];
	        			image.getRGB(0, 0, w, h, pixels, 0, w);
	        			
	        			ByteBuffer buffer = BufferUtils.createByteBuffer(w * h * 4);

	        			for (int y = 0; y < h; y++) {
	        				
	        				for (int x = 0; x < w; x++) {
	        					
	        					int i = pixels[x + y * w];
	        					
	        					buffer.put((byte) ((i >> 16) & 0xFF));
	        					buffer.put((byte) ((i >> 8) & 0xFF));
	        					buffer.put((byte) ((i) & 0xFF));
	        					buffer.put((byte) ((i >> 24) & 0xFF));
	        					
	        				}
	        				
	        			}
	        			
	        			buffer.flip();
	        			
	        			icon.addIcon(IconResolution.X128, buffer);
	        			
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
