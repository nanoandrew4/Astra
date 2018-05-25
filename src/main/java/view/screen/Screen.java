package view.screen;

import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import view.View;

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

	public void drawChar(Point p, char c) {
		drawChar(p.x, p.y, c);
	}

	public void drawChar(int x, int y, char c) {
		pixels[x][y].setChar(c);
	}

	public void flipChar(Point p) {
		pixels[p.x][p.y].flipChar();
	}

	public void flipChar(int x, int y) {
		pixels[x][y].flipChar();
	}

	public void drawText(String label, Point p, String text) {
		drawText(p.x, p.y, text);
	}

	public void drawText(int x, int y, String text) {
		drawText(x, y, COLUMNS, text);
	}

	public void setFontColor(int x, int y, Color c) {
		pixels[x][y].setCharColor(c);
	}

	public void setBackgroundColor(int x, int y, Color c) {
		pixels[x][y].setBackgroundColor(c);
	}

	public void drawText(int x, int y, int maxX, String text) {
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

	public void drawMarker(Point prevMarkerPos, Point newMarkerPos) {
		drawMarker(prevMarkerPos, newMarkerPos, '>');
	}

	public void drawMarker(Point prevMarkerPos, Point newMarkerPos, char symbol) {
		if (prevMarkerPos != null)
			pixels[prevMarkerPos.x][prevMarkerPos.y].setChar(' ');
		pixels[newMarkerPos.x][newMarkerPos.y].setChar(symbol);
	}
}
