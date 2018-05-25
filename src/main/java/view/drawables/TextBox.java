package view.drawables;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import view.screen.Screen;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TextBox extends Drawable {
	private EventHandler<KeyEvent> keyHandler;

	private final static int maxLinesDisplayed = 5;

	private List<String> text;
	private List<String> currLine;
	private Point moreTextBelow, moreTextAbove;

	private int lineDisplayed = 0, currLineDisplayed = 0, requestedWidth;

	public TextBox(Screen parentScreen, int x, int y, int requestedWidth, List<String> text) {
		super(parentScreen);
		this.text = text;
		this.requestedWidth = requestedWidth;

		textBounds = new Bounds(
				new Point(x + BORDER_PADDING, y + BORDER_PADDING),
				new Point(x + BORDER_PADDING + requestedWidth, y + BORDER_PADDING),
				new Point(x + BORDER_PADDING, y + maxLinesDisplayed * LINE_SPACING),
				new Point(x + BORDER_PADDING + requestedWidth, y + maxLinesDisplayed * LINE_SPACING)
		);

		borderBounds = new Bounds(
				new Point(x, y),
				new Point(textBounds.getTopRightX() + BORDER_PADDING, y),
				new Point(x, textBounds.getBottomLeftY() + BORDER_PADDING),
				new Point(textBounds.getBottomRightX() + BORDER_PADDING, textBounds.getBottomRightY() + BORDER_PADDING)
		);

		fitsOnScreen();

		moreTextAbove = new Point(borderBounds.getTopRightX() - 1, borderBounds.getTopRightY() + 1);
		moreTextBelow = new Point(borderBounds.getBottomRightX() - 1, borderBounds.getBottomRightY() - 1);
	}

	public void draw() {
		if (!fitsOnScreen)
			return;

		drawBorders();
		drawNextLine();
		parentScreen.flipChar(moreTextBelow);

		keyHandler = event -> {
			if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.SPACE)
				drawNextLine();
			else if (event.getCode() == KeyCode.UP) {
				if (currLine.size() > maxLinesDisplayed && currLineDisplayed > 0)
					drawLines(currLine, --currLineDisplayed, maxLinesDisplayed, true);
			} else if (event.getCode() == KeyCode.DOWN) {
				if (currLine.size() > maxLinesDisplayed && currLineDisplayed + maxLinesDisplayed < currLine.size())
					drawLines(currLine, ++currLineDisplayed, maxLinesDisplayed, true);
			}
		};
	}

	private void drawNextLine() {
		currLineDisplayed = 0;
		if (currLine != null)
			removeText();

		if (lineDisplayed < text.size()) {
			String[] split = text.get(lineDisplayed).split(" ");
			StringBuilder tmp = new StringBuilder();
			currLine = new ArrayList<>();
			for (String s : split) {
				if (tmp.length() + s.length() > requestedWidth) {
					currLine.add(tmp.toString());
					tmp = new StringBuilder();
				}

				tmp.append(s).append(" ");
			}
			currLine.add(tmp.toString());

			drawLines(currLine, currLineDisplayed, maxLinesDisplayed, false);
		}
		lineDisplayed++;
	}

	// TODO: OPTION TO PRINT TEXT SLOWLY
	private void drawLines(List<String> text, int offset, int linesToPrint, boolean clearText) {
		if (clearText)
			removeText();
		for (int y = offset; y < text.size() && y < offset + linesToPrint; y++)
			parentScreen.drawText(
					textBounds.getTopLeftX(), textBounds.getTopLeftY() + (y - offset) * LINE_SPACING,
					textBounds.getTopRightX(), text.get(y)
			);
		if (currLine.size() > maxLinesDisplayed && currLineDisplayed + maxLinesDisplayed < currLine.size())
			parentScreen.drawChar(moreTextBelow, '^');
		else
			parentScreen.drawChar(moreTextBelow, ' ');
		if (currLine.size() > maxLinesDisplayed && currLineDisplayed > 0)
			parentScreen.drawChar(moreTextAbove, '^');
		else
			parentScreen.drawChar(moreTextAbove, ' ');
	}

	private void removeText() {
		for (int x = textBounds.getTopLeftX(); x <= textBounds.getTopRightX(); x++)
			for (int y = textBounds.getTopLeftY(); y <= textBounds.getBottomRightY(); y++)
				parentScreen.drawChar(x, y, ' ');
	}

	public void remove() {
		for (int x = borderBounds.getTopLeftX(); x < borderBounds.getTopRightX(); x++)
			for (int y = borderBounds.getTopLeftY(); y < borderBounds.getBottomRightY(); y++)
				parentScreen.drawChar(x, y, ' ');

		parentScreen.flipChar(moreTextBelow);
	}

	public EventHandler<KeyEvent> getKeyHandler() {
		return keyHandler;
	}
}