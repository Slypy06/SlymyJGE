package fr.slypy.test;

import java.awt.Color;
import java.awt.Font;
import java.io.File;

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
import fr.slypy.slymyjge.graphics.shape.composite.TexturedCircle;
import fr.slypy.slymyjge.graphics.shape.dynamic.DynamicText;

public class SurfaceTest extends Game {
	
	private DynamicText text;
	private Animator anim;
	private TexturedCircle circle;
	private Texture tex;

	public SurfaceTest(int width, int height, String title, Color backgroundColor, boolean resizable) {
		
		super(width, height, title, backgroundColor, resizable);
		
	}
	
	public static void main(String[] args) {
		
		System.setProperty("org.lwjgl.librarypath", new File("lib/natives").getAbsolutePath());
		
		new SurfaceTest(1920, 1080, "Test", Color.white, false).start();
		
	}

	@Override
	public void stop() {
		
		
		
	}

	@Override
	public void render(double alpha) {

		NewGenRenderer.renderText(text, new Vector2f(150, 150));
		
		NewGenRenderer.renderShape(circle);
		
	}

	@Override
	public void init() {
		
		setShowFPS(true);
		setShowTPS(true);
		
		setTickCap(60);
		setFrameCap(240);
		
		SlymyFont font = new SlymyFont(new Font("", Font.BOLD, 64), Color.black);
		text = new DynamicText(font, "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", this);
		text.setSize(20);
		
		anim = new Animator();
		AnimationTrack<Vector2f> track = new AnimationTrack<>(new AnimationTrack.Vector2Interpolator(AnimationTrack.Interpolator.EASE_IN_OUT_QUAD));
		track.addKeyFrame(new KeyFrame<>(0, new Vector2f(0, -50)));
		track.addKeyFrame(new KeyFrame<>(1, new Vector2f(0, 50)));
		track.addKeyFrame(new KeyFrame<>(2, new Vector2f(0, -50)));
		anim.addTrack(track, Shape::translate);
		anim.setLooping(true);
		
		tex = Texture.loadTexture("origin.png");
		
		circle = (TexturedCircle) new TexturedCircle(new Vector2f(600,800), 100, 50, tex).rotate((float) Math.toRadians(30)).sheer(new Vector2f(0f, 0));
		
	}

	@Override
	public void update(double alpha) {
		
		anim.step((float) alpha);
		
		text.perCharacterTransform((i, c) -> (TexturedQuad) anim.apply(c, i*0.1f));
		
	}

}
