package fr.slypy.slymyjge.test;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.ShapeType;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.lwjgl.input.Keyboard;

import fr.slypy.slymyjge.Game;
import fr.slypy.slymyjge.GameState;
import fr.slypy.slymyjge.InitType;
import fr.slypy.slymyjge.graphics.HUDRenderer;
import fr.slypy.slymyjge.graphics.Renderer;
import fr.slypy.slymyjge.graphics.Texture;
import fr.slypy.slymyjge.physics.SlymyBody;

public class MainGameState extends GameState {

	Game g;

	World w;
	int translation = 150;
	List<SlymyBody> bodies = new ArrayList<SlymyBody>();
	
	Texture backgroundTexture;

	int speed = 30;
	
	public MainGameState(Game g) {
		
		this.g = g;
		
	}
	
	@Override
	public void render(double alpha) {
		
		HUDRenderer.renderTexturedQuad(0, 0, 1280, 720, backgroundTexture);
		
		List<SlymyBody> bodiesTemp = new ArrayList<SlymyBody>(bodies);
		
		for(SlymyBody b : bodiesTemp) {
			
				Vec2 position = b.getBody().getPosition().mul(translation);
				Vec2 size = b.getSize().mul(translation);
				
				position.y = g.getHeight() - position.y;

				float width = size.x * 2;
				
				float height = size.y * 2;
				
				Renderer.setRotation(b.getBody().getAngle());
					Renderer.renderQuad(position.x - width / 2, position.y - height / 2, (int) width, (int) height, b.getColor());
				Renderer.setRotation(0);
			
		}
		
	}

	@Override
	public void init() {

		w = new World(new Vec2(0, -30), false);
		initBodies();
		
		addKeyToListen(Keyboard.KEY_SPACE);
		addKeyToListen(Keyboard.KEY_CAPITAL);
		
		backgroundTexture = Texture.loadTexture("background.jpg");
		
	}

	@Override
	public void update(double alpha) {

		w.step(1.0f / (float) g.getFrameCap(), 8, 3);
		
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT) && !Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {

			bodies.get(0).getBody().applyForce(new Vec2(-speed, 0), bodies.get(0).getBody().getPosition());
			
		} else if(!Keyboard.isKeyDown(Keyboard.KEY_LEFT) && Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			
			bodies.get(0).getBody().applyForce(new Vec2(speed, 0), bodies.get(0).getBody().getPosition());
			
		}
		
		System.out.println(bodies.get(0).getBody().getPosition().mul(translation).x + g.getXCam());
		//System.out.println(g.getXCam() + ((bodies.get(0).getBody().getPosition().mul(translation).x - g.getXCam()) - 1080));
		
		if(bodies.get(0).getBody().getPosition().mul(translation).x + g.getXCam() > 1080) {
			
			g.setXCam(g.getXCam() - (((bodies.get(0).getBody().getPosition().mul(translation).x + g.getXCam()) - 1080) / 4));
			
			if(bodies.get(0).getBody().getPosition().mul(translation).x + g.getXCam() > 1280) {
				
				g.setXCam(g.getXCam() - (((bodies.get(0).getBody().getPosition().mul(translation).x + g.getXCam()) - 1080)));
				
			}
			
		}
		
