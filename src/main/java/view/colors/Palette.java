package view.colors;

import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

/**
 * Loads RGB combinations stored in a palette file, with the character it is associated with. This means there may be
 * as many colors in any given palette as there are characters on the keyboard.
 *
 * The associated characters are later used in color files for graphics, and the engine uses the mapped colors when
 * rendering the graphics.
 */
public class Palette {
	private HashMap<Character, Color> palette;

	/**
	 * Initializes a Palette.
	 *
	 * @param paletteFile File in which the desired colors for the Palette are stored
	 */
	public Palette(String paletteFile) {
		palette = new HashMap<>();
		try {
			List<String> rawPalette = Files.readAllLines(Paths.get(this.getClass().getResource(paletteFile).toURI()));
			for (String s : rawPalette) {
				String[] charSplit = s.split("=");
				String[] colorSplit = charSplit[1].split(",");
				palette.put(
						charSplit[0].charAt(0),
						Color.rgb(
								Integer.valueOf(colorSplit[0]), Integer.valueOf(colorSplit[1]), Integer.valueOf(colorSplit[2])
						)
				);
			}
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves a color given a character, used by the engine to draw the appropriate colors when rendering graphics.
	 * @param c Character from color file
	 * @return Color to be rendered
	 */
	public Color getColor(char c) {
		return palette.get(c);
	}
}
