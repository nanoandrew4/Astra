package lvleditor;

import javafx.scene.paint.Color;
import view.drawables.Bounds;
import view.screen.Plane;
import view.screen.Screen;

import java.awt.*;

public class LevelEditor {

	private Screen screen;
	private Plane toolPlane, editorPlane;
	
	private Bounds colorPicker, rColorSlider, gColorSlider, bColorSlider, gfxEditor;

	private Color selectedColor = Color.rgb(128, 128, 128);

	public LevelEditor(Screen screen) {
		this.screen = screen;
		toolPlane = new Plane(
				screen, new Point(Screen.COLUMNS / 2, Screen.ROWS), 1,
				new Point(Screen.COLUMNS / 2, Screen.ROWS), new Point(0, 0), new Point(0, 0)
		);

		editorPlane = new Plane(
				screen, new Point(10, 10), 3, new Point(Screen.COLUMNS / 2, Screen.ROWS),
				new Point(Screen.COLUMNS / 2, 0), new Point(0, 0)
		);

		drawColorTools();
		drawEditor();
	}

	private void drawColorTools() {
		int topLeftX = Screen.COLUMNS / 16 - 4;
		int topLeftY = (int) (Screen.ROWS / 1.5) - 4;

		int colorsPerColumn = 8;
		int multiplier = 255 / colorsPerColumn;
		
		colorPicker = new Bounds(
				new Point(topLeftX, topLeftY), new Point(topLeftX + colorsPerColumn * 2, topLeftY),
				new Point(topLeftX, topLeftY + colorsPerColumn * 2), new Point(topLeftX + colorsPerColumn * 2, topLeftY + colorsPerColumn * 2)
		);

		for (int z = 0; z < colorsPerColumn; z++)
			for (int y = 0; y < colorsPerColumn; y++)
				for (int x = 0; x <= colorsPerColumn; x++) {
					toolPlane.setBackgroundColor(topLeftX + x + colorsPerColumn * (z % 4), topLeftY + y + (z / 4) * colorsPerColumn,
							Color.rgb(x * multiplier, y * multiplier, z * multiplier)
					);
				}

		Point rPickerPos = new Point(topLeftX + colorsPerColumn * 4 + 5, topLeftY + colorsPerColumn);
		Point gPickerPos = new Point(topLeftX + colorsPerColumn * 4 + 8, topLeftY + colorsPerColumn);
		Point bPickerPos = new Point(topLeftX + colorsPerColumn * 4 + 11, topLeftY + colorsPerColumn);
		
		rColorSlider = new Bounds(
				new Point(rPickerPos.x, rPickerPos.y - colorsPerColumn), new Point(rPickerPos.x, rPickerPos.y - colorsPerColumn),
				new Point(rPickerPos.x, rPickerPos.y + colorsPerColumn), new Point(rPickerPos.x, rPickerPos.y + colorsPerColumn)
		);

		gColorSlider = new Bounds(
				new Point(gPickerPos.x, gPickerPos.y - colorsPerColumn), new Point(gPickerPos.x, gPickerPos.y - colorsPerColumn),
				new Point(gPickerPos.x, gPickerPos.y + colorsPerColumn), new Point(gPickerPos.x, gPickerPos.y + colorsPerColumn)
		);

		bColorSlider = new Bounds(
				new Point(bPickerPos.x, bPickerPos.y - colorsPerColumn), new Point(bPickerPos.x, bPickerPos.y - colorsPerColumn),
				new Point(bPickerPos.x, bPickerPos.y + colorsPerColumn), new Point(bPickerPos.x, bPickerPos.y + colorsPerColumn)
		);
		
		drawSlidingPalette(rPickerPos, colorsPerColumn * 2, 'r');
		drawSlidingPalette(gPickerPos, colorsPerColumn * 2, 'g');
		drawSlidingPalette(bPickerPos, colorsPerColumn * 2, 'b');
	}

	private void drawSlidingPalette(Point centerPos, int length, char rgb) {
		for (int y = -length / 2; y < length / 2; y++) {
			int r = (int) (selectedColor.getRed() * 255.0) + (rgb == 'r' ? y * 10 : 0);
			int g = (int) (selectedColor.getGreen() * 255.0) + (rgb == 'g' ? y * 10 : 0);
			int b = (int) (selectedColor.getBlue() * 255.0) + (rgb == 'b' ? y * 10 : 0);
			toolPlane.setBackgroundColor(centerPos.x, centerPos.y + y, Color.rgb(r, g, b));
		}
	}

	private void drawEditor() {
		gfxEditor = new Bounds(
				new Point(Screen.COLUMNS / 2, 0), new Point(Screen.COLUMNS, 0),
				new Point(Screen.COLUMNS / 2, Screen.ROWS), new Point(Screen.COLUMNS, Screen.ROWS)
		);


	}
}
