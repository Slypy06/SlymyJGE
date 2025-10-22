package fr.slypy.slymyjge.components;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.Point;

import fr.slypy.slymyjge.Game;
import fr.slypy.slymyjge.animations.framed.Animation;
import fr.slypy.slymyjge.animations.framed.AnimationFrame;
import fr.slypy.slymyjge.font.SlymyFont;
import fr.slypy.slymyjge.graphics.Renderer;
import fr.slypy.slymyjge.utils.MouseButtons;
import fr.slypy.slymyjge.utils.RenderType;

public abstract class TextFieldComponent extends Component {

	public List<String> text = new ArrayList<String>();
	public int cap = -1;
	public boolean allowMultilines = false;
	public boolean focus;
	public SlymyFont f;
	public SlymyFont ghostF;
	public String ghostText;
	public int margin = 10;
	public Animation cursorAnimation;
	public long delDuration = Long.MAX_VALUE;
	public long newLineDuration = Long.MAX_VALUE;
	public long backDuration = Long.MAX_VALUE;
	public long upDuration = Long.MAX_VALUE;
	public long downDuration = Long.MAX_VALUE;
	public long leftDuration = Long.MAX_VALUE;
	public long rightDuration = Long.MAX_VALUE;
	public int desiredCursorX = 0;
	public int cursorx = 0;
	public int cursory = 0;
	public int offsetx = 0;
	public int offsety = 0;
	public boolean beamCursor = false;
	
	//TODO : CLICK SET CURSOR POSITION - ADD DEL KEY - ALWAYS HAVE AT LEAST ONE CARAC BEFORE CURSOR WHEN DELETING
	
	//TODO : setText cause a StringIndexOutOfBoundsException because of a cursor mess or something (fixed... I think)
	
	public TextFieldComponent(float x, float y, int w, int h, Game game, SlymyFont font) {
		
		this(x, y, w, h, game, font, RenderType.ONMAP);

	}
	
	public TextFieldComponent(float x, float y, int w, int h, Game game, SlymyFont font, RenderType type) {
		
		super(x, y, w, h, game, type);
		
		this.f = font;
		this.ghostF = font;
		
		this.addMouseButtonToListen(MouseButtons.LEFT_BUTTON);
		this.addMouseButtonToListen(MouseButtons.RIGHT_BUTTON);
		this.addMouseButtonToListen(MouseButtons.MIDDLE_BUTTON);	
		this.addKeyToListen(Keyboard.KEY_ESCAPE);
		this.addKeyToListen(Keyboard.KEY_BACK);
		this.addKeyToListen(Keyboard.KEY_LEFT);
		this.addKeyToListen(Keyboard.KEY_RIGHT);
		this.addKeyToListen(Keyboard.KEY_UP);
		this.addKeyToListen(Keyboard.KEY_DOWN);
		this.addKeyToListen(Keyboard.KEY_DELETE);
		this.addKeyToListen(Keyboard.KEY_RETURN);
		
		text.add("");
		
		cursorAnimation = new Animation(new AnimationFrame[] {
				
				new AnimationFrame() {
					
					@Override
					public void render(float x, float y, int w, int h, Color c) {
						
						Renderer.renderLine(x, y, x + w, y + h, c);
						
					}
					
				},
				
				new AnimationFrame() {
					
					@Override
					public void render(float x, float y, int w, int h, Color c) {}
					
				}
				
		});
		
		cursorAnimation.setSpeed(1.5f);
		cursorAnimation.setPlaying(true);
		
	}
	
	public boolean isMultilines() {
		
		return allowMultilines;
		
	}
	
	public void setAllowMultilines(boolean allowMultilines) {
		
		this.allowMultilines = allowMultilines;
		
	}
	
	public List<String> getLines() {
		
		return text;
		
	}
	
	public int length() {
		
		int i = 0;
		
		for(String s : text) {
			
			i += s.length();
			
		}
		
		return i;
		
	}
	
	public String getText() {
		
		if(text.size() > 1) {
		
			String textLine = "";
			
			int i = 0;
			
			for(String s : text) {
				
				textLine += ((i == 0 ? "" : "\n") + s);
				
				i++;
				
			}
			
			return textLine;
		
		} else if(text.size() == 1) {
			
			return text.get(0);
			
		} else {
			
			return "";
			
		}
		
	}
	
	public void setString(String text) {
		
		this.text = new ArrayList<String>(Arrays.asList(text.split("\n")));
		
	}
	
	public int getCap() {
		
		return cap;
		
	}
	
	public void setCap(int cap) {
		
		this.cap = cap;
		
	}
	
