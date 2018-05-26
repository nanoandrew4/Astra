package view.drawables;

import com.sun.istack.internal.NotNull;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import view.screen.Screen;

import java.awt.*;
import java.util.List;

public class Menu extends Drawable {
	private List<String> options;
	private List<MenuEvent> events;
	private Point[] optionMarkerCoords;
	private int highlightedOpt = 0;

	private int columns;

	private EventHandler<KeyEvent> keyHandler;

	public Menu(@NotNull Screen parentScreen, int sx, int sy, @NotNull List<String> options,
				@NotNull List<MenuEvent> events, int columns) {
		super(parentScreen);
		this.options = options;
		this.events = events;
		this.columns = columns;
		optionMarkerCoords = new Point[options.size()];

		int maxStrLength = 0;

		for (String s : options)
			if (s.length() > maxStrLength)
				maxStrLength = s.length();

		maxStrLength += MARKER_PADDING;

		for (int s = 0; s < options.size(); s++) {
			optionMarkerCoords[s] = new Point(sx + (s % columns) * maxStrLength + BORDER_PADDING,
					sy + LINE_SPACING * (s / columns) + BORDER_PADDING);
		}

		textBounds = new Bounds(
				new Point(sx + BORDER_PADDING, sy + BORDER_PADDING),
				new Point(sx + maxStrLength * columns + BORDER_PADDING + MARKER_PADDING, sy),
				new Point(sx, sy + (LINE_SPACING * ((options.size() / columns)) - (options.size() % columns == 0 ? BORDER_PADDING : 0)) + BORDER_PADDING),
				new Point(sx + maxStrLength * columns + BORDER_PADDING + MARKER_PADDING, sy + (LINE_SPACING * ((options.size() / columns)) - (options.size() % columns == 0 ? BORDER_PADDING : 0)) + BORDER_PADDING)
		);

		borderBounds = new Bounds(
				new Point(sx, sy),
				new Point(textBounds.getTopRightX() + BORDER_PADDING, sy),
				new Point(sx, textBounds.getBottomLeftY() + BORDER_PADDING),
				new Point(textBounds.getBottomRightX() + BORDER_PADDING, textBounds.getBottomRightY() + BORDER_PADDING)
		);

		fitsOnScreen();
	}

	public void draw() {
		if (!fitsOnScreen)
			return;

		drawBorders();
		parentScreen.drawMarker(null, optionMarkerCoords[0], '>');

		for (int i = 0; i < options.size(); i++) {
			parentScreen.drawText(
					optionMarkerCoords[i].x + MARKER_PADDING,
					optionMarkerCoords[i].y, options.get(i)
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
					events.get(highlightedOpt).event();
					return;
			}

			if (highlightedOpt >= options.size())
				highlightedOpt = prevOption % columns;
			else if (highlightedOpt < 0) {
				highlightedOpt = options.size() + prevOption - (options.size() % columns);
				if (highlightedOpt >= options.size())
					highlightedOpt -= columns;
			}
			parentScreen.drawMarker(optionMarkerCoords[prevOption], optionMarkerCoords[highlightedOpt]);
		};
	}

	public void remove() {
		if (!drawn)
			return;

		for (int x = borderBounds.getTopLeftX(); x < borderBounds.getBottomRightX(); x++)
			for (int y = borderBounds.getTopLeftY(); y <= borderBounds.getBottomRightY(); y++)
				parentScreen.drawChar(x, y, ' ');

		highlightedOpt = 0;
		drawn = false;
	}

	public EventHandler<KeyEvent> getKeyHandler() {
		return keyHandler;
	}
}
