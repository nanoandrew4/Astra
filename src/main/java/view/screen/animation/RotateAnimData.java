package view.screen.animation;

import javafx.animation.Interpolator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import view.screen.Axis;

/**
 * Builder for all used attributes that can be set in RotateTransition. Passed when requesting an animation to the Screen.
 * Allows for more compact attribute setting.
 */
public class RotateAnimData {
	private int cycles = 1, durationMillis = 1000;
	private double startAngle = 0, endAngle = 360;
	private Axis axis = Axis.X;
	private Interpolator interpolator = Interpolator.LINEAR;

	private EventHandler<ActionEvent> onFinished = event -> {};
	private boolean isFinished = false;

	/**
	 * Number of times to perform the rotation before the animation ends.
	 *
	 * @param cycles Number of rotations
	 * @return RotateAnimData with the requested number of cycles
	 */
	public RotateAnimData cycles(int cycles) {
		this.cycles = cycles;
		return this;
	}

	/**
	 * Desired duration for the animation, in milliseconds.
	 *
	 * @param durationMillis Duration in milliseconds
	 * @return RotateAnimData with the requested duration
	 */
	public RotateAnimData durationMillis(int durationMillis) {
		this.durationMillis = durationMillis;
		return this;
	}

	/**
	 * Angle at which the animation starts (0-360). All graphical elements will be rotated to this angle when the
	 * animation starts.
	 *
	 * @param startAngle Starting angle
	 * @return RotateAnimData with the requested starting angle
	 */
	public RotateAnimData startAngle(double startAngle) {
		this.startAngle = startAngle;
		return this;
	}

	/**
	 * Angle at which the animation ends (0-360). All graphical elements will be left at this angle, once the animation
	 * ends.
	 *
	 * @param endAngle Ending angle
	 * @return RotateAnimData with the requested ending angle
	 */
	public RotateAnimData endAngle(double endAngle) {
		this.endAngle = endAngle;
		return this;
	}

	/**
	 * Axis on which to rotate the graphical element(s). See the Axis enum.
	 * @param axis Axis on which to rotate the graphical element(s)
	 * @return RotateAnimData with the requested rotational axis
	 */
	public RotateAnimData axis(Axis axis) {
		this.axis = axis;
		return this;
	}

	/**
	 * Interpolator to be used for the animation. See Interpolator class.
	 *
	 * @param interpolator Desired interpolator
	 * @return RotateAnimData with the requested interpolator
	 */
	public RotateAnimData interpolator(Interpolator interpolator) {
		this.interpolator = interpolator;
		return this;
	}

	/**
	 * Event handler to run when the animation is finished executing.
	 *
	 * @param onFinished Desired event handler to execute once the animation is finished
	 * @return RotateAnimData with the requested event handler
	 */
	public RotateAnimData onFinish(EventHandler<ActionEvent> onFinished) {
		this.onFinished = onFinished;
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
		return onFinished;
	}
}
