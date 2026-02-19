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
import fr.slypy.slymyjge.graphics.shape.TexturedQuad;
import fr.slypy.slymyjge.graphics.shape.TexturedRectangle;
import fr.slypy.slymyjge.graphics.shape.dynamic.StaticText;

public class TextTest extends Game {

	private StaticText text;
	private TexturedQuad rect;
	private Animator textAnimator;
	
	public TextTest(int width, int height, String title, Color backgroundColor, boolean resizable) {
		
		super(width, height, title, backgroundColor, resizable);

	}
	
	public static void main(String[] args) {
		
		System.setProperty("org.lwjgl.librarypath", new File("lib/natives").getAbsolutePath());
		
		new TextTest(1920, 1080, "Test", Color.blue, false).start();
		
	}

	@Override
	public void init() {
		
		setShowFPS(true);
		setShowTPS(true);
		
		setTickCap(100);
		
		text = new StaticText(new SlymyFont(new Font("Serif", Font.BOLD, 200), Color.white), "Apache", getGame());
		rect = text.getTextShape(new Vector2f(250,300));
		
		textAnimator = new Animator();
		AnimationTrack<Float> rotation = new AnimationTrack<>(new AnimationTrack.FloatInterpolator(AnimationTrack.Interpolator.EASE_IN_OUT_QUAD));
		rotation.addKeyFrame(new KeyFrame<>(0f, (float) Math.toRadians(0)));
		rotation.addKeyFrame(new KeyFrame<>(2f, (float) Math.toRadians(180)));
		rotation.addKeyFrame(new KeyFrame<>(3.5f, (float) Math.toRadians(360)));
		rotation.addKeyFrame(new KeyFrame<>(3f, (float) Math.toRadians(90)));
		rotation.addKeyFrame(new KeyFrame<>(1f, (float) Math.toRadians(360)));
		
		Vector2f r1Center = new Vector2f(600, 300);
		AnimationTrack<Float> r1 = new AnimationTrack<>(new AnimationTrack.FloatInterpolator(AnimationTrack.Interpolator.EASE_IN_OUT_QUAD));
		r1.addKeyFrame(new KeyFrame<>(0f, (float) Math.toRadians(0)));
		r1.addKeyFrame(new KeyFrame<>(3.5f, (float) Math.toRadians(360)));
		
		AnimationTrack<Float> r2 = new AnimationTrack<>(new AnimationTrack.FloatInterpolator(AnimationTrack.Interpolator.EASE_IN_OUT_QUAD));
		r2.addKeyFrame(new KeyFrame<>(0f, (float) Math.toRadians(0)));
		r2.addKeyFrame(new KeyFrame<>(3.5f, (float) -Math.toRadians(360)));
		
		AnimationTrack<Color> color = new AnimationTrack<>(new AnimationTrack.ColorInterpolator(AnimationTrack.Interpolator.EASE_IN_OUT_QUAD));
		color.addKeyFrame(new KeyFrame<>(0f, Color.white));
		color.addKeyFrame(new KeyFrame<>(1.5f, Color.red));
		color.addKeyFrame(new KeyFrame<>(2f, Color.green));
		color.addKeyFrame(new KeyFrame<>(2.5f, Color.pink));
		color.addKeyFrame(new KeyFrame<>(3f, Color.MAGENTA));
		color.addKeyFrame(new KeyFrame<>(3.5f, Color.orange));
		
		//Vector2f scalingCenter = new Vector2f(650, 300);
		AnimationTrack<Vector2f> scaling = new AnimationTrack<>(new AnimationTrack.Vector2Interpolator(AnimationTrack.Interpolator.EASE_IN_OUT_QUAD));
		scaling.addKeyFrame(new KeyFrame<>(0f, new Vector2f(1, 1)));
		scaling.addKeyFrame(new KeyFrame<>(2f, new Vector2f(1, 2)));
		scaling.addKeyFrame(new KeyFrame<>(3f, new Vector2f(2, 1)));
		scaling.addKeyFrame(new KeyFrame<>(3.5f, new Vector2f(1, 1)));
		
		AnimationTrack<Vector2f> sheer = new AnimationTrack<>(new AnimationTrack.Vector2Interpolator(AnimationTrack.Interpolator.LINEAR));
		sheer.addKeyFrame(new KeyFrame<>(0, new Vector2f(0, 0)));
		sheer.addKeyFrame(new KeyFrame<>(1, new Vector2f(0, 2)));
		sheer.addKeyFrame(new KeyFrame<>(2, new Vector2f(0, 0)));
		sheer.addKeyFrame(new KeyFrame<>(3, new Vector2f(5, 0)));
		sheer.addKeyFrame(new KeyFrame<>(3.5f, new Vector2f(2, 120)));
		
		//textAnimator.addTrack(sheer, (shape, s) -> shape.sheer(s, scalingCenter));
		
		//textAnimator.addTrack(rotation, (shape, angle) -> shape.rotate(angle));
		textAnimator.addTrack(r1, (shape, angle) -> shape.rotate(angle, shape.getVertexes()[0]));
		textAnimator.addTrack(r2, (shape, angle) -> shape.rotate(angle, r1Center));
        textAnimator.addTrack(color, (shape, c) -> shape.color(c));
		textAnimator.addTrack(scaling, (shape, scale) -> shape.scale(scale));
		
		//textAnimator.setLooping(true);
		textAnimator.setDuration(5);
		//textAnimator.setSpeed(0.2f);
		
	}

	@Override
	public void update(double alpha) {

		textAnimator.step((float) alpha);

		if(textAnimator.isFinished()) {
			
			textAnimator.reverse();
			
		}
		
		//System.out.println(textAnimator.getTime());
		
	}
	
	@Override
	public void render(double alpha) {
		
		NewGenRenderer.renderShape(textAnimator.apply(rect));
		
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
	public void keyTyped(char key, boolean pressed) {
		
		if(key == 'z' && pressed) {
			
			text.setText(text.getText() + "aha");
			rect = text.getTextShape(new Vector2f(250,300));
			
		} else if(key == 's' && pressed) {
			
			text.setText(text.getText().substring(0, Math.max(0, text.getText().length() - 3)));
			rect = text.getTextShape(new Vector2f(250,300));
			
		}
		
	}
	
	@Override
	public void stop() {
		
		
		
	}

}
