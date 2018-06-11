package view.screen.animation;

import com.sun.istack.internal.NotNull;
import javafx.animation.Interpolator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Builder for all used attributes that can be set in FadeTransition. Passed when requesting an animation to the Screen.
 * Allows for more compact attribute setting.
 */
public class FadeAnimData {
	private int durationMillis = 1000;
	private double fromValue = 1, toValue = 0;
	private Interpolator interpolator = Interpolator.LINEAR;
	private EventHandler<ActionEvent> onFinished = (event) -> {
	};

	/**
	 * Desired duration for the animation, in milliseconds.
	 *
	 * @param durationMillis Duration in milliseconds
	 * @return FadeAnimData with the requested duration
	 */
	public FadeAnimData durationMillis(int durationMillis) {
		this.durationMillis = durationMillis;
		return this;
	}

	/**
	 * Opacity value to fade from (0.0-1.0). The graphical element will be set to this opacity when the animation
	 * starts.
	 *
	 * @param fromValue Initial opacity
	 * @return FadeAnimData with the requested initial opacity
	 */
	public FadeAnimData fromValue(double fromValue) {
		this.fromValue = fromValue;
		return this;
	}

	/**
	 * Opacity value to fade to (0.0-1.0). This will be the opacity the graphical element will be left at, once the
	 * animation has ended.
	 *
	 * @param toValue Final opacity
	 * @return FadeAnimData with the requested final opacity
	 */
	public FadeAnimData toValue(double toValue) {
		this.toValue = toValue;
		return this;
	}

	/**
	 * Interpolator to be used for the animation. See Interpolator class.
	 *
	 * @param interpolator Desired interpolator
	 * @return FadeAnimData with the requested interpolator
	 */
	public FadeAnimData interpolator(@NotNull Interpolator interpolator) {
		this.interpolator = interpolator;
		return this;
	}

	/**
	 * Event handler to run when the animation is finished executing.
	 *
	 * @param onFinished Desired event handler to execute once the animation is finished
	 * @return FadeAnimData with the requested event handler
	 */
	public FadeAnimData onFinished(@NotNull EventHandler<ActionEvent> onFinished) {
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
