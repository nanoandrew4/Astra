package view.screen;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import view.View;

public class Char {
	private Text ch;
	private Rectangle background;

	Char(Pane pane, double x, double y, double w, double h) {
		ch = new Text();
		ch.relocate(x, y);
		ch.setFont(View.font);
		ch.setFill(Color.WHITE);

		background = new Rectangle(w, h, Color.BLACK);
		background.relocate(x, y);

		pane.getChildren().addAll(background, ch);
	}

	public void setBackgroundColor(Color c) {
		background.setFill(c);
	}

	public void setCharColor(Color c) {
		ch.setFill(c);
	}

	public void setChar(char c) {
		ch.setText(String.valueOf(c));
	}

	public void flipChar() {
		ch.setRotate(ch.getRotate() == 180.0 ? 0.0 : 180.0);
	}
}
