package fr.slypy.test;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector2f;

import fr.slypy.slymyjge.Game;
import fr.slypy.slymyjge.animations.dynamic.AnimationTrack;
import fr.slypy.slymyjge.animations.dynamic.AnimationTrack.KeyFrame;
import fr.slypy.slymyjge.animations.dynamic.Animator;
import fr.slypy.slymyjge.animations.framed.Animation;
import fr.slypy.slymyjge.animations.framed.AnimationFrame;
import fr.slypy.slymyjge.font.SlymyFont;
import fr.slypy.slymyjge.graphics.NewGenRenderer;
import fr.slypy.slymyjge.graphics.Shader;
import fr.slypy.slymyjge.graphics.Surface;
import fr.slypy.slymyjge.graphics.TexCoords;
import fr.slypy.slymyjge.graphics.Texture;
import fr.slypy.slymyjge.graphics.shape.EmptyShape;
import fr.slypy.slymyjge.graphics.shape.Line;
import fr.slypy.slymyjge.graphics.shape.Rectangle;
import fr.slypy.slymyjge.graphics.shape.Shape;
import fr.slypy.slymyjge.graphics.shape.TexturedQuad;
import fr.slypy.slymyjge.graphics.shape.TexturedRectangle;
import fr.slypy.slymyjge.graphics.shape.composite.CircleBorder;
import fr.slypy.slymyjge.graphics.shape.composite.TexturedCircle;
import fr.slypy.slymyjge.graphics.shape.dynamic.DynamicText;
import fr.slypy.slymyjge.graphics.shape.dynamic.StaticText;
import fr.slypy.slymyjge.media.MediaPlayer;
import fr.slypy.slymyjge.utils.ResizingRules;

public class SurfaceTest extends Game {
	
	private DynamicText text;
	private StaticText text2;
	private Animator anim;
	private Animator anim2;
	private Animator anim3;
	private TexturedCircle circle;
	private CircleBorder border;
	private Texture tex;
	private MediaPlayer player;
	private Animation cursorAnimation;
	private Shape videoQuad;
	private Surface sur;
	private Shader s;

	public SurfaceTest(int width, int height, String title, Color backgroundColor, boolean resizable) {
		
		super(width, height, title, backgroundColor, resizable);
		
	}
	
	
	public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		Game.setNativesLocation(new File("lib/natives").getAbsolutePath());
		
