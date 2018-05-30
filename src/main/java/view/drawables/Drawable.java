package view.drawables;

import view.screen.Screen;

public abstract class Drawable {
	/**
	 * Bounds for the Drawable object, and for the text it may or may not contain. There are two bounds to allow for
	 * padding around the text, and easier border drawing, if desired.
	 */
	protected Bounds borderBounds, textBounds;

	/**
	 * Screen on which the Drawable will be rendered.
	 */
	protected Screen parentScreen;

	/**
	 * Pixels of padding for optional use when determining the bounds of objects, or the spacing of them.
	 */
	protected final static int BORDER_PADDING = 2, MARKER_PADDING = 2, LINE_SPACING = 2;
	protected boolean drawn, fitsOnScreen = true, borderless = false;

	Drawable(Screen parentScreen) {
		this.parentScreen = parentScreen;
	}

	/**
	 * Draws the Drawable on to the parentScreen.
	 */
	public abstract void draw();

	/**
	 * Removes the Drawable from the parentScreen, by resetting all the colors on the pixels it occupied, and blanking
	 * all the characters.
	 */
	public abstract void remove();

	/**
	 * Prevents borders from being drawn around Drawables which would otherwise draw borders.
	 */
	public void setBorderless() {
		borderless = true;
	}

	/**
	 * Draws the borders for a Drawable, if it is not borderless and the implementation of draw() calls this method.
	 * Draws the borders using the borderBounds.
	 */
	protected void drawBorders() {
		if (borderless)
			return;
		for (int y = borderBounds.getTopLeftY(); y <= borderBounds.getBottomRightY(); y += (borderBounds.getBottomRightY() - borderBounds.getTopRightY()))
			for (int x = borderBounds.getTopLeftX() + 1; x < borderBounds.getTopRightX(); x++)
				parentScreen.drawChar(x, y, 0,'-');
		for (int y = borderBounds.getTopLeftY(); y <= borderBounds.getBottomRightY(); y++)
			for (int x = borderBounds.getTopLeftX(); x <= borderBounds.getTopRightX(); x += (borderBounds.getBottomRightX() - borderBounds.getTopLeftX()))
				parentScreen.drawChar(x, y, 0,'|');
	}

	/**
	 * Determines if the Drawable will fit on screen, and prints an error if not. The Drawable will also be prevented
	 * from drawing in order to avoid errors, by setting the fitsOnScreen boolean to false.
	 */
	protected void fitsOnScreen() {
		if (borderBounds == null)
			return;

		if (borderBounds.getTopLeftX() < 0 || borderBounds.getTopLeftY() < 0
				|| borderBounds.getBottomRightX() >= Screen.COLUMNS || borderBounds.getBottomRightY() >= Screen.ROWS) {
			fitsOnScreen = false;
			System.err.println("Drawable with bounds: " + borderBounds.toString() + " is out of bounds");
		}
	}
}
