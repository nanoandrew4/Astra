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
	protected ColorPicker colorPicker;
	protected ColorSliders colorSliders;
	private ArrayList<EventHandler<KeyEvent>> keyHandlers;
	private int activeHandler = 0;

	private Bounds gfxEditor;

	protected final static int COLORS_PER_COLUMN = 8;
	private char activeColorSlider = 'r';
	private HashMap<Character, Point> pickerCentreCoords;

	private Point offset = new Point(0, 0);

	protected char getActiveColorSlider() {
		return activeColorSlider;
	}

	protected void setActiveColorSlider(char color) {
		if (color == 'r' || color == 'g' || color == 'b')
			activeColorSlider = color;
	}

	protected ArrayList<EventHandler<KeyEvent>> getKeyHandlers() {
		return keyHandlers;
	}

	protected Screen getParentScreen() {
		return parentScreen;
	}

	protected void nextKeyHandler() {
		parentScreen.setKeyHandlers(keyHandlers.get(++activeHandler % keyHandlers.size()));
	}

	protected Color getSelectedColor() {
		return colorPicker.selectedColor;
	}

	protected HashMap<Character, Point> getPickerCentreCoords() {
		return pickerCentreCoords;
	}

	protected Plane getToolPlane() {
		return toolPlane;
	}

	protected Point getActivePickerCoordinates() {
		return pickerCentreCoords.get(activeColorSlider);
	}

	public LevelEditor(Screen parentScreen) {
		parentScreen.clearScreen();

		this.parentScreen = parentScreen;
		keyHandlers = new ArrayList<>();
		pickerCentreCoords = new HashMap<>();

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

	public void setSelectedColor(Color c) {
		colorPicker.selectedColor = c;
		parentScreen.drawToBackgroundLayer(colorPicker.getSelectedColorPos(), c);
	}

	private void drawColorTools() {
		int topLeftX = Screen.COLUMNS / 16 - 4;
		int topLeftY = (int) (Screen.ROWS / 1.5) - 4;

		colorPicker = new ColorPicker(topLeftX, topLeftY, this);
		colorSliders = new ColorSliders(this);

		parentScreen.setKeyHandlers(keyHandlers.get(0));

		toolPlane.drawPixelBorder(pickerCentreCoords.get(activeColorSlider));
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
