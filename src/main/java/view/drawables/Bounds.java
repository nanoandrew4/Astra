package view.drawables;

import com.sun.istack.internal.NotNull;

import java.awt.*;

/**
 * Determine the four points by which any Drawable is bounded.
 */
public class Bounds {
	private Point tl, tr, bl, br;

	Bounds(@NotNull Point tl, @NotNull Point tr, @NotNull Point bl, @NotNull Point br) {
		this.tl = tl;
		this.tr = tr;
		this.bl = bl;
		this.br = br;
	}

	public Point getTopLeft() {
		return tl;
	}

	public Point getTopRight() {
		return tr;
	}

	public Point getBottomLeft() {
		return bl;
	}

	public Point getBottomRight() {
		return br;
	}

	public int getTopLeftX() {
		return tl.x;
	}

	public int getTopLeftY() {
		return tl.y;
	}

	public int getTopRightX() {
		return tr.x;
	}

	public int getTopRightY() {
		return tr.y;
	}

	public int getBottomLeftX() {
		return bl.x;
	}

	public int getBottomLeftY() {
		return bl.y;
	}

	public int getBottomRightX() {
		return br.x;
	}

	public int getBottomRightY() {
		return br.y;
	}

	public String toString() {
		return tl.toString() + " -> " + tr.toString() + " -> " + bl.toString() + " -> " + br.toString();
	}
}
