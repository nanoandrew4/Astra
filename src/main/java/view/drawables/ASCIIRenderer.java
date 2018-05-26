package view.drawables;

import com.sun.istack.internal.NotNull;
import javafx.scene.paint.Color;
import view.colors.Palette;
import view.screen.Screen;
import view.screen.animation.RotateAnimData;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ASCIIRenderer extends Drawable {

	private List<String> gfxFile;
	private List<String> textColorFile, backgroundColorFile;

	private Palette textPalette, backgroundPalette;

	public ASCIIRenderer(@NotNull Screen parentScreen, int x, int y, @NotNull String gfxFileName) {
		super(parentScreen);

		try {
			gfxFile = Files.readAllLines(Paths.get(this.getClass().getResource(gfxFileName).toURI()));
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}

		initRenderer(x, y);
	}

	public ASCIIRenderer(@NotNull Screen parentScreen, int x, int y, @NotNull List<String> gfxFile) {
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

	public void setTextColor(@NotNull String textColorFileName, @NotNull String textPaletteFileName) {
		try {
			textColorFile = Files.readAllLines(Paths.get(this.getClass().getResource(textColorFileName).toURI()));
			textPalette = new Palette(textPaletteFileName);
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
	}

	public void setTextColor(@NotNull List<String> textColor, @NotNull Palette textPalette) {
		this.textColorFile = textColor;
		this.textPalette = textPalette;
	}

	public void setBackgroundColor(@NotNull String backgroundColorFileName, @NotNull String backgroundPaletteFileName) {
		try {
			backgroundColorFile = Files.readAllLines(Paths.get(this.getClass().getResource(backgroundColorFileName).toURI()));
			backgroundPalette = new Palette(backgroundPaletteFileName);
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
	}

	public void setBackgroundColor(@NotNull List<String> backgroundColorFile, @NotNull Palette backgroundPalette) {
		this.backgroundColorFile = backgroundColorFile;
		this.backgroundPalette = backgroundPalette;
	}

	@Override
	public void draw() {
		for (int y = textBounds.getTopLeftY(); y < textBounds.getBottomRightY(); y++)
			parentScreen.drawText(textBounds.getTopLeftX(), y, gfxFile.get(y - textBounds.getTopLeftY()));

		setColors(textColorFile, textPalette, true);
		setColors(backgroundColorFile, backgroundPalette, false);
	}

	private void setColors(List<String> colorFile, Palette palette, boolean textColor) {
		if (colorFile != null && palette != null) {
			Color c = Color.WHITE;
			String s = "";

			if (colorFile.size() == 1 && colorFile.get(0).length() == 1)
				c = palette.getColor(colorFile.get(0).charAt(0));

			for (int y = textBounds.getTopLeftY(); y < textBounds.getBottomRightY(); y++) {
				if (colorFile.get(0).length() > 1 && colorFile.size() > 1)
					s = colorFile.get(y = textBounds.getTopLeftY());
				else if (colorFile.size() > 1 && colorFile.get(0).length() == 1)
					c = palette.getColor(s.charAt(0));

				for (int x = textBounds.getTopLeftX(); x < textBounds.getBottomRightX(); x++) {
					if (colorFile.get(0).length() > 1 && colorFile.size() > 1)
						c = palette.getColor(s.charAt(x - textBounds.getTopLeftX()));

					if (textColor)
						parentScreen.setFontColor(x, y, c);
					else
						parentScreen.setBackgroundColor(x, y, c);
				}
			}
		}
	}

	public void rotateChars(RotateAnimData rotAnData) {
		parentScreen.rotateCharsAnim(
				textBounds.getTopLeftX(), textBounds.getTopRightX(),
				textBounds.getTopLeftY(), textBounds.getBottomRightY(), rotAnData
		);
	}

	@Override
	public void remove() {
		for (int x = borderBounds.getTopLeftX(); x < borderBounds.getTopRightX(); x++)
			for (int y = borderBounds.getTopLeftY(); y < borderBounds.getBottomRightY(); y++)
				parentScreen.drawChar(x, y, ' ');

		if (textColorFile != null && textPalette != null)
			for (int y = textBounds.getTopLeftY(); y < textBounds.getBottomRightY(); y++)
				for (int x = textBounds.getTopLeftX(); x < textBounds.getBottomRightX(); x++)
					parentScreen.setFontColor(x, y, Color.WHITE);

		if (backgroundColorFile != null && backgroundPalette != null)
			for (int y = textBounds.getTopLeftY(); y < textBounds.getBottomRightY(); y++)
				for (int x = textBounds.getTopLeftX(); x < textBounds.getBottomRightX(); x++)
					parentScreen.setBackgroundColor(x, y, Color.BLACK);
	}
}
