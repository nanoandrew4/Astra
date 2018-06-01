package view.screen;

import com.sun.istack.internal.NotNull;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import view.View;

import java.awt.*;
import java.util.ArrayList;

public class Plane {

	private ArrayList<ArrayList<Color>> backgroundLayer;
	private ArrayList<ArrayList<ArrayList<Text>>> textLayers;

	private Point centerXY;

	public Plane(int initWidth, int initHeight, int initTextLayers) {
		backgroundLayer = new ArrayList<>();
		textLayers = new ArrayList<>();

		for (int l = 0; l < initTextLayers; l++) {
			textLayers.add(new ArrayList<>());
			for (int j = 0; j < initHeight; j++) {
				if (l == 0)
					backgroundLayer.add(new ArrayList<>());
				textLayers.get(l).add(new ArrayList<>());
				for (int i = 0; i < initWidth; i++) {
					if (l == 0)
						backgroundLayer.get(j).add(Color.BLACK);
					textLayers.get(l).get(j).add(buildDefaultText());
				}
			}
		}
	}

	public int getNumOfLayers() {
		return textLayers.size();
	}

	public int getWidth() {
		return backgroundLayer.get(0).size();
	}

	public int getHeight() {
		return backgroundLayer.size();
	}

	public Color getRect(int x, int y) {
		return backgroundLayer.get(y).get(x);
	}

	public Text getText(int x, int y, int layer) {
		return textLayers.get(layer).get(y).get(x);
	}

	private Text buildDefaultText() {
		Text t = new Text(" ");
		t.setFont(View.font);
		t.setFill(Color.WHITE);
		return t;
	}

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

	public void drawChar(@NotNull Point p, int layer, char c) {
		drawChar(p.x, p.y, layer, c);
	}

	public void drawChar(int x, int y, int layer, char c) {
		textLayers.get(layer).get(y).get(x).setText(Character.toString(c));
	}

	public void flipChar(@NotNull Point p, int layer) {
		flipChar(p.x, p.y, layer);
	}

	public void flipChar(int x, int y, int layer) {
		textLayers.get(layer).get(y).get(x).setRotate(textLayers.get(layer).get(y).get(x).getRotate() == 180.0 ? 0.0 : 180.0);
	}

	public void drawText(@NotNull Point p, int layer, @NotNull String text) {
		drawText(p.x, p.y, layer, text);
	}

	public void drawText(int x, int y, int layer, @NotNull String text) {
		drawText(x, y, layer, backgroundLayer.get(0).size(), text);
	}

	public void setFontColor(int x, int y, int layer, @NotNull Color c) {
		textLayers.get(layer).get(y).get(x).setFill(c);
	}

	public void setBackgroundColor(@NotNull Point p, @NotNull Color c) {
		setBackgroundColor(p.x, p.y, c);
	}

	public void setBackgroundColor(int x, int y, @NotNull Color c) {
		backgroundLayer.get(y).set(x, c);
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
				textLayers.get(layer).get(yOff).get(xOff++).setText(Character.toString(str.charAt(p)));
			if (xOff < backgroundLayer.get(0).size())
				textLayers.get(layer).get(yOff).get(xOff++).setText(" ");
		}
	}

	public void drawMarker(@NotNull Point prevMarkerPos, int layer, @NotNull Point newMarkerPos) {
		drawMarker(prevMarkerPos, layer, newMarkerPos, '>');
	}

	public void drawMarker(@NotNull Point prevMarkerPos, int layer, @NotNull Point newMarkerPos, char symbol) {
		if (prevMarkerPos != null)
			textLayers.get(layer).get(prevMarkerPos.y).get(prevMarkerPos.x).setText(" ");
		textLayers.get(layer).get(newMarkerPos.y).get(newMarkerPos.x).setText(Character.toString(symbol));
	}
}