	@Override
	public void keyTyped(char key, boolean eventKeyState) {
		
		if(!eventKeyState || !focus || !activated) {
			
			return;
			
		}
		
		if(f.getCharset().contains(key + "")) {
		
			type(key);
		
		} else if(allowMultilines && (int) key == 13) { //NEWLINE

			newLine();
			
		} else if((int) key == 8) {
			
			backspace();
			
		}
		
	}
	
	@Override
	public void componentUpdate(float xCursor, float yCursor, Game game) {
		
		if(!activated || !focus) {
			
			return;
			
		}
		
		if(System.currentTimeMillis() - backDuration >= 500l) {
			
			backDuration += 40;
			
			backspace();
			
		}
		
		if(System.currentTimeMillis() - newLineDuration >= 500l) {
			
			newLineDuration += 40;
			
			newLine();
			
		}
		
		if(System.currentTimeMillis() - delDuration >= 500l) {
			
			delDuration += 40;
			
			delete();
			
		}
		
		if(System.currentTimeMillis() - upDuration >= 500l) {
			
			upDuration += 40;
			
			up();
			
		}
		
		if(System.currentTimeMillis() - downDuration >= 500l) {
			
			downDuration += 40;
			
			down();

		}
		
		if(System.currentTimeMillis() - rightDuration >= 500l) {
			
			rightDuration += 40;
			
			right();
			
		}
		
		if(System.currentTimeMillis() - leftDuration >= 500l) {
			
			leftDuration += 40;
			
			left();
			
		}
		
	}
	
	private void type(char key) {
		
		cursorAnimation.resetAnimation();
		
		if(text.size() == 0) {
			
			text.add(key + "");
			
			cursorx++;
			desiredCursorX = f.getWidth(text.get(cursory).substring(0, cursorx));
			
			textChanged();
			
		} else {
			
			if(cap <= -1) {
				
				text.set(cursory, text.get(cursory).substring(0, cursorx) + key + text.get(cursory).substring(cursorx, text.get(cursory).length()));
				
				cursorx++;
				desiredCursorX = f.getWidth(text.get(cursory).substring(0, cursorx));
				
				textChanged();

			} else if(length() + 1 <= cap) {
				
				text.set(cursory, text.get(cursory).substring(0, cursorx) + key + text.get(cursory).substring(cursorx, text.get(cursory).length()));
				
				cursorx++;
				desiredCursorX = f.getWidth(text.get(cursory).substring(0, cursorx));
				
				textChanged();
				
			}
		
		}
		
	}
	
	private void up() {
		
		if(cursory > 0) {
			
			cursorAnimation.resetAnimation();
			
			cursory--;
			
			//Binary search go brrrr
			
			int a = 0;
			int b = text.get(cursory).length();
			int m = (a+b) / 2;
			
			while(b-a > 1) {
				
				int w = f.getWidth(text.get(cursory).substring(0, m));
				
				if(w < desiredCursorX) {
					
					a = m;
					
				} else if(w == desiredCursorX) {
					
					a = m;
					b = m;
					break;
					
				} else {
					
					b = m;
					
				}
				
				m = (a+b) / 2;
				
			}
			
			if(a == b) {
				
				cursorx = a;
				
			} else {
				
				if(desiredCursorX - f.getWidth(text.get(cursory).substring(0, a)) <= f.getWidth(text.get(cursory).substring(0, b)) - desiredCursorX) {
					
					cursorx = a;
					
				} else {
					
					cursorx = b;
					
				}
				
			}
			
		}
		
	}
	
	private void down() {
		
		if(cursory + 1 < text.size()) {
			
			cursorAnimation.resetAnimation();
			
			cursory++;
			
			//Binary search go brrrr
			
			int a = 0;
			int b = text.get(cursory).length();
			int m = (a+b) / 2;
			
			while(b-a > 1) {
				
				int w = f.getWidth(text.get(cursory).substring(0, m));
				
				if(w < desiredCursorX) {
					
					a = m;
					
				} else if(w == desiredCursorX) {
					
					a = m;
					b = m;
					break;
					
				} else {
					
					b = m;
					
				}
				
				m = (a+b) / 2;
				
			}
			
			if(a == b) {
				
				cursorx = a;
				
			} else {
				
				if(desiredCursorX - f.getWidth(text.get(cursory).substring(0, a)) <= f.getWidth(text.get(cursory).substring(0, b)) - desiredCursorX) {
					
					cursorx = a;
					
				} else {
					
					cursorx = b;
					
				}
				
			}
			
		}
		
	}
	
	private void right() {
		
		if(cursorx < text.get(cursory).length()) {
			
			cursorAnimation.resetAnimation();
			
			cursorx++;
			desiredCursorX = f.getWidth(text.get(cursory).substring(0, cursorx));
			
		} else if(cursory + 1 < text.size()) {
			
			cursorAnimation.resetAnimation();
			
			cursorx = 0;
			desiredCursorX = 0;
			
			cursory++;
			
		}
		
	}
	
