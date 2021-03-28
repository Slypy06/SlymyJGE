package fr.slypy.slymyjge.components;

import org.lwjgl.input.Mouse;

import fr.slypy.slymyjge.Game;


public abstract class MoveableComponent extends Component {
	
	public boolean isMove = false;
	public float originX = 0;
	public float originY = 0;
	public boolean mouseClick = false;
	
	public MoveableComponent(float x, float y, int w, int h, Game game) {
		
		super(x, y, w, h, game);

	}

	public boolean isMove() {
		
		return isMove;
		
	}

	public void setMove(boolean isMove) {
		
		if(!activated) {
			
			this.isMove = false;
			
			return;
			
		}
		
		this.isMove = isMove;
		
		mouseClick = (isMove ? mouseClick : false);
		
		if(!isMove) {
			
			originX = 0;
			originY = 0;
			
		}
		
	}
	
	public void move() {
		
		if(!activated) {
			
			return;
			
		}
		
		if(!this.isMove) {
			
			this.setMove(true);
			
		}
		
		if(!mouseClick) {
			
			originX = (Mouse.getX() / game.getWidthDiff()) - this.getX();
			originY = (Mouse.getY() / game.getHeightDiff()) + this.getY();
			
			mouseClick = true;
			
		}
		
		this.setX((Mouse.getX() / game.getWidthDiff()) - originX);
		this.setY(-((Mouse.getY() / game.getHeightDiff()) - originY));
		
	}

}
