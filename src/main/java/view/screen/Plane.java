package view.screen;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import view.View;

import javax.validation.constraints.NotNull;
import java.awt.*;
import java.util.ArrayList;

public class Plane {
	private Screen parentScreen;

	private ArrayList<ArrayList<Color>> backgroundLayer;
	private ArrayList<ArrayList<ArrayList<Text>>> textLayers;

	private ArrayList<Line[]> pixelBorders; // TODO: ALT USE HASHMAP IF NEED TO PRESERVE SPCIFIC BORDERS

	private ArrayList<Point> mouseClickHandlers;

	private Point onScreenDimensions;
	private Point topLeftInParent;
	private Point topLeftInLocal;

	/**
	 * Constructor for Plane. Initializes a Plane with the requested parentScreen, dimensions and initial text layers.
	 * Real dimensions for the plane, the dimensions it occupies on the parentScreen, are set to the width and height
	 * of the Screen.
	 * The top left coordinate in the parentScreen is (0,0), and the top left visible coordinate in the plane is (0,0)
	 *
	 * @param parentScreen   Screen on which the elements of the plane will be drawn on
	 * @param dimensions     Initial dimensions for the plane. It can be grown as needed
	 * @param initTextLayers Number of text layers the plane contains, allows for layered graphics
	 */
	public Plane(Screen parentScreen, Point dimensions, int initTextLayers) {
		initPlane(parentScreen, dimensions, initTextLayers,
				  new Point(Screen.COLUMNS, Screen.ROWS), new Point(0, 0), new Point(0, 0));
	}

	/**
	 * Constructor for Plane. Initializes a Plane with the requested parentScreen, dimensions, initial text layers,
	 * the dimensions the plane will occupy on the parentScreen, coordinate on parentScreen on which to place the top
	 * left coordinate of the plane, and top left coordinate to be rendered from the plane on to the parentScreen.
	 *
	 * @param parentScreen       Screen on which the elements of the plane will be drawn on
	 * @param planeDimensions    Initial dimensions for the plane (not bound by parentScreen). It can grow as needed
	 * @param initTextLayers     Number of text layers the plane contains, allows for layered graphics
	 * @param onScreenDimensions Dimensions that the plane is allowed to occupy on the parentScreen. If the real
	 *                           dimensions are smaller than the 'dimensions', then a portion of the plane will be
	 *                           displayed, and the plane can be moved around to show all its contents, as needed
	 * @param topLeftInParent    Top left coordinate on which to place this Plane on the parentScreen. The space it
	 *                           occupies will depend on this coordinate and the real dimensions
	 * @param topLeftInLocal     Top left coordinate to of the plane to be rendered to the parentScreen. Allows for
	 *                           large planes to be partially displayed, and moved around, in order to load big
	 *                           graphics and move them around (e.g when the player moves)
	 */
	public Plane(Screen parentScreen, Point planeDimensions, int initTextLayers, Point onScreenDimensions,
			Point topLeftInParent, Point topLeftInLocal) {
		initPlane(parentScreen, planeDimensions, initTextLayers, onScreenDimensions, topLeftInParent, topLeftInLocal);
	}

	/*
	 * Initializes the plane, called by constructors. Allows for constructors to use default values.
	 */
	private void initPlane(Screen parentScreen, Point dimensions, int initTextLayers, Point realDimensions, Point
			topLeftInParent, Point topLeftInLocal) {
		this.parentScreen = parentScreen;
		this.onScreenDimensions = realDimensions;
		this.topLeftInParent = topLeftInParent;
		this.topLeftInLocal = topLeftInLocal;

		backgroundLayer = new ArrayList<>();
		textLayers = new ArrayList<>();

		pixelBorders = new ArrayList<>();
		this.mouseClickHandlers = new ArrayList<>();

		for (int l = 0; l < initTextLayers; l++) {
			textLayers.add(new ArrayList<>());
			for (int j = 0; j < dimensions.y; j++) {
				if (l == 0)
					backgroundLayer.add(new ArrayList<>());
				textLayers.get(l).add(new ArrayList<>());
				for (int i = 0; i < dimensions.x; i++) {
					if (l == 0)
						backgroundLayer.get(j).add(Color.BLACK);
					textLayers.get(l).get(j).add(buildDefaultText());
				}
			}
		}

		parentScreen.registerPlane(this);
	}

	public void deletePlane() {
		for (Point p : mouseClickHandlers)
			parentScreen.setMouseClickHandler(p.x, p.y, null);
		mouseClickHandlers.clear();

		for (int k = 0; k < getNumOfLayers(); k++)
			for (int y = topLeftInParent.y; y < topLeftInParent.y + onScreenDimensions.y; y++)
				for (int x = topLeftInParent.x; x < topLeftInParent.x + onScreenDimensions.x; x++) {
					if (k == 0)
						parentScreen.setBackgroundColor(x, y, Color.BLACK);
					parentScreen.drawTextToLayer(x, y, k, ' ');
					parentScreen.setTextColor(x, y, k, Color.WHITE);
				}

	}

