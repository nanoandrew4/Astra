package view;

import controller.Controller;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.scene.Node;
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
import view.screen.animation.FadeAnimData;

import java.io.InputStream;
import java.util.List;

public class View extends Application {
	private Screen screen;

	private Scene scene;
	private Pane pane;

	public static Font font;

	public static void main(String... args) {
		launch(args);
	}

	public void start(Stage primaryStage) {
		pane = new Pane();
		screen = new Screen();
		font = loadGameFont("/fonts/SpaceMono-Regular.ttf");
		screen.initScreen(pane);
		scene = new Scene(pane, Screen.screenPxWidth, Screen.screenPxHeight);

		scene.setFill(Color.BLACK);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Astra");
		primaryStage.show();

		new Controller(this);
	}

	public ASCIIRenderer buildASCIIRenderer(int x, int y, String gfxFileName) {
		return new ASCIIRenderer(screen, x, y, gfxFileName);
	}

	public Menu buildMenu(int x, int y, List<String> options, List<MenuEvent> events, int columns) {
		return new Menu(screen, x, y, options, events, columns);
	}

	public Pane getPane () {
		return pane;
	}

	public Scene getScene() {
		return scene;
	}

	public void playFadeAnimation(FadeAnimData fadeAnimData, Node n) {
		FadeTransition ft = new FadeTransition(Duration.millis(fadeAnimData.getDurationMillis()), n);
		ft.setFromValue(fadeAnimData.getFromValue());
		ft.setToValue(fadeAnimData.getToValue());
		ft.setInterpolator(fadeAnimData.getInterpolator());
		ft.setOnFinished(fadeAnimData.getOnFinished());
		ft.play();
	}

	private Font loadGameFont(String urlToRes) {
		InputStream is = this.getClass().getResourceAsStream(urlToRes);
		if (is == null)
			System.err.println("Font not loaded");
		return Font.loadFont(is, Screen.screenPxHeight / (double) (screen.getScreenHeight() + 2));
	}
}
