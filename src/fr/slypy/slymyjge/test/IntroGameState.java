package fr.slypy.slymyjge.test;

import fr.slypy.slymyjge.Game;
import fr.slypy.slymyjge.GameState;
import fr.slypy.slymyjge.InitType;
import fr.slypy.slymyjge.media.MediaPlayer;
import uk.co.caprica.vlcj.player.base.State;

public class IntroGameState extends GameState {

	MediaPlayer p;
	Game g;
	boolean started = false;
	
	public IntroGameState(Game game) {
		
		g = game;
		
	}
	
	@Override
	public void init() {
		
		p = new MediaPlayer("C:\\Users\\Enzo\\Desktop\\vid.mp4");
		
	}

	@Override
	public void update(double alpha) {

		if(!started) {
			
			p.play();
			started = true;
			
		}
		
		if(p.getState() == State.ENDED || p.getState() == State.STOPPED) {
			
			g.setState(ExampleGame.MAIN_ID);
			
		}
		
	}
	
	@Override
	public void render(double alpha) {

		p.render(0, 0, 1280, 720);
		
	}

	@Override
	public InitType getInitType() {

		return InitType.INIT_ON_REGISTER;
		
	}

	@Override
	public Game getGame() {

		return g;
		
	}

}
