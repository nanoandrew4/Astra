package view.screen;

import com.sun.istack.internal.NotNull;
import javafx.animation.RotateTransition;
import javafx.geometry.Point3D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import view.View;
import view.screen.animation.RotateAnimData;

import java.awt.Point;
import java.util.HashMap;

public class Screen {
	public static int screenPxWidth, screenPxHeight;
	public final static int COLUMNS = 208, ROWS = 68;

	private Rectangle[][] backgroundLayer;
	private Text[][][] textLayers;

	public static HashMap<Axis, Point3D> axisMap = new HashMap<>();

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

	public int getScreenWidth() {
		return COLUMNS;
	}

	public int getScreenHeight() {
		return ROWS;
	}

	public int getNumOfLayers() {
		return textLayers[0][0].length;
	}

	public void initScreen(Pane pane) {
		double width = Math.ceil(computeTextWidth());
		double height = Math.ceil(View.font.getSize()) + 1;

		double paddingX = 0, paddingY = 0;

		for (double d = 0; d < screenPxWidth; d += width)
			if (d + width > screenPxWidth)
				paddingX = (screenPxWidth - d) / 2;

		for (double d = 0; d < screenPxHeight; d += height)
			if (d + height > screenPxHeight)
				paddingY = (screenPxHeight - d) / 4;

		for (int k = 0; k < textLayers[0][0].length; k++)
			for (int i = 0; i < COLUMNS; i++)
				for (int j = 0; j < ROWS; j++) {
					double x = Math.ceil(i * width);
					double y = Math.ceil(j * height);
					double nx = Math.ceil((i + 1) * width);
					double ny = Math.ceil((j + 1) * height);

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

	private double computeTextWidth() {
		Text t = new Text("a");
		t.setFont(View.font);
		new Scene(new Group(t));
		t.applyCss();
		return t.getLayoutBounds().getWidth();
	}

	public void drawChar(@NotNull Point p, int layer, char c) {
		drawChar(p.x, p.y, layer, c);
	}

	public void drawChar(int x, int y, int layer, char c) {
		textLayers[x][y][layer].setText(Character.toString(c));
	}

	public void flipChar(@NotNull Point p, int layer) {
		flipChar(p.x, p.y, layer);
	}

	public void flipChar(int x, int y, int layer) {
		textLayers[x][y][layer].setRotate(textLayers[x][y][layer].getRotate() == 180.0 ? 0.0 : 180.0);
	}

	public void drawText(@NotNull Point p, int layer, @NotNull String text) {
		drawText(p.x, p.y, layer, text);
	}

	public void drawText(int x, int y, int layer, @NotNull String text) {
		drawText(x, y, layer, COLUMNS, text);
	}

	public void setFontColor(int x, int y, int layer, @NotNull Color c) {
		textLayers[x][y][layer].setFill(c);
	}

	public void setBackgroundColor(@NotNull Point p, @NotNull Color c) {
		setBackgroundColor(p.x, p.y, c);
	}

	public void setBackgroundColor(int x, int y, @NotNull Color c) {
		backgroundLayer[x][y].setFill(c);
	}

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
				textLayers[xOff++][yOff][layer].setText(Character.toString(str.charAt(p)));
			if (xOff < COLUMNS)
				textLayers[xOff++][yOff][layer].setText(" ");
		}
	}

	public void drawMarker(@NotNull Point prevMarkerPos, int layer, @NotNull Point newMarkerPos) {
		drawMarker(prevMarkerPos, layer, newMarkerPos, '>');
	}

	public void drawMarker(@NotNull Point prevMarkerPos, int layer, @NotNull Point newMarkerPos, char symbol) {
		if (prevMarkerPos != null)
			textLayers[prevMarkerPos.x][prevMarkerPos.y][layer].setText(" ");
		textLayers[newMarkerPos.x][newMarkerPos.y][layer].setText(Character.toString(symbol));
	}

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

	public void rotateCharsAnim(int startX, int endX, int startY, int endY, int layer, RotateAnimData rotAnData) {
		for (int x = startX; x < endX; x++)
			for (int y = startY; y < endY; y++)
				if (!"".equals(textLayers[x][y][layer]))
					rotateAnimChar(x, y, layer, rotAnData);
	}
}
