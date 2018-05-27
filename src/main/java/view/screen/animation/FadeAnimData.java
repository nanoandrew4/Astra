package view.screen.animation;

import javafx.animation.Interpolator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class FadeAnimData {
	private int durationMillis = 1000;
	private double fromValue = 1, toValue = 0;
	private Interpolator interpolator = Interpolator.LINEAR;
	private EventHandler<ActionEvent> onFinished = (event) -> {};

	public FadeAnimData durationMillis(int durationMillis) {
		this.durationMillis = durationMillis;
		return this;
	}

	public FadeAnimData fromValue(double fromValue) {
		this.fromValue = fromValue;
		return this;
	}

	public FadeAnimData toValue(double toValue) {
		this.toValue = toValue;
		return this;
	}

	public FadeAnimData interpolator(Interpolator interpolator) {
		this.interpolator = interpolator;
		return this;
	}

	public FadeAnimData onFinished(EventHandler<ActionEvent> onFinished) {
		this.onFinished = onFinished;
		return this;
	}

	public int getDurationMillis() {
		return durationMillis;
	}

	public double getFromValue() {
		return fromValue;
	}

	public double getToValue() {
		return toValue;
	}

	public Interpolator getInterpolator() {
		return interpolator;
	}

	public EventHandler<ActionEvent> getOnFinished() {
		return onFinished;
	}
}
