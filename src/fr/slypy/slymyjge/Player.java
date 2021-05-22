package fr.slypy.slymyjge;

public class Player {

	public String name;
	public int hp;
	public Location loc;
	public BoundingBox bb;
	
	public Player(String name, int hp, Location loc, BoundingBox bb) {
		
		this.name = name;
		this.hp = hp;
		this.loc = loc;
		this.bb = bb;		
	}

	public String getName() {
		
		return name;
		
	}

	public void setName(String name) {
		
		this.name = name;
		
	}

	public int getHp() {
		
		return hp;
		
	}

	public void setHp(int hp) {
		
		this.hp = hp;
		
	}

	public Location getLoc() {
		
		return loc;
		
	}

	public void setLoc(Location loc) {
		
		this.loc = loc;
		
	}

	public BoundingBox getBb() {
		
		return bb;
		
	}

	public void setBb(BoundingBox bb) {
		
		this.bb = bb;
		
	}

}
