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

/**
 * Loads ASCII graphics stored in files, as well as optionally loading colors and palettes for the characters used to
 * render the ASCII graphics, and the background on which they are drawn.
 */
public class ASCIIRenderer extends Drawable {

	// List of strings that compose the graphics to be drawn
	private List<String> gfxFile;

	// Lists of colors to be used in coloring the text and background of the graphics
	private List<String> textColorFile, backgroundColorFile;

	private Palette textPalette, backgroundPalette;

	/**
	 * Initializes the ASCIIRenderer and loads the desired graphics from the specified file.
	 *
	 * @param parentScreen Screen on which the ASCII graphics will be rendered
	 * @param x            X coordinate at which to place the top left of the graphics
	 * @param y            Y coordinate at which to place the top left of the graphics
	 * @param gfxFileName  Path to file in which the desired graphics are stored
	 */
	public ASCIIRenderer(@NotNull Screen parentScreen, int x, int y, @NotNull String gfxFileName) {
		super(parentScreen);

		try {
			gfxFile = Files.readAllLines(Paths.get(this.getClass().getResource(gfxFileName).toURI()));
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}

		initRenderer(x, y);
	}

	/**
	 * Initializes the ASCIIRenderer.
	 *
	 * @param parentScreen Screen on which the ASCII graphics will be rendered
	 * @param x            X coordinate at which to place the top left of the graphics
	 * @param y            Y coordinate at which to place the top left of the graphics
	 * @param gfxFile      List of strings representing the desired graphics to be rendered
	 */
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

	/**
	 * Loads the file containing the colors for each pixel of the text with which the graphics will be rendered, as
	 * characters, as well as the palette with which to determine the specific colors mapped to each character in the
	 * color file.
	 *
	 * @param textColorFileName   Path to file in which the colors for the graphics are stored
	 * @param textPaletteFileName Path to file in which the palette to be used is
	 */
	public void setTextColor(@NotNull String textColorFileName, @NotNull String textPaletteFileName) {
		try {
			textColorFile = Files.readAllLines(Paths.get(this.getClass().getResource(textColorFileName).toURI()));
			textPalette = new Palette(textPaletteFileName);
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Receives and sets the colors for the text used to render the graphics, as well as the palette with which to
	 * determine the specific colors mapped to each character in the color file.
	 *
	 * @param textColor   List of strings containing the characters that determine the colors of the text with which
	 *                    the graphics will be rendered
	 * @param textPalette Palette mapping the colors contained in the textColor list to actual RGB colors
	 */
	public void setTextColor(@NotNull List<String> textColor, @NotNull Palette textPalette) {
		this.textColorFile = textColor;
		this.textPalette = textPalette;
	}

	/**
	 * Loads the file containing the colors for each pixel on which the text will be rendered, as characters, as well
	 * as the palette with which to determine the specific colors mapped to each character in the color file.
	 *
	 * @param backgroundColorFileName Path to file in which the colors for the graphics are stored
	 * @param backgroundPaletteFileName Path to file in which the palette to be used is
	 */
	public void setBackgroundColor(@NotNull String backgroundColorFileName, @NotNull String backgroundPaletteFileName) {
		try {
			backgroundColorFile = Files.readAllLines(Paths.get(this.getClass().getResource(backgroundColorFileName).toURI()));
			backgroundPalette = new Palette(backgroundPaletteFileName);
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Receives and sets the colors for the background on which the graphics are rendered, as well as the palette with
	 * which to determine the specific colors mapped to each character in the color file.
	 *
	 * @param backgroundColorFile Path to file in which the colors for the graphics are stored
	 * @param backgroundPalette Path to file in which the palette to be used is
	 */
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

	/*
	 * Sets the colors specified in the color file on the individual pixels that make up the graphic.
	 * Colors can be requested in three ways through color files:
	 * - Specifying the color for each pixel
	 * - Specifying the color for the first row, all rows will use the same colors as the first
	 * - Specifying the color for the first column, all columns will the use the same colors as the first
	 */
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

	/**
	 * Rotates the individual pixels around their individual center coordinate.
	 * @param rotAnData RotateAnimData object containing the desired options for the rotation animation
	 */
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
