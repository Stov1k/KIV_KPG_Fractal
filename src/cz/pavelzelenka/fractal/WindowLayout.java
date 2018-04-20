package cz.pavelzelenka.fractal;

import java.awt.image.RenderedImage;
import java.io.File;

import javax.imageio.ImageIO;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Rozvrzeni okna aplikace
 * @author Pavel Zelenka A16B0176P
 * @version 2018-04-15
 */
public class WindowLayout {

	/** Hlavni stage aplikace */
	private final Stage stage;
	
	/** Zakladni rozvrzeni okna */
	private BorderPane borderPane;
	
	/** Kresba */
	private Drawing drawing;
	
	/**
	 * Konstruktor
	 * @param stage (hlavni) stage aplikace
	 */
	public WindowLayout(Stage stage) {
		this.stage = stage;
	}
	
	/**
	 * Vrati zakladni rozvrzeni okna aplikace
	 * @return zakladni rozvrzeni okna aplikace
	 */
	public Parent get() {
		borderPane = new BorderPane();
		borderPane.setTop(getMenuBar());
		borderPane.setCenter(getCanvasPane());
		borderPane.setBottom(getBottomPane());
		return borderPane;
	}
	
	/**
	 * Vrati horni menu
	 * @return horni menu
	 */
	public Parent getMenuBar() {
		MenuBar menuBar = new MenuBar();
		Menu fileMenu = new Menu("File");
		MenuItem saveAsMI = new MenuItem("Save As...");
		saveAsMI.setOnAction(action -> handleSaveAs());
		MenuItem closeMI = new MenuItem("Close");
		closeMI.setOnAction(action -> handleClose());
		fileMenu.getItems().addAll(saveAsMI, new SeparatorMenuItem(), closeMI);
		menuBar.getMenus().add(fileMenu);
		return menuBar;
	}
	
	/**
	 * Vrati spodni panel
	 * @return spodni panel
	 */
	public Parent getBottomPane() {	
		HBox hBox = new HBox();
		hBox.setPadding(new Insets(5D, 5D, 5D, 5D));
		hBox.setMinHeight(40D);
		hBox.setAlignment(Pos.CENTER);
		hBox.setSpacing(5D);
		hBox.setStyle("-fx-font-size: 9pt;");
		
		Label fractalLabel = new Label("Fractal:");
		ChoiceBox<String> fractalChoiceBox = new ChoiceBox<String>();
//		splineChoiceBox.getSelectionModel().select(drawing.getSpline());
		
		Label colorLabel = new Label("Color:");
		ColorPicker colorPicker = new ColorPicker();
		colorPicker.setValue(drawing.getSplineColor());
		colorPicker.setPrefWidth(50D);
		
		Label lineWidthLabel = new Label("Line Width:");
		Slider lineWidthSlider = new Slider(); 
		lineWidthSlider.setMin(1);
		lineWidthSlider.setMax(14);
		lineWidthSlider.setValue(drawing.getLineWidth());
		lineWidthSlider.setBlockIncrement(1);
		
		Pane pane = new Pane();
		HBox.setHgrow(pane, Priority.ALWAYS);
		
		Button clearButton = new Button("Clear");
		
		hBox.getChildren().addAll(fractalLabel, fractalChoiceBox, colorLabel, colorPicker, lineWidthLabel, lineWidthSlider, pane, clearButton);
		
		clearButton.setOnAction(action -> {
			if(drawing != null) {
				drawing.throwOutSpline();
			}
		});
		
		fractalChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue != null) {
			//	drawing.setSpline(newValue);
			}
		});
		
		lineWidthSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			if(drawing != null) {
				if(newValue != null) {
					drawing.setLineWidth(newValue.doubleValue());
				}
			}
		});
		
		colorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue != null) {
				drawing.setSplineColor(newValue);
			}
		});
		
		return hBox;
	}
	
	/**
	 * Vrati panel kresby
	 * @return panel kresby
	 */
	public Parent getCanvasPane() {
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setStyle("-fx-font-size: 11px;");
		
		BorderPane pane = new BorderPane();
		
        Canvas canvas = new Canvas(pane.getWidth(), pane.getHeight());
        
        pane.widthProperty().addListener(event -> canvas.setWidth(pane.getWidth()));
        pane.heightProperty().addListener(event -> canvas.setHeight(pane.getHeight()));
        
        drawing = new Drawing(canvas);
        drawing.draw();
        
        pane.getChildren().add(canvas);
        
        drawing.requiredWidthProperty().addListener((observable, oldValue, newValue) -> {
        	scrollPaneResize(scrollPane, pane);
        });
        
        drawing.requiredHeightProperty().addListener((observable, oldValue, newValue) -> {
        	scrollPaneResize(scrollPane, pane);
        });
        
        scrollPane.widthProperty().addListener(resize ->  {
        	scrollPaneResize(scrollPane, pane);
    	});
    
        scrollPane.heightProperty().addListener(resize -> {
        	scrollPaneResize(scrollPane, pane);
    	});
    
        scrollPane.setContent(pane);
        scrollPane.setHbarPolicy(ScrollBarPolicy.ALWAYS);
        scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        
		return scrollPane;
	}
	
	/** Nastavi nutnou velikost panelu pro vykresleni platna */ 
	public void scrollPaneResize(ScrollPane scrollPane, Pane pane) {
    	double scrollerSize = 14;
		double maxWidth = Math.max(drawing.getRequiredWidth()+scrollerSize, scrollPane.getWidth()-scrollerSize);
		double maxHeight = Math.max(drawing.getRequiredHeight()+scrollerSize, scrollPane.getHeight()-scrollerSize);
		pane.setPrefWidth(maxWidth);
		pane.setPrefHeight(maxHeight);
	}
	
	/** Ukonci aplikaci */
	private void handleClose() {
		Platform.exit();
	}
    
    /** Otevre FileChooser pro ulozeni obrazku */
    private void handleSaveAs() {
    /*    FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setInitialFileName("my_spline.png");
        
        // Nastaveni filtru pripony
        FileChooser.ExtensionFilter pngExtFilter = new FileChooser.ExtensionFilter("PNG file (.png)", "*.png");
        fileChooser.getExtensionFilters().add(pngExtFilter);

        // Zobrazeni ukladaciho dialogu
        File file = fileChooser.showSaveDialog(stage);

        if(file != null) {
        	try {
        		if(!file.getPath().endsWith(".png")) {
        			file = new File(file.getPath() + ".png");
        		}
        		WritableImage splineImage = drawing.getSplineImage();
        		if(splineImage != null) {
        			RenderedImage renderedImage = SwingFXUtils.fromFXImage(drawing.getSplineImage(), null);
        			ImageIO.write(renderedImage, "png", file);
        		} else {
        			Alert alert = new Alert(AlertType.WARNING);
        			alert.setTitle("Warning");
        			alert.setHeaderText("Image cannot be saved!");
        			alert.setContentText("Spline does not exist!");
        			alert.showAndWait();
        		}
        	} catch(Exception e) {
        		e.printStackTrace();
        	}
        }
        */
    }
	
}
