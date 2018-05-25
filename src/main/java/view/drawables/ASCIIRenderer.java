package view.drawables;

import view.screen.Screen;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ASCIIRenderer extends Drawable {

	private List<String> gfxFile;
	private List<String> colorFile;

	ASCIIRenderer(Screen parentScreen, int x, int y, String gfxFileName) {
		super(parentScreen);

		try {
			gfxFile = Files.readAllLines(Paths.get(this.getClass().getResource(gfxFileName).toURI()));
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}

		initRenderer(x, y);
	}

	ASCIIRenderer(Screen parentScreen, int x, int y, List<String> gfxFile) {
		super(parentScreen);
		this.gfxFile = gfxFile;

		initRenderer(x, y);
	}

	private void initRenderer(int x, int y) {
		int lengthLongestLine = 0;
		for (String s : gfxFile)
			if (s.length() > lengthLongestLine)
				lengthLongestLine = s.length();

		textBounds = borderBounds = new Bounds(
				new Point(x, y),
				new Point(x + lengthLongestLine, y),
				new Point(x, y + gfxFile.size()),
				new Point(x + lengthLongestLine, y + gfxFile.size())
		);

		fitsOnScreen();
	}

	@Override
	public void draw() {

	}

	@Override
	public void remove() {

	}
}
