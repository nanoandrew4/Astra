package view.drawables;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import view.screen.Screen;

import java.awt.*;
import java.util.ArrayList;

public class TextBox extends Drawable {
	private EventHandler<KeyEvent> keyHandler;

	private final static int maxLinesDisplayed = 5;

	private ArrayList<String> text;

	TextBox(Screen parentScreen, int x, int y, int requestedWidth, ArrayList<String> text) {
		super(parentScreen);
		this.text = text;

		bounds = new Bounds(
				new Point(x, y), new Point(x + requestedWidth, y),
				new Point(x, y + maxLinesDisplayed), new Point(x + requestedWidth, y + maxLinesDisplayed));
	}

	public void draw() {
		drawBorders();
	}

	public void remove() {

	}
}