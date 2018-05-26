package view.colors;

import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class Palette {
	private HashMap<Character, Color> palette;

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

	public Color getColor(char c) {
		return palette.get(c);
	}
}
