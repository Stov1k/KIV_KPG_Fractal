package cz.pavelzelenka.fractal;

import java.awt.image.RenderedImage;
import java.io.File;

import javax.imageio.ImageIO;

import cz.pavelzelenka.fractal.fractals.FractalType;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
import javafx.scene.control.Tooltip;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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
		GridPane gridPane = new GridPane();
		gridPane.setPadding(new Insets(5D, 5D, 5D, 5D));
		gridPane.setMinHeight(40D);
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setHgap(5D);
		gridPane.setVgap(5D);
		gridPane.setStyle("-fx-font-size: 9pt;");
		
		ChoiceBox<FractalType> fractalChoiceBox = new ChoiceBox<FractalType>(FractalType.getDefaultList());
		fractalChoiceBox.setTooltip(new Tooltip("Select a spline"));
		fractalChoiceBox.getSelectionModel().select(null);
		fractalChoiceBox.setPrefWidth(150D);
		
		Label colorLabel = new Label("Color:");
		ColorPicker colorPicker = new ColorPicker();
		colorPicker.setValue(drawing.getStrokeColor());
		colorPicker.setPrefWidth(50D);
		CheckBox rainbowColor = new CheckBox("Rainbow");
		
		Label backgroundLabel = new Label("Background:");
		ColorPicker backgroundPicker = new ColorPicker();
		backgroundPicker.setValue(drawing.getBackgroundColor());
		backgroundPicker.setPrefWidth(50D);
			
		Label lineWidthLabel = new Label("Line Width:");
		Slider lineWidthSlider = new Slider(); 
		lineWidthSlider.setMin(Drawing.MIN_LINE_WIDTH);
		lineWidthSlider.setMax(Drawing.MAX_LINE_WIDTH);
		lineWidthSlider.setValue(drawing.getLineWidth());
		lineWidthSlider.setBlockIncrement(1);
		
		Label pointSizeLabel = new Label("Point Size:");
		Slider pointSizeSlider = new Slider(); 
		pointSizeSlider.setMin(Drawing.MIN_POINT_SIZE);
		pointSizeSlider.setMax(Drawing.MAX_POINT_SIZE);
		pointSizeSlider.setValue(drawing.getLineWidth());
		pointSizeSlider.setBlockIncrement(1);
		
		Pane pane = new Pane();

		GridPane.setHgrow(pane, Priority.ALWAYS);
		
		Button clearButton = new Button("Clear");
		clearButton.setMaxHeight(5000D);
		
		gridPane.add(fractalChoiceBox, 0, 0, 2, 1);
		
		gridPane.add(rainbowColor, 0, 1, 2, 1);
		
		gridPane.add(colorLabel, 2, 0);
		gridPane.add(colorPicker, 3, 0);
		
		gridPane.add(backgroundLabel, 2, 1);
		gridPane.add(backgroundPicker, 3, 1);
		
		gridPane.add(lineWidthLabel, 4, 0);
		gridPane.add(lineWidthSlider, 5, 0);
		gridPane.add(pointSizeLabel, 4, 1);
		gridPane.add(pointSizeSlider, 5, 1);		
		
		gridPane.add(pane, 6, 0);
		
		gridPane.add(clearButton, 7, 0, 1, 2);
		
		clearButton.setOnAction(action -> {
			if(drawing != null) {
				drawing.throwOut();
			}
		});
		
		rainbowColor.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue != null) {
				drawing.setRainbowColor(newValue);;
			}
		});
		
		fractalChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue != null) {
				drawing.setCurve(newValue.getFractal());
			}
		});
		
		lineWidthSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			if(drawing != null) {
				if(newValue != null) {
					drawing.setLineWidth(newValue.doubleValue());
				}
			}
		});
		
		pointSizeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			if(drawing != null) {
				if(newValue != null) {
					drawing.setPointSize(newValue.doubleValue());
				}
			}
		});
		
		colorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue != null) {
				drawing.setStrokeColor(newValue);
			}
		});
		
		backgroundPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue != null) {
				drawing.setBackgroundColor(newValue);
			}
		});
		
		return gridPane;
	}
	
	/**
	 * Vrati panel kresby
	 * @return panel kresby
	 */
	public Parent getCanvasPane() {
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setStyle("-fx-font-size: 11px;");
		
		BorderPane pane = new BorderPane();
		
        Canvas canvas = new Canvas(1024, 768);
        
        drawing = new Drawing(canvas);
        drawing.draw();
        
        pane.getChildren().add(canvas);
        
        drawing.requiredWidthProperty().addListener((observable, oldValue, newValue) -> {
        	scrollPaneResize(scrollPane, pane, canvas);
        });
        
        drawing.requiredHeightProperty().addListener((observable, oldValue, newValue) -> {
        	scrollPaneResize(scrollPane, pane, canvas);
        });
        
        scrollPane.widthProperty().addListener(resize ->  {
        	scrollPaneResize(scrollPane, pane, canvas);
    	});
    
        scrollPane.heightProperty().addListener(resize -> {
        	scrollPaneResize(scrollPane, pane, canvas);
    	});
    
        scrollPane.setContent(pane);
        scrollPane.setHbarPolicy(ScrollBarPolicy.ALWAYS);
        scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        
		return scrollPane;
	}
	
	/** Nastavi nutnou velikost panelu pro vykresleni platna */ 
	public void scrollPaneResize(ScrollPane scrollPane, Pane pane, Canvas canvas) {
    	double scrollerSize = 14;
    	double maxWidth = Math.max(drawing.getRequiredWidth()+scrollerSize, scrollPane.getWidth()-scrollerSize);
		double maxHeight = Math.max(drawing.getRequiredHeight()+scrollerSize, scrollPane.getHeight()-scrollerSize);
		canvas.setWidth(maxWidth);
		canvas.setHeight(maxHeight);
		pane.setPrefWidth(maxWidth);
		pane.setPrefHeight(maxHeight);
	}
	
	/** Ukonci aplikaci */
	private void handleClose() {
		Platform.exit();
	}
    
    /** Otevre FileChooser pro ulozeni obrazku */
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setInitialFileName("my_fractal.png");
        
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
    }
	
}
