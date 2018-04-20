package cz.pavelzelenka.fractal;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Hlavni trida aplikace
 * @author Pavel Zelenka A16B0176P
 * @version 2018-04-14
 */
public class MainFractal extends Application {

	/** Popisek okna aplikace */
	public static final String WINDOW_TITLE = "Fractal";
	/** Minimalni sirka okna aplikace */
	public static final double MIN_WINDOW_WIDTH = 600D;
	/** Minimalni vyska okna aplikace */
	public static final double MIN_WINDOW_HEIGHT = 600D;
	
	/** Scena */
	public static Scene scene;
	
	/** Vychozi rozvrzeni okna */
	private WindowLayout wl;
	
	/** 
	 * Hlavni metoda aplikace
	 * @param args argumenty pri spusteni
	 */
    public static void main(String[] args) {
        launch(args);
    }
	
    /**
     * Spusteni GUI aplikace
     */
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle(WINDOW_TITLE);
		primaryStage.setMinWidth(MIN_WINDOW_WIDTH);
		primaryStage.setMinHeight(MIN_WINDOW_HEIGHT);
		primaryStage.getIcons().add(IconType.APPLICATION_128.get());
		wl = new WindowLayout(primaryStage);
		scene = new Scene(wl.get());
        primaryStage.setScene(scene);
        primaryStage.setWidth(560D);
        primaryStage.setHeight(620D);
        primaryStage.show();
	}
	
}
