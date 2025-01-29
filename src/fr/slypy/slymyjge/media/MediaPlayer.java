package fr.slypy.slymyjge.media;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.GL_BGRA;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

import java.awt.Color;
import java.nio.ByteBuffer;

import fr.slypy.slymyjge.graphics.Renderer;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.State;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormat;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormatCallback;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.RenderCallback;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.format.RV32BufferFormat;

public class MediaPlayer {
	
	private MediaPlayerFactory factory;
	private EmbeddedMediaPlayer mediaPlayer;
	private boolean playing;
	private int id;
	private ByteBuffer byteBuffer;
	private int width;
	private int height;
	private String media;
	
	public MediaPlayer(String filename) {
		
		media = filename;
		
		factory = new MediaPlayerFactory();
		mediaPlayer = factory.mediaPlayers().newEmbeddedMediaPlayer();
		
		mediaPlayer.videoSurface().set(factory.videoSurfaces().newVideoSurface(new BufferFormatCallback() {
			
			@Override
			public BufferFormat getBufferFormat(int w, int h) {
				
				return new RV32BufferFormat(w, h);
				
			}
			
			@Override
			public void allocatedBuffers(ByteBuffer[] bb) {
				
				
				
			}
			
		}, new RenderCallback() {
			
			@Override
			public void display(uk.co.caprica.vlcj.player.base.MediaPlayer mp, ByteBuffer[] bb, BufferFormat bf) {
				
				byteBuffer = bb[0];
				width = bf.getWidth();
				height = bf.getHeight();
				
			}
			
		}, false));
		
		mediaPlayer.media().startPaused("resources/" + media);

	}
	
	public void render(int x, int y, int w, int h, Color c) {
		
		if(playing && (mediaPlayer.status().state() == State.ENDED || mediaPlayer.status().state() == State.PAUSED || mediaPlayer.status().state() == State.STOPPED || mediaPlayer.status().state() == State.ERROR)) {
				
			playing = false;

		}
		
		if(byteBuffer != null) {
				
			glDeleteTextures(id);
				
			id = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, id);
				
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
				
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
				
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_BGRA, GL_UNSIGNED_BYTE, byteBuffer);
				
			glBindTexture(GL_TEXTURE_2D, 0);
	
			Renderer.renderTexturedQuad(x, y, w, h, id, c);	
				
		}
		
	}
	
	public void render(int x, int y, int w, int h) {
		
		render(x, y, w, h, Color.white);
		
	}
	
	public void play() {
		
		playing = true;
		mediaPlayer.controls().play();
		
	}
	
	public void pause() {
		
		playing = false;
		
		mediaPlayer.controls().pause();
		
	}
	
	public boolean isPlaying() {
		
		return playing;
		
	}
	
	public State getState() {
		
		return mediaPlayer.status().state();
		
	}
	
	public EmbeddedMediaPlayer getMediaPlayer() {
		
		return mediaPlayer;
		
	}
	
}
