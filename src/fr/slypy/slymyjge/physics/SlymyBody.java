package fr.slypy.slymyjge.physics;

import java.awt.Color;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

public class SlymyBody {

	Body b;
	Vec2 size;
	Color c;
	
	public SlymyBody(Body b, Vec2 size, Color c) {

		this.b = b;
		this.size = size;
		this.c = c;
		
	}

	public Body getBody() {
		
		return b;
		
	}
	
	public void setBody(Body b) {
		
		this.b = b;
		
	}
	
	public Vec2 getSize() {
		
		return size;
		
	}
	
	public void setSize(Vec2 size) {
		
		this.size = size;
		
	}
	
	public void setColor(Color c) {
		
		this.c = c;
		
	}
	
	public Color getColor() {
		
		return c;
		
	}
	
}
