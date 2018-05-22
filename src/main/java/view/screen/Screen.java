package view.screen;

import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import view.View;

import java.awt.*;
import java.util.HashMap;

public class Screen {
	public static int screenPxWidth, screenPxHeight;
	private final static int COLUMNS = 208, ROWS = 68;

	private HashMap<String, StrRecord> displayedText;

	private Char pixels[][];

	public Screen() {
		displayedText = new HashMap<>();

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

	public void drawText(String label, Point p, String text) {
		drawText(label, p.x, p.y, text);
	}

	public void drawText(String label, int x, int y, String text) {
		String[] split = text.split(" ");
		int xOff = x, yOff = y;
		for (String str : split) {
			if (x + str.length() > COLUMNS) {
				System.err.println("The following text cannot be displayed properly: \"" + text + "\"");
				break;
			}
			if (str.length() + xOff > COLUMNS) {
				yOff++;
				xOff = x;
			}
			for (int p = 0; p < str.length(); p++)
				pixels[xOff++][yOff].setChar(str.charAt(p));
			if (xOff < COLUMNS)
				pixels[xOff++][yOff].setChar(' ');
		}

		displayedText.put(label, new StrRecord(x, y, xOff, yOff));
	}

	public void clearLine(String label) {
		StrRecord str = displayedText.get(label);
		for (int y = str.sy; y <= str.ey; y++)
			for (int x = str.sx; x < str.ex || (x < COLUMNS && y < str.ey - 1); x++) {
				pixels[x][y].setChar(' ');
				pixels[x][y].setBackgroundColor(Color.BLACK);
				pixels[x][y].setCharColor(Color.WHITE);
			}
		displayedText.remove(label);
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
