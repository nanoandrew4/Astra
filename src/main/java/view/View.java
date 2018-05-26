package view;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import view.drawables.ASCIIRenderer;
import view.drawables.Menu;
import view.drawables.MenuEvent;
import view.screen.Screen;
import view.screen.animation.RotateAnimData;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class View extends Application {
	private Screen screen;

	private Scene scene;
	private Pane pane;

	public static Font font;

	public void start(Stage primaryStage) throws Exception {
		pane = new Pane();
		screen = new Screen();
		font = loadGameFont("/fonts/SpaceMono-Regular.ttf");
		screen.initScreen(pane);
		scene = new Scene(pane, Screen.screenPxWidth, Screen.screenPxHeight);

		scene.setFill(Color.BLACK);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Astra");
		primaryStage.show();

		displayLogo();
	}

	private void displayLogo() {
		ASCIIRenderer studioLogo = new ASCIIRenderer(screen, 56, 27, "/gfx/logo.gfx");
		studioLogo.setTextColor("/gfx/logo.col", "/palettes/testPalette.pal");
		studioLogo.draw();

		FadeTransition fadeIn = new FadeTransition(Duration.millis(4000), pane);
		fadeIn.setFromValue(0);
		fadeIn.setToValue(1);
		fadeIn.setInterpolator(Interpolator.LINEAR);
		fadeIn.play();
		fadeIn.setOnFinished(fadeInEvent -> {
			RotateAnimData rotateAnimData = new RotateAnimData();
			rotateAnimData.cycles = 1;
			rotateAnimData.duration = 3000;
			rotateAnimData.interpolator = Interpolator.EASE_IN;
			rotateAnimData.onFinish = rotEvent -> {
				if (!rotateAnimData.isFinished) {
					rotateAnimData.isFinished = true;
					FadeTransition fadeOut = new FadeTransition(Duration.millis(2500), pane);
					fadeOut.setFromValue(1);
					fadeOut.setToValue(0);
					fadeOut.setInterpolator(Interpolator.LINEAR);
					fadeOut.play();
					fadeOut.setOnFinished(fadeOutEvent -> {
						studioLogo.remove();
						pane.setOpacity(1);
						displayMainMenu();
					});
				}
			};
			studioLogo.rotateChars(rotateAnimData);
		});
	}

	private void displayMainMenu() {
		ASCIIRenderer astraLogo = new ASCIIRenderer(screen, 84, 20, "/gfx/astra.gfx");
		astraLogo.setTextColor("/gfx/astra.col", "/palettes/testPalette.pal");
		astraLogo.draw();

		List<String> menuOptions = Arrays.asList("Play", "Options", "Exit");
		List<MenuEvent> events = Arrays.asList(
				this::play,
				this::displayOptions,
				Platform::exit
		);
		Menu mainMenu = new Menu(screen, 97, 30, menuOptions, events, 1);
		mainMenu.setBorderless();
		mainMenu.draw();
		scene.setOnKeyPressed(mainMenu.getKeyHandler());
	}

	private void play() {

	}

	private void displayOptions() {

	}

	private Font loadGameFont(String urlToRes) {
		InputStream is = this.getClass().getResourceAsStream(urlToRes);
		if (is == null)
			System.err.println("Font not loaded");
		return Font.loadFont(is, Screen.screenPxHeight / (double) (screen.getScreenHeight() + 2));
	}
}
