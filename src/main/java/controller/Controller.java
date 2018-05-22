package controller;

import javafx.application.Application;
import model.Model;
import view.View;

public class Controller {
	private View view;
	private Model model;

	public Controller() {
		new Thread(() -> Application.launch(View.class)).start();
		model = new Model();
	}
}
