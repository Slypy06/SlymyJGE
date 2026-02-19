package fr.slypy.slymyjge.audio;

import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

import fr.slypy.slymyjge.graphics.Texture;

public class AudioMaster {
	
	protected static ArrayList<Integer> buffers = new ArrayList<Integer>();

	public static void init() {
		
		try {
			
			AL.create();
			
		} catch (LWJGLException e) {

			e.printStackTrace();
			
		}
		
	}
	
	public static void setListenerPosition(float x, float y, float z) {

		AL10.alListener3f(AL10.AL_POSITION, x, y, z);
		
	}
	
	public static void setListenerVelocity(float x, float y, float z) {

		AL10.alListener3f(AL10.AL_VELOCITY, x, y, z);
		
	}
	
	public static int loadSound(String path) {
		
		int buffer = AL10.alGenBuffers();
		
		buffers.add(buffer);
		
		WaveData wavData = WaveData.create(Texture.class.getResource("/" + path));
		
		AL10.alBufferData(buffer, wavData.format, wavData.data, wavData.samplerate);
		
		wavData.dispose();
		
		return buffer;
		
	}
	
	public static void cleanUp() {
		
		for(int buffer : buffers) {
			
			AL10.alDeleteBuffers(buffer);
			
		}
		
		AL.destroy();
		
	}
	
}
