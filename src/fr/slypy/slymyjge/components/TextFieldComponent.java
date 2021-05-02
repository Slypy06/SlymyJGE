package fr.slypy.slymyjge.components;

import org.lwjgl.input.Keyboard;

import fr.slypy.slymyjge.Game;
import fr.slypy.slymyjge.font.SlymyFont;
import fr.slypy.slymyjge.utils.MouseButtons;
import fr.slypy.slymyjge.utils.RenderType;

public abstract class TextFieldComponent extends Component {

	public String text = "";
	public int cap = -1;
	public String allowed = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890&È~" + '"' + "#'{([-|Ë`_\\Á^‡@)]∞+=}^®$£§%˘ß!/:.;?,*≤<>‚‰„ÍÎ˚¸ÓÔÙˆıÒ¬ƒ√ À€‹Œœ‘÷’—µ";
	public boolean allowMultilines = false;
	public boolean focus;
	public SlymyFont f;
	public int margin = 10;
	
	public TextFieldComponent(float x, float y, int w, int h, Game game, SlymyFont f) {
		
		this(x, y, w, h, game, f, RenderType.ONMAP);

	}
	
	public TextFieldComponent(float x, float y, int w, int h, Game game, SlymyFont f, RenderType type) {
		
		super(x, y, w, h, game, type);
		
		this.f = f;
		
		this.addMouseButtonToListen(MouseButtons.LEFT_BUTTON);
		this.addMouseButtonToListen(MouseButtons.RIGHT_BUTTON);
		this.addMouseButtonToListen(MouseButtons.MIDDLE_BUTTON);
		this.addKeyToListen(Keyboard.KEY_ESCAPE);
		
	}
	
	public void setNewCharset(String charset) {
		
		allowed = charset;
		
	}
	
	public String getCharset() {
		
		return allowed;
		
	}
	
	public boolean isAllowMultilines() {
		
		return allowMultilines;
		
	}
	
	public void setAllowMultilines(boolean allowMultilines) {
		
		this.allowMultilines = allowMultilines;
		
	}
	
	public String getText() {
		
		return text;
		
	}
	
	public void setString(String text) {
		
		this.text = text;
		
	}
	
	public int getCap() {
		
		return cap;
		
	}
	
	public void setCap(int cap) {
		
		this.cap = cap;
		
		if(text.length() < cap) {
			
			cap = text.length();
			
		}
		
		if(cap > 0) {
			
			text = text.substring(0, cap - 1);
		
		} else if(cap == 0) {
			
			text = "";
			
		}
		
	}
	
	public void keyTyped(char key) {
		
		if(allowed.contains(key + "") || (allowMultilines && (int) key == 13)) {
		
			if(cap <= -1) {
				
				text += key;
				
			} else if(text.length() + 1 <= cap) {
				
				text += key;
				
			}
		
		}
		
	}
	
	@Override
	public void update(float xCursor, float yCursor, Game game) {
		
		while(Keyboard.next()) {
			
			if(Keyboard.getEventKeyState()) {
				
				if(focus) {
					
					keyTyped(Keyboard.getEventCharacter());
					
				}
				
			}
			
		}
		
		this.keyUpdate();
		
		float x = xCursor;
		float y = yCursor;
	
		if(renderType == RenderType.HUD) {
			
			x = xCursor + game.getXCam();
			y = yCursor + game.getYCam();
			
		}
		
		if((x >= hitboxX && x <= hitboxX + hitboxW) && (y >= hitboxY && y <= hitboxY + hitboxH)) {
			
			hover = true;
			
		} else {
			
			hover = false;
			
		}
		
		if(game.componentHover() != this) {
			
			hover = false;
			
		}
		
	}
	
	public boolean isFocus() {
		
		return focus;
		
	}
	
	public void setFocus(boolean focus) {
		
		this.focus = focus;
		
	}
	
	@Override
	public void mouseButtonPressed(int button) {
		
		if(game.componentHover() != null && game.componentHover().equals(this) && button == MouseButtons.LEFT_BUTTON) {
			
			if(!focus) {
				
				focus = true;
				
			}
			
		} else if((game.componentHover() == null || !game.componentHover().equals(this)) && focus) {
			
			focus = false;
			
		}
		
	}
	
	@Override
	public void keyPressed(int key) {
		
		if(focus) {
			
			focus = false;
			
		}
		
	}
	
	public SlymyFont getF() {
		
		return f;
		
	}

	public void setF(SlymyFont f) {
		
		this.f = f;
		
	}

	public int getMargin() {
		
		return margin;
		
	}

	public void setMargin(int margin) {
		
		this.margin = margin;
		
	}

	public void setText(String text) {
		
		this.text = text;
		
	}

	@Override
	public void render() {
		
		if(f.getWidth(text) > w - (margin * 2)) {
			
			
			
		} else {
			
			
			
		}
		
		renderField();
		
	}
	
	public abstract void renderField();

}
