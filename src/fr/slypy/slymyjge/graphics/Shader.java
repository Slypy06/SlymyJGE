package fr.slypy.slymyjge.graphics;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUniform2i;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniform3i;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUniform4i;
import static org.lwjgl.opengl.GL20.glUniformMatrix2;
import static org.lwjgl.opengl.GL20.glUniformMatrix3;
import static org.lwjgl.opengl.GL20.glUniformMatrix4;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix2f;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;

public class Shader {

	public static final int FRAGMENT_SHADER_TYPE = GL_FRAGMENT_SHADER;
	public static final int VERTEX_SHADER_TYPE = GL_VERTEX_SHADER;
	
	public static final String DEFAULT_VERTEX_SHADER = 
			"// vertex.glsl\n" 
			+ "#version 120\n"
			+ "void main() {\n"
			+ "		gl_TexCoord[0] = gl_MultiTexCoord0;\n"
			+ "    	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;\n"
			+ "}";

	private int programId;
	private int vertexShaderId = -1;
	private int fragmentShaderId = -1;
	private String vertexShader = DEFAULT_VERTEX_SHADER;
	private String fragmentShader = "";
	private Map<String, Object> uniforms = new HashMap<>();
	private Map<String, Integer> uniformsLocation = new HashMap<>();

	public Shader() {

		this.programId = glCreateProgram();

	}

	public boolean setVertexShader(String shader) {

		vertexShader = shader;

		if (vertexShaderId != -1) {

			glDetachShader(programId, vertexShaderId);
			glDeleteShader(vertexShaderId);
			
		}

		vertexShaderId = loadShader(shader, GL_VERTEX_SHADER);

		return vertexShaderId != -1;

	}

	public boolean setFragmentShader(String shader) {

		fragmentShader = shader;

		if (fragmentShaderId != -1) {

			glDetachShader(programId, fragmentShaderId);
			glDeleteShader(fragmentShaderId);

		}

		fragmentShaderId = loadShader(shader, GL_FRAGMENT_SHADER);

		return fragmentShaderId != -1;

	}

	public void attachShaders() {
		
		if (vertexShaderId == -1) {

			vertexShaderId = loadShader(vertexShader, GL_VERTEX_SHADER);

			if (vertexShaderId == -1) {

				throw new RuntimeException("Unexpected error loading default shader");

			}

		}
		
		glAttachShader(programId, vertexShaderId);

		if (fragmentShaderId != -1)
			glAttachShader(programId, fragmentShaderId);

		glLinkProgram(programId);
		glValidateProgram(programId);
		
		uniformsLocation.clear();
		
		for(String key : uniforms.keySet()) {
			
			uniformsLocation.put(key, glGetUniformLocation(programId, key));
			
		}

	}
	
	public void setUniformValue(String uniform, Object value) {
		
		if(!(value instanceof Integer) && !(value instanceof int[]) && !(value instanceof Float) && !(value instanceof float[]) && !(value instanceof Matrix2f) && !(value instanceof Matrix3f) && !(value instanceof Matrix4f))
			return;
		
		uniforms.put(uniform, value);
		
	}

	public void start() {

		glUseProgram(programId);
		
		for(Entry<String, Object> uniform : uniforms.entrySet()) {
			
			if(!uniformsLocation.containsKey(uniform.getKey())) {
				
				uniformsLocation.put(uniform.getKey(), glGetUniformLocation(programId, uniform.getKey()));
				
			}
			
			int i = uniformsLocation.get(uniform.getKey());
			
			if(i != -1) {
				
				if(uniform.getValue() instanceof Integer) {
					
					glUniform1i(i, ((Integer) uniform.getValue()).intValue());
					
				} else if(uniform.getValue() instanceof Float) {

					glUniform1f(i, ((Float) uniform.getValue()).floatValue());
					
				} else if(uniform.getValue() instanceof Matrix2f) {
					
				    FloatBuffer buf = BufferUtils.createFloatBuffer(4);
				    ((Matrix2f) uniform.getValue()).store(buf);
				    buf.flip();
				    glUniformMatrix2(i, false, buf);
				    
				} else if(uniform.getValue() instanceof Matrix3f) {
					
				    FloatBuffer buf = BufferUtils.createFloatBuffer(9);
				    ((Matrix3f) uniform.getValue()).store(buf);
				    buf.flip();
				    glUniformMatrix3(i, false, buf);
				    
				} else if(uniform.getValue() instanceof Matrix4f) {
					
				    FloatBuffer buf = BufferUtils.createFloatBuffer(16);
				    ((Matrix4f) uniform.getValue()).store(buf);
				    buf.flip();
				    glUniformMatrix4(i, false, buf);
				    
				} else if(uniform.getValue() instanceof int[]) {
					
					int[] array = (int[]) uniform.getValue();
					
					switch(array.length) {
					
						case 2:
							glUniform2i(i, array[0], array[1]);
							break;
						case 3:
							glUniform3i(i, array[0], array[1], array[2]);
							break;
						case 4:
							glUniform4i(i, array[0], array[1], array[2], array[3]);
							break;
					
					}
					
				} else if(uniform.getValue() instanceof float[]) {
					
					float[] array = (float[]) uniform.getValue();
					
					switch(array.length) {
					
						case 2:
							glUniform2f(i, array[0], array[1]);
							break;
						case 3:
							glUniform3f(i, array[0], array[1], array[2]);
							break;
						case 4:
							glUniform4f(i, array[0], array[1], array[2], array[3]);
							break;
							
					}
					
				}
				
			}
			
		}

	}

	public void stop() {

		glUseProgram(0);

	}

	public void destroy() {

		glDetachShader(programId, vertexShaderId);
		glDetachShader(programId, fragmentShaderId);
		glDeleteShader(vertexShaderId);
		glDeleteShader(fragmentShaderId);
		glDeleteProgram(programId);

	}

	public int getUniformLocation(String name) {

		return glGetUniformLocation(programId, name);

	}

	public int getProgramId() {
		return programId;
	}

	public int getVertexShaderId() {
		
		return vertexShaderId;
		
	}

	public int getFragmentShaderId() {
		
		return fragmentShaderId;
		
	}

	public String getVertexShader() {
		
		return vertexShader;
		
	}

	public String getFragmentShader() {
		
		return fragmentShader;
		
	}

	private static int loadShader(String source, int type) {

		int shaderID = glCreateShader(type);
		glShaderSource(shaderID, source);
		glCompileShader(shaderID);

		if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {

			System.err.println("Shader compilation error:");
			System.err.println(glGetShaderInfoLog(shaderID, 512));
			glDeleteShader(shaderID);
			return -1;

		}

		return shaderID;

	}
	
	public static String readShader(String shaderPath) {
		
	    try (BufferedReader reader = new BufferedReader(new InputStreamReader(Shader.class.getResource("/"+shaderPath).openStream()))) {
	        StringBuilder sb = new StringBuilder();
	        String line;
	        while ((line = reader.readLine()) != null) {
	            sb.append(line).append("\n");
	        }
	        return sb.toString();
	    } catch(IOException e) {
	    	
	    	return "";
	    	
	    }
	    
	}

}
