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
	public static int screenPxWidth, screenPxHeight, fontWidth, fontHeight;
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
		fontWidth = (int) Math.ceil(computeTextWidth());
		fontHeight = (int) Math.ceil(View.font.getSize()) + 1;

		double paddingX = 0, paddingY = 0;

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

	public void drawPlane(Plane plane, int planeTX, int planeTY) {
		for (int k = 0; k < plane.getNumOfLayers(); k++)
			for (int j = 0; j < ROWS; j++)
				for (int i = 0; i < COLUMNS; i++) {
					if (k == 0 && i + planeTX < plane.getWidth() && j + planeTY < plane.getHeight())
						backgroundLayer[i][j].setFill(plane.getRect(i + planeTX, j + planeTY));
					Text t = plane.getText(i + planeTX, j + planeTY, k);
					textLayers[i][j][k].setText(t.getText());
					textLayers[i][j][k].setFill(t.getFill());
				}
	}

	private double computeTextWidth() {
		Text t = new Text("a");
		t.setFont(View.font);
		new Scene(new Group(t));
		t.applyCss();
		return t.getLayoutBounds().getWidth();
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
				if (!"".equals(textLayers[x][y][layer].getText()))
					rotateAnimChar(x, y, layer, rotAnData);
	}
}
