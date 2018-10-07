package lvleditor;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import view.drawables.Bounds;

import java.awt.Point;

public class ColorSliders {
	private LevelEditor levelEditor;

	private Bounds rColorSliderBounds, gColorSliderBounds, bColorSliderBounds;

	private char[] charColorSliders = {'r', 'g', 'b'};
	private int activeSlider = 0;

	public ColorSliders(LevelEditor levelEditor) {
		this.levelEditor = levelEditor;

		levelEditor.getKeyHandlers().add((event -> {
			if (event.getCode() == KeyCode.TAB)
				levelEditor.nextKeyHandler();
		}));

		Point rPickerPos = levelEditor.getPickerCentreCoords().get('r');
		rColorSliderBounds = new Bounds(
				new Point(rPickerPos.x, rPickerPos.y - LevelEditor.COLORS_PER_COLUMN),
				new Point(rPickerPos.x, rPickerPos.y - LevelEditor.COLORS_PER_COLUMN),
				new Point(rPickerPos.x, rPickerPos.y + LevelEditor.COLORS_PER_COLUMN),
				new Point(rPickerPos.x, rPickerPos.y + LevelEditor.COLORS_PER_COLUMN)
		);

		Point gPickerPos = levelEditor.getPickerCentreCoords().get('g');
		gColorSliderBounds = new Bounds(
				new Point(gPickerPos.x, gPickerPos.y - LevelEditor.COLORS_PER_COLUMN),
				new Point(gPickerPos.x, gPickerPos.y - LevelEditor.COLORS_PER_COLUMN),
				new Point(gPickerPos.x, gPickerPos.y + LevelEditor.COLORS_PER_COLUMN),
				new Point(gPickerPos.x, gPickerPos.y + LevelEditor.COLORS_PER_COLUMN)
		);

		Point bPickerPos = levelEditor.getPickerCentreCoords().get('b');
		bColorSliderBounds = new Bounds(
				new Point(bPickerPos.x, bPickerPos.y - LevelEditor.COLORS_PER_COLUMN),
				new Point(bPickerPos.x, bPickerPos.y - LevelEditor.COLORS_PER_COLUMN),
				new Point(bPickerPos.x, bPickerPos.y + LevelEditor.COLORS_PER_COLUMN),
				new Point(bPickerPos.x, bPickerPos.y + LevelEditor.COLORS_PER_COLUMN)
		);

		drawSlidingPalettes();

		levelEditor.getKeyHandlers().add((event) -> {
			KeyCode keyCode = event.getCode();
			if (keyCode == KeyCode.TAB)
				levelEditor.nextKeyHandler();

			else if (keyCode == KeyCode.UP || keyCode == KeyCode.DOWN) {
				if (canPaletteSliderMove(keyCode)) {
					Point pickerPos = levelEditor.getActivePickerCoordinates();
					int delta = keyCode == KeyCode.UP ? -1 : 1;
					levelEditor.colorPicker.selectedColor =
							levelEditor.getToolPlane().getBackgroundColor(pickerPos.x, pickerPos.y + delta);
					drawSlidingPalettes();
				}
			} else if (keyCode == KeyCode.RIGHT) {
				activeSlider = (++activeSlider % charColorSliders.length);

				levelEditor.getToolPlane().clearBorders();
				Point pickerPos = levelEditor.getActivePickerCoordinates();
				levelEditor.getToolPlane().drawPixelBorder(pickerPos.x, pickerPos.y);
			} else if (keyCode == KeyCode.LEFT) {
				activeSlider = ((--activeSlider + charColorSliders.length) % charColorSliders.length);

				levelEditor.getToolPlane().clearBorders();
				Point pickerPos = levelEditor.getActivePickerCoordinates();
				levelEditor.getToolPlane().drawPixelBorder(pickerPos.x, pickerPos.y);
			}
		});
	}

