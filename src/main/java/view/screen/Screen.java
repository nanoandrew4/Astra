package view.screen;

import com.sun.istack.internal.NotNull;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import view.View;
import view.screen.animation.RotateAnimData;

import java.awt.*;

public class Screen {
	public static int screenPxWidth, screenPxHeight;
	public final static int COLUMNS = 208, ROWS = 68;

	private Char pixels[][];

	public Screen() {
		Rectangle2D scrBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
		screenPxWidth = (int) scrBounds.getWidth();
		screenPxHeight = (int) scrBounds.getHeight();
	}

	public int getScreenWidth() {
		return COLUMNS;
	}

	public int getScreenHeight() {
		return ROWS;
	}

	public void initScreen(Pane pane) {
		double width = computeTextWidth();
		double height = View.font.getSize();

		double paddingX = 0, paddingY = 0;

		for (double d = 0; d < screenPxWidth; d += width)
			if (d + width > screenPxWidth)
				paddingX = (screenPxWidth - d) / 2;

		for (double d = 0; d < screenPxHeight; d += height)
			if (d + height > screenPxHeight)
				paddingY = (screenPxHeight - d) / 4;

		pixels = new Char[COLUMNS][ROWS];
		for (int x = 0; x < pixels.length; x++)
			for (int y = 0; y < pixels[0].length; y++)
				pixels[x][y] = new Char(pane, paddingX + x * width, paddingY + y * height, width, height);
	}

	private double computeTextWidth() {
		Text t = new Text("a");
		t.setFont(View.font);
		new Scene(new Group(t));
		t.applyCss();
		return t.getLayoutBounds().getWidth();
	}

	public void drawChar(@NotNull Point p, char c) {
		drawChar(p.x, p.y, c);
	}

	public void drawChar(int x, int y, char c) {
		pixels[x][y].setChar(c);
	}

	public void flipChar(@NotNull Point p) {
		pixels[p.x][p.y].flipChar();
	}

	public void flipChar(int x, int y) {
		pixels[x][y].flipChar();
	}

	public void drawText(@NotNull Point p, @NotNull String text) {
		drawText(p.x, p.y, text);
	}

	public void drawText(int x, int y, @NotNull String text) {
		drawText(x, y, COLUMNS, text);
	}

	public void setFontColor(int x, int y, @NotNull Color c) {
		pixels[x][y].setCharColor(c);
	}

	public void setBackgroundColor(@NotNull Point p, @NotNull Color c) {
		pixels[p.x][p.y].setBackgroundColor(c);
	}

	public void setBackgroundColor(int x, int y, @NotNull Color c) {
		pixels[x][y].setBackgroundColor(c);
	}

	public void drawText(int x, int y, int maxX, @NotNull String text) {
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
				pixels[xOff++][yOff].setChar(str.charAt(p));
			if (xOff < COLUMNS)
				pixels[xOff++][yOff].setChar(' ');
		}
	}

	public void drawMarker(@NotNull Point prevMarkerPos, @NotNull Point newMarkerPos) {
		drawMarker(prevMarkerPos, newMarkerPos, '>');
	}

	public void drawMarker(@NotNull Point prevMarkerPos, @NotNull Point newMarkerPos, char symbol) {
		if (prevMarkerPos != null)
			pixels[prevMarkerPos.x][prevMarkerPos.y].setChar(' ');
		pixels[newMarkerPos.x][newMarkerPos.y].setChar(symbol);
	}

	public void rotateCharAnim(int x, int y, RotateAnimData rotAnData) {
		pixels[x][y].rotateAnimChar(rotAnData);
	}

	public void rotateCharsAnim(int startX, int endX, int startY, int endY, RotateAnimData rotAnData) {
		for (int x = startX; x < endX; x++)
			for (int y = startY; y < endY; y++)
				if (!pixels[x][y].noChar())
					pixels[x][y].rotateAnimChar(rotAnData);
	}
}