	private void left() {
		
		if(cursorx > 0) {
			
			cursorAnimation.resetAnimation();
			
			cursorx--;
			desiredCursorX = f.getWidth(text.get(cursory).substring(0, cursorx));
			
		} else if(cursory > 0) {
			
			cursorAnimation.resetAnimation();
			
			cursory--;
			
			cursorx = text.get(cursory).length();
			desiredCursorX = f.getWidth(text.get(cursory));
			
		}
		
	}
	
	private void newLine() {
		
		cursorAnimation.resetAnimation();
		text.add(cursory + 1, text.get(cursory).substring(cursorx, text.get(cursory).length()));
		text.set(cursory, text.get(cursory).substring(0, cursorx));
		cursory++;
		cursorx = 0;
		desiredCursorX = 0;
		
		textChanged();
		
	}
	
	private void delete() {
		
		if(length() > 0) {
			
			cursorAnimation.resetAnimation();
			
			if(text.size() > 0) {
				
				String line = text.get(cursory).substring(cursorx, text.get(cursory).length());
				
				if(line.length() == 0 && cursory + 1 < text.size()) {

					text.set(cursory, text.get(cursory) + text.get(cursory + 1));
					text.remove(cursory + 1);
					
				} else if(line.length() > 0) {

					text.set(cursory, text.get(cursory).substring(0, cursorx) + text.get(cursory).substring(cursorx+1, text.get(cursory).length()));
					
				}
				
				textChanged();
				
			}
			
		}
		
	}
	
