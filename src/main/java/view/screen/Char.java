package view.screen;

import javafx.animation.RotateTransition;
import javafx.geometry.Point3D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.util.Duration;
import view.View;
import view.screen.animation.RotateAnimData;

import java.util.HashMap;

public class Char {
	private Text ch;
	private Rectangle background;

	public static HashMap<Axis, Point3D> axisMap = new HashMap<>();

	static {
		axisMap.put(Axis.X, new Point3D(1, 0, 0));
		axisMap.put(Axis.Y, new Point3D(0, 1, 0));
		axisMap.put(Axis.Z, new Point3D(0, 0, 1));
	}

	Char(Pane pane, double x, double y, double w, double h) {
		ch = new Text();
		ch.relocate(x, y);
		ch.setFont(View.font);
		ch.setFill(Color.WHITE);

		background = new Rectangle(w, h, Color.BLACK);
		background.relocate(x, y);

		ch.setBoundsType(TextBoundsType.VISUAL);

		pane.getChildren().addAll(background, ch);
	}

	public boolean noChar() {
		return ch.getText().equals("") || ch.getText().equals(" ");
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

	public void rotateAnimChar(RotateAnimData rotAnDat) {
		RotateTransition rt = new RotateTransition(Duration.millis(rotAnDat.getDurationMillis()), ch);
		rt.setFromAngle(rotAnDat.getStartAngle());
		rt.setToAngle(rotAnDat.getEndAngle());
		rt.setAxis(axisMap.get(rotAnDat.getAxis()));
		rt.setCycleCount(rotAnDat.getCycles());
		rt.setInterpolator(rotAnDat.getInterpolator());
		rt.setOnFinished(rotAnDat.getOnFinish());
		rt.play();
	}
}
