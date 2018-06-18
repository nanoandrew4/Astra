package lvleditor;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import view.drawables.Bounds;
import view.screen.Plane;
import view.screen.Screen;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class LevelEditor {

	private Screen parentScreen;
	private Plane toolPlane, editorPlane;
	private ArrayList<EventHandler<KeyEvent>> keyHandlers;
	private int activeHandler = 0;

	private Bounds colorPicker, rColorSlider, gColorSlider, bColorSlider, gfxEditor;

	private int colorsPerColumn = 8;
	private char activeColorSlider = 'r';
	private HashMap<Character, Point> pickerCenterCoords;

	private Point selectedColorPos;
	private Color selectedColor = Color.rgb(128, 128, 128);
	private Point offset = new Point(0, 0);

	public LevelEditor(Screen parentScreen) {
		parentScreen.clearScreen();

		this.parentScreen = parentScreen;
		keyHandlers = new ArrayList<>();
		pickerCenterCoords = new HashMap<>();

		toolPlane = new Plane(
				parentScreen, new Point(Screen.COLUMNS / 2, Screen.ROWS), 1,
				new Point(Screen.COLUMNS / 2, Screen.ROWS), new Point(0, 0), new Point(0, 0)
		);

		editorPlane = new Plane(
				parentScreen, new Point(10, 10), 3, new Point(Screen.COLUMNS / 2, Screen.ROWS),
				new Point(Screen.COLUMNS / 2, 0), new Point(0, 0)
		);

		drawColorTools();
		drawEditor();
	}

	private void setSelectedColor(Color c) {
		selectedColor = c;
		parentScreen.drawToBackgroundLayer(selectedColorPos.x, selectedColorPos.y, c);
	}

	private void drawColorTools() {
		int topLeftX = Screen.COLUMNS / 16 - 4;
		int topLeftY = (int) (Screen.ROWS / 1.5) - 4;

		String selectedColorText = "Selected color: ";
		selectedColorPos = new Point(topLeftX + selectedColorText.length(), topLeftY - 4);

		toolPlane.drawText(topLeftX, selectedColorPos.y, 0, selectedColorText);
		toolPlane.setBackgroundColor(selectedColorPos.x, selectedColorPos.y, selectedColor);

		int multiplier = 255 / colorsPerColumn;

		pickerCenterCoords.put('r', new Point(topLeftX + colorsPerColumn * 4 + 5, topLeftY + colorsPerColumn));
		pickerCenterCoords.put('g', new Point(topLeftX + colorsPerColumn * 4 + 8, topLeftY + colorsPerColumn));
		pickerCenterCoords.put('b', new Point(topLeftX + colorsPerColumn * 4 + 11, topLeftY + colorsPerColumn));
		toolPlane.drawPixelBorder(pickerCenterCoords.get(activeColorSlider));

		colorPicker = new Bounds(
				new Point(topLeftX, topLeftY), new Point(topLeftX + colorsPerColumn * 2, topLeftY),
				new Point(topLeftX, topLeftY + colorsPerColumn * 2), new Point(topLeftX + colorsPerColumn * 2, topLeftY + colorsPerColumn * 2)
		);

		for (int z = 0; z < colorsPerColumn; z++)
			for (int y = 0; y < colorsPerColumn; y++)
				for (int x = 0; x <= colorsPerColumn; x++) {
					int colorX = topLeftX + x + colorsPerColumn * (z % 4);
					int colorY = topLeftY + y + (z / 4) * colorsPerColumn;
					toolPlane.setBackgroundColor(colorX, colorY,
							Color.rgb(x * multiplier, y * multiplier, z * multiplier)
					);
					parentScreen.setMouseClickHandler(colorX, colorY, (event) -> {
						setSelectedColor(toolPlane.getBackgroundColor(colorX, colorY));
						toolPlane.clearBorders();
						drawSlidingPalettes();
						toolPlane.drawPixelBorder(pickerCenterCoords.get(activeColorSlider));
					});
				}

		keyHandlers.add((event -> {
			if (event.getCode() == KeyCode.TAB)
				parentScreen.setKeyHandlers(keyHandlers.get(++activeHandler % keyHandlers.size()));
		}));
		parentScreen.setKeyHandlers(keyHandlers.get(0));

		Point rPickerPos = pickerCenterCoords.get('r');
		rColorSlider = new Bounds(
				new Point(rPickerPos.x, rPickerPos.y - colorsPerColumn), new Point(rPickerPos.x, rPickerPos.y - colorsPerColumn),
				new Point(rPickerPos.x, rPickerPos.y + colorsPerColumn), new Point(rPickerPos.x, rPickerPos.y + colorsPerColumn)
		);

		Point gPickerPos = pickerCenterCoords.get('g');
		gColorSlider = new Bounds(
				new Point(gPickerPos.x, gPickerPos.y - colorsPerColumn), new Point(gPickerPos.x, gPickerPos.y - colorsPerColumn),
				new Point(gPickerPos.x, gPickerPos.y + colorsPerColumn), new Point(gPickerPos.x, gPickerPos.y + colorsPerColumn)
		);

		Point bPickerPos = pickerCenterCoords.get('b');
		bColorSlider = new Bounds(
				new Point(bPickerPos.x, bPickerPos.y - colorsPerColumn), new Point(bPickerPos.x, bPickerPos.y - colorsPerColumn),
				new Point(bPickerPos.x, bPickerPos.y + colorsPerColumn), new Point(bPickerPos.x, bPickerPos.y + colorsPerColumn)
		);

		drawSlidingPalettes();

		keyHandlers.add((event) -> {
			KeyCode keyCode = event.getCode();
			if (keyCode == KeyCode.TAB)
				parentScreen.setKeyHandlers(keyHandlers.get(++activeHandler % keyHandlers.size()));
			else if (keyCode == KeyCode.UP || keyCode == KeyCode.DOWN) {
				Point pickerPos = pickerCenterCoords.get(activeColorSlider);
				if ((activeColorSlider == 'r' && ((selectedColor.getRed() != 1f && keyCode != KeyCode.UP) || (selectedColor.getRed() != 0 && keyCode != KeyCode.DOWN)))
						|| (activeColorSlider == 'g' && ((selectedColor.getGreen() != 1f && keyCode != KeyCode.UP) || (selectedColor.getGreen() != 0 && keyCode != KeyCode.DOWN)))
						|| (activeColorSlider == 'b' && ((selectedColor.getBlue() != 1f && keyCode != KeyCode.UP) || (selectedColor.getBlue() != 0 && keyCode != KeyCode.DOWN))))
					setSelectedColor(toolPlane.getBackgroundColor(pickerPos.x, pickerPos.y + (keyCode == KeyCode.UP ? -1 : 1)));
				drawSlidingPalettes();
			} else if (keyCode == KeyCode.RIGHT) {
				if (activeColorSlider == 'r')
					activeColorSlider = 'g';
				else if (activeColorSlider == 'g')
					activeColorSlider = 'b';
				else if (activeColorSlider == 'b')
					activeColorSlider = 'r';

				toolPlane.clearBorders();
				Point pickerPos = pickerCenterCoords.get(activeColorSlider);
				toolPlane.drawPixelBorder(pickerPos.x, pickerPos.y);
			} else if (keyCode == KeyCode.LEFT) {
				if (activeColorSlider == 'r')
					activeColorSlider = 'b';
				else if (activeColorSlider == 'g')
					activeColorSlider = 'r';
				else if (activeColorSlider == 'b')
					activeColorSlider = 'g';

				toolPlane.clearBorders();
				Point pickerPos = pickerCenterCoords.get(activeColorSlider);
				toolPlane.drawPixelBorder(pickerPos.x, pickerPos.y);
			}
		});
	}

	private void drawSlidingPalettes() {
		drawSlidingPalette(pickerCenterCoords.get('r'), colorsPerColumn * 2, 'r');
		drawSlidingPalette(pickerCenterCoords.get('g'), colorsPerColumn * 2, 'g');
		drawSlidingPalette(pickerCenterCoords.get('b'), colorsPerColumn * 2, 'b');
	}

	private void drawSlidingPalette(Point centerPos, int length, char rgb) {
		boolean maxed = false;
		for (int y = 0; y < length; y++) {
			int r = (int) (selectedColor.getRed() * 255.0) + (rgb == 'r' ? ((y / 2) * (y % 2 == 0 ? 1 : -1)) * 8 : 0);
			int g = (int) (selectedColor.getGreen() * 255.0) + (rgb == 'g' ? ((y / 2) * (y % 2 == 0 ? 1 : -1)) * 8 : 0);
			int b = (int) (selectedColor.getBlue() * 255.0) + (rgb == 'b' ? ((y / 2) * (y % 2 == 0 ? 1 : -1)) * 8 : 0);
			if (r <= 0 || r >= 255 || g <= 0 || g >= 255 || b <= 0 || b >= 255) {
				if (!maxed && r >= 255 && selectedColor.getRed() < 1.0) {
					r = 255;
					maxed = true;
				} else if (!maxed && r <= 0 && selectedColor.getRed() > 0.0) {
					r = 0;
					maxed = true;
				} else if (r > 255 || r < 0)
					r = g = b = 0;

				if (!maxed && g >= 255 && selectedColor.getGreen() < 1.0) {
					g = 255;
					maxed = true;
				} else if (!maxed && g <= 0 && selectedColor.getGreen() > 0.0) {
					g = 0;
					maxed = true;
				} else if (g > 255 || g < 0)
					g = b = r = 0;

				if (!maxed && b > 255 && selectedColor.getBlue() < 1.0) {
					b = 255;
					maxed = true;
				} else if (!maxed && b <= 0 && selectedColor.getBlue() > 0.0) {
					b = 0;
					maxed = true;
				} else if (b > 255 || b < 0)
					b = r = g = 0;
			}
			toolPlane.setBackgroundColor(centerPos.x, centerPos.y + ((y / 2) * (y % 2 == 0 ? 1 : -1)), Color.rgb(r, g, b));
		}

		toolPlane.drawChar(centerPos.x, centerPos.y - length / 2 - 1, 0, rgb);
		if (rgb == 'r')
			toolPlane.setFontColor(centerPos.x, centerPos.y - length / 2 - 1, 0, Color.RED);
		else if (rgb == 'g')
			toolPlane.setFontColor(centerPos.x, centerPos.y - length / 2 - 1, 0, Color.GREEN);
		else
			toolPlane.setFontColor(centerPos.x, centerPos.y - length / 2 - 1, 0, Color.BLUE);
	}

	private void drawEditor() {
		gfxEditor = new Bounds(
				new Point(Screen.COLUMNS / 2, 0), new Point(Screen.COLUMNS, 0),
				new Point(Screen.COLUMNS / 2, Screen.ROWS), new Point(Screen.COLUMNS, Screen.ROWS)
		);

		keyHandlers.add((event) -> {
			switch (event.getCode()) {
				case TAB:
					parentScreen.setKeyHandlers(keyHandlers.get(++activeHandler % keyHandlers.size()));
					break;
				case UP:
					break;
				case DOWN:
					break;
				case LEFT:
					break;
				case RIGHT:
					break;
			}
		});
	}
}
