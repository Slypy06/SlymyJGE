package fr.slypy.slymyjge.components;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.Point;
import org.lwjgl.util.vector.Vector2f;

import fr.slypy.slymyjge.Game;
import fr.slypy.slymyjge.animations.framed.Animation;
import fr.slypy.slymyjge.animations.framed.AnimationFrame;
import fr.slypy.slymyjge.font.SlymyFont;
import fr.slypy.slymyjge.graphics.NewGenRenderer;
import fr.slypy.slymyjge.graphics.shape.EmptyShape;
import fr.slypy.slymyjge.graphics.shape.Line;
import fr.slypy.slymyjge.graphics.shape.Shape;
import fr.slypy.slymyjge.graphics.shape.dynamic.DynamicText;
import fr.slypy.slymyjge.utils.MouseButtons;

public abstract class TextFieldComponent extends Component {

	public List<String> text = new ArrayList<String>();
	public int cap = -1;
	public boolean allowMultilines = false;
	public boolean focus;
	public SlymyFont f;
	public SlymyFont ghostF;
	public String ghostText;
	public DynamicText ghostTextShape;
	public DynamicText textShape;
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
	
	public TextFieldComponent(float x, float y, int w, int h, Game game, SlymyFont font) {
		
		super(x, y, w, h, game);
		
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
					public Shape getShape(Vector2f position, Vector2f size) {

						return new Line(position, Vector2f.add(position, size, null), 3, Color.white);
						
					}
					
				},
				
				new AnimationFrame() {

					@Override
					public Shape getShape(Vector2f position, Vector2f size) {

						return new EmptyShape();
						
					}
					
				}
				
		});
		
		cursorAnimation.setSpeed(1.5f);
		cursorAnimation.setPlaying(true);
		
		this.ghostTextShape = new DynamicText(ghostF, "", game);
		this.textShape = new DynamicText(font, "", game);
		
		
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
	public void componentUpdate() {
		
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
		this.textShape.setFont(f);
		
	}

	public int getMargin() {
		
		return margin;
		
	}

	public void setMargin(int margin) {
		
		this.margin = margin;
		
	}

	public void setText(List<String> text) {
		
		this.text = text;
		
		StringBuilder b = new StringBuilder();
		text.forEach(line -> b.append(line).append('\n'));
		
		this.textShape.setText(b.toString().substring(0, b.toString().length()-1));
		
	}
	
	public void setText(String text) {
		
		if(this.cursorx > text.length()) {
			
			this.cursorx = text.length();
			
		}
		
		if(this.offsetx > text.length()) {
			
			this.offsetx = 0;
			
		}
		
		this.text = new ArrayList<String>(Arrays.asList(text));
		this.textShape.setText(text);
		
	}

	public SlymyFont getGhostFont() {
		
		return ghostF;
		
	}

	public void setGhostFont(SlymyFont ghostF) {
		
		this.ghostF = ghostF;
		this.ghostTextShape.setFont(ghostF);
		
	}

	public String getGhostText() {
		
		return ghostText;
		
	}

	public void setGhostText(String ghostText) {
		
		this.ghostText = ghostText;
		this.ghostTextShape.setText(ghostText);
		
	}

	@Override
	public void render() {
		
		if(!isVisible()) {
			
			return;
			
		}
		
		renderBackground();
		
		List<String> linesTemp = new ArrayList<String>();
		String lineTemp = "";
		
		if(text.size() <= 1 && (text.size() == 0 || text.get(0).equals("")) && !focus && ghostText != null && !ghostText.isEmpty()) {
			
			NewGenRenderer.renderInsideArea((int) position.getX() + margin, (int) position.getY() + margin, (int) size.getX() - margin, (int) size.getY() - margin, () -> {
				
				NewGenRenderer.renderText(ghostTextShape, new Vector2f(position.getX() + margin, position.getY() + margin));
				
			});
			
		} else {
		
			int i = 1;
			
			int maxLines = (int) Math.floor((size.getY() - (margin * 2)) / f.getHeight());

			
			int cursorxSnapshot = cursorx;
			int cursorySnapshot = cursory;
			
			if(cursorySnapshot + 1 > maxLines + offsety) {
				
				offsety = cursorySnapshot + 1 - maxLines;
				
			} else if(cursorySnapshot < offsety) {
				
				offsety = cursorySnapshot;
				
			}
			
			List<String> linesTmp = new ArrayList<String>(text);
			List<String> linesCalc = new ArrayList<String>(text);
			
			if(cursorxSnapshot < offsetx) {
				
				offsetx = cursorxSnapshot;
				
			}
				
			String cursorLine = linesCalc.get(cursorySnapshot).substring(offsetx, cursorxSnapshot);

			if(f.getWidth(cursorLine) > size.getX() - (margin * 2) - 5) {  //5 is the minimum amount of pixel of the next letter we need to see
					
				boolean fit = false;
					
				for(int k = offsetx; k <= cursorxSnapshot && !fit; k++) {
						
					cursorLine = linesCalc.get(cursorySnapshot).substring(k, cursorxSnapshot);
						
					if(f.getWidth(cursorLine) <= size.getX() - (margin * 2)) {
							
						fit = true;
						offsetx = k;
							
					}
						
				}
					
			} 
			
			int offsetxPx = f.getWidth("cursorLine");
			int offsetyPx = (int) (1 + textShape.getLineSpacing())*f.getHeight()*textShape.getSize();
			
			NewGenRenderer.renderInsideArea((int) position.getX() + margin, (int) position.getY() + margin, (int) size.getX() - margin, (int) size.getY() - margin, () -> {

				int cursorypos = cursorySnapshot - offsety;
				int cursorxpos = f.getWidth(linesCalc.get(cursorySnapshot).substring(offsetx, cursorxSnapshot));

				System.out.println("rendering " + textShape.getText());
				
				NewGenRenderer.renderText(textShape, new Vector2f(position.getX() + margin - offsetxPx, position.getY() + margin - offsetyPx));

				NewGenRenderer.renderShape(cursorAnimation.getShape(position.getX() + margin + cursorxpos + (textShape.getSize() / 50), position.getY() + margin + (cursorypos*textShape.getSize()), 0, textShape.getHeight()).color(f.getColor()));
			
			});
		
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