	private void backspace() {
		
		if(length() > 0) {
			
			cursorAnimation.resetAnimation();
			
			if(text.size() > 0) {
				
				String completeLine = text.get(cursory);
				String line = text.get(cursory).substring(0, cursorx);
				
				if(line.length() == 0 && cursory > 0) {
					
					cursory--;
					cursorx = text.get(cursory).length();
					desiredCursorX = f.getWidth(text.get(cursory)); 
					text.set(cursory, text.get(cursory) + completeLine);
					text.remove(cursory + 1);
					
				} else if(cursorx > 0) {

					text.set(cursory, text.get(cursory).substring(0, cursorx - 1) + text.get(cursory).substring(cursorx, text.get(cursory).length()));
					cursorx--;
					desiredCursorX = f.getWidth(text.get(cursory).substring(0, cursorx)); 
					
				}
				
				textChanged();
				
			}
			
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
		
		if(!this.activated) {
			
			return;
			
		}
		
		if(game.getComponentHover() != null && game.getComponentHover().equals(this) && button == MouseButtons.LEFT_BUTTON) {
			
			if(!focus) {
				
				focus = true;
				
				focusGained();
				
				cursorAnimation.resetAnimation();
				
			}
			
		} else if((game.getComponentHover() == null || !game.getComponentHover().equals(this)) && focus) {
			
			focus = false;
			
			focusLost();
			
		}
		
	}
	
	@Override
	public void keyPressed(int key) {
		
		if(!activated || !focus) {
			
			return;
			
		}
		
		if(key == Keyboard.KEY_BACK) {
			
			backDuration = System.currentTimeMillis();

		} else if(key == Keyboard.KEY_RETURN) {
			
			newLineDuration = System.currentTimeMillis();

		} else if(key == Keyboard.KEY_DELETE) {
			
			delDuration = System.currentTimeMillis();
			
			delete();

		} else if(key == Keyboard.KEY_UP) {
			
			upDuration = System.currentTimeMillis();
			
			up();

		} else if(key == Keyboard.KEY_DOWN) {
			
			downDuration = System.currentTimeMillis();
			
			down();

		} else if(key == Keyboard.KEY_RIGHT) {
			
			rightDuration = System.currentTimeMillis();
			
			right();

		} else if(key == Keyboard.KEY_LEFT) {
			
			leftDuration = System.currentTimeMillis();
			
			left();

		} else {
		
			if(focus) {
				
				focus = false;
				
			}
		
		}
		
	}
	
	public SlymyFont getFont() {
		
		return f;
		
	}

	public void setFont(SlymyFont f) {
		
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
		
		if(this.cursorx > text.length()) {
			
			this.cursorx = text.length();
			
		}
		
		if(this.offsetx > text.length()) {
			
			this.offsetx = 0;
			
		}
		
		this.text = new ArrayList<String>(Arrays.asList(text));
		 
	}

	public SlymyFont getGhostFont() {
		
		return ghostF;
		
	}
	
	public SlymyFont getGhostFontName() {
		
		return ghostF;
		
	}

	public void setGhostFont(SlymyFont ghostF) {
		
		this.ghostF = ghostF;
		
	}

	public String getGhostText() {
		
		return ghostText;
		
	}

	public void setGhostText(String ghostText) {
		
		this.ghostText = ghostText;
		
	}

	@Override
	public void render() {
		
		if(!isVisible()) {
			
			return;
			
		}
		
		renderBackground();
		
		List<String> linesTemp = new ArrayList<String>();
		String lineTemp = "";
		
		if(text.size() <= 1 && (text.size() == 0 || text.get(0).equals("")) && !focus && ghostText != null) {
			
			if(ghostF.getHeight() <= h - (margin * 2)) {
				
				for(char c : ghostText.toCharArray()) {
					
					if(ghostF.getWidth(lineTemp + c) <= w - (margin * 2)) {
						
						lineTemp += c;
						
					}
					
				}
				
			}
			
			Renderer.renderText(x + margin, y + margin, ghostF, lineTemp, ghostF.getColor());
			
		} else {
		
			int i = 1;
			
			int maxLines = (h - (margin * 2)) / f.getHeight();

			if(cursory + 1 > maxLines + offsety) {
				
				offsety = cursory + 1 - maxLines;
				
			} else if(cursory < offsety) {
				
				offsety = cursory;
				
			}
			
			
			int cursorxTmp = cursorx;
			int cursoryTmp = cursory;
			
			List<String> linesTmp = new ArrayList<String>(text);
			List<String> linesCalc = new ArrayList<String>(text);
			
			if(cursorxTmp < offsetx) {
				
				offsetx = cursorxTmp;
				
			}
				
				String cursorLine = linesCalc.get(cursoryTmp).substring(offsetx, cursorxTmp);

				if(f.getWidth(cursorLine) > w - (margin * 2) - 5) {  //5 is the minimum amount of pixel of the next letter we need to see
					
					boolean fit = false;
					
					for(int k = offsetx; k <= cursorxTmp && !fit; k++) {
						
						cursorLine = linesCalc.get(cursoryTmp).substring(k, cursorxTmp);
						
						if(f.getWidth(cursorLine) <= w - (margin * 2)) {
							
							fit = true;
							offsetx = k;
							
						}
						
					}
					
				} 
				
			for(String line : linesTmp) {
				
				if(i > offsety && f.getHeight() * (i - offsety) <= h - (margin * 2)) {
				
					int j = 1;
					
					for(char c : line.toCharArray()) {
						
						int width = f.getWidth(lineTemp + c);
						
						if(j > offsetx && width <= w - (margin * 2)) {
							
							lineTemp += c;
							
						} else if(width > w - (margin * 2)) {
							
							break;
							
						}
						
						j++;
						
					}
					
					linesTemp.add(lineTemp);
					
					lineTemp = "";
					
				}
				
				i++;
				
			}
			
			i = 0;
			
			int cursorypos = cursoryTmp - offsety;
			int cursorxpos = f.getWidth(linesCalc.get(cursoryTmp).substring(offsetx, cursorxTmp));
			
			for(String line : linesTemp) {

				Renderer.renderText(x + margin, y + margin + (f.getHeight() * i), f, line, f.getColor());
				
				if(i == cursorypos && focus && x + margin + cursorxpos + f.getWidth(" ") + (f.getF().getSize() / 10) <= x + w) {
					
					Renderer.renderAnimation(x + margin + cursorxpos + (f.getF().getSize() / 50), y + margin + (f.getHeight() * i), 0, f.getHeight(), cursorAnimation, f.getColor());
					
				}
				
				i++;
				
			}
		
		}
		
		renderForeground();

	}
	
	@Override
	public void keyReleased(int key) {
		
		if(key == Keyboard.KEY_BACK) {
			
			backDuration = Long.MAX_VALUE;
			
		} else if(key == Keyboard.KEY_DELETE) {
			
			delDuration = Long.MAX_VALUE;
			
		} else if(key == Keyboard.KEY_RETURN) {
			
			newLineDuration = Long.MAX_VALUE;
			
		} else if(key == Keyboard.KEY_UP) {
			
			upDuration = Long.MAX_VALUE;
			
		} else if(key == Keyboard.KEY_DOWN) {
			
			downDuration = Long.MAX_VALUE;
			
		} else if(key == Keyboard.KEY_LEFT) {
			
			leftDuration = Long.MAX_VALUE;
			
		} else if(key == Keyboard.KEY_RIGHT) {
			
			rightDuration = Long.MAX_VALUE;
			
		}
		
	}
	
	public Point getCursorPosition() {
		
		return new Point(cursorx, cursory);
		
	}
	
	public void setCursorPosition(int x, int y) {
		
		cursorx = x; 
		cursory = y;

	}
	
	public abstract void renderBackground();
	
	public abstract void renderForeground();
	
	public void focusGained() {}
	
	public void focusLost() {}
	
	public void textChanged() {}

}
