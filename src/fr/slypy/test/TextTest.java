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
import fr.slypy.slymyjge.graphics.shape.Shape;
import fr.slypy.slymyjge.graphics.shape.TexturedRectangle;
import fr.slypy.slymyjge.graphics.shape.dynamic.StaticText;

public class TextTest extends Game {
	
	private SlymyFont f;
	private StaticText text;
	private TexturedRectangle rect;
	private Animator textAnimator;
	
	public TextTest(int width, int height, String title, Color backgroundColor, boolean resizable) {
		
		super(width, height, title, backgroundColor, resizable);

	}
	
	public static void main(String[] args) {
		
		System.setProperty("org.lwjgl.librarypath", new File("lib/natives").getAbsolutePath());
		
		new TextTest(1920, 1080, "Test", Color.white, false).start();
		
	}

	@Override
	public void init() {
		
		setShowFPS(true);
		setShowTPS(true);
		
		setTickCap(100);
		//setFrameCap(100);
		
		f = new SlymyFont(new Font("", Font.PLAIN, 70), Color.black);
		
		text = new StaticText(f, "Abcdef", getGame());
		rect = new TexturedRectangle(100, 200, text.getSurface().getWidth(), text.getSurface().getHeight());
		rect.setTexture(text.getSurface().getTextureId());
		
		Vector2f center = new Vector2f(250,300);
		
		textAnimator = new Animator(rect);
		AnimationTrack<Float> rotation = new AnimationTrack<>(new AnimationTrack.FloatInterpolator(AnimationTrack.Interpolator.SMOOTHERSTEP));
		rotation.addKeyFrame(new KeyFrame<>(0f, (float) Math.toRadians(0)));
		rotation.addKeyFrame(new KeyFrame<>(1f, (float) Math.toRadians(360)));
		rotation.addKeyFrame(new KeyFrame<>(2f, (float) Math.toRadians(180)));
		rotation.addKeyFrame(new KeyFrame<>(3f, (float) Math.toRadians(90)));
		rotation.addKeyFrame(new KeyFrame<>(3.5f, (float) Math.toRadians(360)));
		textAnimator.addTrack(rotation, (shape, angle) -> shape.rotate(angle, center));
		textAnimator.setLooping(true);
		
	}

	@Override
	public void update(double alpha) {

		textAnimator.update((float) alpha);
		
	}
	
	@Override
	public void render(double alpha) {
		
		NewGenRenderer.renderShape(textAnimator.apply());
		
		//TexturedRectangle rect2 = new TexturedRectangle(200, 200, f.getCharAtlas().getWidth(), f.getCharAtlas().getHeight(), f.getCharAtlas());
		//NewGenRenderer.renderShape(rect2);
		
		//NewGenRenderer.renderShapeBundle(sb);
		
		/*NewGenRenderer.renderShape(text.getCharacterBundle().getShapes().get(0));
		
		System.out.println(text.getCharacterBundle().getShapes().get(0).getVertexes()[0]);
		System.out.println(text.getCharacterBundle().getShapes().get(0).getVertexes()[1]);
		System.out.println(text.getCharacterBundle().getShapes().get(0).getVertexes()[2]);
		System.out.println(text.getCharacterBundle().getShapes().get(0).getVertexes()[3]);
		System.out.println();
		System.out.println(text.getCharacterBundle().getShapes().get(0).getTexture());*/
		
		//Rectangle back = new Rectangle(100, 200, f.getCharAtlas().getWidth(), f.getCharAtlas().getHeight(), Color.green);
		//TexturedRectangle rect2 = new TexturedRectangle(100, 200, f.getCharAtlas().getWidth(), f.getCharAtlas().getHeight(), f.getCharAtlas(), Color.white, f.getCharCoords('Z'));
		//NewGenRenderer.renderShape(rect2);
		
	}
	
	@Override
	public void stop() {
		
		
		
	}

}
