package view.drawables;

import view.screen.Screen;

public abstract class Drawable {
	protected Bounds borderBounds, textBounds;
	protected Screen parentScreen;

	protected final static int BORDER_PADDING = 2, MARKER_PADDING = 2, LINE_SPACING = 2;
	protected boolean drawn;

	Drawable(Screen parentScreen) {
		this.parentScreen = parentScreen;
	}

	public abstract void draw();
	public abstract void remove();

	protected void drawBorders() {
		for (int y = borderBounds.getTopLeftY(); y <= borderBounds.getBottomRightY(); y += (borderBounds.getBottomRightY() - borderBounds.getTopRightY()))
			for (int x = borderBounds.getTopLeftX() + 1; x < borderBounds.getTopRightX(); x++)
				parentScreen.drawChar(x, y, '-');
		for (int y = borderBounds.getTopLeftY(); y <= borderBounds.getBottomRightY(); y++)
			for (int x = borderBounds.getTopLeftX(); x <= borderBounds.getTopRightX(); x += (borderBounds.getBottomRightX() - borderBounds.getTopLeftX()))
				parentScreen.drawChar(x, y, '|');
	}
}
