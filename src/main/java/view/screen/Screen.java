package view.screen;

import javafx.animation.RotateTransition;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import view.View;
import view.screen.animation.RotateAnimData;

import java.util.ArrayList;
import java.util.HashMap;

public class Screen {
	private Scene parentScene;
	private Pane parentPane;

	public static int screenPxWidth, screenPxHeight, fontWidth, fontHeight;
	public final static int COLUMNS = 208, ROWS = 68;

	public double paddingX, paddingY;

	private Rectangle[][] backgroundLayer;
	private Text[][][] textLayers;

	public static HashMap<Axis, Point3D> axisMap = new HashMap<>();

	private ArrayList<Plane> planes = new ArrayList<>();

	/*
	 * Map enums to Point3D objects used in rotations.
	 */
	static {
		axisMap.put(Axis.X, new Point3D(1, 0, 0));
		axisMap.put(Axis.Y, new Point3D(0, 1, 0));
		axisMap.put(Axis.Z, new Point3D(0, 0, 1));
	}

	public Screen() {
		Rectangle2D scrBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
		screenPxWidth = (int) scrBounds.getWidth();
		screenPxHeight = (int) scrBounds.getHeight();

		backgroundLayer = new Rectangle[COLUMNS][ROWS];
		textLayers = new Text[COLUMNS][ROWS][3]; // 3 text layers
	}

	public void registerPlane(Plane p) {
		this.planes.add(p);
	}

	/**
	 * Returns number of columns that the screen spans.
	 *
	 * @return Number of columns in the screen
	 */
	public int getScreenWidth() {
		return COLUMNS;
	}

	/**
	 * Returns number of rows that the screen spans.
	 *
	 * @return Number of rows in the screen
	 */
	public int getScreenHeight() {
		return ROWS;
	}

	/**
	 * Returns number of layers in the screen.
	 *
	 * @return Number of layers
	 */
	public int getNumOfLayers() {
		return textLayers[0][0].length;
	}

	public int getPixelXCoord(int x, int y) {
		return (int) (backgroundLayer[x][y].getLayoutX() + paddingX);
	}

	public int getPixelYCoord(int x, int y) {
		return (int) (backgroundLayer[x][y].getLayoutY() + paddingY);
	}

	public int getPixelWidth() {
		return (int) backgroundLayer[0][0].getBoundsInLocal().getWidth();
	}

	public int getPixelHeight() {
		return (int) backgroundLayer[0][0].getBoundsInLocal().getHeight();
	}

	/**
	 * Draw all text boxes and rectangles on to passed pane. This should only happen once, when the engine starts up.
	 * Any graphical modifications should be done through the use of Plane objects, only one screen should exist during
	 * the entire run of the program.
	 *
	 * @param parentScene Scene on which this Screen is drawn
	 * @param pane        Pane on which to draw the graphical objects
	 */
	public void initScreen(Scene parentScene, Pane pane) {
		this.parentScene = parentScene;
		this.parentPane = pane;

		fontWidth = (int) Math.ceil(computeTextWidth());
		fontHeight = (int) Math.ceil(View.font.getSize()) + 1;

		for (double d = 0; d < screenPxWidth; d += fontWidth)
			if (d + fontWidth > screenPxWidth)
				paddingX = (screenPxWidth - d) / 2;

		for (double d = 0; d < screenPxHeight; d += fontHeight)
			if (d + fontHeight > screenPxHeight)
				paddingY = (screenPxHeight - d) / 4;

		for (int k = 0; k < textLayers[0][0].length; k++)
			for (int i = 0; i < COLUMNS; i++)
				for (int j = 0; j < ROWS; j++) {
					double x = Math.ceil(i * fontWidth);
					double y = Math.ceil(j * fontHeight);
					double nx = Math.ceil((i + 1) * fontWidth);
					double ny = Math.ceil((j + 1) * fontHeight);

					if (k == 0) {
						backgroundLayer[i][j] = new Rectangle(nx - x, ny - y);
						backgroundLayer[i][j].relocate(paddingX + x, paddingY + y);
						backgroundLayer[i][j].setFill(Color.BLACK);
						pane.getChildren().add(backgroundLayer[i][j]);
					}
					textLayers[i][j][k] = new Text(" ");
					textLayers[i][j][k].relocate(paddingX + x, paddingY + y);
					textLayers[i][j][k].setFill(Color.WHITE);
					textLayers[i][j][k].setFont(View.font);
					pane.getChildren().add(textLayers[i][j][k]);
				}
	}

