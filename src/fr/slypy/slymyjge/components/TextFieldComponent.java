package fr.slypy.slymyjge.components;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.input.Keyboard;

import fr.slypy.slymyjge.Game;
import fr.slypy.slymyjge.font.SlymyFont;
import fr.slypy.slymyjge.graphics.Renderer;
import fr.slypy.slymyjge.utils.MouseButtons;
import fr.slypy.slymyjge.utils.RenderType;

public abstract class TextFieldComponent extends Component {

	public List<String> text = new ArrayList<String>();
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
	
	public List<String> getText() {
		
		return text;
		
	}
	
	public void setString(List<String> text) {
		
		this.text = text;
		
	}
	
	public int getCap() {
		
		return cap;
		
	}
	
	public int getTextSize() {
		
		int size = 0;
		
		for(String s : text) {
			
			size += s.length();
			
		}
		
		return size;
		
	}
	
	public void setCap(int cap) {
		
		this.cap = cap;
		
	}
	
	public void keyTyped(char key) {
		System.out.println((int) key);
		if(allowed.contains(key + "")) {
		
			if(text.size() == 0) {
				
				text.add(key + "");
				
			} else {
			
				if(cap <= -1) {
					
					text.set(text.size() - 1, text.get(text.size() - 1) + key);
					
				} else if(getTextSize() + 1 <= cap) {
					
					text.set(text.size() - 1, text.get(text.size() - 1) + key);
					
				}
			
			}
		
		} else if(allowMultilines && (int) key == 13) { //BACKLINE
			
			text.add("");
			
		} else if((int) key == 8) { //EREASE
			
			if(getTextSize() > 0) {
				

				
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

	public void setText(List<String> text) {
		
		this.text = text;
		
	}
	
	public void setText(String text) {
		
		this.text = new ArrayList<String>(Arrays.asList(text));
		
	}

	@Override
	public void render() {
		
		renderBackground();
		
		List<String> linesTemp = new ArrayList<String>();
		String lineTemp = "";
		int i = 1;
		
		for(String line : text) {
			
			if(f.getHeight() * i < h - (margin * 2)) {
			
				for(char c : line.toCharArray()) {
					
					if(f.getWidth(lineTemp + c) < w - (margin * 2)) {
						
						lineTemp += c;
						
					}
					
				}
				
				linesTemp.add(lineTemp);
				
				lineTemp = "";
				
			}
			
			i++;

		}
		
		i = 0;
		
		for(String line : linesTemp) {
			
			Renderer.renderText(x + margin, y + margin + (f.getHeight() * i), f, line, Color.black);
			
			i++;
			
		}
		
		renderForeground();

	}
	
	public abstract void renderBackground();
	
	public abstract void renderForeground();

}