		new SurfaceTest(1920, 1080, "Test", Color.cyan, true).start();
		
	}

	
	
	@Override
	public void stop() {
		
	}

	@Override
	public void render(double alpha) {

		NewGenRenderer.renderOnSurface(() -> {
			
			NewGenRenderer.renderShape(new Rectangle(0, 0, sur.getWidth(), sur.getHeight(), Color.pink));
			
			NewGenRenderer.renderShape(new Rectangle(50, 50, sur.getWidth()-100, 50, Color.white));
			
			NewGenRenderer.renderText(text, new Vector2f(150, 150));
			
			NewGenRenderer.renderShape(text2.getTextShape(new Vector2f(1100, 700)));
			
			//NewGenRenderer.renderShape(anim2.apply(border, 10*anim2.getTime()));
			
			//videoQuad = player.getShape(new Vector2f(50, 300), new Vector2f(player.getVideoWidth()*2, player.getVideoHeight()*2));
			//NewGenRenderer.renderShape(videoQuad);
			
			//NewGenRenderer.renderShape(new Point(new Vector2f(320+1100, 80+700), 10, Color.blue));
		
			
			
			NewGenRenderer.renderShape(anim2.apply(circle));
			
			NewGenRenderer.renderShape(cursorAnimation.getShape(new Vector2f(100, 100), new Vector2f(0, 100)).color(Color.black));
			
		}, sur);
		
		    //GL20.glUniform1i(s.getUniformLocation("screen"), 0);
		    //GL20.glUniform2f(s.getUniformLocation("resolution"), width, height);
		    //GL20.glUniform1f(s.getUniformLocation("strength"), 0.005f);
			
			NewGenRenderer.renderShape(new TexturedRectangle(0, 0, width, height, sur.getTextureId(), Color.white, TexCoords.QUAD_DEFAULT_COORDS));

	}

	@Override
	public void init(InitEvent t) {
		
		setShowFPS(true);
		setShowTPS(true);
		
		setTickCap(60);
		setFrameCap(480);
		
		setResizingRules(ResizingRules.ASPECT_RATIO_CONSERVED_CENTERED);
		
		sur = new Surface(width, height, getGame());
		s = new Shader();
		System.out.println(s.setFragmentShader("#version 120\r\n"
		        + "\r\n"
		        + "varying vec2 f_texCoord;\r\n"
		        + "uniform sampler2D textureLayer;\r\n"
		        + "\r\n"
		        + "void main() {\r\n"
		        + "    // --- Constants ---\r\n"
		        + "    float curvatureStrength = 0.03;  // higher = more bent\r\n"
		        + "    float crtLines         = 480;  // number of scanlines\r\n"
		        + "    float scanlineStrength = 0.06;   // darkness of scanlines\r\n"
		        + "    float borderFade       = 0.01;   // size of the border fade (0.0 to 0.5)\r\n"
		        + "\r\n"
		        + "    // --- Curvature (applied first so everything uses curved UV) ---\r\n"
		        + "    vec2 curved = f_texCoord * 2.0 - 1.0;\r\n"
		        + "    curved *= 1.0 + dot(curved.yx, curved.yx) * curvatureStrength;\r\n"
		        + "    vec2 uv = curved * 0.5 + 0.5;\r\n"
		        + "\r\n"
		        + "    // --- Out of bounds = black ---\r\n"
		        + "    if (uv.x < 0.0 || uv.x > 1.0 || uv.y < 0.0 || uv.y > 1.0) {\r\n"
		        + "        gl_FragColor = vec4(0.0, 0.0, 0.0, 1.0);\r\n"
		        + "        return;\r\n"
		        + "    }\r\n"
		        + "\r\n"
		        + "    // --- Sample texture with curved UV ---\r\n"
		        + "    vec4 color = texture2D(textureLayer, uv);\r\n"
		        + "\r\n"
		        + "    // --- Scanlines ---\r\n"
		        + "    float scanline = sin(uv.y * crtLines * 3.14159) * scanlineStrength;\r\n"
		        + "    color.rgb -= scanline;\r\n"
		        + "\r\n"
		        + "    // --- Border fade (smooth black vignette at edges) ---\r\n"
		        + "    float fadeX = smoothstep(0.0, borderFade, uv.x) * smoothstep(1.0, 1.0 - borderFade, uv.x);\r\n"
		        + "    float fadeY = smoothstep(0.0, borderFade, uv.y) * smoothstep(1.0, 1.0 - borderFade, uv.y);\r\n"
		        + "    color.rgb *= fadeX * fadeY;\r\n"
		        + "\r\n"
		        + "    gl_FragColor = vec4(color.rgb, 1.0);\r\n"
		        + "}"));
		s.loadShaders();
		
		Font f = new Font("Sewer Sys", Font.ITALIC, 256);

		SlymyFont font = new SlymyFont(new Font("", Font.BOLD, 64), Color.black);
		SlymyFont font2 = new SlymyFont(f, new Color(150, 0, 0));
		
		text = new DynamicText(font, "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", this);
		text.setSize(20);
		
		text2 = new StaticText(font2, "Apache\n2", this);
		text2.setSize(180);
		
		anim = new Animator();
		AnimationTrack<Vector2f> track = new AnimationTrack<>(new AnimationTrack.Vector2Interpolator(AnimationTrack.Interpolator.EASE_IN_OUT_QUAD));
		track.addKeyFrame(new KeyFrame<>(0, new Vector2f(0, -50)));
		track.addKeyFrame(new KeyFrame<>(1, new Vector2f(0, 50)));
		track.addKeyFrame(new KeyFrame<>(2, new Vector2f(0, -50)));
		anim.addTrack(track, Shape::translate);
		anim.setLooping(true);
		
		anim2 = new Animator();
		AnimationTrack<Float> track2 = new AnimationTrack<>(new AnimationTrack.FloatInterpolator(AnimationTrack.Interpolator.LINEAR));
		track2.addKeyFrame(new KeyFrame<>(0, 0.0f));
		track2.addKeyFrame(new KeyFrame<>(1, (float) Math.toRadians(360)));
		anim2.addTrack(track2, Shape::rotate);
		anim2.setLooping(true);
		anim2.setSpeed(0.2f);
		
		anim3 = new Animator();
		AnimationTrack<Float> track4 = new AnimationTrack<>(new AnimationTrack.FloatInterpolator(AnimationTrack.Interpolator.SMOOTHERSTEP));
		track4.addKeyFrame(new KeyFrame<>(0, 1.3f));
		track4.addKeyFrame(new KeyFrame<>(0.2f, 0.8f));
		track4.addKeyFrame(new KeyFrame<>(0.3f, 1.3f));
		track4.addKeyFrame(new KeyFrame<>(0.6f, 0.7f));
		track4.addKeyFrame(new KeyFrame<>(1f, 1.3f));
		AnimationTrack<Float> track3 = new AnimationTrack<>(new AnimationTrack.FloatInterpolator(AnimationTrack.Interpolator.LINEAR));
		track3.addKeyFrame(new KeyFrame<>(0, (float) Math.toRadians(360)));
		track3.addKeyFrame(new KeyFrame<>(1, 0.0f));
		anim3.addTrack(track4, (shape, scale) -> shape.scale(new Vector2f(scale, 1), new Vector2f(320, 80)));
		anim3.addTrack(track3, (shape, angle) -> shape.rotate(angle, new Vector2f(300, 0)));
		anim3.setLooping(true);
		anim3.setSpeed(0.2f);
		
		tex = Texture.loadTexture("origin.png");
		
		circle = (TexturedCircle) new TexturedCircle(new Vector2f(600,800), 100, 50, tex).rotate((float) Math.toRadians(30)).sheer(new Vector2f(0f, 0));
		
		Vector2f center = circle.getCenter();
		
		border = new CircleBorder(center, 120, 32, 32, Color.red);
		
		//player = new MediaPlayer("funky_size_change.webm", this);
		
		//videoQuad = player.getShape(new Vector2f(50, 300), new Vector2f(player.getVideoWidth()*2, player.getVideoHeight()));
		//player.play();
		//player.setVolumeOnStart(25);
		
		cursorAnimation = new Animation(new AnimationFrame[] {
				
				new AnimationFrame() {

					@Override
					public Shape getShape(Vector2f position, Vector2f size) {

						return new Line(position, Vector2f.add(position, size, null), 3, Color.white);
						
					}
					
				},
				
				new AnimationFrame() {

					@Override
					public Shape getShape(Vector2f position, Vector2f size) {

						return new EmptyShape();
						
					}
					
				}
				
		});
		
		cursorAnimation.setSpeed(1.3f);
		cursorAnimation.setPlaying(true);
		
	}

	@Override
	public void update(double alpha) {
		
		anim.step((float) alpha);
		anim2.step((float) alpha);
		anim3.step((float) alpha);
		cursorAnimation.step((float) alpha);
		
		text.perCharacterTransform((i, c) -> (TexturedQuad) anim.apply(c, i*0.1f));

	}

	@Override
	public void exit(ExitEvent evnt) {
		
		if(evnt.getEventType() == ExitType.STOPPING_GAME) {
			
			//player.destroy();
			text.getFont().getCharAtlas().free();
			text2.getFont().getCharAtlas().free();
			tex.free();
			
		} else {
			
			evnt.addResources("texture", tex);
			
		}
		
	}

}