	private boolean canPaletteSliderMove(KeyCode keyCode) {
		return (activeSlider == 0 &&
				((levelEditor.colorPicker.selectedColor.getRed() != 1f && keyCode != KeyCode.UP) ||
				 (levelEditor.colorPicker.selectedColor.getRed() != 0 && keyCode != KeyCode.DOWN))
			   ) ||
			   (activeSlider == 1 &&
				((levelEditor.colorPicker.selectedColor.getGreen() != 1f && keyCode != KeyCode.UP) ||
				 (levelEditor.colorPicker.selectedColor.getGreen() != 0 && keyCode != KeyCode.DOWN))
			   ) ||
			   (activeSlider == 2 &&
				((levelEditor.colorPicker.selectedColor.getBlue() != 1f && keyCode != KeyCode.UP) ||
				 (levelEditor.colorPicker.selectedColor.getBlue() != 0 && keyCode != KeyCode.DOWN))
			   );
	}

	protected void drawSlidingPalettes() {
		drawSlidingPalette(levelEditor.getPickerCentreCoords().get('r'), LevelEditor.COLORS_PER_COLUMN * 2, 'r');
		drawSlidingPalette(levelEditor.getPickerCentreCoords().get('g'), LevelEditor.COLORS_PER_COLUMN * 2, 'g');
		drawSlidingPalette(levelEditor.getPickerCentreCoords().get('b'), LevelEditor.COLORS_PER_COLUMN * 2, 'b');
	}

	private void drawSlidingPalette(Point centerPos, int length, char channel) {
		boolean maxed = false;
		for (int y = 0; y < length; y++) {
			int r = getChannelColorValue((int) (levelEditor.getSelectedColor().getRed() * 255.0), y, channel == 'r');
			int g = getChannelColorValue((int) (levelEditor.getSelectedColor().getGreen() * 255.0), y, channel == 'g');
			int b = getChannelColorValue((int) (levelEditor.getSelectedColor().getBlue() * 255.0), y, channel == 'b');
			if (r <= 0 || r >= 255 || g <= 0 || g >= 255 || b <= 0 || b >= 255) {
				if (!maxed && r >= 255 && levelEditor.getSelectedColor().getRed() < 1.0) {
					r = 255;
					maxed = true;
				} else if (!maxed && r <= 0 && levelEditor.getSelectedColor().getRed() > 0.0) {
					r = 0;
					maxed = true;
				} else if (r > 255 || r < 0)
					r = g = b = 0;

				if (!maxed && g >= 255 && levelEditor.getSelectedColor().getGreen() < 1.0) {
					g = 255;
					maxed = true;
				} else if (!maxed && g <= 0 && levelEditor.getSelectedColor().getGreen() > 0.0) {
					g = 0;
					maxed = true;
				} else if (g > 255 || g < 0)
					g = b = r = 0;

				if (!maxed && b > 255 && levelEditor.getSelectedColor().getBlue() < 1.0) {
					b = 255;
					maxed = true;
				} else if (!maxed && b <= 0 && levelEditor.getSelectedColor().getBlue() > 0.0) {
					b = 0;
					maxed = true;
				} else if (b > 255 || b < 0)
					b = r = g = 0;
			}
			levelEditor.getToolPlane().setBackgroundColor(centerPos.x,
														  centerPos.y + ((y / 2) * (y % 2 == 0 ? 1 : -1)),
														  Color.rgb(r, g, b)
			);
		}

		levelEditor.getToolPlane().drawChar(centerPos.x, centerPos.y - length / 2 - 1, 0, channel);
		if (channel == 'r')
			levelEditor.getToolPlane().setFontColor(centerPos.x, centerPos.y - length / 2 - 1, 0, Color.RED);
		else if (channel == 'g')
			levelEditor.getToolPlane().setFontColor(centerPos.x, centerPos.y - length / 2 - 1, 0, Color.GREEN);
		else if (channel == 'b')
			levelEditor.getToolPlane().setFontColor(centerPos.x, centerPos.y - length / 2 - 1, 0, Color.BLUE);
	}

	private int getChannelColorValue(int baseValue, int y, boolean gradient) {
		return baseValue + (gradient ? ((y / 2) * (y % 2 == 0 ? 1 : -1)) * LevelEditor.COLORS_PER_COLUMN : 0);
	}

	public Bounds getRedColorSliderBounds() {
		return rColorSliderBounds;
	}

	public Bounds getGreenColorSliderBounds() {
		return gColorSliderBounds;
	}

	public Bounds getBlueColorSliderBounds() {
		return bColorSliderBounds;
	}

	public char getActiveSlider() {
		return charColorSliders[activeSlider];
	}
}
