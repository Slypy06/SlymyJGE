package fr.slypy.test;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.lwjgl.util.vector.Vector2f;

import fr.slypy.slymyjge.Game;
import fr.slypy.slymyjge.animations.dynamic.AnimationTrack;
import fr.slypy.slymyjge.animations.dynamic.AnimationTrack.KeyFrame;
import fr.slypy.slymyjge.animations.dynamic.Animator;
import fr.slypy.slymyjge.font.SlymyFont;
import fr.slypy.slymyjge.graphics.NewGenRenderer;
import fr.slypy.slymyjge.graphics.Texture;
import fr.slypy.slymyjge.graphics.shape.Shape;
import fr.slypy.slymyjge.graphics.shape.TexturedQuad;
import fr.slypy.slymyjge.graphics.shape.composite.CircleBorder;
import fr.slypy.slymyjge.graphics.shape.composite.TexturedCircle;
import fr.slypy.slymyjge.graphics.shape.dynamic.DynamicText;
import fr.slypy.slymyjge.media.MediaPlayer;

public class SurfaceTest extends Game {
	
	private DynamicText text;
	private DynamicText text2;
	private Animator anim;
	private Animator anim2;
	private Animator anim3;
	private TexturedCircle circle;
	private CircleBorder border;
	private Texture tex;
	private MediaPlayer player;
	private Shape videoQuad;

	public SurfaceTest(int width, int height, String title, Color backgroundColor, boolean resizable) {
		
		super(width, height, title, backgroundColor, resizable);
		
	}
	
	
	public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		System.setProperty("org.lwjgl.librarypath", new File("lib/natives").getAbsolutePath());
		
		new SurfaceTest(1920, 1080, "Test", Color.cyan, false).start();
		
	}

	
	
	@Override
	public void stop() {
		
		
		
	}

	@Override
	public void render(double alpha) {

		NewGenRenderer.renderText(text, new Vector2f(150, 150));
		
		NewGenRenderer.renderText(text2, new Vector2f(1100, 700));
		
		NewGenRenderer.renderShape(anim2.apply(circle));
		
		NewGenRenderer.renderShape(anim2.apply(border, 10*anim2.getTime()));
		
		videoQuad = player.getShape(new Vector2f(50, 300), new Vector2f(player.getVideoWidth()*2, player.getVideoHeight()*2)).rotate((float) Math.toRadians(180));
		NewGenRenderer.renderShape(videoQuad);
		
		//NewGenRenderer.renderShape(new Point(new Vector2f(320+1100, 80+700), 10, Color.blue));
	
	}

	@Override
	public void init(InitEvent t) {
		
		System.exit(0);
		
		setShowFPS(true);
		setShowTPS(true);
		
		setTickCap(60);
		setFrameCap(480);
		
		Font f = new Font("Sewer Sys", Font.ITALIC, 256);

		SlymyFont font = new SlymyFont(new Font("", Font.BOLD, 64), Color.black);
		SlymyFont font2 = new SlymyFont(f, new Color(100, 0, 0));
		
		text = new DynamicText(font, "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", this);
		text.setSize(20);
		
		text2 = new DynamicText(font2, "Apache2", this);
		text2.setSize(160);
		
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
		
		player = new MediaPlayer("piston.webm", this);
		
		videoQuad = player.getShape(new Vector2f(50, 300), new Vector2f(player.getVideoWidth()*2, player.getVideoHeight())).rotate((float) Math.toRadians(180));
		player.play();
		player.setVolumeOnStart(25);

	}

	@Override
	public void update(double alpha) {
		
		anim.step((float) alpha);
		anim2.step((float) alpha);
		anim3.step((float) alpha);
		
		text.perCharacterTransform((i, c) -> (TexturedQuad) anim.apply(c, i*0.1f));
		
		text2.perCharacterTransform((i, c) -> (TexturedQuad) anim3.apply(c));
		
		//System.out.println(player.getAudioHandler().volume() + " - " + player.getAudioHandler().isMute());
		
	}

	@Override
	public void exit(ExitEvent evnt) {
		
		if(evnt.getEventType() == ExitType.STOPPING_GAME) {
			
			player.destroy();
			text.getFont().getCharAtlas().free();
			text2.getFont().getCharAtlas().free();
			tex.free();
			
		} else {
			
			evnt.addResources("texture", tex);
			
		}
		
	}

}
