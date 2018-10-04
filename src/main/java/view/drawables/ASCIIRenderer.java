package view.drawables;

import ioutils.InputStreamReader;
import javafx.scene.paint.Color;
import view.colors.Palette;
import view.screen.Plane;
import view.screen.Screen;
import view.screen.animation.RotateAnimData;

import javax.validation.constraints.NotNull;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
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

	private Color defaultText = Color.WHITE, defaultBack = Color.BLACK;

	/**
	 * Initializes the ASCIIRenderer and loads the desired graphics from the specified file.
	 *
	 * @param parentScreen Screen on which the plane the graphics exist on will be drawn
	 * @param parentPlane  Plane on which to draw the graphics
	 * @param x            X coordinate at which to place the top left of the graphics
	 * @param y            Y coordinate at which to place the top left of the graphics
	 * @param gfxFileName  Path to file in which the desired graphics are stored
	 */
	public ASCIIRenderer(@NotNull Screen parentScreen, @NotNull Plane parentPlane, int x, int y, @NotNull String
			gfxFileName) {
		super(parentScreen, parentPlane);

		gfxFile = InputStreamReader.readAsStringList(this.getClass().getResourceAsStream(gfxFileName));

		findAndLoadColorFiles(gfxFileName);
		initRenderer(x, y);
	}

	/**
	 * Initializes the ASCIIRenderer and loads the desired graphics from the specified file, and centers it on the
	 * screen.
	 *
	 * @param parentScreen Screen on which the ASCII graphics will be rendered
	 * @param parentPlane  Plane on which to draw the graphics
	 * @param gfxFileName  Path to file in which the desired graphics are stored
	 */
	public ASCIIRenderer(@NotNull Screen parentScreen, @NotNull Plane parentPlane, @NotNull String gfxFileName) {
		super(parentScreen, parentPlane);

		gfxFile = InputStreamReader.readAsStringList(this.getClass().getResourceAsStream(gfxFileName));

		int maxX = 0;
		for (String aGfxFile : gfxFile)
			if (aGfxFile.length() > maxX)
				maxX = aGfxFile.length();

		findAndLoadColorFiles(gfxFileName);
		initRenderer((Screen.COLUMNS - maxX) / 2, (Screen.ROWS - gfxFile.size()) / 2);
	}

	/*
	 * Given the name of the graphics file, searches for the corresponding color files, if they exist. This is done
	 * by trimming the .gfx file extension, and adding the .tcol and .bcol extensions. Search is conducted in the same
	 * dir as the .gfx file exists
	 *
	 * Defaults can be specified in the color files:
	 * -Default text color - What color to use when blank spaces are encountered in the graphics file
	 * -Default background color - What color to use when blank spaces are encountered on the background layer in the
	 * 							   graphics file
	 * -Palette - What palette file (relative path inside resources dir, no file extension) to use for determining
	 * 			  colors in the color files
	 */
	private void findAndLoadColorFiles(@NotNull String gfxFileName) {
		String[] removedFileExt = gfxFileName.split("\\.");
		String fileName = removedFileExt[0];
		String paletteFileName = null;
		LinkedList<String> toBeRemoved = new LinkedList<>();

		for (String aGfxFile : gfxFile) {
			String[] split = aGfxFile.split(" ");
			if (split.length > 2 && "using".equals(split[0]) && "palette".equals(split[1]))
				paletteFileName = "/palettes/" + split[2] + ".pal";
			else if (split.length > 3 && "using".equals(split[0]) && "default".equals(split[1])) {
				String[] rgbDefault = split[3].split(",");
				if ("textcolor".equals(split[2]))
					defaultText = Color.rgb(Integer.valueOf(rgbDefault[0]), Integer.valueOf(rgbDefault[1]), Integer
							.valueOf(rgbDefault[2]));
				else if ("backgroundcolor".equals(split[2]))
					defaultBack = Color.rgb(Integer.valueOf(rgbDefault[0]), Integer.valueOf(rgbDefault[1]), Integer
							.valueOf(rgbDefault[2]));
			} else if (!"".equals(aGfxFile.trim()))
				break;
			toBeRemoved.add(aGfxFile);
		}

		for (String s : toBeRemoved)
			gfxFile.remove(s);

		URL tUrl = this.getClass().getResource(fileName + ".tcol");
		URL bUrl = this.getClass().getResource(fileName + ".bcol");
		if (tUrl != null && paletteFileName != null)
			setTextColor(fileName + ".tcol", paletteFileName);
		if (bUrl != null && paletteFileName != null)
			setBackgroundColor(fileName + ".bcol", paletteFileName);
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
		textColorFile = InputStreamReader.readAsStringList(this.getClass().getResourceAsStream(textColorFileName));
		textPalette = new Palette(textPaletteFileName);
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
	 * @param backgroundColorFileName   Path to file in which the colors for the graphics are stored
	 * @param backgroundPaletteFileName Path to file in which the palette to be used is
	 */
	public void setBackgroundColor(@NotNull String backgroundColorFileName, @NotNull String
			backgroundPaletteFileName) {
		backgroundColorFile = InputStreamReader.readAsStringList(
				this.getClass().getResourceAsStream(backgroundColorFileName)
		);
		backgroundPalette = new Palette(backgroundPaletteFileName);
	}

	/**
	 * Receives and sets the colors for the background on which the graphics are rendered, as well as the palette with
	 * which to determine the specific colors mapped to each character in the color file.
	 *
	 * @param backgroundColorFile Path to file in which the colors for the graphics are stored
	 * @param backgroundPalette   Path to file in which the palette to be used is
	 */
	public void setBackgroundColor(@NotNull List<String> backgroundColorFile, @NotNull Palette backgroundPalette) {
		this.backgroundColorFile = backgroundColorFile;
		this.backgroundPalette = backgroundPalette;
	}

	@Override
	public void draw() {
		for (int y = textBounds.getTopLeftY(); y < textBounds.getBottomRightY(); y++)
			parentPlane.drawText(textBounds.getTopLeftX(), y, 0, gfxFile.get(y - textBounds.getTopLeftY()));

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
			Color c = textColor ? defaultText : defaultBack;
			String s = "";

			if (colorFile.size() == 1 && colorFile.get(0).length() == 1)
				c = getColorFromPalette(colorFile.get(0).charAt(0), textColor);

			for (int y = textBounds.getTopLeftY(); y < textBounds.getBottomRightY(); y++) {
				if (y < colorFile.size())
					s = colorFile.get(y);
				if (colorFile.size() > 1 && colorFile.get(y).length() == 1)
					c = getColorFromPalette(s.charAt(0), textColor);

				for (int x = textBounds.getTopLeftX(); x < textBounds.getBottomRightX(); x++) {
					if (y < colorFile.size() && colorFile.get(y).length() > 1 && colorFile.size() > 1) {
						if (x - textBounds.getTopLeftX() >= s.length())
							c = textColor ? defaultText : defaultBack;
						else
							c = getColorFromPalette(s.charAt(x - textBounds.getTopLeftX()), textColor);
					}

					if (textColor)
						parentPlane.setFontColor(x, y, 0, c);
					else
						parentPlane.setBackgroundColor(x, y, c);
				}
			}
		}
	}

	private Color getColorFromPalette(char c, boolean textColor) {
		if (c == ' ')
			return textColor ? defaultText : defaultBack;
		return textColor ? textPalette.getColor(c) : backgroundPalette.getColor(c);
	}

	/**
	 * Rotates the individual pixels around their individual center coordinate.
	 *
	 * @param rotAnData RotateAnimData object containing the desired options for the rotation animation
	 */
	public void rotateChars(RotateAnimData rotAnData) {
		parentScreen.rotateCharsAnim(
				textBounds.getTopLeftX(), textBounds.getTopRightX(),
				textBounds.getTopLeftY(), textBounds.getBottomRightY(), 0, rotAnData
		);
	}

	@Override
	public void remove() {
		for (int x = borderBounds.getTopLeftX(); x < borderBounds.getTopRightX(); x++)
			for (int y = borderBounds.getTopLeftY(); y < borderBounds.getBottomRightY(); y++)
				parentPlane.drawChar(x, y, 0, ' ');

		if (textColorFile != null && textPalette != null)
			for (int y = textBounds.getTopLeftY(); y < textBounds.getBottomRightY(); y++)
				for (int x = textBounds.getTopLeftX(); x < textBounds.getBottomRightX(); x++)
					parentPlane.setFontColor(x, y, 0, Color.WHITE);

		if (backgroundColorFile != null && backgroundPalette != null)
			for (int y = textBounds.getTopLeftY(); y < textBounds.getBottomRightY(); y++)
				for (int x = textBounds.getTopLeftX(); x < textBounds.getBottomRightX(); x++)
					parentPlane.setBackgroundColor(x, y, Color.BLACK);
	}
}
