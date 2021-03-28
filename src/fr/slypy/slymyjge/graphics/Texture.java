package fr.slypy.slymyjge.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;

public class Texture {
	
	int width;
	int height;
	int id;
	BufferedImage image;
	
	public Texture(int width, int height, int id, BufferedImage image) {
		
		this.width = width;
		this.height = height;
		this.id = id;
		this.image = image;
		
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
		
		return new Texture(w, h, id, image);
		
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
		
		return new Texture(w, h, id, image);
		
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
		
		return new Texture(w, h, id, img);
		
	}
	
}
