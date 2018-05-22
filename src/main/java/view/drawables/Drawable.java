package view.drawables;

import view.screen.Screen;

public abstract class Drawable {
	protected Bounds bounds;
	protected Screen parentScreen;

	protected final static int BORDER_PADDING = 2, MARKER_PADDING = 2, LINE_SPACING = 2;
	protected boolean drawn;

	Drawable(Screen parentScreen) {
		this.parentScreen = parentScreen;
	}

	public abstract void draw();
	public abstract void remove();

	protected void drawBorders() {
		for (int y = bounds.getTopLeftY(); y <= bounds.getBottomRightY(); y += (bounds.getBottomRightY() - bounds.getTopRightY()))
			for (int x = bounds.getTopLeftX() + 1; x < bounds.getTopRightX() - 1; x++)
				parentScreen.drawChar(x, y, '-');
		for (int y = bounds.getTopLeftY(); y <= bounds.getBottomRightY(); y++)
			for (int x = bounds.getTopLeftX(); x <= bounds.getTopRightX(); x += (bounds.getBottomRightX() - bounds.getTopLeftX() - 1))
				parentScreen.drawChar(x, y, '|');
	}
}
