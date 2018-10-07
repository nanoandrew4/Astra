package lvleditor;

import view.drawables.Bounds;
import view.screen.Plane;
import view.screen.Screen;

import java.awt.Point;

public class Editor {
	private LevelEditor levelEditor;
	private Plane editorPlane;
	private Bounds editorBounds;

	// Position on the editor screen
	private Point editorPos = new Point(Screen.COLUMNS / 2, 0);

	// Position in the scene being created
	private Point editingScenePos = new Point(0, 0);

	public Editor(LevelEditor levelEditor) {
		this.levelEditor = levelEditor;

		editorBounds = new Bounds(
				new Point(Screen.COLUMNS / 2, 0), new Point(Screen.COLUMNS, 0),
				new Point(Screen.COLUMNS / 2, Screen.ROWS), new Point(Screen.COLUMNS, Screen.ROWS)
		);

		editorPlane = new Plane(
				levelEditor.getParentScreen(),
				new Point(Screen.COLUMNS / 2, Screen.ROWS / 2), 3, new Point(Screen.COLUMNS / 2, Screen.ROWS),
				new Point(Screen.COLUMNS / 2, 0), new Point(0, 0)
		);

		levelEditor.getKeyHandlers().add((event) -> {
			System.out.println(editorPos);
			System.out.println(editingScenePos);
			System.out.println();
			switch (event.getCode()) {
				case TAB:
					levelEditor.nextKeyHandler();
					break;
				case UP:
					if (editorPos.y > editorBounds.getTopLeftY())
						--editorPos.y;

					if (editingScenePos.y <= 0)
						editorPlane.addRow(true);
					else
						--editingScenePos.y;
					break;
				case DOWN:
					if (editorPos.y < editorBounds.getBottomRightY() - 1)
						++editorPos.y;

					++editingScenePos.y;
					if (editingScenePos.y >= editorPlane.getHeight())
						editorPlane.addRow(false);
					break;
				case LEFT:
					if (editorPos.x > editorBounds.getTopLeftX())
						--editorPos.x;

					if (editingScenePos.x <= 0)
						editorPlane.addColumn(true);
					else
						--editingScenePos.x;
					break;
				case RIGHT:
					if (editorPos.x < editorBounds.getBottomRightX() - 1)
						++editorPos.x;

					++editingScenePos.x;
					if (editingScenePos.x >= editorPlane.getWidth())
						editorPlane.addColumn(false);
					break;
			}

			editorPlane.clearBorders();
			editorPlane.drawPixelBorder(editingScenePos.x, editingScenePos.y);
		});
	}
}
