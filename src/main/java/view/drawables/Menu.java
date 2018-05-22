package view.drawables;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import view.screen.Screen;

import java.awt.*;
import java.util.List;

public class Menu extends Drawable {
	private String[] options;
	private Point[] optionMarkerCoords;
	private int highlightedOpt = 0, selectedOpt = -1;

	private int columns;

	private EventHandler<KeyEvent> keyHandler;

	public Menu(Screen screen, int sx, int sy, List<String> options, int columns) {
		super(screen);
		this.options = new String[options.size()];
		this.columns = columns;
		optionMarkerCoords = new Point[options.size()];

		int maxStrLength = 0;

		for (String s : options)
			if (s.length() > maxStrLength)
				maxStrLength = s.length();

		maxStrLength += MARKER_PADDING;

		for (int s = 0; s < options.size(); s++) {
			String str = options.get(s);
			this.options[s] = str;
			optionMarkerCoords[s] = new Point(sx + (s % columns) * maxStrLength + BORDER_PADDING,
					sy + LINE_SPACING * (s / columns) + BORDER_PADDING);
		}

		bounds = new Bounds(
				new Point(sx, sy), new Point(sx + maxStrLength * columns + BORDER_PADDING * 2 + MARKER_PADDING, sy),
				new Point(
						sx,
						sy + (LINE_SPACING * ((options.size() / columns)) - (options.size() % columns == 0 ? BORDER_PADDING : 0)) + BORDER_PADDING * 2
				),
				new Point(
						sx + maxStrLength * columns + BORDER_PADDING * 2 + MARKER_PADDING,
						sy + (LINE_SPACING * ((options.size() / columns)) - (options.size() % columns == 0 ? BORDER_PADDING : 0)) + BORDER_PADDING * 2
				)
		);
	}

	public void draw() {
		drawBorders();
		parentScreen.drawMarker(null, optionMarkerCoords[0], '>');

		for (int i = 0; i < options.length; i++) {
			parentScreen.drawText(
					null, optionMarkerCoords[i].x + MARKER_PADDING,
					optionMarkerCoords[i].y, options[i]
			);
		}

		drawn = true;

		keyHandler = event -> {
			if (!drawn)
				return;
			int prevOption = highlightedOpt;
			switch (event.getCode()) {
				case UP:
					highlightedOpt -= columns;
					break;
				case DOWN:
					highlightedOpt += columns;
					break;
				case RIGHT:
					highlightedOpt++;
					break;
				case LEFT:
					highlightedOpt--;
					break;
				case ENTER:
					selectedOpt = highlightedOpt; // TODO: HOW TO HANDLE SELECTIONS IN MENUS
					return;
			}

			if (highlightedOpt >= options.length)
				highlightedOpt = prevOption % columns;
			else if (highlightedOpt < 0) {
				highlightedOpt = options.length + prevOption - (options.length % columns);
				if (highlightedOpt >= options.length)
					highlightedOpt -= columns;
			}
			parentScreen.drawMarker(optionMarkerCoords[prevOption], optionMarkerCoords[highlightedOpt]);
		};
	}

	public void remove() {
		if (!drawn)
			return;

		for (int x = bounds.getTopLeftX(); x < bounds.getBottomRightX(); x++)
			for (int y = bounds.getTopLeftY(); y <= bounds.getBottomRightY(); y++)
				parentScreen.drawChar(x, y, ' ');

		highlightedOpt = 0;
		drawn = false;
	}

	public EventHandler<KeyEvent> getKeyHandler() {
		return keyHandler;
	}
}
