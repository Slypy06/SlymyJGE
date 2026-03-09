package fr.slypy.slymyjge.components;

import java.awt.Color;

import org.lwjgl.input.Keyboard;
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
import fr.slypy.slymyjge.utils.SimpleEntry;

public abstract class TextFieldComponent extends Component {

	private StringBuilder text;
	private int cap = -1;
	private boolean allowMultilines = false;
	private boolean focus;
	private SlymyFont font;
	private SlymyFont ghostFont;
	private String ghostText;
	private DynamicText ghostTextShape;
	private DynamicText textShape;
	private int margin = 10;
	private Animation cursorAnimation;
	private long delDuration = Long.MAX_VALUE;
	private long newLineDuration = Long.MAX_VALUE;
	private long backDuration = Long.MAX_VALUE;
	private long upDuration = Long.MAX_VALUE;
	private long downDuration = Long.MAX_VALUE;
	private long leftDuration = Long.MAX_VALUE;
	private long rightDuration = Long.MAX_VALUE;
	private float interline = 0.0f;

	private int cursorIndex = 0;

	private int offsetx = 0;
	private int offsety = 0;
	
	//TODO add highlighting

	public TextFieldComponent(float x, float y, int w, int h, Game game, SlymyFont font) {

		super(x, y, w, h, game);

		this.font = font;
		this.ghostFont = font;
		this.text = new StringBuilder();

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

		cursorAnimation = new Animation(new AnimationFrame[] {

				new AnimationFrame() {

					@Override
					public Shape getShape(Vector2f position, Vector2f size) {

						return new Line(position, Vector2f.add(position, size, null), 2, Color.white);

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

		this.ghostTextShape = new DynamicText(ghostFont, "", game);
		this.textShape = new DynamicText(font, "", game);
		this.textShape.setLineSpacing(interline);

	}

	public boolean isMultilines() {

		return allowMultilines;

	}

	public void setAllowMultilines(boolean allowMultilines) {

		this.allowMultilines = allowMultilines;

	}


	public int length() {

		int i = 0;

		for (int c = 0; c < text.length(); c++) {

			i += font.getCharset().contains(text.charAt(c) + "") ? 1 : 0;

		}

		return i;

	}

	public String getText() {

		return text.toString();

	}

	public int getCap() {

		return cap;

	}

	public void setCap(int cap) {

		this.cap = cap;

	}

	public int getCursorLine() {

		int line = 0;

		for (int i = 0; i < cursorIndex; i++) {

			if (text.charAt(i) == '\n')
				line++;

		}

		return line;

	}

	public int getCursorColumn() {

		int col = 0;

		for (int i = 0; i < cursorIndex; i++) {

			if (text.charAt(i) == '\n') {

				col = 0;

			} else {

				col++;

			}

		}

		return col;

	}

	private int getCursorPixelX() {

		int line = getCursorLine();
		String[] lines = text.toString().split("\n", -1);
		int col = getCursorColumn();
		return font.getWidth(lines[line].substring(0, Math.min(lines[line].length(), col)));

	}

	private int findIndexOnLine(int targetLine, int desiredPixelX) {

		String[] lines = text.toString().split("\n", -1);
		String line = lines[targetLine];

		// Binary search for closest column
		int a = 0;
		int b = line.length();

		while (b - a > 1) {

			int m = (a + b) / 2;
			int w = font.getWidth(line.substring(0, m));

			if (w < desiredPixelX) {

				a = m;

			} else if (w == desiredPixelX) {

				a = b = m;
				break;

			} else {

				b = m;

			}

		}

		int col;

		if (a == b) {

			col = a;

		} else {

			int wA = font.getWidth(line.substring(0, a));
			int wB = font.getWidth(line.substring(0, b));
			col = (desiredPixelX - wA <= wB - desiredPixelX) ? a : b;

		}

		int index = 0;
		String[] allLines = lines;

		for (int l = 0; l < targetLine; l++) {

			index += allLines[l].length() + 1;

		}

		index += col;
		return index;

	}

	private int lineCount() {

		int count = 1;

		for (int i = 0; i < text.length(); i++) {

			if (text.charAt(i) == '\n')
				count++;

		}

		return count;

	}

	@Override
	public void keyTyped(char key, boolean eventKeyState) {

		if (!eventKeyState || !focus || !activated)
			return;

		if (font.getCharset().contains(key + "")) {

			type(key);

		} else if (allowMultilines && (int) key == 13) { // NEWLINE

			newLine();

		} else if ((int) key == 8) {

			backspace();

		}

	}

	@Override
	public void componentUpdate(double alpha) {

		if (!activated || !focus)
			return;
		
		cursorAnimation.step(alpha);

		if (System.currentTimeMillis() - backDuration >= 500L) {

			backDuration += 40;
			backspace();

		}

		if (System.currentTimeMillis() - newLineDuration >= 500L) {

			newLineDuration += 40;
			newLine();

		}

		if (System.currentTimeMillis() - delDuration >= 500L) {

			delDuration += 40;
			delete();

		}

		if (System.currentTimeMillis() - upDuration >= 500L) {

			upDuration += 40;
			up();

		}

		if (System.currentTimeMillis() - downDuration >= 500L) {

			downDuration += 40;
			down();

		}

		if (System.currentTimeMillis() - rightDuration >= 500L) {

			rightDuration += 40;
			right();

		}

		if (System.currentTimeMillis() - leftDuration >= 500L) {

			leftDuration += 40;
			left();

		}

	}

	private void type(char key) {

		cursorAnimation.resetAnimation();

		if (cap == -1 || length() < cap) {

			text.insert(cursorIndex, key);
			cursorIndex++;
			
			updateOffsets();
			
			textChanged();

		}

	}

	private void newLine() {

		cursorAnimation.resetAnimation();
		text.insert(cursorIndex, '\n');
		cursorIndex++;
		
		updateOffsets();
		
		textChanged();

	}

	private void backspace() {

		if (cursorIndex > 0) {

			cursorAnimation.resetAnimation();
			text.deleteCharAt(cursorIndex - 1);
			cursorIndex--;
			
			updateOffsets();
			
			textChanged();

		}

	}

	private void delete() {

		if (cursorIndex < text.length()) {

			cursorAnimation.resetAnimation();
			text.deleteCharAt(cursorIndex);
			
			updateOffsets();

			textChanged();

		}

	}

	private void left() {

		if (cursorIndex > 0) {

			cursorAnimation.resetAnimation();
			cursorIndex--;
			updateOffsets();

		}

	}

	private void right() {

		if (cursorIndex < text.length()) {

			cursorAnimation.resetAnimation();
			cursorIndex++;
			updateOffsets();

		}

	}

	private void up() {

		int currentLine = getCursorLine();

		if (currentLine > 0) {

			cursorAnimation.resetAnimation();
			int desiredPixelX = getCursorPixelX();
			cursorIndex = findIndexOnLine(currentLine - 1, desiredPixelX);
			updateOffsets();

		}

	}

	private void down() {

		int currentLine = getCursorLine();

		if (currentLine + 1 < lineCount()) {

			cursorAnimation.resetAnimation();
			int desiredPixelX = getCursorPixelX();
			cursorIndex = findIndexOnLine(currentLine + 1, desiredPixelX);
			updateOffsets();

		}

	}

	@Override
	public void mouseButtonPressed(int button) {

		if (!this.activated)
			return;

		if (game.getComponentHover() != null && game.getComponentHover().equals(this) && button == MouseButtons.LEFT_BUTTON) {

			float mouseX = isAbsoluteCoordinate()
	                ? game.getAbsoluteXCursor()
	                : game.getRelativeXCursor();
	        float mouseY = isAbsoluteCoordinate()
	                ? game.getAbsoluteYCursor()
	                : game.getRelativeYCursor();

	        int relativeX = (int)(mouseX - getPosition().getX() - margin) + offsetx;
	        int relativeY = (int)(mouseY - getPosition().getY() - margin) + offsety;

	        int clickedLine = Math.max(0, Math.min(relativeY / font.getHeight(), lineCount() - 1));

	        cursorIndex = findIndexOnLine(clickedLine, relativeX);
			
	        updateOffsets();
	        cursorAnimation.resetAnimation();
	        
			if (!focus) {

				focus = true;
				focusGained();

			}
			
		} else if ((game.getComponentHover() == null || !game.getComponentHover().equals(this)) && focus) {

			focus = false;
			focusLost();

		}

	}

	@Override
	public void keyPressed(int key) {

		if (!activated || !focus)
			return;

		switch (key) {

			case Keyboard.KEY_BACK:
				backDuration = System.currentTimeMillis();
				break;

			case Keyboard.KEY_RETURN:
				newLineDuration = System.currentTimeMillis();
				break;

			case Keyboard.KEY_DELETE:
				delDuration = System.currentTimeMillis();
				delete();
				break;

			case Keyboard.KEY_UP:
				upDuration = System.currentTimeMillis();
				up();
				break;

			case Keyboard.KEY_DOWN:
				downDuration = System.currentTimeMillis();
				down();
				break;

			case Keyboard.KEY_RIGHT:
				rightDuration = System.currentTimeMillis();
				right();
				break;

			case Keyboard.KEY_LEFT:
				leftDuration = System.currentTimeMillis();
				left();
				break;

			case Keyboard.KEY_ESCAPE:

				focus = false;
				focusLost();
				break;

		}

	}

	@Override
	public void keyReleased(int key) {

		switch (key) {

			case Keyboard.KEY_BACK:
				backDuration = Long.MAX_VALUE;
				break;

			case Keyboard.KEY_DELETE:
				delDuration = Long.MAX_VALUE;
				break;

			case Keyboard.KEY_RETURN:
				newLineDuration = Long.MAX_VALUE;
				break;

			case Keyboard.KEY_UP:
				upDuration = Long.MAX_VALUE;
				break;

			case Keyboard.KEY_DOWN:
				downDuration = Long.MAX_VALUE;
				break;

			case Keyboard.KEY_LEFT:
				leftDuration = Long.MAX_VALUE;
				break;

			case Keyboard.KEY_RIGHT:
				rightDuration = Long.MAX_VALUE;
				break;

		}

	}
	
	private void updateOffsets() {

	    int lineHeight = (int) (font.getHeight() * (1+interline));
	    int visibleWidth = (int) size.getX() - 2 * margin;
	    int visibleHeight = (int) size.getY() - 2 * margin;

	    // --- Vertical (line-based, offset in pixels) ---
	    if (allowMultilines) {

	        int cursorLine = getCursorLine();
	        int cursorTop    = cursorLine * lineHeight;
	        int cursorBottom = cursorTop + font.getHeight();

	        if (cursorTop < offsety) {
	            offsety = cursorTop;
	        } else if (cursorBottom > offsety + visibleHeight) {
	            offsety = cursorBottom - visibleHeight;
	        }
	        
	    }

	    // --- Horizontal ---
	    int cursorPixelX = getCursorPixelX();
	    int col = getCursorColumn();
	    String currentLine = text.toString().split("\n", -1)[getCursorLine()];
	    int minWidth = col <= 0 ? 0 : font.getWidth(currentLine.charAt(col-1));

	    // Requirement 1: cursor must be fully inside the visible width
	    if (cursorPixelX > offsetx + visibleWidth) {
	        // Cursor is past the right edge — scroll right
	        offsetx = cursorPixelX - visibleWidth;
	    }
	    // Cursor is left of visible area (e.g. after pressing left)
	    if (cursorPixelX-minWidth < offsetx) {
	    	
	        offsetx = cursorPixelX-minWidth;
	        
	    }
	    
	}

	public SlymyFont getFont() {

		return font;

	}

	public void setFont(SlymyFont f) {

		this.font = f;
		this.textShape.setFont(f);

	}

	public int getMargin() {

		return margin;

	}

	public void setMargin(int margin) {

		this.margin = margin;

	}
	
	public float getInterline() {

		return interline;

	}

	public void setInterline(float interline) {

		this.interline = interline;
		textShape.setLineSpacing(interline);

	}

	public void setText(String newText) {

		this.text = new StringBuilder(newText != null ? newText : "");
		cursorIndex = Math.min(cursorIndex, this.text.length());
		updateOffsets();

	}

	public SlymyFont getGhostFont() {

		return ghostFont;

	}

	public void setGhostFont(SlymyFont ghostF) {

		this.ghostFont = ghostF;
		this.ghostTextShape.setFont(ghostF);

	}

	public String getGhostText() {

		return ghostText;

	}

	public void setGhostText(String ghostText) {

		this.ghostText = ghostText;

	}

	public boolean isFocus() {

		return focus;

	}

	public void setFocus(boolean focus) {

		this.focus = focus;

	}

	/**
	 * Returns (column, line) for the current cursor position. Prefer using
	 * {@link #cursorIndex} directly where possible.
	 */
	public SimpleEntry<Integer, Integer> getCursorPosition() {

		return new SimpleEntry<>(getCursorColumn(), getCursorLine());

	}

	/**
	 * Sets the cursor by (column, line). Converts to an absolute index internally.
	 */
	public void setCursorPosition(int col, int line) {

		cursorIndex = findIndexOnLine(Math.min(line, lineCount() - 1),
				font.getWidth(text.toString().split("\n", -1)[Math.min(line, lineCount() - 1)].substring(0, col)));

	}

	@Override
	public void render() {
		
		if(!textShape.getText().equals(text.toString())) {
			
			textShape.setText(text.toString());
			
		}
		
		if(ghostText != null && !ghostTextShape.getText().equals(ghostText.toString())) {
			
			ghostTextShape.setText(ghostText.toString());
			
		}

	    renderBackground();
	    
	    NewGenRenderer.renderInsideArea((int) position.getX() + margin, (int) position.getY() + margin, (int) size.getX() - 2 * margin, (int) size.getY() - 2 * margin, () -> {

	        // Ghost text — only when empty and unfocused
	        if (text.length() == 0 && ghostText != null && !ghostText.isEmpty()) {
	            NewGenRenderer.renderText(ghostTextShape, new Vector2f(margin-offsetx, margin-offsety));
	        }

	        // Normal text
	        if (text.length() > 0) {
	            NewGenRenderer.renderText(textShape, new Vector2f(margin-offsetx, margin-offsety));
	        }
	        
	    });

        if (focus) {

            int cursorLine = getCursorLine();
            int cursorPixelX = getCursorPixelX();
            int lineHeight = (int) (font.getHeight()*(1+interline));

            Vector2f cursorPos = new Vector2f(margin + cursorPixelX - offsetx, margin + cursorLine * lineHeight - offsety);
            Vector2f cursorSize = new Vector2f(0, font.getHeight());

            NewGenRenderer.renderShape(cursorAnimation.getShape(cursorPos, cursorSize).color(font.getColor()));
            
        }

	    renderForeground();

	}

	public abstract void renderBackground();

	public abstract void renderForeground();

	public void focusGained() {}

	public void focusLost() {}

	public void textChanged() {}

}