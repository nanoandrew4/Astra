package view.screen.animation;

import javafx.animation.Interpolator;
import view.screen.Axis;

public class RotateAnimData {
	public int cycles = 1, duration = 1000;
	public double startAngle = 0, endAngle = 360;
	public Axis axis = Axis.X;
	public Interpolator interpolator = Interpolator.LINEAR;
}
