package view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import view.drawables.Menu;
import view.screen.Screen;

import java.io.InputStream;
import java.util.ArrayList;

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
		/*
		ArrayList<String> list = new ArrayList<>();
		list.add("Option 1");
		list.add("Option 2");
		list.add("Option 3");
		list.add("Option 4");
		list.add("Option 5");
		list.add("Option 6");
		list.add("Option 7");
		list.add("Option 8");
		list.add("Option 9");
		list.add("Option 10");
		Menu menu = new Menu(screen, 50, 30, list, 3);
		menu.draw();
		scene.setOnKeyPressed(menu.getKeyHandler());
		*/

		scene.setFill(Color.BLACK);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private Font loadGameFont(String urlToRes) {
		InputStream is = this.getClass().getResourceAsStream(urlToRes);
		if (is == null)
			System.err.println("Font not loaded");
		return Font.loadFont(is, Screen.screenPxHeight / (double) (screen.getScreenHeight() + 2));
	}
}