	public void clearScreen() {
		for (Plane p : planes)
			p.deletePlane();
		planes.clear();
	}

	/**
	 * Sets the color of the background layer at the given coordinates.
	 *
	 * @param x x-coordinate at which to change the background color
	 * @param y y-coordinate at which to change the background color
	 * @param c Desired background color
	 */
	public void drawToBackgroundLayer(int x, int y, Color c) {
		backgroundLayer[x][y].setFill(c);
	}

	/**
	 * Draws a character at the given coordinates and layer.
	 *
	 * @param x     x-coordinate at which to draw the character
	 * @param y     y-coordinate at which to draw the character
	 * @param layer Layer on which to draw the character
	 * @param c     Character to draw
	 */
	public void drawTextToLayer(int x, int y, int layer, char c) {
		textLayers[x][y][layer].setText(Character.toString(c));
	}

	/**
	 * Sets the color of the character at the given coordinate and layer.
	 *
	 * @param x     x-coordinates of character to change color for
	 * @param y     y-coordinate of character to change color for
	 * @param layer Layer of character to change color for
	 * @param c     Desired text color
	 */
	public void setTextColor(int x, int y, int layer, Color c) {
		textLayers[x][y][layer].setFill(c);
	}

	/**
	 * Sets the rotation of a Text object in the pane, to allow for rotations.
	 *
	 * @param x     x-coordinate of character to rotate
	 * @param y     y-coordinate of character to rotate
	 * @param layer Layer on which character to rotate is located on
	 * @param rot   Angle (in degrees) to rotate character to
	 */
	public void setTextRotation(int x, int y, int layer, int rot) {
		textLayers[x][y][layer].setRotate(rot);
	}

	/*
	 * Computes the pixel width of all characters (since a monospace font is used).
	 * Used to determine padding and columns for Screen.
	 */
	private double computeTextWidth() {
		Text t = new Text("a");
		t.setFont(View.font);
		new Scene(new Group(t));
		t.applyCss();
		return t.getLayoutBounds().getWidth();
	}

	/**
	 * Animates the rotation of a character at a given position, layer, and with the desired parameters.
	 *
	 * @param x        x-coordinate of character to rotate
	 * @param y        y-coordinate of character to rotate
	 * @param layer    Layer at which character to rotate is located on
	 * @param rotAnDat Object containing parameters for RotateTransition
	 */
	public void rotateAnimChar(int x, int y, int layer, RotateAnimData rotAnDat) {
		RotateTransition rt = new RotateTransition(Duration.millis(rotAnDat.getDurationMillis()), textLayers[x][y][layer]);
		rt.setFromAngle(rotAnDat.getStartAngle());
		rt.setToAngle(rotAnDat.getEndAngle());
		rt.setAxis(axisMap.get(rotAnDat.getAxis()));
		rt.setCycleCount(rotAnDat.getCycles());
		rt.setInterpolator(rotAnDat.getInterpolator());
		rt.setOnFinished(rotAnDat.getOnFinish());
		rt.play();
	}

	/**
	 * Animates the rotation of a group of characters, in a rectangular shape.
	 *
	 * @param startX    Leftmost x-coordinate of characters to rotate
	 * @param endX      Rightmost x-coordinate of characters to rotate
	 * @param startY    Topmost y-coordinate of characters to rotate
	 * @param endY      Bottommost y-coordinate of characters to rotate
	 * @param layer     Layer on which characters to rotate are located on
	 * @param rotAnData Object containing parameters for RotateTransition
	 */
	public void rotateCharsAnim(int startX, int endX, int startY, int endY, int layer, RotateAnimData rotAnData) {
		for (int x = startX; x < endX; x++)
			for (int y = startY; y < endY; y++)
				if (!"".equals(textLayers[x][y][layer].getText()))
					rotateAnimChar(x, y, layer, rotAnData);
	}

	public void setKeyHandlers(EventHandler<KeyEvent> event) {
		parentScene.setOnKeyPressed(event);
	}

	public void setMouseClickHandler(int x, int y, EventHandler<MouseEvent> event) {
		textLayers[x][y][getNumOfLayers() - 1].setOnMouseClicked(event);
	}

	public void renderNode(Node n) {
		parentPane.getChildren().add(n);
	}

	public void removeNode(Node n) {
		if (parentPane.getChildren().contains(n))
			parentPane.getChildren().remove(n);
	}
}
