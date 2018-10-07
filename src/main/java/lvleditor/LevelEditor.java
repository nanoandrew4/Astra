package lvleditor;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import view.screen.Plane;
import view.screen.Screen;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class LevelEditor {

	private Screen parentScreen;
	private Plane toolPlane;

	private Editor editor;
	protected ColorPicker colorPicker;
	protected ColorSliders colorSliders;

	private ArrayList<EventHandler<KeyEvent>> keyHandlers;
	private int activeHandler = 0;


	protected final static int COLORS_PER_COLUMN = 8;
	private HashMap<Character, Point> pickerCentreCoords;

	private Point offset = new Point(0, 0);

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
		return pickerCentreCoords.get(colorSliders.getActiveSlider());
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

		parentScreen.setBackgroundColor(0,0, Color.WHITE);
		parentScreen.setBackgroundColor(Screen.COLUMNS - 1, 0, Color.WHITE);
		parentScreen.setBackgroundColor(0, Screen.ROWS - 1, Color.WHITE);
		parentScreen.setBackgroundColor(Screen.COLUMNS - 1, Screen.ROWS - 1, Color.WHITE);
		parentScreen.setBackgroundColor(Screen.COLUMNS - 2, Screen.ROWS - 1, Color.WHITE);

		initLevelEditor();
	}

	protected void setSelectedColor(Color c) {
		colorPicker.selectedColor = c;
		parentScreen.setBackgroundColor(colorPicker.getSelectedColorPos(), c);
	}

	private void initLevelEditor() {
		int topLeftX = Screen.COLUMNS / 16 - 4;
		int topLeftY = (int) (Screen.ROWS / 1.5) - 4;

		colorPicker = new ColorPicker(topLeftX, topLeftY, this);
		colorSliders = new ColorSliders(this);
		editor = new Editor(this);

		parentScreen.setKeyHandlers(keyHandlers.get(0));

		toolPlane.drawPixelBorder(pickerCentreCoords.get(colorSliders.getActiveSlider()));
	}
}