	/**
	 * Returns number of text layers that exist in this plane.
	 *
	 * @return Integer representing number of text layers in this plane
	 */
	public int getNumOfLayers() {
		return textLayers.size();
	}

	/**
	 * Returns width of this plane, in terms of how many characters it can display horizontally.
	 *
	 * @return Width of the plane
	 */
	public int getWidth() {
		return backgroundLayer.get(0).size();
	}

	/**
	 * Returns height of this plane, in terms of how many characters it can display vertically.
	 *
	 * @return Height of the plane
	 */
	public int getHeight() {
		return backgroundLayer.size();
	}

	/*
	 * Generates a text object with default values
	 */
	private Text buildDefaultText() {
		Text t = new Text(" ");
		t.setFont(View.font);
		t.setFill(Color.WHITE);
		return t;
	}

	/**
	 * Adds a column to the plane.
	 *
	 * @param left True if the column should be added on the left side of the plane, false if it should be added on the
	 *             right side of the plane
	 */
	public void addColumn(boolean left) {
		int width = textLayers.get(0).get(0).size();
		int height = textLayers.get(0).size();
		for (int l = 0; l < textLayers.size(); l++) {
			for (int j = 0; j < height; j++) {
				if (l == 0)
					backgroundLayer.get(j).add(left ? 0 : width, Color.BLACK);
				textLayers.get(l).get(j).add(left ? 0 : width, buildDefaultText());
			}
		}
	}

	/**
	 * Adds a row to the plane.
	 *
	 * @param top True if the row should be added on top of the plane, false if it should be added on the bottom of the
	 *            plane
	 */
	public void addRow(boolean top) {
		int width = textLayers.get(0).get(0).size();
		int height = textLayers.get(0).size();
		int y = top ? 0 : height;
		for (int l = 0; l < textLayers.size(); l++) {
			if (l == 0)
				backgroundLayer.add(y, new ArrayList<>());
			textLayers.get(l).add(y, new ArrayList<>());
			for (int x = 0; x < width; x++) {
				if (l == 0)
					backgroundLayer.get(y).add(Color.BLACK);
				textLayers.get(l).get(y).add(buildDefaultText());
			}
		}
	}

	private boolean inVisualBounds(int x, int y) {
		return x < topLeftInParent.x + onScreenDimensions.x && x > topLeftInParent.x
			   && y < topLeftInParent.y + onScreenDimensions.y && y > topLeftInParent.y;
	}

	/**
	 * Draws a character on to the plane, and to the parentScreen, if the 'pixel' it is being drawn on is on the
	 * parentScreen.
	 *
	 * @param p     Point at which to draw the character
	 * @param layer Layer on which to draw the character
	 * @param c     Character to draw
	 */
	public void drawChar(@NotNull Point p, int layer, char c) {
		drawChar(p.x, p.y, layer, c);
	}

	/**
	 * Draws a character on to the plane, and to the parentScreen, if the 'pixel' it is being drawn on is on the
	 * parentScreen.
	 *
	 * @param x     x-coordinate of point on which to draw the character
	 * @param y     y-coordinate of point on which to draw the character
	 * @param layer Layer on which to draw the character
	 * @param c     Character to draw
	 */
	public void drawChar(int x, int y, int layer, char c) {
		textLayers.get(layer).get(y).get(x).setText(Character.toString(c));
		if (inVisualBounds(x, y))
			parentScreen.drawTextToLayer(x, y, layer, c);
	}

	/**
	 * Flips a 'pixel' by 180 degrees.
	 *
	 * @param p     Coordinate of desired 'pixel' to flip
	 * @param layer Layer on which to flip the 'pixel'
	 */
	public void flipChar(@NotNull Point p, int layer) {
		flipChar(p.x, p.y, layer);
	}

	/**
	 * Flips a 'pixel' by 180 degrees.
	 *
	 * @param x     x-coordinate of desired 'pixel' to flip
	 * @param y     y-coordinate of desired 'pixel' to flip
	 * @param layer Layer on which to flip the 'pixel'
	 */
	public void flipChar(int x, int y, int layer) {
		Text t = textLayers.get(layer).get(y).get(x);
		t.setRotate(t.getRotate() == 180.0 ? 0.0 : 180.0);
		if (inVisualBounds(x, y))
			parentScreen.setTextRotation(x, y, layer, (int) t.getRotate());
	}

	/**
	 * Sets the font color for a character at the given position and layer.
	 *
	 * @param x     x-coordinate of desired character to change color for
	 * @param y     y-coordinate of desired character to change color for
	 * @param layer Layer on which to change the character color
	 * @param c     Color to change the character to
	 */
	public void setFontColor(int x, int y, int layer, @NotNull Color c) {
		textLayers.get(layer).get(y).get(x).setFill(c);
		if (inVisualBounds(x, y))
			parentScreen.setTextColor(x, y, layer, c);
	}

