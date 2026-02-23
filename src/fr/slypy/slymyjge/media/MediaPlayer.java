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
import static org.lwjgl.opengl.GL11.glTexSubImage2D;
import static org.lwjgl.opengl.GL12.GL_BGRA;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

import java.awt.Color;
import java.nio.ByteBuffer;

import org.lwjgl.util.vector.Vector2f;

import fr.slypy.slymyjge.Game;
import fr.slypy.slymyjge.graphics.TexCoords;
import fr.slypy.slymyjge.graphics.shape.Shape;
import fr.slypy.slymyjge.graphics.shape.TexturedRectangle;
import fr.slypy.slymyjge.utils.Logger;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.AudioApi;
import uk.co.caprica.vlcj.player.base.ControlsApi;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.base.State;
import uk.co.caprica.vlcj.player.base.VideoApi;
import uk.co.caprica.vlcj.player.component.CallbackMediaListPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormat;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormatCallback;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.RenderCallback;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.format.RV32BufferFormat;

public class MediaPlayer {

	private CallbackMediaListPlayerComponent mediaPlayerComponent;
	private boolean playing;
	private int id;
	private ByteBuffer byteBuffer;
	private int width;
	private int height;
	private String media;

	public MediaPlayer(String filename, Game g) {
		
		//System.setProperty("jna.library.path", "C:\\Program Files\\VideoLAN\\VLC");
		
		media = filename;

		mediaPlayerComponent = new CallbackMediaListPlayerComponent(new MediaPlayerFactory(
			    "--vout=vmem",
			    "--no-video-title-show",
			    "--quiet"
			), null, null, true, null, new RenderCallback() {
			
			@Override
			public void display(uk.co.caprica.vlcj.player.base.MediaPlayer mp, ByteBuffer[] bb, BufferFormat bf) {
				
				byteBuffer = bb[0];
				
				if(bf.getWidth() != width || bf.getHeight() != height) {
					
					Logger.log("INFO Media resolution changed, reallocating buffer");
					width = bf.getWidth();
					height = bf.getHeight();
					
					g.executeInRenderThread(() -> {
						
						glBindTexture(GL_TEXTURE_2D, id);
						
						glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
						glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
							
						glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
						glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
							
						glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_BGRA, GL_UNSIGNED_BYTE, byteBuffer);
						
						glBindTexture(GL_TEXTURE_2D, 0);
						
					});
					
				} else {
					
					g.executeInRenderThread(() -> {
						
						glBindTexture(GL_TEXTURE_2D, id);
					
						glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, width, height, GL_BGRA, GL_UNSIGNED_BYTE, byteBuffer);
						
						glBindTexture(GL_TEXTURE_2D, 0);
						
					});
					
				}
				
				if(playing && (mediaPlayerComponent.mediaPlayer().status().state() == State.ENDED || mediaPlayerComponent.mediaPlayer().status().state() == State.PAUSED || mediaPlayerComponent.mediaPlayer().status().state() == State.STOPPED || mediaPlayerComponent.mediaPlayer().status().state() == State.ERROR)) {
					
					playing = false;

				}
				
			}
			
		}, new BufferFormatCallback() {
			
			@Override
			public BufferFormat getBufferFormat(int w, int h) {
				
				return new RV32BufferFormat(w, h);
				
			}

			@Override
			public void allocatedBuffers(ByteBuffer[] arg0) {}
			
		}, null);
		
		id = glGenTextures();
		
		mediaPlayerComponent.mediaPlayer().media().startPaused("resources/" + media);

	}
	
	public Shape getShape(int x, int y, int w, int h) {
		
		return getShape(new Vector2f(x, y), new Vector2f(w, h));
		
	}
	
	public Shape getShape(Vector2f position, Vector2f size) {
		
		return new TexturedRectangle(position.getX(), position.getY(), size.getX(), size.getY(), id, Color.white, TexCoords.QUAD_DEFAULT_COORDS);
		
	}
	
	public void play() {
		
		playing = true;
		mediaPlayerComponent.mediaPlayer().controls().play();
		
	}
	
	public void pause() {
		
		playing = false;
		
		mediaPlayerComponent.mediaPlayer().controls().pause();
		
	}
	
	public boolean isPlaying() {
		
		return playing;
		
	}
	
	public State getState() {
		
		return mediaPlayerComponent.mediaPlayer().status().state();
		
	}
	
	public EmbeddedMediaPlayer getMediaPlayer() {
		
		return mediaPlayerComponent.mediaPlayer();
		
	}
	
	public AudioApi getAudioHandler() {
		
		return mediaPlayerComponent.mediaPlayer().audio();
		
	}
	
	public VideoApi getVideoHandler() {
		
		return mediaPlayerComponent.mediaPlayer().video();
		
	}
	
	public ControlsApi getControls() {
		
		return mediaPlayerComponent.mediaPlayer().controls();
		
	}
	
	public void muteOnStart(boolean mute) {
		
		mediaPlayerComponent.mediaPlayer().events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
			
			@Override
			public void timeChanged(uk.co.caprica.vlcj.player.base.MediaPlayer p, long time) {
				
				p.audio().setMute(mute);
				p.events().removeMediaPlayerEventListener(this);
				
			}
			
		});
		
	}
	
	public void setVolumeOnStart(int volume) {
		
		mediaPlayerComponent.mediaPlayer().events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
			
			@Override
			public void timeChanged(uk.co.caprica.vlcj.player.base.MediaPlayer p, long time) {
				
				p.audio().setVolume(volume);
				p.events().removeMediaPlayerEventListener(this);
				
			}
			
		});
		
	}
	
	public void setMedia(String newMedia) {
		
		this.media = newMedia;
		
		mediaPlayerComponent.mediaPlayer().controls().stop();
		mediaPlayerComponent.mediaPlayer().media().startPaused("resources/" + media);
		
	}
	
	public void addEventHandler(MediaPlayerEventAdapter handler) {
		
		mediaPlayerComponent.mediaPlayer().events().addMediaPlayerEventListener(handler);
		
	}
	
	public void destroy() {
		
		mediaPlayerComponent.release();
		
		glDeleteTextures(id);
		
	}
	
}
