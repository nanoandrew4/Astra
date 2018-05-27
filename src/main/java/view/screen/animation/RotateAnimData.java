package view.screen.animation;

import javafx.animation.Interpolator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import view.screen.Axis;


public class RotateAnimData {
	private int cycles = 1, durationMillis = 1000;
	private double startAngle = 0, endAngle = 360;
	private Axis axis = Axis.X;
	private Interpolator interpolator = Interpolator.LINEAR;

	private EventHandler<ActionEvent> onFinish = event1 -> {
	};
	private boolean isFinished = false;

	public RotateAnimData cycles(int cycles) {
		this.cycles = cycles;
		return this;
	}

	public RotateAnimData durationMillis(int millis) {
		this.durationMillis = millis;
		return this;
	}

	public RotateAnimData startAngle(double startAngle) {
		this.startAngle = startAngle;
		return this;
	}

	public RotateAnimData endAngle(double endAngle) {
		this.endAngle = endAngle;
		return this;
	}

	public RotateAnimData axis(Axis axis) {
		this.axis = axis;
		return this;
	}

	public RotateAnimData interpolator(Interpolator interpolator) {
		this.interpolator = interpolator;
		return this;
	}

	public RotateAnimData onFinish(EventHandler<ActionEvent> onFinish) {
		this.onFinish = onFinish;
		return this;
	}

	public int getCycles() {
		return cycles;
	}

	public int getDurationMillis() {
		return durationMillis;
	}

	public double getStartAngle() {
		return startAngle;
	}

	public double getEndAngle() {
		return endAngle;
	}

	public Axis getAxis() {
		return axis;
	}

	public Interpolator getInterpolator() {
		return interpolator;
	}

	public EventHandler<ActionEvent> getOnFinish() {
		return onFinish;
	}
}
