package lvleditor;

import javafx.scene.paint.Color;
import view.drawables.Bounds;

import java.awt.Point;

public class ColorPicker {
	private Point selectedColorPos;
	private Bounds colorPickerBounds;

	public Color selectedColor = Color.rgb(128, 128, 128);

	public ColorPicker(int topLeftX, int topLeftY, LevelEditor levelEditor) {
		String selectedColorText = "Selected color: ";
		selectedColorPos = new Point(topLeftX + selectedColorText.length(), topLeftY - 4);

		levelEditor.getToolPlane().drawText(topLeftX, selectedColorPos.y, 0, selectedColorText);
		levelEditor.getToolPlane().setBackgroundColor(selectedColorPos.x, selectedColorPos.y, selectedColor);

		int multiplier = 255 / LevelEditor.COLORS_PER_COLUMN;

		levelEditor.getPickerCentreCoords().put('r', new Point(topLeftX + LevelEditor.COLORS_PER_COLUMN * 4 + 5,
															   topLeftY + LevelEditor.COLORS_PER_COLUMN));
		levelEditor.getPickerCentreCoords().put('g', new Point(topLeftX + LevelEditor.COLORS_PER_COLUMN * 4 + 8,
															   topLeftY + LevelEditor.COLORS_PER_COLUMN));
		levelEditor.getPickerCentreCoords().put('b', new Point(topLeftX + LevelEditor.COLORS_PER_COLUMN * 4 + 11,
															   topLeftY + LevelEditor.COLORS_PER_COLUMN));

		colorPickerBounds = new Bounds(
				new Point(topLeftX, topLeftY),
				new Point(topLeftX + LevelEditor.COLORS_PER_COLUMN * 2, topLeftY),
				new Point(topLeftX, topLeftY + LevelEditor.COLORS_PER_COLUMN * 2),
				new Point(topLeftX + LevelEditor.COLORS_PER_COLUMN * 2,
						  topLeftY + LevelEditor.COLORS_PER_COLUMN * 2)
		);

		for (int z = 0; z < LevelEditor.COLORS_PER_COLUMN; z++)
			for (int y = 0; y < LevelEditor.COLORS_PER_COLUMN; y++)
				for (int x = 0; x <= LevelEditor.COLORS_PER_COLUMN; x++) {
					int colorX = topLeftX + x + LevelEditor.COLORS_PER_COLUMN * (z % 4);
					int colorY = topLeftY + y + (z / 4) * LevelEditor.COLORS_PER_COLUMN;
					levelEditor.getToolPlane().setBackgroundColor(colorX, colorY,
												 Color.rgb(x * multiplier, y * multiplier, z * multiplier)
					);
					levelEditor.getParentScreen().setMouseClickHandler(colorX, colorY, (event) -> {
						levelEditor.setSelectedColor(levelEditor.getToolPlane().getBackgroundColor(colorX, colorY));
						levelEditor.getToolPlane().clearBorders();
						levelEditor.colorSliders.drawSlidingPalettes();
						levelEditor.getToolPlane()
								   .drawPixelBorder(levelEditor.getActivePickerCoordinates());
					});
				}
	}

	public Point getSelectedColorPos() {
		return selectedColorPos;
	}

	public Bounds getBounds() {
		return colorPickerBounds;
	}
}
