package controller;

import javafx.animation.Interpolator;
import javafx.application.Platform;
import model.Model;
import view.View;
import view.drawables.ASCIIRenderer;
import view.drawables.Menu;
import view.drawables.MenuEvent;
import view.screen.animation.FadeAnimData;
import view.screen.animation.RotateAnimData;

import java.util.Arrays;
import java.util.List;

public class Controller {
	private View view;
	private Model model;

	public Controller(View view) {
		this.view = view;
		model = new Model();

//		displayLogo();
		displayMainMenu();
	}

	private void displayLogo() {
		ASCIIRenderer studioLogo = view.buildASCIIRenderer(56, 27, "/gfx/logo.gfx");
		studioLogo.setTextColor("/gfx/logo.col", "/palettes/testPalette.pal");
		studioLogo.draw();

		FadeAnimData fadeIn = new FadeAnimData().durationMillis(4000).fromValue(0).toValue(1).onFinished(
				(event -> {
					RotateAnimData rad = new RotateAnimData().durationMillis(3000).interpolator(Interpolator.EASE_IN).onFinish(rotEvent -> {
						FadeAnimData fadeOut = new FadeAnimData().durationMillis(2500).onFinished(fadeOutEvent -> {
							studioLogo.remove();
							view.getPane().setOpacity(1);
							displayMainMenu();
						});
						view.playFadeAnimation(fadeOut, view.getPane());
					});
					studioLogo.rotateChars(rad);
				})
		);
		view.playFadeAnimation(fadeIn, view.getPane());
	}

	private void displayMainMenu() {
		ASCIIRenderer astraLogo = view.buildASCIIRenderer(84, 20, "/gfx/astra.gfx");
		astraLogo.setTextColor("/gfx/astra.tcol", "/palettes/testPalette.pal");
		astraLogo.draw();

		List<String> menuOptions = Arrays.asList("Play", "Options", "Exit");
		List<MenuEvent> events = Arrays.asList(
				this::play,
				this::displayOptions,
				Platform::exit
		);
		Menu mainMenu = view.buildMenu(97, 30, menuOptions, events, 1);
		mainMenu.setBorderless();
		mainMenu.draw();
		view.getScene().setOnKeyPressed(mainMenu.getKeyHandler());
	}

	private void play() {
		view.resetScreen();
		ASCIIRenderer testRoom = view.buildASCIIRenderer(0, 0, "/gfx/rooms/test.gfx");

		testRoom.draw();
	}

	private void displayOptions() {

	}
}