	/**
	 * Sets the background color for a 'pixel' at a given point.
	 *
	 * @param p Coordinates of desired 'pixel' to change background color for
	 * @param c Desired background color
	 */
	public void setBackgroundColor(@NotNull Point p, @NotNull Color c) {
		setBackgroundColor(p.x, p.y, c);
	}

	/**
	 * Set the background color for a 'pixel' at a given point.
	 *
	 * @param x x-coordinate of desired 'pixel' to change background color for
	 * @param y y-coordinate of desired 'pixel' to change background color for
	 * @param c Desired background color
	 */
	public void setBackgroundColor(int x, int y, @NotNull Color c) {
		backgroundLayer.get(y).set(x, c);
		if (inVisualBounds(x, y))
			parentScreen.setBackgroundColor(x, y, c);
	}

	public Color getBackgroundColor(int x, int y) {
		return backgroundLayer.get(y).get(x);
	}

	/**
	 * Draw string of text to the plane.
	 *
	 * @param p     Point at which to start drawing the string
	 * @param layer Layer on which to draw the string
	 * @param text  String to draw
	 */
	public void drawText(@NotNull Point p, int layer, @NotNull String text) {
		drawText(p.x, p.y, layer, text);
	}

	/**
	 * Draw string of text to the plane.
	 *
	 * @param x     x-coordinate at which to start drawing the string
	 * @param y     y-coordinate at which to start drawing the string
	 * @param layer Layer on which to draw the string
	 * @param text  String to draw
	 */
	public void drawText(int x, int y, int layer, @NotNull String text) {
		drawText(x, y, layer, backgroundLayer.get(0).size(), text);
	}

	/**
	 * Draw a string of text to the plane.
	 *
	 * @param x     x-coordinate at which to start drawing the string
	 * @param y     y-coordinate at which to start drawing the string
	 * @param layer Layer on which to draw the string
	 * @param maxX  Maximum x-coordinate for string drawing, if it is reached, the text will wrap to the next line
	 * @param text  String to draw
	 */
	public void drawText(int x, int y, int layer, int maxX, @NotNull String text) {
		String[] split = text.split(" ");
		int xOff = x, yOff = y;
		for (String str : split) {
			if (x + str.length() > maxX) {
				System.err.println("The following text cannot be displayed properly: \"" + text + "\"");
				break;
			}
			if (str.length() + xOff > maxX) {
				yOff++;
				xOff = x;
			}

			for (int p = 0; p < str.length(); p++)
				drawChar(xOff++, yOff, layer, str.charAt(p));
			if (xOff < backgroundLayer.get(0).size())
				drawChar(xOff++, yOff, layer, ' ');
		}
	}

	public void drawMarker(@NotNull Point prevMarkerPos, int layer, @NotNull Point newMarkerPos) {
		drawMarker(prevMarkerPos, layer, newMarkerPos, '>');
	}

	public void drawMarker(@NotNull Point prevMarkerPos, int layer, @NotNull Point newMarkerPos, char symbol) {
		if (prevMarkerPos != null)
			drawChar(prevMarkerPos.x, prevMarkerPos.y, layer, ' ');
		drawChar(newMarkerPos.x, newMarkerPos.y, layer, symbol);
	}

	public void setMouseClickHandler(int x, int y, EventHandler<MouseEvent> event) {
		mouseClickHandlers.add(new Point(x, y));
		parentScreen.setMouseClickHandler(x, y, event);
	}

	public void drawPixelBorder(Point p) {
		drawPixelBorder(p.x, p.y);
	}

	public void drawPixelBorder(int x, int y) {
		int xCoord = parentScreen.getGlobalPixelXCoord(x + topLeftInParent.x, y + topLeftInParent.y);
		int yCoord = parentScreen.getGlobalPixelYCoord(x + topLeftInParent.x, y + topLeftInParent.y);

		Line[] borders = new Line[4];
		borders[0] = new Line(xCoord, yCoord, xCoord + parentScreen.getPixelWidth(), yCoord);
		borders[1] = new Line(xCoord + parentScreen.getPixelWidth(), yCoord, xCoord + parentScreen.getPixelWidth(),
							  yCoord + parentScreen.getPixelHeight());
		borders[2] = new Line(xCoord + parentScreen.getPixelWidth(), yCoord + parentScreen.getPixelHeight(), xCoord,
							  yCoord + parentScreen.getPixelHeight());
		borders[3] = new Line(xCoord, yCoord + parentScreen.getPixelHeight(), xCoord, yCoord);

		Paint borderColor;
		Color bckgColor = getBackgroundColor(x, y);

		if ((bckgColor.getRed() + bckgColor.getGreen() + bckgColor.getBlue()) / 3d < 1.5)
			borderColor = Color.WHITE;
		else
			borderColor = Color.BLACK;

		for (Line l : borders) {
			l.setStroke(borderColor);
			l.setStrokeWidth(2);
			parentScreen.renderNode(l);
		}

		pixelBorders.add(borders);
	}

	public void clearBorders() {
		for (Line[] border : pixelBorders)
			for (Line l : border)
				parentScreen.removeNode(l);
		pixelBorders.clear();
	}
}
