package fr.slypy.slymyjge.components;

import fr.slypy.slymyjge.Game;
import fr.slypy.slymyjge.utils.RenderType;

public abstract class TextFieldComponent extends Component {

	public TextFieldComponent(float x, float y, int w, int h, Game game) {
		
		this(x, y, w, h, game, RenderType.ONMAP);

	}
	
	public TextFieldComponent(float x, float y, int w, int h, Game game, RenderType type) {
		
		super(x, y, w, h, game, type);

	}

}