		if(bodies.get(0).getBody().getPosition().mul(translation).x + g.getXCam() < 200) {
			
			g.setXCam(g.getXCam() + ((200 - (bodies.get(0).getBody().getPosition().mul(translation).x + g.getXCam())) / 4));
			
			if(bodies.get(0).getBody().getPosition().mul(translation).x + g.getXCam() < 0) {
				
				g.setXCam(g.getXCam() + ((200 - (bodies.get(0).getBody().getPosition().mul(translation).x + g.getXCam()))));
				
			}
			
		}
		
	}

	@Override
	public InitType getInitType() {
		
		return InitType.INIT_ON_LOAD;
		
	}

	@Override
	public Game getGame() {

		return g;
		
	}
	
	public void initBodies() {
		
		BodyDef playerDefinition = new BodyDef();
		playerDefinition.type = BodyType.DYNAMIC;
		playerDefinition.position = new Vec2(1, 720.0f / translation / 2.0f);
		playerDefinition.fixedRotation = true;
		
		Body player = w.createBody(playerDefinition);
		
		PolygonShape playerShape = new PolygonShape();
		playerShape.setAsBox(0.5f, 0.5f);
		
		FixtureDef playerFixtureDefinition = new FixtureDef();
		playerFixtureDefinition.density = 1; 
		playerFixtureDefinition.shape = playerShape;
		playerFixtureDefinition.friction = 3f;
		
		player.createFixture(playerFixtureDefinition);

		bodies.add(new SlymyBody(player, new Vec2(0.5f, 0.5f), Color.red));
		
		BodyDef groundDefinition = new BodyDef();
		groundDefinition.type = BodyType.STATIC;
		groundDefinition.position = new Vec2(0, 0);
		
		PolygonShape groundShape = new PolygonShape();
		groundShape.setAsBox(1280.0f / translation / 2.0f, 0f);
		
		Body ground = w.createBody(groundDefinition);
		
		FixtureDef groundFixtureDefinition = new FixtureDef();
		groundFixtureDefinition.density = 1; 
		groundFixtureDefinition.shape = groundShape;
		groundFixtureDefinition.restitution = 0.1f;
		
		ground.createFixture(groundFixtureDefinition);

		bodies.add(new SlymyBody(ground, new Vec2(1280.0f / translation / 2.0f, 0.05f), Color.gray));
		
		BodyDef ground2Definition = new BodyDef();
		ground2Definition.type = BodyType.STATIC;
		ground2Definition.position = new Vec2(1280.0f / translation, 0);
		
		PolygonShape ground2Shape = new PolygonShape();
		ground2Shape.setAsBox(1280.0f / translation / 2.0f, 0.0f);
		
		Body ground2 = w.createBody(ground2Definition);
		
		FixtureDef ground2FixtureDefinition = new FixtureDef();
		ground2FixtureDefinition.density = 1; 
		ground2FixtureDefinition.shape = ground2Shape;
		ground2FixtureDefinition.restitution = 0.5f;
		
		ground2.createFixture(ground2FixtureDefinition);

		bodies.add(new SlymyBody(ground2, new Vec2(1280.0f / translation / 2.0f, 0.05f), Color.green));
		
		BodyDef ground3Definition = new BodyDef();
		ground3Definition.type = BodyType.STATIC;
		ground3Definition.position = new Vec2(250 / translation, 250 / translation);
		
		PolygonShape ground3Shape = new PolygonShape();
		ground3Shape.setAsBox(150 / translation, 0.0f);
		
		Body ground3 = w.createBody(ground3Definition);
		
		FixtureDef ground3FixtureDefinition = new FixtureDef();
		ground3FixtureDefinition.density = 1;  
		ground3FixtureDefinition.shape = ground3Shape;
		ground3FixtureDefinition.restitution = 0.1f;
		
		ground3.createFixture(ground3FixtureDefinition);

		bodies.add(new SlymyBody(ground3, new Vec2(150 / translation, 0.05f), Color.black));

	}
	
	@Override
	public void keyPressed(int key) {
		
		if(key == Keyboard.KEY_SPACE) {
			
			PolygonShape ground3Shape = new PolygonShape();
			ground3Shape.setAsBox(0.5f, 0.3f);
	
			bodies.get(0).getBody().m_fixtureList.m_shape = ground3Shape;
			
			bodies.get(0).setSize(new Vec2(0.5f, 0.4f));
	
		}
		
		if(key == Keyboard.KEY_CAPITAL) {
			
			PolygonShape ground3Shape = new PolygonShape();
			ground3Shape.setAsBox(0.5f, 0.3f);
	
			bodies.get(0).getBody().m_fixtureList.m_shape = ground3Shape;
			
			bodies.get(0).setSize(new Vec2(0.5f, 0.3f));
			
			speed = 24;
			
		}
		
	}
	
	@Override
	public void keyReleased(int key) {
		
		if(key == Keyboard.KEY_SPACE) {
			
			PolygonShape ground3Shape = new PolygonShape();
			ground3Shape.setAsBox(0.5f, 0.5f);
	
			bodies.get(0).getBody().m_fixtureList.m_shape = ground3Shape;
			
			bodies.get(0).setSize(new Vec2(0.5f, 0.5f));
			
			bodies.get(0).getBody().applyForce(new Vec2(0, 500), bodies.get(0).getBody().getPosition());
			
		}
		
		if(key == Keyboard.KEY_CAPITAL) {
			
			PolygonShape ground3Shape = new PolygonShape();
			ground3Shape.setAsBox(0.5f, 0.5f);
	
			bodies.get(0).getBody().m_fixtureList.m_shape = ground3Shape;
			
			bodies.get(0).setSize(new Vec2(0.5f, 0.5f));
			
			speed = 30;
			
		}
		
	}

}
